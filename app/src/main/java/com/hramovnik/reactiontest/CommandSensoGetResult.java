package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandSensoGetResult extends TaskObject{

    private CommandSensoGetResult(){}
    CommandSensoGetResult(int serialLen){
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
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        if (!super.setResult(result)){return false;}
        if (response == RSP_DATA_SENSOMOTORIC) {return true;}
        return false;
    }


    @Override
    public int getTimeOut() {
        return 2000;
    }
}
