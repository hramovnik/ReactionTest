package com.hramovnik.reactiontest;

import android.graphics.Color;

/**
 * Created by gshabalev on 11/3/2016.
 */

public class CommandStartFlickerOne extends TaskObject {

    private int [] task = new int[10];
    private CommandStartFlickerOne(){}
    public CommandStartFlickerOne(boolean flickerOne, int color, int dotSize, int brightness, int initialSpeed_Hz, int maxSpeed_Hz, int blackScreenDelay_ms){
        super();
        task = new int[9];
        task[0] = flickerOne?CMD_START_FLICKER1:CMD_START_FLICKER2;
        task[1] = Color.red(color);
        task[2] = Color.green(color);
        task[3] = Color.blue(color);
        task[4] = dotSize;
        task[5] = brightness;
        task[6] = initialSpeed_Hz;
        task[7] = maxSpeed_Hz;
        task[8] = blackScreenDelay_ms;
        task[9] = 0;
    }

    @Override
    public int[] getTask() {
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        answer = result;
        if (result == null) return !fail;
        fail = !((answer.length > 0)&&(answer[0]==RSP_OK));
        return !fail;
    }

    @Override
    public String getInterpretation() {
        if(!fail){return "Запущен тест";}
        else {return "Ощибка. Тест не запущен";}
    }

}
