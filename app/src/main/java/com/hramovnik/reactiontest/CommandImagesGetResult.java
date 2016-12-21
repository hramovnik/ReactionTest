package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandImagesGetResult extends TaskObject{


    CommandImagesGetResult(){}

    @Override
    public int[] getTask() {
        int [] task = {CMD_REQUEST_IMAGES_DATA};
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        if (!super.setResult(result)){return false;}
        if (response == RSP_DATA_IMAGES) {return true;}
        return false;
    }


    @Override
    public int getTimeOut() {
        return 2000;
    }
}
