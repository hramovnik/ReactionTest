package com.hramovnik.reactiontest;

import android.util.Pair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public final class SessionSensomotoric extends SessionObject {
    private SessionSensomotoric(){}
    private CommandSensoGetResult redResult = null;
    private CommandSensoGetResult greenResult = null;
    public SessionSensomotoric(ResultDisplayable display, int serialLen, int dotSize, int firstColor, int secondColor){
        setDispalyable(display);
        this.serialLen = serialLen;
        tasks = new LinkedBlockingQueue<>();

        int realDotSize = (int) ((float) dotSize / 0.219 / 2);

        tasks.add(new CommandSensomotoric(firstColor, realDotSize, serialLen, 1000));
        redResult = new CommandSensoGetResult(serialLen);
        tasks.add(redResult);

        tasks.add(new CommandSensomotoric(secondColor, realDotSize, serialLen, 1000));
        greenResult = new CommandSensoGetResult(serialLen);
        tasks.add(greenResult);

        tasksInSession = tasks.size();
        tasksElapsed = tasksInSession;
    }

    int serialLen = 0;

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }

    @Override
    public void analyze() {
        resultInterpritation.clear();

        for (int col = 1; col <= 2; col++) {
            int [] result = (col == 1) ? redResult.getResult() : greenResult.getResult();

            int leftPre = 0;
            int rightPre = 0;
            int leftPost = 0;
            int rightPost = 0;
            LinkedList<Integer> leftCorrect = new LinkedList<Integer>();
            LinkedList<Integer> rightCorrect = new LinkedList<Integer>();

            int fuckedUpCounter = 0;

            //первая половина массива для правой, вторая для левой руки

            LinkedList< Pair<Integer, Integer> > inData = new LinkedList< Pair<Integer, Integer>>();

            for (int i = 1; i < serialLen; i++) {
                Pair<Integer, Integer> pair = new Pair<Integer, Integer>(result[1 + i], result[21 + i]);
                if ((pair.first >= 0)&&(pair.second >=0)) {inData.add(pair);}
                else{fuckedUpCounter++;}
            }

            for (Pair<Integer, Integer> pair : inData) {
                if (pair.first < 100){rightPre++;}
                else if(pair.first < 500){rightCorrect.add(pair.first);}
                else {rightPost++;}

                if (pair.second < 100){leftPre++;}
                else if(pair.second < 500){leftCorrect.add(pair.second);}
                else {leftPost++;}
            }

            double standDeltaRight = 0;
            double medRight = 0;
            if (rightCorrect.size() != 0) {
                for (Integer value : rightCorrect) {
                    medRight += value;
                }
                medRight /= (double) rightCorrect.size();
                for (Integer value : rightCorrect) {
                    medRight += Math.pow(value - medRight, 2);
                }
                standDeltaRight = Math.sqrt((standDeltaRight / rightCorrect.size()));
            }

            double standDeltaLeft = 0;
            double medLeft = 0;
            if (leftCorrect.size() != 0) {

                for (Integer value : leftCorrect) {
                    medLeft += value;
                }
                medLeft /= (double) leftCorrect.size();
                for (Integer value : leftCorrect) {
                    standDeltaLeft += Math.pow(value - medLeft, 2);
                }
                standDeltaLeft = Math.sqrt((standDeltaLeft / leftCorrect.size()));
            }

            resultInterpritation.add(new Pair<String, Double>("Цвет " + String.valueOf(col) +
                    ": Среднее время реакции левой руки (мс)", medLeft));
            resultInterpritation.add(new Pair<String, Double>("Цвет " + String.valueOf(col) +
                    ": Среднее время реакции правой руки (мс)", medRight));
            resultInterpritation.add(new Pair<String, Double>("Цвет " + String.valueOf(col) +
                    ": Стандартное отклонение времени реакции левой руки (мс)", standDeltaLeft));
            resultInterpritation.add(new Pair<String, Double>("Цвет " + String.valueOf(col) +
                    ": Стандартное отклонение время реакции правой руки (мс)", standDeltaRight));

        }


        StringBuilder builder = new StringBuilder();
        for (Pair<String, Double> value:resultInterpritation) {
            builder.append(value.first + " - " + String.valueOf(value.second) + "\n");
        }
        display.displayResult(builder.toString());
    }

}
