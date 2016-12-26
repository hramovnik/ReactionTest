package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandSensomotoricGetResult extends TaskObject{

    private CommandSensomotoricGetResult(){}
    CommandSensomotoricGetResult(TaskObject sendingCommand, int serialLen){
        dataOffset = sendingCommand.getSendedSize();

        if ((serialLen < 21)&&(serialLen > 0)){
            this.serialLen = serialLen;
        }else{
            this.serialLen = 1;
        }

    }

    private int serialLen = 20;

    @Override
    public int[] getTask() {
        int [] task ={CMD_REQUEST_SENSOMOTORIC_DATA};
        sendSize = task.length;
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        if (!super.setResult(result)){return false;}
        if (response == RSP_DATA_SENSOMOTORIC) {
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

    public int [] dataRight = new int[20];
    public int [] dataLeft = new int[20];

    public int pulseDataRate = 0;
    public int pulseDataOxSaturation = 0;

    @Override
    public int getTimeOut() {
        return 2000;
    }
}
