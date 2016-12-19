package com.hramovnik.reactiontest;

import layout.ParametersActivity;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandTapping extends TaskObject {

    private int [] task = new int[4];
    private CommandTapping(){}
    CommandTapping(int imageIndex, int serialLen){
        super();
        task[0] = CMD_START_TAPPING;
        task[1] = imageIndex;
        task[2] = imageIndex;
        task[3] = serialLen;
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
