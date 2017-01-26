package com.hramovnik.reactiontest;

import android.graphics.Color;
import android.util.Pair;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import layout.ParametersActivity;

/**
 * Created by gshabalev on 11/11/2016.
 */

public abstract class SessionObject implements Session {

    public static void setDispalyable(ResultDisplayable newDisplay) {
        display = newDisplay;
    }

    protected static ResultDisplayable display = null;
    protected Integer tasksInSession;
    protected Integer tasksElapsed;
    protected Queue<TaskExecute> tasks = null;
    protected boolean cancel = false;

    @Override
    public Pair<Integer, Integer> countTasks() {
        tasksElapsed = tasks.size();
        return new Pair<Integer, Integer>(tasksElapsed, tasksInSession);
    }

    @Override
    public void setCancelled(){
        synchronized(this) {
            cancel = true;
        }
    }

    @Override
    public boolean getCancelled(){
        synchronized(this) {
            return cancel;
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String getColor(int color){

        int cwoalfa = color&0xffffff;
        switch (cwoalfa){
            case Color.RED&0xffffff: return "Красный";
            case Color.BLUE&0xffffff: return "Синий";
            case Color.GREEN&0xffffff: return "Зелёный";
            case Color.CYAN&0xffffff: return "Голубой";
            case Color.MAGENTA&0xffffff: return "Маджента";
            case Color.YELLOW&0xffffff: return "Жёлтый";
        }

        byte[] bytes = ByteBuffer.allocate(4).putInt(cwoalfa).array();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);

    }
}
