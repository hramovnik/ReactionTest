package com.hramovnik.reactiontest;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hramovnik on 23.10.2016.
 */

public class TaskExecutor extends AsyncTask<Void,Pair<String,Integer>,String> {

    private Session session = null;
    private Connection connection = null;
    private TaskExecutor(){}

    public TaskExecutor(Session session, Connection connection){
        super();
        this.session = session;
        this.connection = connection;
        setProgressBar(0);
        execute();
    }

    private void print(String string){
        if (connection.getStatus() != null) connection.getStatus().setText(string);
    }

    private void setProgressBar(Integer value){
        if (connection.getProgressBar() != null) connection.getProgressBar().setProgress(value);
    }


    @Override
    protected String doInBackground(Void... params) {
        synchronized(this) {
            Log.d("Thread", "Begin");
            Socket socket = null;
            try {

                socket = new Socket(InetAddress.getByName(connection.getAddress()), connection.getPort());
                socket.setKeepAlive(true);
                if (socket.isClosed()) return "Ошибка соединения: невозможно установить соединение";
                socket.setSoTimeout(0);
                Integer iteration = 0;

                TaskExecute executable = null;
                while ((executable = session.getNextTask()) != null) {
                    if (executable.getSleeping() != 0) {
                        TimeUnit.MILLISECONDS.sleep(executable.getSleeping());
                    }

                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    ByteBuffer byteBuffer = ByteBuffer.allocate(executable.getTask().length * 4);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

                    for (int value : executable.getTask()) {byteBuffer.putInt(value);}

                    iteration++;
                    Pair<Integer, Integer> taskSize = session.countTasks();

                    for (int i = 0; i < 200; i++) {

                        if (socket.isClosed()){
                            socket = new Socket(InetAddress.getByName(connection.getAddress()), connection.getPort());
                            socket.setKeepAlive(true);
                            if (socket.isClosed()) return "Ошибка соединения: невозможно установить соединение при отправке данных";
                            socket.setSoTimeout(0);
                            out = new DataOutputStream(socket.getOutputStream());
                            in = new DataInputStream(socket.getInputStream());
                        }


                        out.write(byteBuffer.array());
                        out.flush();

                        TimeUnit.MILLISECONDS.sleep(executable.getTimeOut());
                        for(int t = 0; (t < 20) && (in.available() == 0); t++){
                            TimeUnit.MILLISECONDS.sleep(executable.getTimeOut());
                        }
                        if (in.available() == 0){
                            return "Задача " + String.valueOf(iteration) + " : таймаут принятия сообщений (" + String.valueOf(executable.getTimeOut()*20) + "мc)";
                        }else if(in.available() < 4){
                            return "Задача " + String.valueOf(iteration) + " : ошибка количества принятых данных ( < 4 байт)";
                        }


                        byte[] buffer = new byte[in.available()];
                        in.read(buffer);

                        IntBuffer intBuf = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
                        int[] array = new int[intBuf.remaining()];
                        intBuf.get(array);


                        if (executable.setResult(array)) {
                            if (array.length > 0) {publishProgress(new Pair<String,Integer>("Задача " + String.valueOf(iteration) + ": " + String.valueOf(array[0]), null));}
                            publishProgress(new Pair<String,Integer>(null, (taskSize.second-taskSize.first)*100/taskSize.second));
                            break;
                        } else {
                            if (executable.isCriticalError()){
                                return "Ошибка: невозможно совершить данное действие";
                            }else{
                                if (i == 199) {
                                    if (executable.isInProgress()){ return "Ошибка: превышено время ожидания завершения процесса " + String.valueOf(iteration) ;}
                                    return "Задача " + String.valueOf(iteration) +": ошибка данных";
                                }
                                TimeUnit.MILLISECONDS.sleep(executable.getTimeOut());
                            }
                        }
                    }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "Ошибка сети: неизвестный ip адрес хоста";
            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка ввода-вывода: проблемы с соединением";
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "Ошибка прерывания потока";
            } catch (Throwable e) {
                return "Фатальная ошибка: " + e.getMessage();
            } finally {
                try {
                    socket.close();
                }catch (Exception e){}
            }

            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Pair<String,Integer> ... values){
        if ((values == null)||(values.length < 1)) return;
        if (values[0].first != null){print(values[0].first);}
        if (values[0].second != null){setProgressBar(values[0].second);}

    }

    @Override
    protected void onPostExecute(String message) {
        if (message != null) {
            print(message);
        }else{
            print("Тест успешно завершён");
            session.analyze();
        }
        session = null;
        connection.workingEnds();
    }
}
