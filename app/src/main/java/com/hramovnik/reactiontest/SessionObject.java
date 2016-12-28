package com.hramovnik.reactiontest;

import android.util.Pair;

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

    @Override
    public Pair<Integer, Integer> countTasks() {
        tasksElapsed = tasks.size();
        return new Pair<Integer, Integer>(tasksElapsed, tasksInSession);
    }


}
