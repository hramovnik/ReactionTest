package com.hramovnik.reactiontest;

/**
 * Created by gshabalev on 11/3/2016.
 */

public class CommandFlickerGetResult extends TaskObject {


    private CommandFlickerGetResult(){}
    public CommandFlickerGetResult(TaskObject sendingCommand, int brightness){
        dataOffset = sendingCommand.getSendedSize();
        this.brightness = brightness;
    }


    @Override
    public int[] getTask() {
        int[] task = {CMD_REQUEST_FREQSWEEP_DATA};
        sendSize = task.length;
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        if (!super.setResult(result)){return false;}
        if (response == RSP_DATA_FREQSWEEP) {
            if (result.length >= (dataOffset + 3)){
            fEnd = result[dataOffset];
            pulseDataRate = result[dataOffset+1];
            pulseDataOxSaturation = result[dataOffset+2];
            return true;
        }else return false;
        }
        return false;
    }

    public int fEnd = 0;
    public int pulseDataRate = 0;
    public int pulseDataOxSaturation = 0;
    public int brightness = 0;
}
