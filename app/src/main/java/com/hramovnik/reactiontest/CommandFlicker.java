package com.hramovnik.reactiontest;

import android.graphics.Color;

import layout.ParametersActivity;

/**
 * Created by gshabalev on 11/3/2016.
 */

public class CommandFlicker extends TaskObject {

    private int [] task = new int[10];
    private CommandFlicker(){}
    public CommandFlicker(int color, int dotSize, int brightness, int initialSpeed_Hz, int maxSpeed_Hz){
        super();
        task = new int[11];
        task[0] = CMD_START_FREQSWEEP;
        task[1] = color;
        task[2] = color;
        task[3] = ParametersActivity.getChosenPosition();
        task[4] = ParametersActivity.getChosenPosition();
        task[5] = dotSize;
        task[6] = dotSize;
        task[7] = brightness;
        task[8] = brightness;
        task[9] = initialSpeed_Hz;
        task[10] = maxSpeed_Hz;
    }

    @Override
    public int[] getTask() {
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        answer = result;
        if (result == null) return !fail;
        fail = !((answer.length > 0)&&(answer[0]==RSP_TEST_START_OK));
        return !fail;
    }

    @Override
    public String getInterpretation() {
        if(!fail){return "Запущен тест";}
        else {return "Ощибка. Тест не запущен";}
    }

}
