package com.hramovnik.reactiontest;

import android.os.AsyncTask;
import android.util.Log;
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

public class TaskExecutor extends AsyncTask<Void,Integer,Void> {

    private boolean isOk = true;
    private TaskExecute executeble = null;
    private TextView status = null;
    Socket socket = null;
    Connection connection;
    private TaskExecutor(){};
    public TaskExecutor(TaskExecute task, Socket socket, TextView status, Connection connection){
        super();
        executeble = task;
        this.status = status;
        this.socket = socket;
        this.connection = connection;
        execute();
    }

    private void print(String string){
        if(status != null) status.setText(string);
    }

    @Override
    protected void onPreExecute(){
        isOk = true;
    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.d("Tread","Begin");

        if (executeble.getSleeping() != 0){
            try {
                TimeUnit.MILLISECONDS.sleep(executeble.getSleeping());
            } catch (InterruptedException e) {
                e.printStackTrace();
                isOk = false;
                return null;
            }
        }

        try {
            if ((socket == null)||(socket.isClosed())){
                isOk = false;
                Log.d("Tread","Socket failed");
                return null;
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
                        isOk = false;
                    }
                }
            }

        }
        catch (UnknownHostException e){
            Log.d("Tread","Unknown host fail");
            e.printStackTrace();
            isOk = false;
        }catch (IOException e) {
            Log.d("Tread","Socket failed");
            e.printStackTrace();
            isOk = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        if (!isOk) {
            print("Проблемы с соединением");
            executeble = null;
            return;
        }

        print(executeble.getInterpretation());
        executeble = null;
        connection.executeNext();


    }
}
