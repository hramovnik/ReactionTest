package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 07.10.2016.
 */

public interface TaskExecute {

    public int [] getTask();
    public int [] getResult();
    public boolean setResult(int [] result); //Возвращает true, если данные удовлетворяют
    public void setFailed();
    public boolean isFailed();
    public String getInterpretation();
    public int getTimeOut();
    public int getSleeping();
}
