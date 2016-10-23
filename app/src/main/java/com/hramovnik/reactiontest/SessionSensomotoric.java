package com.hramovnik.reactiontest;

import android.graphics.Color;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

//Fucking Analizer

public final class SessionSensomotoric implements Session {
    private SessionSensomotoric(){}
    private Queue<TaskExecute> tasks = null;
    private CommandSensoGetResult redResult = null;
    private CommandSensoGetResult greenResult = null;
    SessionSensomotoric(int serialLen){
        tasks = new LinkedBlockingQueue<>();

        tasks.add(new CommandSensomotoric(Color.RED, 10, serialLen, 1000));
        redResult = new CommandSensoGetResult(serialLen, 1000);
        tasks.add(redResult);

        tasks.add(new CommandSensomotoric(Color.GREEN, 10, serialLen, 1000));
        greenResult = new CommandSensoGetResult(serialLen, 1000);
        tasks.add(greenResult);

    }

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
        return "Data is not ready";
    }
}
