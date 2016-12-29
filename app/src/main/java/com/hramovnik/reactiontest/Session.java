package com.hramovnik.reactiontest;


import android.util.Pair;


/**
 * Created by Hramovnik on 22.10.2016.
 */

public interface Session {
    public TaskExecute getNextTask();
    public void analyze();
    public Pair<Integer, Integer> countTasks();
    public boolean isCanceled();
    public void setCanceled();

}
