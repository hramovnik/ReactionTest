package com.hramovnik.reactiontest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.os.NetworkOnMainThreadException;

/**
 * Created by Hramovnik on 07.10.2016.
 */

public class Connection implements Closeable{
    private String IPAddress;
    private int port;
    private Socket socket = null;
    private TextView status = null;
    private boolean working = false;
    public Session session = null;

    Connection(String ip, int port, TextView status){
        super();
        IPAddress = ip;
        this.port = port;
        this.status = status;

    }
    private void print(String string){
        if(status != null) status.setText(string);
    }
    public boolean isWorking(){
        return working;
    }

    public void sendSession(Session sess){
        if (isWorking()) return;

        print("Отправка команды");
        session = sess;
        working = true;
        Log.d("Connection","Opening socket");

        executeNext();
    }

    public synchronized void executeNext(){
        TaskExecute task = session.getNextTask();
        if (task != null){
            sendTask(task);
        }else{
            print(session.analyze());
            working = false;
            session = null;
        }
    }

    private void sendTask(final TaskExecute task){

        final Connection connection = this;
        final AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if ((socket == null)||(socket.isClosed())){
                    Log.d("Connection","Creating socket");
                    try {
                        socket = new Socket(InetAddress.getByName(IPAddress), port);
                    }catch (UnknownHostException e){
                        Log.d("Connection","Unknown host fail");
                        e.printStackTrace();
                        //print("Ошибка - некорректный IP адрес");

                        try {
                            socket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        working = false;
                        return null;
                    }catch (IOException e) {
                        Log.d("Connection","Socket failed");
                        e.printStackTrace();
                        //print("Ошибка соединения");
                        try {
                            socket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        working = false;
                        return null;
                    }
                    Log.d("Connection","Socket created");
                }

                if (socket.isClosed()){
                    Log.d("Connection","Socket failed");
                    //print("Ошибка соединения");
                    working = false;
                    return null;
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                final TaskExecutor taskExecutor = new TaskExecutor(session, socket, status, connection);
            }

        };

        asyncTask.execute((Void[]) null);



    }

    @Override
    public void close() throws IOException {
        if (socket != null) {
            try {
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            socket = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try{
            close();
        }catch (Exception e){
            e.printStackTrace();
        }
        super.finalize();
    }
}
