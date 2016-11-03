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
        int [] task = new int[1];
        task[0] = CMD_REQUEST_SENSOMOTORIC_RESULT;
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        fail = true;
        answer = result;
        if (result == null) return !fail;
        if (answer.length > 0){
            switch (answer[0]){
                case RSP_TEST_DATA_OK:
                    fail = false;
                    break;
                case RSP_DATA_NOT_READY:
                    fail = true;
                    break;
                default:
                    fail = true;
            }
        }

        return !fail;
    }

    @Override
    public String getInterpretation() {
            int[] result = getResult();
            if ((fail)||(result == null)) {return "Ошибка получения данных";}
            if (result.length < 41) {return "Получены данные в меньшем объёме";}
            else {return "Получены данные";}
    }

    @Override
    public int getTimeOut() {
        return 2000;
    }
}
