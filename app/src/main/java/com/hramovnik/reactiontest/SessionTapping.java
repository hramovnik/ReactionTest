package com.hramovnik.reactiontest;

import android.graphics.Color;
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
        LinkedList<Pair<Integer, Integer> > inData = new LinkedList<Pair<Integer, Integer> >();
        for (int i = 0; i < serialLen; i++) {
            Pair<Integer, Integer> pair = new Pair<Integer, Integer>(result.dataRight[i], result.dataLeft[i]);
            if ((pair.first >= 0) && (pair.second >= 0)) {inData.add(pair);}
        }

        int [] colors = new int [2];
        colors[0] = Color.RED;
        colors[1] = Color.BLUE;
        if (display != null) display.displayResult(inData, colors, null);
    }

}
