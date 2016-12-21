package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandImages extends TaskObject {

    private int [] task = new int[4];
    private CommandImages(){}
    CommandImages(int imageIndexRight, int imageIndexLeft, int durationTime){
        super();
        task[0] = CMD_START_IMAGES;
        task[1] = imageIndexRight;
        task[2] = imageIndexLeft;
        task[3] = durationTime;
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
        return 1000;
    }
}
