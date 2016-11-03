package com.hramovnik.reactiontest;

import android.util.Pair;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by gshabalev on 11/3/2016.
 */

public class SessionFlicker implements Session {

    private SessionFlicker(){}
    private Queue<TaskExecute> tasks = null;
    private CommandFlickerGetResult result = null;

    public SessionFlicker(boolean flickerOne, int color, int dotSize, int brightness, int initialSpeed_Hz, int maxSpeed_Hz, int blackScreenDelay_ms){
        tasks = new LinkedBlockingQueue<>();

        int realDotSize = (int) ((float) dotSize / 0.219);

        tasks.add(new CommandStartFlickerOne(flickerOne, color, realDotSize, brightness, initialSpeed_Hz, maxSpeed_Hz, blackScreenDelay_ms));
        result = new CommandFlickerGetResult();
        tasks.add(result);

        tasksInSession = tasks.size();
        tasksElapsed = tasksInSession;
    }

    private Integer tasksInSession;
    private Integer tasksElapsed;

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }

    @Override
    public String analyze() {
        int [] mass = result.getResult();
        StringBuilder string = new StringBuilder();
        for(int i = 0 ; i < mass.length; i++){
            string.append(String.valueOf(mass[i]) + " ");
        }

        return "Data: " + string.toString();
    }

    @Override
    public Pair<Integer, Integer> countTasks() {
        tasksElapsed = tasks.size();
        return new Pair<Integer, Integer>(tasksElapsed, tasksInSession);
    }


}
