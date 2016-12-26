package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandImagesGetResult extends TaskObject{

    private CommandImagesGetResult(){}

    public CommandImagesGetResult(TaskObject sendingCommand){
        dataOffset = sendingCommand.getSendedSize();
    }

    @Override
    public int[] getTask() {
        int [] task = {CMD_REQUEST_IMAGES_DATA};
        sendSize = task.length;
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        if (!super.setResult(result)){return false;}
        if (response == RSP_DATA_IMAGES) {
            if (result.length >= (dataOffset + 3)){
                buttonId = result[dataOffset];
                pulseDataRate = result[dataOffset+1];
                pulseDataOxSaturation = result[dataOffset+2];
                return true;
            }else return false;
        }
        return false;
    }

    public int buttonId = 0;
    public int pulseDataRate = 0;
    public int pulseDataOxSaturation = 0;
    @Override
    public int getTimeOut() {
        return 2000;
    }
}
