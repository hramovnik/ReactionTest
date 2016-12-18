package com.hramovnik.reactiontest;

/**
 * Created by gshabalev on 11/3/2016.
 */

public class CommandFlickerGetResult extends TaskObject {

    @Override
    public int[] getTask() {
        int[] task = new int[1];
        task[0] = CMD_REQUEST_FREQSWEEP_DATA;
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        fail = true;
        answer = result;
        if (result == null) return !fail;
        if (answer.length > 0){
            switch (answer[0]){
                case RSP_DATA_FREQSWEEP:
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
        if (result.length < 3) {return "Получены данные в меньшем объёме";}
        else {return "Получены данные";}
    }
}
