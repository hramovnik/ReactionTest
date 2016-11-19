package com.hramovnik.reactiontest;

import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentManager;
import android.util.Pair;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;


public final class SessionSensomotoric extends SessionObject {
    private SessionSensomotoric(){}
    private CommandSensoGetResult redResult = null;
    private CommandSensoGetResult greenResult = null;
    public SessionSensomotoric(int serialLen, int dotSize, int firstColor, int secondColor){


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

    protected int serialLen = 0;

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }


    @Override
    public void analyze() {
        resultInterpritation.clear();
        for (int col = 1; col <= 2; col++) {
            int [] result = (col == 1) ? redResult.getResult() : greenResult.getResult();

            Hand [] hands = new Hand[2];
            hands[0] = new Hand("правой");
            hands[1] = new Hand("левой");

            int fuckedUpCounter = 0;
            //первая половина массива для правой, вторая для левой руки

            LinkedList< Pair<Integer, Integer> > inData = new LinkedList< Pair<Integer, Integer>>();

            for (int i = 1; i < serialLen; i++) {
                Pair<Integer, Integer> pair = new Pair<Integer, Integer>(result[1 + i], result[21 + i]);
                if ((pair.first >= 0)&&(pair.second >=0)) {inData.add(pair);}
                else{fuckedUpCounter++;}
            }

            for (Pair<Integer, Integer> pair : inData) {
                if (pair.first < 100){hands[0].pre++;}
                else if(pair.first < 500){hands[0].correct.add(pair.first);}
                else {hands[0].post++;}

                if (pair.second < 100){hands[1].pre++;}
                else if(pair.second < 500){hands[1].correct.add(pair.second);}
                else {hands[1].post++;}
            }

            resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                    ": количество валидных представлений", String.valueOf(inData.size())));


            for(int hnumber = 0; hnumber <= 1; hnumber++) {
                resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                        ": Количество верных реакций " + hands[hnumber].text + " руки", String.valueOf(hands[hnumber].correct.size())));
                if (hands[hnumber].correct.size() != 0) {
                    double standDelta = 0;
                    double med = 0;
                    double weeple = 0;
                    for (Integer value : hands[hnumber].correct) {med += value;}
                    med /= (double) hands[hnumber].correct.size();
                    for (Integer value : hands[hnumber].correct) {
                        med += Math.pow(value - med, 2);
                    }
                    standDelta = Math.sqrt((standDelta / hands[hnumber].correct.size()));

                    weeple = ((double) (resultInterpritation.size() - hands[hnumber].correct.size())) / (resultInterpritation.size() + hands[hnumber].post + hands[hnumber].pre);
                    resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                            ": Среднее время реакции " + hands[hnumber].text + " руки (мс)", String.valueOf(med)));

                    resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                            ": Стандартное отклонение времени реакции " + hands[hnumber].text + " руки (мс)", String.valueOf(standDelta)));
                    resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                            ": Коэффициент точности Уиппла для " + hands[hnumber].text + " руки", String.valueOf(weeple)));
                }

            }

        }


        StringBuilder builder = new StringBuilder();
        for (Pair<String, String> value:resultInterpritation) {
            builder.append(value.first + " - " + value.second + "\n");
        }
        if(display!=null) display.displayResult(builder.toString(),this);
    }

}
