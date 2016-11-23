package com.hramovnik.reactiontest;

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

    private int serialLen = 0;

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }


    @Override
    public void analyze() {
        resultInterpritation.clear();
        LinkedList< Pair<Integer, Integer> > allPairs = new LinkedList<Pair<Integer, Integer>>();

        double [] colorAsymm = new double[4];

        for (int col = 1; col <= 2; col++) {
            int [] result = (col == 1) ? redResult.getResult() : greenResult.getResult();
            if (result == null) {
                if (display!=null) display.displayResult("Ошибка анализа - не получено нужное количество достоверных результатов", this);
                return;
            }
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
                colorAsymm[2*col] += pair.first;

                if (pair.second < 100){hands[1].pre++;}
                else if(pair.second < 500){hands[1].correct.add(pair.second);}
                else {hands[1].post++;}
                colorAsymm[2*col + 1] += pair.second;
            }

            colorAsymm[2*col] /= inData.size();
            colorAsymm[2*col+1] /= inData.size();

            resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                    ": количество валидных представлений", String.valueOf(inData.size())));


            for(int hnumber = 0; hnumber <= 1; hnumber++) { //hnumber - номер руки
                resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                        ": Количество верных реакций " + hands[hnumber].text + " руки", String.valueOf(hands[hnumber].correct.size())));
                resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                        ": Количество преждевременных реакций " + hands[hnumber].text + " руки", String.valueOf(hands[hnumber].pre)));
                resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                        ": Количество запаздывающих реакций " + hands[hnumber].text + " руки", String.valueOf(hands[hnumber].post)));
                resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                        ": Общее число ошибочных реакций " + hands[hnumber].text + " руки", String.valueOf(hands[hnumber].pre + hands[hnumber].post)));
                if (hands[hnumber].correct.size() != 0) {
                    double standDelta = 0;
                    double med = 0;

                    for (Integer value : hands[hnumber].correct) {med += value;}
                    med /= (double) hands[hnumber].correct.size();
                    for (Integer value : hands[hnumber].correct) {
                        med += Math.pow(value - med, 2);
                    }
                    standDelta = Math.sqrt((standDelta / hands[hnumber].correct.size()));

                    double weeple = ((double) (resultInterpritation.size() - hands[hnumber].correct.size())) / (resultInterpritation.size() + hands[hnumber].post + hands[hnumber].pre);
                    resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                            ": Среднее время реакции " + hands[hnumber].text + " руки (мс)", String.valueOf(med)));
                    resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                            ": Стандартное отклонение времени реакции " + hands[hnumber].text + " руки (мс)", String.valueOf(standDelta)));
                    resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                            ": Коэффициент точности Уиппла для " + hands[hnumber].text + " руки", String.valueOf(weeple)));
                }

            }

            int iter = 0;
            for (Pair<Integer, Integer> value :inData){
                resultInterpritation.add(new Pair<String, String>("Цвет " + String.valueOf(col) +
                        ": коэффициент асимметрии для предъявления №" + String.valueOf(iter),
                        String.valueOf((value.second.doubleValue() - value.first.doubleValue())/(value.second.doubleValue() + value.first.doubleValue()))));
                iter++;
            }

            for (Pair<Integer, Integer> value :inData) {allPairs.add(value);}

        }

        double medAsymmRight = 0;
        double medAsymmLeft = 0;

        int stableAsymmetryRight = 0;
        int stableAsymmetryLeft = 0;

        for (Pair<Integer, Integer> value :allPairs){
            medAsymmRight += value.first;
            medAsymmLeft += value.second;

            if (value.first > value.second){stableAsymmetryRight++;}
            else{stableAsymmetryLeft++;}
        }

        medAsymmRight /= allPairs.size();
        medAsymmLeft /= allPairs.size();

        resultInterpritation.add(new Pair<String, String>("Средний коэффициент асимметрии",
                String.valueOf((medAsymmLeft - medAsymmRight)/(medAsymmLeft + medAsymmRight))));

        resultInterpritation.add(new Pair<String, String>("Коэффициент устойчивой асимметрии",
                String.valueOf(((double)(stableAsymmetryLeft-stableAsymmetryRight)*100.)/allPairs.size())));

        resultInterpritation.add(new Pair<String, String>("Цветовой показатель правой руки",
                String.valueOf(((colorAsymm[0] - colorAsymm[2])/(colorAsymm[0] + colorAsymm[2])))));

        resultInterpritation.add(new Pair<String, String>("Цветовой показатель левой руки",
                String.valueOf(((colorAsymm[0] - colorAsymm[2])/(colorAsymm[1] + colorAsymm[3])))));


        StringBuilder builder = new StringBuilder();
        for (Pair<String, String> value:resultInterpritation) {
            builder.append(value.first + " - " + value.second + "\n");}

        if (display!=null) display.displayResult(builder.toString(), this);
    }

}
