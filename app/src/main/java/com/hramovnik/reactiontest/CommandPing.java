package com.hramovnik.reactiontest;

public final class CommandPing extends TaskObject {
    @Override
    public int[] getTask() {
        int [] task = new int[1];
        task[0] = CMD_PING;
        sendSize = task.length;
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        answer = result;
        if (result == null) return false;
        if (answer.length > 0){response = answer[0];}
        else {return false;}
        return response == RSP_PING;
    }


    @Override
    public int getTimeOut() {
        return 1000;
    }
}
