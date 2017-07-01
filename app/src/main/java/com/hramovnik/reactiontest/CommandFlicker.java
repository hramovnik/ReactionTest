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
        task[1] = ParametersActivity.isUsingRightEye() ? color : 0;
        task[2] = ParametersActivity.isUsingLeftEye() ? color : 0;
        task[3] = ParametersActivity.isUsingRightEye() ? brightness : 0;
        task[4] = ParametersActivity.isUsingLeftEye() ? brightness : 0;
        task[5] = ParametersActivity.getChosenPosition();
        task[6] = ParametersActivity.getChosenPosition();
        task[7] = dotSize;
        task[8] = dotSize;

        task[9] = initialSpeed_Hz;
        task[10] = maxSpeed_Hz;

        sendSize = task.length;
    }

    @Override
    public int[] getTask() {
        return task;
    }


}
