package com.hramovnik.reactiontest;

import android.graphics.Color;
import android.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by gshabalev on 11/3/2016.
 */

public class SessionFlicker extends SessionObject {

    private SessionFlicker(){}
    private ArrayList<CommandFlickerGetResult> resultOne = new ArrayList<>();
    private ArrayList<CommandFlickerGetResult> resultTwo = new ArrayList<>();
    private boolean flickerOne = true;
    private int [] colors = new int[2];

    public SessionFlicker(boolean flickerOne, int color, int dotSize, int brightness, int initialSpeed_Hz, int maxSpeed_Hz){
        tasks = new LinkedBlockingQueue<>();
        this.flickerOne = flickerOne;
        colors[0] = Color.RED;
        colors[1] = color;
        int realDotSize = dotSize;

        if (flickerOne){
            CommandFlicker task = new CommandFlicker(Color.RED, realDotSize, (int)(((float)brightness)/12)*255, initialSpeed_Hz, maxSpeed_Hz);
            tasks.add(task);
            CommandFlickerGetResult tempResult = new CommandFlickerGetResult(task);
            resultOne.add(tempResult);
            tasks.add(tempResult);

            task = new CommandFlicker(color, realDotSize, (int)(((float)brightness)/12)*255, initialSpeed_Hz, maxSpeed_Hz);
            tasks.add(task);
            CommandFlickerGetResult tempResult2 = new CommandFlickerGetResult(task);
            resultTwo.add(tempResult2);
            tasks.add(tempResult2);
        }else{

            for (int i = 1; i <= brightness; i++) {
                CommandFlicker task = new CommandFlicker(Color.RED, realDotSize, (int)(((float)i)/5)*255, initialSpeed_Hz, maxSpeed_Hz);
                tasks.add(task);
                CommandFlickerGetResult tempResult = new CommandFlickerGetResult(task);
                resultOne.add(tempResult);
                tasks.add(tempResult);
            }

            for (int i = 1; i <= brightness; i++) {
                CommandFlicker task = new CommandFlicker(color, realDotSize, (int)(((float)i)/5)*255, initialSpeed_Hz, maxSpeed_Hz);
                tasks.add(task);
                CommandFlickerGetResult tempResult = new CommandFlickerGetResult(task);
                resultTwo.add(tempResult);
                tasks.add(tempResult);
            }
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

        LinkedList<Pair<Integer, Integer> > freqList = new LinkedList<>();

        for(int i = 0; (i < resultOne.size())&&(i < resultTwo.size()); i++){
            freqList.add(new Pair<Integer, Integer>(resultOne.get(i).fEnd, resultTwo.get(i).fEnd));
        }

        if (display != null) display.displayResult(freqList, colors, null);
    }




}
