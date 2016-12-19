package com.hramovnik.reactiontest;
import android.graphics.Color;

import layout.ParametersActivity;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandSensomotoric extends TaskObject {

    private int [] task = new int[9];
    private CommandSensomotoric(){}
    CommandSensomotoric(int color, int dotSize, int serialLen, int maxWaitMs){
        super();
        task[0] = CMD_START_SENSOMOTORIC;
        task[1] = color;
        task[2] = color;
        task[3] = ParametersActivity.getChosenPosition();
        task[4] = ParametersActivity.getChosenPosition();
        task[5] = dotSize;
        task[6] = dotSize;
        task[7] = maxWaitMs;
        task[8] = serialLen;
    }

    @Override
    public int[] getTask() {
        return task;
    }

    @Override
    public int getTimeOut() {
        return 2000;
    }

    @Override
    public int getSleeping(){
        return 2000;
    }
}
