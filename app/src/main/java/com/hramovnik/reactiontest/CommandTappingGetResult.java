package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandTappingGetResult extends TaskObject{

    private CommandTappingGetResult(){}
    CommandTappingGetResult(int serialLen){
        if ((serialLen < 13)&&(serialLen > 0)){
            this.serialLen = serialLen;
        }else{
            this.serialLen = 1;
        }
    }
    private int serialLen = 12;
    @Override
    public int[] getTask() {
        int [] task = {CMD_REQUEST_TAPPING_DATA};
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        if (!super.setResult(result)){return false;}
        if (response == RSP_DATA_TAPPING) {return true;}
        return false;
    }


    @Override
    public int getTimeOut() {
        return 2000;
    }
}
