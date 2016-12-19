package com.hramovnik.reactiontest;

/**
 * Created by gshabalev on 11/3/2016.
 */

public class CommandFlickerGetResult extends TaskObject {

    @Override
    public int[] getTask() {
        int[] task = {CMD_REQUEST_FREQSWEEP_DATA};
        return task;
    }


    @Override
    public boolean setResult(int[] result) {
        if (!super.setResult(result)){return false;}
        if (response == RSP_DATA_FREQSWEEP) {return true;}
        return false;
    }




}
