package com.hramovnik.reactiontest;

/**
 * Created by Hramovnik on 22.10.2016.
 */

public interface Session {
    TaskExecute getNextTask();
    String analyze();

}
