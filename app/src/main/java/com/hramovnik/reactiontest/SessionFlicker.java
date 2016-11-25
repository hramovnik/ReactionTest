package com.hramovnik.reactiontest;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by gshabalev on 11/3/2016.
 */

public class SessionFlicker extends SessionObject {

    private SessionFlicker(){}
    private ArrayList<CommandFlickerGetResult> result = new ArrayList<>();
    private boolean flickerOne;

    public SessionFlicker(boolean flickerOne, int color, int dotSize, int brightness, int initialSpeed_Hz, int maxSpeed_Hz, int blackScreenDelay_ms){
        tasks = new LinkedBlockingQueue<>();
        this.flickerOne = flickerOne;

        int realDotSize = (int) ((float) dotSize / 0.219 / 2);

        for (int i = flickerOne? 1:brightness; i <= brightness; i++) {
            tasks.add(new CommandStartFlickerOne(flickerOne, color, realDotSize, i, initialSpeed_Hz, maxSpeed_Hz, blackScreenDelay_ms));
            CommandFlickerGetResult tempResult = new CommandFlickerGetResult();
            result.add(tempResult);
            tasks.add(tempResult);
        }

        tasksInSession = tasks.size();
        tasksElapsed = tasksInSession;
    }


    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }

    @Override
    public void analyze() {
        /*int [] mass = result.getResult();
        StringBuilder string = new StringBuilder();
        for(int i = 0 ; i < mass.length; i++){
            string.append(String.valueOf(mass[i]) + " ");
        }

        if(display!=null) display.displayResult(string.toString(),this);*/
    }




}
