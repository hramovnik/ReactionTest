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
        if(connection.getStatus() != null) connection.getStatus().setText(string);
    }

    private void setProgressBar(Integer value){
        if (connection.getProgressBar() != null) connection.getProgressBar().setProgress(value);
    }


    @Override
    protected String doInBackground(Void... params) {
        synchronized(this) {
            Log.d("Thread", "Begin");

            try (Socket socket = new Socket(InetAddress.getByName(connection.getAddress()), connection.getPort())) {
                if (socket.isClosed()) return "Ошибка соединения: невозможно установить соединение";
                socket.setSoTimeout(0);
                Integer iteration = 0;

                TaskExecute executeble = null;
                while ((executeble = session.getNextTask()) != null) {
                    if (executeble.getSleeping() != 0) {
                        TimeUnit.MILLISECONDS.sleep(executeble.getSleeping());
                    }

                    Log.d("Tread", "Creation streams");
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    ByteBuffer byteBuffer = ByteBuffer.allocate(executeble.getTask().length * 4);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

                    for (int value : executeble.getTask()) {
                        byteBuffer.putInt(value);
                    }
                    out.write(byteBuffer.array());
                    out.flush();
                    wait(100);

                    StringBuilder res = new StringBuilder();
                    iteration++;
                    Pair<Integer, Integer> taskSize = session.countTasks();
                    publishProgress(new Pair<String,Integer>(null, (taskSize.second-taskSize.first)*100/taskSize.second));

                    for (int i = 0; i < 20; i++) {
                        out.write(byteBuffer.array());
                        out.flush();
                        wait(100);

                        for(int t = 0; (t < 10) && (in.available() == 0); t++){
                            TimeUnit.MILLISECONDS.sleep(executeble.getTimeOut());
                        }
                        if (in.available() == 0){return "Таймаут принятия сообщений (" + String.valueOf(executeble.getTimeOut()*10) + "c)";}

                        byte[] buffer = new byte[in.available()];
                        in.read(buffer);
                        IntBuffer intBuf = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
                        int[] array = new int[intBuf.remaining()];
                        intBuf.get(array);

                        if (executeble.setResult(array)) {
                            break;
                        } else {
                            if (executeble.isError()){
                                return "Ошибка данных, или процессов в устрйостве";
                            }else{
                                if (i == 19) {return "Задача " + String.valueOf(iteration) + ": ошибка данных \n" + res.toString();}
                                TimeUnit.MILLISECONDS.sleep(executeble.getTimeOut());
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
                return "Ошибка приостановки потока";
            } catch (Throwable e) {
                return "Фатальная ошибка: " + e.getMessage();
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
            print(session.analyze());
        }
        session = null;
        connection.workingEnds();
    }
}
