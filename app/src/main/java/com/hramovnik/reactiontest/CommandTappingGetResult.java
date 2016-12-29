package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandTappingGetResult extends TaskObject{

    private CommandTappingGetResult(){}
    CommandTappingGetResult(TaskObject sendingCommand, int serialLen){
        dataOffset = sendingCommand.getSendedSize();

        if ((serialLen < 13)&&(serialLen > 0)){
            this.serialLen = serialLen;
        }else{
            this.serialLen = 1;
        }
    }

    @Override
    public int[] getTask() {
        int [] task = {CMD_REQUEST_TAPPING_DATA};
        sendSize = task.length;
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        if (!super.setResult(result)){return false;}
        if (response == RSP_DATA_TAPPING) {
            if (result.length >= (dataOffset + dataRight.length + dataLeft.length + 2)){

            for (int i = 0; i < dataRight.length; i++){
                dataRight[i] = result[dataOffset + i];
            }
            for (int i = 0; i < dataLeft.length; i++){
                dataLeft[i] = result[dataOffset + dataRight.length + i];
            }

            pulseDataRate = result[dataOffset+dataRight.length+dataLeft.length];
            pulseDataOxSaturation = result[dataOffset+dataRight.length+dataLeft.length+1];
            return true;
        }else return false;
        }
        return false;
    }

    public int [] dataRight = new int[12];
    public int [] dataLeft = new int[12];

    public int pulseDataRate = 0;
    public int pulseDataOxSaturation = 0;
    public int serialLen = 12;


    @Override
    public int getTimeOut() {
        return 2000;
    }
}
