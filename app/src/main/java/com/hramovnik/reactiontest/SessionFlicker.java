package com.hramovnik.reactiontest;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by gshabalev on 11/3/2016.
 */

public class SessionFlicker extends SessionObject {

    private SessionFlicker(){}
    private ArrayList<CommandFlickerGetResult> resultOne = new ArrayList<>();
    private ArrayList<CommandFlickerGetResult> resultTwo = new ArrayList<>();
    private boolean flickerOne = true;

    public SessionFlicker(boolean flickerOne, int color, int dotSize, int brightness, int initialSpeed_Hz, int maxSpeed_Hz){
        tasks = new LinkedBlockingQueue<>();
        this.flickerOne = flickerOne;

        int realDotSize = dotSize;

        for (int i = /*flickerOne? 1:brightness*/2; i <= brightness; i+=2) {
            tasks.add(new CommandFlicker(Color.RED, realDotSize, i, initialSpeed_Hz, maxSpeed_Hz));
            CommandFlickerGetResult tempResult = new CommandFlickerGetResult();
            resultOne.add(tempResult);
            tasks.add(tempResult);
        }

        for (int i = /*flickerOne? 1:brightness*/2; i <= brightness; i+=2) {
            tasks.add(new CommandFlicker(color, realDotSize, i, initialSpeed_Hz, maxSpeed_Hz));
            CommandFlickerGetResult tempResult = new CommandFlickerGetResult();
            resultTwo.add(tempResult);
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
