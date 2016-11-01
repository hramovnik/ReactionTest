package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 07.10.2016.
 */

public abstract class TaskObject implements TaskExecute {

    protected final int CMD_PING = 0x00;
    protected final int CMD_START_SENSOMOTORIC =0x01;
    protected final int CMD_START_FLICKER1 = 0x02;
    protected final int CMD_START_FLICKER2 = 0x03;
    protected final int CMD_REQUEST_SENSOMOTORIC_RESULT = 0x04;
    protected final int CMD_REQUEST_FLICKER_RESULT = 0x05;
    protected final int CMD_FORCE_STOP_TEST = 0x06;
    protected final int CMD_GET_VBAT = 0x07;
    protected final int CMD_QUERY_PROGRESS = 0x08;
    protected final int CMD_GET_T = 0x09;

    protected final int RSP_PING = 0xFFAA0055;
    protected final int RSP_DATA_NOT_READY  = 0x01;
    protected final int RSP_TEST_DATA_OK = 0x02;
    protected final int RSP_ERROR_UNAVAILABLE = 0x03;
    protected final int RSP_OK = 0x04;
    protected final int RSP_ERROR_IN_PROGRESS = 0x05;
    protected final int RSP_PROGRESS_PECENT = 0x06;

    @Override
    public int[] getResult() {
        return answer;
    }

    @Override
    public void setFailed() {
        fail = true;
    }

    @Override
    public boolean isFailed(){
        return fail;
    }

    @Override
    public int getSleeping(){
        return 0;
    }

    @Override
    public boolean isError() {
        if (answer == null) return false;
        return !((answer.length > 0)&&((answer[0]==RSP_ERROR_IN_PROGRESS)||answer[0]==RSP_ERROR_UNAVAILABLE));
    }

    protected int [] answer = null;
    protected boolean fail = false;
    protected boolean error = false;
}
