package com.hramovnik.reactiontest;

import android.util.Pair;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;


public final class SessionSensomotoric extends SessionObject {
    private SessionSensomotoric(){}
    private CommandSensomotoricGetResult redResult = null;
    private CommandSensomotoricGetResult greenResult = null;
    public SessionSensomotoric(int serialLen, int dotSize, int firstColor, int secondColor){

        this.serialLen = serialLen;
        tasks = new LinkedBlockingQueue<>();

        int realDotSize = dotSize;
        colors[0] = firstColor;
        colors[1] = secondColor;

        CommandSensomotoric task = new CommandSensomotoric(firstColor, 255, realDotSize, serialLen, 1000);
        tasks.add(task);
        redResult = new CommandSensomotoricGetResult(task, serialLen);
        tasks.add(redResult);

        task = new CommandSensomotoric(secondColor, 255, realDotSize, serialLen, 1000);
        tasks.add(task);
        greenResult = new CommandSensomotoricGetResult(task, serialLen);
        tasks.add(greenResult);

        tasksInSession = tasks.size();
        tasksElapsed = tasksInSession;
    }

    private int serialLen = 0;
    private int [] colors = new int[2];

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }


    @Override
    public void analyze() {
        LinkedList<Pair<Integer, Integer>> allPairs = new LinkedList<Pair<Integer, Integer>>();

        for (int col = 1; col <= 2; col++) {
            CommandSensomotoricGetResult currentResult = (col == 1) ? redResult : greenResult;
            if (currentResult.getResult() == null) {
                if (display != null)
                    display.displayResult("Ошибка анализа - не получено нужное количество достоверных результатов", null);
                return;
            }
            Hand[] hands = new Hand[2];
            hands[0] = new Hand("Правая");
            hands[1] = new Hand("Левая");

            int fuckedUpCounter = 0;
            //первая половина массива для правой, вторая для левой руки

            LinkedList<Pair<Integer, Integer>> inData = new LinkedList<Pair<Integer, Integer>>();

            for (int i = 1; i < serialLen; i++) {
                Pair<Integer, Integer> pair = new Pair<Integer, Integer>(currentResult.dataRight[i], currentResult.dataLeft[i]);
                if ((pair.first >= 0) && (pair.second >= 0)) {
                    inData.add(pair);
                } else {
                    fuckedUpCounter++;
                }
            }

            for (Pair<Integer, Integer> pair : inData) {
                if (pair.first < 100) {
                    hands[0].pre++;
                } else if (pair.first < 500) {
                    hands[0].correct.add(pair.first);
                } else {
                    hands[0].post++;
                }

                if (pair.second < 100) {
                    hands[1].pre++;
                } else if (pair.second < 500) {
                    hands[1].correct.add(pair.second);
                } else {
                    hands[1].post++;
                }
            }

            allPairs.addAll(inData);

        }
        if (display != null) display.displayResult(allPairs, colors, null);
    }

}
