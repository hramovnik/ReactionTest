package com.hramovnik.reactiontest;
import android.graphics.Color;
/**
 * Created by Hramovnik on 09.10.2016.
 */

public final class CommandSensomotoric extends TaskObject {

    private int [] task = new int[7];
    private CommandSensomotoric(){}
    CommandSensomotoric(int color, int dotSize, int serialLen, int maxWaitMs){
        super();
        task[0] = CMD_START_SENSOMOTORIC;
        task[1] = Color.red(color);
        task[2] = Color.green(color);
        task[3] = Color.blue(color);
        task[4] = dotSize;
        task[5] = serialLen;
        task[6] = maxWaitMs;
    }

    @Override
    public int[] getTask() {
        return task;
    }

    @Override
    public boolean setResult(int[] result) {
        answer = result;
        fail = !((answer.length > 0)&&(answer[0]==RSP_OK));
        return !fail;
    }

    @Override
    public String getInterpretation() {
        if(!fail){return "Запущен тест";}
        else {return "Ощибка. Тест не запущен";}
    }

    @Override
    public int getTimeOut() {
        return 2000;
    }

    @Override
    public int getSleeping(){
        return 2000;
    }
}
