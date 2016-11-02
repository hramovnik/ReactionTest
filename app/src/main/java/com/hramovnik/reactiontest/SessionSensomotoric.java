package com.hramovnik.reactiontest;

import android.util.Pair;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public final class SessionSensomotoric implements Session {
    private SessionSensomotoric(){}
    private Queue<TaskExecute> tasks = null;
    private CommandSensoGetResult redResult = null;
    private CommandSensoGetResult greenResult = null;
    public SessionSensomotoric(int serialLen, int dotSize, int firstColor, int secondColor){
        tasks = new LinkedBlockingQueue<>();

        int realDotSize = (int) ((float) dotSize / 0.219);

        tasks.add(new CommandSensomotoric(firstColor, realDotSize, serialLen, 1000));
        redResult = new CommandSensoGetResult(serialLen);
        tasks.add(redResult);

        tasks.add(new CommandSensomotoric(secondColor, realDotSize, serialLen, 1000));
        greenResult = new CommandSensoGetResult(serialLen);
        tasks.add(greenResult);

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
/*
        double medLeft = 0;
        double medRight = 0;

        int leftFails = 0;
        int rightFails = 0;

        for (int i = 0; i < serialLen; i++){
            if (result[1+i] >= 0){medRight += result[1+i];}
            else{rightFails++;}
            if (result[21+i] >= 0){medLeft += result[21+i];}
            else{leftFails++;}
        }
        medLeft /= (serialLen-leftFails);
        medRight /= (serialLen-rightFails);

        double standDeltaLeft = 0;
        double standDeltaRight = 0;

        for (int i = 0; i < serialLen; i++){
            if (result[1+i] >= 0) {standDeltaRight += Math.pow(result[1+i]-medRight,2);}
            if (result[21+i] >=0) {standDeltaLeft += Math.pow(result[21+i]-medLeft,2);}
        }

        standDeltaLeft = Math.sqrt((standDeltaLeft/(serialLen - leftFails - 1)));
        standDeltaRight = Math.sqrt((standDeltaRight/(serialLen - rightFails - 1)));

        StringBuilder builder = new StringBuilder();
        builder.append("Среднее время реакции левой руки " + medLeft + " мс\n");
        builder.append("Среднее время реакции правой руки " + medRight + " мс\n");
        builder.append("Стандартное отклонение времени реакции левой руки " + standDeltaLeft  + " мс\n");
        builder.append("Стандартное отклонение время реакции правой руки " + standDeltaRight + " мс\n");
        return builder.toString();

*/
        return "Data is not ready but loaded successfully";
    }

    @Override
    public Pair<Integer, Integer> countTasks() {
        tasksElapsed = tasks.size();
        return new Pair<Integer, Integer>(tasksElapsed, tasksInSession);
    }
}
