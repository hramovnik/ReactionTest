package com.hramovnik.reactiontest;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private TextView status = null;
    private Socket socket = null;
    private ProgressBar progressBar = null;
    private TaskExecutor(){};

    public TaskExecutor(Session session, Socket socket, TextView status, ProgressBar progressBar){
        super();
        this.session = session;
        this.status = status;
        this.socket = socket;
        this.progressBar = progressBar;
        execute();
    }

    private void print(String string){
        if( status != null) status.setText(string);
    }

    private void setProgressBar(Integer value){
        if (progressBar != null) progressBar.setProgress(value);
    }


    @Override
    protected String doInBackground(Void... params) {
        Log.d("Tread","Begin");
        TaskExecute executeble = null;
        while ((executeble = session.getNextTask()) != null) {
            if (executeble.getSleeping() != 0){
                try {
                    TimeUnit.MILLISECONDS.sleep(executeble.getSleeping());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "Ошибка потока: Sleep failed";
                }
            }

            try {
                if ((socket == null)||(socket.isClosed())){
                    Log.d("Tread","Socket failed");
                    return "Ошибка соединения: Socket failed";
                }

                Log.d("Tread","Creation streams");
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());

                ByteBuffer byteBuffer = ByteBuffer.allocate(executeble.getTask().length * 4);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                for (int value : executeble.getTask()) {
                    byteBuffer.putInt(value);
                }
                out.write(byteBuffer.array());
                out.flush();

                for(int i = 0; i < 100; i++){
                    Log.d("Tread","Sending byte array");
                    byte [] buffer = new byte [in.available()];
                    IntBuffer intBuf = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
                    int[] array = new int[intBuf.remaining()];
                    intBuf.get(array);

                    if (executeble.setResult(array)){
                        break;
                    }else{
                        try {
                            wait(executeble.getTimeOut());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return "Ошибка потока: Waiting failed";
                        }
                    }
                }

            }
            catch (UnknownHostException e){
                Log.d("Tread","Unknown host fail");
                e.printStackTrace();
                return "Ошибка сети: неизвестный ip адрес хоста";
            }catch (IOException e) {
                Log.d("Tread","Socket failed");
                e.printStackTrace();
                return "Ошибка ввода-вывода: проблемы с соединением";
            }

        }
        return null;
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
    }
}
