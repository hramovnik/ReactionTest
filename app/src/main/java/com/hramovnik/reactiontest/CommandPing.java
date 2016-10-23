package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 08.10.2016.
 */

public final class CommandPing extends TaskObject {
    @Override
    public int[] getTask() {
        int [] massive = new int[1];
        massive[0] = CMD_PING;
        return massive;
    }

    @Override
    public boolean setResult(int[] result) {
        fail = true;
        answer = result;
        if((answer.length > 0)&&(answer[0] == RSP_PING)){
            fail = false;
        }

        return !fail;
    }

    @Override
    public String getInterpretation() {
        if (!fail){
            return "Подключение к прибору активно";
        }else{
            return "Не удаётся установить подключение к прибору";
        }
    }

    @Override
    public int getTimeOut() {
        return 5;
    }
}
