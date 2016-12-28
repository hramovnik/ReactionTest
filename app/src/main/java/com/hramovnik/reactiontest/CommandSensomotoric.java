package com.hramovnik.reactiontest;
import android.graphics.Color;

import layout.ParametersActivity;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandSensomotoric extends TaskObject {

    private int [] task = new int[11];
    private CommandSensomotoric(){}
    CommandSensomotoric(int color, int brightness, int dotSize, int serialLen, int maxWaitMs){
        super();
        task[0] = CMD_START_SENSOMOTORIC;
        task[1] = color;
        task[2] = color;
        task[3] = ParametersActivity.isUsingRightEye() ? brightness : 0;
        task[4] = ParametersActivity.isUsingLeftEye() ? brightness : 0;
        task[5] = ParametersActivity.getChosenPosition();
        task[6] = ParametersActivity.getChosenPosition();
        task[7] = dotSize;
        task[8] = dotSize;
        task[9] = maxWaitMs;
        task[10] = serialLen;

        sendSize = task.length;
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
