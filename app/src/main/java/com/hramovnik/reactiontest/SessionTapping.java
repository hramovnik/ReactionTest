package com.hramovnik.reactiontest;

import android.util.Pair;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;


public final class SessionTapping extends SessionObject {
    private SessionTapping(){}
    private CommandTappingGetResult result = null;

    public SessionTapping(int serialLen){
        this.serialLen = serialLen;
        tasks = new LinkedBlockingQueue<>();

        tasks.add(new CommandTapping(serialLen, 2000));
        result = new CommandTappingGetResult(serialLen);
        tasks.add(result);

        tasksInSession = tasks.size();
        tasksElapsed = tasksInSession;
    }

    private int serialLen = 0;

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }


    @Override
    public void analyze() {


        if (display != null) display.displayResult("Готово", null);
    }

}
