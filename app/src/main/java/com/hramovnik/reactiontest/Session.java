package com.hramovnik.reactiontest;


import android.util.Pair;

/**
 * Created by Hramovnik on 22.10.2016.
 */

public interface Session {
    TaskExecute getNextTask();
    String analyze();
    Pair<Integer, Integer> countTasks();
}
