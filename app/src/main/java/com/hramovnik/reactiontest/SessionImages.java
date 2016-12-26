package com.hramovnik.reactiontest;

import java.util.concurrent.LinkedBlockingQueue;


public final class SessionImages extends SessionObject {
    private SessionImages(){}
    private CommandImagesGetResult result = null;

    public SessionImages(int indexImageRight, int indexImageLeft, int testDuration){
        tasks = new LinkedBlockingQueue<>();

        CommandImages task = new CommandImages(indexImageRight, indexImageLeft, testDuration);
        tasks.add(task);
        result = new CommandImagesGetResult(task);
        tasks.add(result);

        tasksInSession = tasks.size();
        tasksElapsed = tasksInSession;
    }

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }


    @Override
    public void analyze() {
        if (display != null) display.displayResult("Готово", null);
    }

}
