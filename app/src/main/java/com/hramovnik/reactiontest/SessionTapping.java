package com.hramovnik.reactiontest;

import android.util.Pair;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;


public final class SessionTapping extends SessionObject {
    private SessionTapping(){}
    private CommandTappingGetResult result = null;

    public SessionTapping(int imageIndex, int serialLen){
        this.serialLen = serialLen;
        tasks = new LinkedBlockingQueue<>();

        CommandTapping task = new CommandTapping(imageIndex, serialLen);
        tasks.add(task);
        result = new CommandTappingGetResult(task, serialLen);
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
