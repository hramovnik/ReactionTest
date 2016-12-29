package com.hramovnik.reactiontest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import layout.ParametersActivity;
import layout.PrefActivity;
import layout.ResultDisplay;
import layout.ResultDisplayDiagram;
import layout.ResultDisplayGraphic;
import layout.TabFourFragment;
import layout.TabOneFragment;
import layout.TabThreeFragment;
import layout.TabTwoFragment;
import layout.TabFiveFragment;

public class MainActivity extends FragmentActivity implements ResultDisplayable, View.OnClickListener{

    TextView tvResult;
    Button buttonStartStop;
    FragmentTabHost tabs;
    ProgressBar progressBar;
    Button buttonProfiles;
    Button buttonParameters;

    Connection connection = null;
    private SharedPreferences sp;
    int port;
    String ipAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        connection = new Connection();
        ParametersActivity.loadStaticData(this);
        PrefActivity.loadStaticData(this);
        Log.d("Tag", "Main activity created");

        tvResult = (TextView) findViewById(R.id.tvResult);
        buttonStartStop = (Button) findViewById(R.id.buttonStartStopTest);
        buttonStartStop.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonProfiles = (Button) findViewById(R.id.buttonOpenProfile);
        buttonProfiles.setOnClickListener(this);
        buttonParameters = (Button) findViewById(R.id.buttonOpenParameters);
        buttonParameters.setOnClickListener(this);

        tabs = (FragmentTabHost) findViewById(R.id.tabHost);
        tabs.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabs.clearAllTabs();

        tabs.addTab(tabs.newTabSpec("tab1").setIndicator("СМТ"), TabOneFragment.class, null);
        tabs.addTab(tabs.newTabSpec("tab2").setIndicator("КЧСМ"), TabTwoFragment.class, null);
        tabs.addTab(tabs.newTabSpec("tab3").setIndicator("КЧСМ Авто"), TabThreeFragment.class, null);
        tabs.addTab(tabs.newTabSpec("tab4").setIndicator("ТТ"), TabFourFragment.class, null);
        tabs.addTab(tabs.newTabSpec("tab5").setIndicator("ОДПГМ"), TabFiveFragment.class, null);

        for(int i = 0; i < 5; i++){
            ((TextView) tabs.getTabWidget().getChildAt(i).findViewById(android.R.id.title)).setTextSize(12);
        }
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        ipAddress = "192.168.0.16";
        port = 8080;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
    }


    @Override
    public void onResume(){
        super.onResume();

        connection.setControls(ipAddress, port, getBaseContext(), progressBar);
        dialogResult = new ResultDisplay();
        SessionObject.setDispalyable(this);

        tvResult.setText(PrefActivity.getProfileResult());
        activity = this;
    }




    @Override
    public void onClick(View v) {
        tvResult.clearComposingText();
        switch (v.getId()){
            case R.id.buttonStartStopTest:

                /*final LinkedList<Pair<Integer, Integer>> l = new LinkedList<>();
                for (int i = -100; i < 1000 ; i+= 100){
                    l.add(new Pair<Integer, Integer>(i/2,i));
                }
                Pair<LinkedList<Pair<Integer, Integer> > , LinkedList<Pair<Integer, Integer> >> p = new Pair<>(l,l);
                final int [] c = new int[2];
                c[0] = Color.RED;
                c[1] = Color.YELLOW;
                displayResult(l,c,new SessionResultActionInterface() {
                    @Override
                    public void doSomething() {
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh-mm-ss", Locale.ROOT);
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
                                for (int i = 0; i < ((k == 0) ? l.size() : l.size()); i++) {
                                    LinkedList<String> savable = new LinkedList<String>();
                                    savable.add(ParametersActivity.getStringPosition());
                                    savable.add(ParametersActivity.getStringEye());
                                    savable.add(String.valueOf(3));
                                    savable.add(SessionObject.getColor(c[k]));
                                    savable.add(String.valueOf(i + 1));
                                    Pair<Integer, Integer> pair = ((k == 0) ? l.get(i) : l.get(i));
                                    savable.add((pair.first >= 0) ? String.valueOf(pair.first) : "-");
                                    savable.add((pair.second >= 0) ? String.valueOf(pair.second) : "-");
                                    savable.add("-");
                                    savable.add("-");
                                    saver.save(savable);
                                }
                            }
                        }catch (Exception e){

                        }
                    }});*/


                if (connection.isWorking() != null) {
                    return;
                }
                Fragment currentTab = getSupportFragmentManager().findFragmentByTag(tabs.getCurrentTabTag());
                if (currentTab instanceof TaskActivityInterface){
                 Session session = ((TaskActivityInterface) currentTab).getSession();
                    if (session!=null){
                        connection.sendSession(session);
                    }else{
                        Toast.makeText(this, "Нереализованное действие", Toast.LENGTH_SHORT).show();
                   }
                }else{
                    Toast.makeText(this, "Ошибка реализации", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonOpenProfile:
                startActivity(new Intent(this, PrefActivity.class));
                break;
            case R.id.buttonOpenParameters:
                startActivity(new Intent(this, ParametersActivity.class));
                break;
            default:
                Toast.makeText(this, "Случилось что-то странное", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        connection.setControls(ipAddress, port, null, null);
        SessionObject.setDispalyable(null);
        super.onDestroy();
    }


    private ResultDisplay dialogResult = null;
    static private MainActivity activity = null;

    public static void display(String value){
        activity.dialogResult.show(activity.getSupportFragmentManager(), "Ошибка", value, null);
    }


    @Override
    public void displayResult(String value, SessionResultActionInterface action){
        dialogResult.show(getSupportFragmentManager(), "Результат", value, action);
    }

    @Override
    public void displayResult(LinkedList<Pair<Integer, Integer>>dataList, int [] colors, SessionResultActionInterface action){
        ResultDisplayDiagram.setData(dataList,colors, action);
        startActivity(new Intent(this, ResultDisplayDiagram.class));
    }

    @Override
    public void displayResult(Pair<LinkedList<Pair<Integer, Integer> > , LinkedList<Pair<Integer, Integer> > > dataList, int [] colors, SessionResultActionInterface action){
        ResultDisplayGraphic.setData(dataList, colors, action);
        startActivity(new Intent(this, ResultDisplayGraphic.class));
    }
}
