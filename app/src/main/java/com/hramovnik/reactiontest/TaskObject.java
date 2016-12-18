package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 07.10.2016.
 */

public abstract class TaskObject implements TaskExecute {

    //Ping, most basic command
    static final int CMD_PING                            = 0x00;

    //"Start test" command block
    static final int CMD_START_IMAGES                    = 0x01;
    static final int CMD_START_TAPPING                   = 0x02;
    static final int CMD_START_SENSOMOTORIC              = 0x03;
    static final int CMD_START_FREQSWEEP                 = 0x04;

    //Test management command block
    static final int CMD_QUERY_TEST_PROGRESS             = 0x05;
    static final int CMD_FORCE_STOP_TEST                 = 0x06;

    //"Request data" command block
    static final int CMD_REQUEST_IMAGES_DATA             = 0x07;
    static final int CMD_REQUEST_TAPPING_DATA            = 0x08;
    static final int CMD_REQUEST_SENSOMOTORIC_DATA       = 0x09;
    static final int CMD_REQUEST_FREQSWEEP_DATA          = 0x0A;

    //System facilities command block
    static final int CMD_GET_VBAT                        = 0x0B;
    static final int CMD_GET_TEMPERATURE                 = 0x0C;

    //Ping response
    static final int RSP_PING                            = 0xFFAA0055;

    //Response to any command from "start test" block
    //Indicates if the test routine was able to start correctly
    static final int RSP_TEST_START_OK                   = 0x10;
    static final int RSP_TEST_START_FAIL                 = 0x20;
    //What if you'll try to start a test while other is currently running?
    static final int RSP_OTHER_TEST_IN_PROGRESS          = 0x30;

    //Test management responses
    static final int RSP_TEST_PROGRESS                   = 0x40;
    static final int RSP_TEST_STOPPED                    = 0x50;

    //Responses to data request commands
    static final int RSP_DATA_NOT_READY                  = 0x60;
    static final int RSP_DATA_IMAGES                     = 0x70;
    static final int RSP_DATA_TAPPING                    = 0x80;
    static final int RSP_DATA_SENSOMOTORIC               = 0x90;
    static final int RSP_DATA_FREQSWEEP                  = 0xA0;

    //Responses to system data requests
    static final int RSP_VBAT                            = 0xB0;
    static final int RSP_LOCAL_TEMPERATURE               = 0xC0;

    //Returned if the server was unable to identify the command
    static final int RSP_UNKNOWN_COMMAND                 = 0xFF00FF00;



    @Override
    public int[] getResult() {
        return answer;
    }

    @Override
    public int getSleeping(){
        return 0;
    }


    @Override
    public int getTimeOut() {
        return 2000;
    }

/*    @Override
    public boolean isError() {
        if (answer == null) return false;
        return ((answer.length > 0)&&((answer[0]==RSP_TEST_PROGRESS)||answer[0]==RSP_DATA_NOT_READY));
    }
*/
    protected int [] answer = null;
    protected boolean fail = false;
}
