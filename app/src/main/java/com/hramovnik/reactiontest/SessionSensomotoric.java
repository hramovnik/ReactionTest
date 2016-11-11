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

            for (int i = 0; i < serialLen; i++) {
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

            double medRight = 0;
            for (Integer value:rightCorrect) {medRight+=value;}
            medRight /= (double) rightCorrect.size();

            double medLeft = 0;
            for (Integer value:leftCorrect) {medLeft+=value;}
            medLeft /= (double) leftCorrect.size();


            double standDeltaLeft = 0;
            double standDeltaRight = 0;

            for (int i = 0; i < serialLen; i++) {
                if (result[1 + i] >= 0) {
                    standDeltaRight += Math.pow(result[1 + i] - medRight, 2);
                }
                if (result[21 + i] >= 0) {
                    standDeltaLeft += Math.pow(result[21 + i] - medLeft, 2);
                }
            }

            standDeltaLeft = Math.sqrt((standDeltaLeft / (serialLen - leftFails - 1)));
            standDeltaRight = Math.sqrt((standDeltaRight / (serialLen - rightFails - 1)));


            builder.append("Среднее время реакции левой руки " + medLeft + " мс\n");
            builder.append("Среднее время реакции правой руки " + medRight + " мс\n");
            builder.append("Стандартное отклонение времени реакции левой руки " + standDeltaLeft + " мс\n");
            builder.append("Стандартное отклонение время реакции правой руки " + standDeltaRight + " мс\n");

        }





        resultInterpritation.clear();



        StringBuilder builder = new StringBuilder();



        int [] mass = redResult.getResult();
        StringBuilder string = new StringBuilder();
        string.append("First: ");
        for(int i = 0 ; i < mass.length; i++){
            string.append(String.valueOf(mass[i]) + " ");
        }
        string.append("\nSecond: ");
        mass = greenResult.getResult();
        for(int i = 0 ; i < mass.length; i++){
            string.append(String.valueOf(mass[i]) + " ");
        }

        display.displayResult(string.toString());
    }




}
