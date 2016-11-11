package com.hramovnik.reactiontest;

import android.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by gshabalev on 11/11/2016.
 */

public abstract class SessionObject implements Session {

    @Override
    public void setDispalyable(ResultDisplayable display) {
        this.display = display;
    }

    protected ResultDisplayable display = null;
    protected Integer tasksInSession;
    protected Integer tasksElapsed;
    protected Queue<TaskExecute> tasks = null;
    protected LinkedList< Pair<String, Double> >resultInterpritation = new LinkedList<Pair<String, Double> >();

    @Override
    public Pair<Integer, Integer> countTasks() {
        tasksElapsed = tasks.size();
        return new Pair<Integer, Integer>(tasksElapsed, tasksInSession);
    }
}
