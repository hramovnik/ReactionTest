package com.hramovnik.reactiontest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import layout.ParametersActivity;
import layout.PrefActivity;
import layout.ResultDisplay;
import layout.ResultDisplayDiagram;
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

    public Connection connection = null;
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
        ipAddress = "192.168.0.10";
        port = 8080;


        connection.setControls(ipAddress, port, tvResult, progressBar);
        dialogResult = new ResultDisplay();
        SessionObject.setDispalyable(this);

        tvResult.setText(PrefActivity.getProfileResult());
        dialogResultGraph = new ResultDisplayDiagram();
    }
    ResultDisplayDiagram dialogResultGraph;

    @Override
    public void onClick(View v) {
        tvResult.clearComposingText();
        switch (v.getId()){
            case R.id.buttonStartStopTest:
                //dialogResult.show(getSupportFragmentManager(), "Результат", "Bada-bum!");
                dialogResultGraph.show(getSupportFragmentManager(), "Arrr");

                /*if (connection.isWorking()) {
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
                }*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /*MenuItem item = menu.add(0,1,0, "Настройки");
        item.setIntent(new Intent(this, PrefActivity.class));*/
        return super.onCreateOptionsMenu(menu);
    }

    private ResultDisplay dialogResult = null;


    public void displayResult(String value){
        dialogResult.show(getSupportFragmentManager(), "Результат", value);

    }
}
