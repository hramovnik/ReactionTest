package com.hramovnik.reactiontest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
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
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Hramovnik on 07.10.2016.
 */

public class Connection{
    private String IPAddress = null;
    private int port = 0;
    private Context context = null;
    private Session currentSession = null;
    private ProgressBar progressBar = null;

    Connection(){
        super();
        Log.d("Tag", "Connection created");
    }
    public void setControls(String ip, int port, Context context, ProgressBar progressBar){
        IPAddress = ip;
        this.port = port;
        this.context = context;
        this.progressBar = progressBar;
    }
    public void print(String string){
        if(context != null) Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public Session isWorking(){
        return currentSession;
    }

    public void sendSession(Session session){
        synchronized(this) {
            if (isWorking() != null) return;
            currentSession = session;
            print("Отправка команды");

            TaskExecutor taskExecutor = new TaskExecutor(session, this);
        }
    }

    public int getPort() {
        return port;
    }

    String getAddress(){
        return IPAddress;
    }

    ProgressBar getProgressBar(){
        return progressBar;
    }

    void workingEnds(){
        synchronized(this) {
            currentSession = null;
        }
    }

    void stopSession(){
        synchronized(this) {
            if (currentSession != null){
                currentSession.setCanceled();
            }
        }
    }


}
