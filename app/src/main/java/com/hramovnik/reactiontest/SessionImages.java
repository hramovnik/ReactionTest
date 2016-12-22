package com.hramovnik.reactiontest;

import java.util.concurrent.LinkedBlockingQueue;


public final class SessionImages extends SessionObject {
    private SessionImages(){}
    private CommandImagesGetResult result = null;

    public SessionImages(int indexImageRight, int indexImageLeft){
        tasks = new LinkedBlockingQueue<>();

        tasks.add(new CommandImages(indexImageRight, indexImageLeft, 10000));
        result = new CommandImagesGetResult();
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
