package com.hramovnik.reactiontest;

import android.graphics.Color;
import android.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import layout.ParametersActivity;


public final class SessionTapping extends SessionObject {
    private SessionTapping(){}
    private CommandTappingGetResult result = null;

    public SessionTapping(int imageIndex, int serialLen){
        this.serialLen = serialLen;
        this.imageIndex = imageIndex;
        tasks = new LinkedBlockingQueue<>();

        CommandTapping task = new CommandTapping(imageIndex, serialLen);
        tasks.add(task);
        result = new CommandTappingGetResult(task, serialLen);
        tasks.add(result);

        tasksInSession = tasks.size();
        tasksElapsed = tasksInSession;
    }

    private int serialLen = 0;
    private int imageIndex = 0;

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }


    @Override
    public void analyze() {
        LinkedList<Pair<Integer, Integer> > inData = new LinkedList<Pair<Integer, Integer> >();
        for (int i = 0; i < serialLen; i++) {
            Pair<Integer, Integer> pair = new Pair<Integer, Integer>(result.dataRight[i], result.dataLeft[i]);
            if ((pair.first >= 0) && (pair.second >= 0)) {inData.add(pair);}
        }

        int [] colors = new int [2];
        colors[0] = Color.RED;
        colors[1] = Color.BLUE;

        if (display != null) display.displayResult(inData, colors,true,new SessionResultActionInterface() {
            @Override
            public void doSomething() {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("HH-mm-ss", Locale.ROOT);
                try(CsvSaver saver = new CsvSaver("","ТТ " + dateFormatter.format(new Date()))) {
                    LinkedList<String> header = new LinkedList<String>();

                    header.add("№ итерации");
                    header.add("Количество нажатий правой рукой");
                    header.add("Количество нажатий левой рукой");
                    header.add("Код изображения");
                    header.add("Пульс");
                    header.add("Оксиометр");
                    saver.save(header);

                    for (int i = 0; i < result.serialLen; i++) {
                        LinkedList<String> savable = new LinkedList<String>();

                        savable.add(String.valueOf(i + 1));
                        savable.add(String.valueOf(String.valueOf(result.dataRight[i])));
                        savable.add(String.valueOf(String.valueOf(result.dataLeft[i])));
                        savable.add(String.valueOf(imageIndex));
                        savable.add("-");
                        savable.add("-");
                        saver.save(savable);
                    }
                }catch (Exception e){

                }

            }
        });
    }

}
