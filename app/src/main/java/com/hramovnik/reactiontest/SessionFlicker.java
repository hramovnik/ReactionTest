package com.hramovnik.reactiontest;

import android.graphics.Color;
import android.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import layout.ParametersActivity;


/**
 * Created by gshabalev on 11/3/2016.
 */

public class SessionFlicker extends SessionObject {

    private SessionFlicker(){}
    private ArrayList<CommandFlickerGetResult> resultOne = new ArrayList<>();
    private ArrayList<CommandFlickerGetResult> resultTwo = new ArrayList<>();
    private boolean flickerOne = true;
    private int dotSize;
    private int [] colors = new int[2];
    int realDotSize = 0;

    public SessionFlicker(boolean flickerOne, int color, int dotSize, int brightness, int initialSpeed_Hz, int maxSpeed_Hz){
        tasks = new LinkedBlockingQueue<>();
        this.flickerOne = flickerOne;
        this.dotSize = dotSize;
        colors[0] = Color.RED;
        colors[1] = color;
        realDotSize = dotSize;

        if (flickerOne){
            int br = (int)((((float)brightness)/12.)*255.);
            CommandFlicker task = new CommandFlicker(colors[0], realDotSize, br, initialSpeed_Hz, maxSpeed_Hz);
            tasks.add(task);
            CommandFlickerGetResult tempResult = new CommandFlickerGetResult(task,br);
            resultOne.add(tempResult);
            tasks.add(tempResult);

            task = new CommandFlicker(colors[1], realDotSize, br, initialSpeed_Hz, maxSpeed_Hz);
            tasks.add(task);
            CommandFlickerGetResult tempResult2 = new CommandFlickerGetResult(task,br);
            resultTwo.add(tempResult2);
            tasks.add(tempResult2);
        }else{

            for (int i = 1; i <= brightness; i++) {
                int br = (int)((((float)i)/6.)*255.);
                CommandFlicker task = new CommandFlicker(colors[0], realDotSize, br, initialSpeed_Hz, maxSpeed_Hz);
                tasks.add(task);
                CommandFlickerGetResult tempResult = new CommandFlickerGetResult(task,br);
                resultOne.add(tempResult);
                tasks.add(tempResult);
            }

            for (int i = 1; i <= brightness; i++) {
                int br = (int)((((float)i)/6.)*255.);
                CommandFlicker task = new CommandFlicker(colors[1], realDotSize, br, initialSpeed_Hz, maxSpeed_Hz);
                tasks.add(task);
                CommandFlickerGetResult tempResult = new CommandFlickerGetResult(task,br);
                resultTwo.add(tempResult);
                tasks.add(tempResult);
            }
        }
        tasksInSession = tasks.size();
        tasksElapsed = tasksInSession;
    }

    @Override
    public TaskExecute getNextTask() {
        return tasks.poll();
    }

    @Override
    public void analyze() {
        /*int [] mass = result.getResult();
        StringBuilder string = new StringBuilder();
        for(int i = 0 ; i < mass.length; i++){
            string.append(String.valueOf(mass[i]) + " ");
        }

        if(display!=null) display.displayResult(string.toString(),this);*/

        LinkedList<Pair<Integer, Integer> > freqList = new LinkedList<>();

        for(int i = 0; (i < resultOne.size())&&(i < resultTwo.size()); i++){
            freqList.add(new Pair<Integer, Integer>(resultOne.get(i).fEnd, resultTwo.get(i).fEnd));
        }

        if (display != null) display.displayResult(freqList, colors, false, new SessionResultActionInterface() {
            @Override
            public void doSomething() {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("HH-mm-ss", Locale.ROOT);
                try (CsvSaver saver = new CsvSaver("","КЧСМ " + dateFormatter.format(new Date()))) {

                    LinkedList<String> header = new LinkedList<String>();

                    header.add("Позиция кружка");
                    header.add("Режим (глаз)");
                    header.add("Яркость пятна (0-255)");
                    header.add("Размер кружка (мм)");
                    header.add("Цвет кружка");
                    header.add("Частота слияния (Гц)");
                    header.add("Пульс");
                    header.add("Оксиометр");

                    saver.save(header);

                    for (int k = 0; k < 2; k++) {
                        ArrayList<CommandFlickerGetResult> result = ((k == 0) ? resultOne : resultTwo);
                        for (int i = 0; i < result.size(); i++) {
                            CommandFlickerGetResult currentGR = result.get(i);
                            LinkedList<String> savable = new LinkedList<String>();

                            savable.add(ParametersActivity.getStringPosition());
                            savable.add(ParametersActivity.getStringEye());
                            savable.add(String.valueOf(currentGR.brightness));
                            savable.add(String.valueOf(realDotSize));
                            savable.add(getColor(colors[k]));
                            savable.add(currentGR.fEnd > 0 ? String.valueOf(currentGR.fEnd) : "-");
                            savable.add(currentGR.pulseDataRate > 0 ? String.valueOf(currentGR.pulseDataRate) : "-");
                            savable.add(currentGR.pulseDataOxSaturation > 0 ? String.valueOf(currentGR.pulseDataOxSaturation) : "-");
                            saver.save(savable);
                        }
                    }
                }catch (Exception e){

                }
            }
        });

    }




}
