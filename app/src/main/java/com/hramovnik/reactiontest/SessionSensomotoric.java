package com.hramovnik.reactiontest;

import android.util.Pair;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import layout.ParametersActivity;


public final class SessionSensomotoric extends SessionObject {
    private SessionSensomotoric(){}
    private CommandSensomotoricGetResult redResult = null;
    private CommandSensomotoricGetResult greenResult = null;
    public SessionSensomotoric(int serialLen, int dotSize, int firstColor, int secondColor){

        this.serialLen = serialLen;
        tasks = new LinkedBlockingQueue<>();

        realDotSize = dotSize;
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
    int realDotSize = 0;

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }


    @Override
    public void analyze() {

        final LinkedList<Pair<Integer, Integer>> first = new LinkedList<Pair<Integer, Integer>>();
        final LinkedList<Pair<Integer, Integer>> second = new LinkedList<Pair<Integer, Integer>>();

        for (int col = 0; col < 2; col++) {
            CommandSensomotoricGetResult currentResult = (col == 0) ? redResult : greenResult;
            if (currentResult.getResult() == null) {
                if (display != null)
                    display.displayResult("Ошибка анализа - не получено нужное количество достоверных результатов", null);
                return;
            }
            try {
                Hand[] hands = new Hand[2];
                hands[0] = new Hand("Правая");
                hands[1] = new Hand("Левая");

                //первая половина массива для правой, вторая для левой руки

                LinkedList<Pair<Integer, Integer>> inData = (col == 0)?first:second;

                for (int i = 0; i < serialLen; i++) {
                    Pair<Integer, Integer> pair = new Pair<Integer, Integer>(currentResult.dataRight[i], currentResult.dataLeft[i]);
                    inData.add(pair);
                }

            }catch (Exception e){
                MainActivity.display("Ошибка обработки данных " + e.getMessage());
                return;
            }


        }

        //if (display != null) display.displayResult("Готово", null);
        if (display != null) display.displayResult(new Pair<LinkedList<Pair<Integer, Integer>>, LinkedList<Pair<Integer, Integer>>>(first, second), colors, new SessionResultActionInterface() {
            @Override
            public void doSomething() {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("HH-mm-ss", Locale.ROOT);
                try (CsvSaver saver = new CsvSaver("","Сенсомоторный тест " + dateFormatter.format(new Date()))) {

                    LinkedList<String> header = new LinkedList<String>();

                    header.add("Позиция кружка");
                    header.add("Режим (глаз)");
                    header.add("Размер кружка (мм)");
                    header.add("Цвет кружка");
                    header.add("№ проявления");
                    header.add("Время реакции правой руки (мс)");
                    header.add("Время реакции левой руки (мс)");
                    header.add("Пульс");
                    header.add("Оксиометр");

                    saver.save(header);

                    for (int k = 0; k < 2; k++) {
                        for (int i = 0; i < ((k == 0) ? first.size() : second.size()); i++) {
                            LinkedList<String> savable = new LinkedList<String>();
                            savable.add(ParametersActivity.getStringPosition());
                            savable.add(ParametersActivity.getStringEye());
                            savable.add(String.valueOf(realDotSize));
                            savable.add(getColor(colors[k]));
                            savable.add(String.valueOf(i + 1));
                            Pair<Integer, Integer> pair = ((k == 0) ? first.get(i) : second.get(i));
                            savable.add((pair.first >= 0) ? String.valueOf(pair.first) : "-");
                            savable.add((pair.second >= 0) ? String.valueOf(pair.second) : "-");
                            savable.add("-");
                            savable.add("-");
                            saver.save(savable);
                        }
                    }
                }catch (Exception e){

                }
            }
        });


    }

}
