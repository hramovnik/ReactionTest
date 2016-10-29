package com.hramovnik.reactiontest;

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

import org.w3c.dom.Text;

/**
 * Created by Hramovnik on 07.10.2016.
 */

public class Connection{
    private String IPAddress;
    private int port;
    private TextView status = null;
    private boolean working = false;
    private ProgressBar progressBar;

    Connection(String ip, int port, TextView status, ProgressBar progressBar){
        super();
        IPAddress = ip;
        this.port = port;
        this.status = status;
        this.progressBar = progressBar;

    }
    private void print(String string){
        if(status != null) status.setText(string);
    }
    public boolean isWorking(){
        return working;
    }

    public void sendSession(Session session){
        if (isWorking()) return;
        working = true;
        print("Отправка команды");

        TaskExecutor taskExecutor = new TaskExecutor(session, this);
    }

    public int getPort() {
        return port;
    }

    String getAddress(){
        return IPAddress;
    }

    TextView getStatus(){
        return status;
    }

    ProgressBar getProgressBar(){
        return progressBar;
    }

    void workingEnds(){
        working = false;
    }


}
