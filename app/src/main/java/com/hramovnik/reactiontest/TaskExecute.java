package com.hramovnik.reactiontest;


public interface TaskExecute {

    public int [] getTask();
    public int [] getResult();
    public boolean setResult(int [] result); //Возвращает true, если данные удовлетворяют
    public int getTimeOut();
    public int getSleeping();


    public boolean isCriticalError();
    public boolean isInProgress();
}
