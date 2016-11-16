package com.hramovnik.reactiontest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import layout.TabFourFragment;
import layout.TabOneFragment;
import layout.TabThreeFragment;
import layout.TabTwoFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    TextView tvResult;
    Button buttonStartStop;
    FragmentTabHost tabs;
    ProgressBar progressBar;

    public static Connection connection = new Connection();
    private SharedPreferences sp;
    int port;
    String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
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

        tabs = (FragmentTabHost) findViewById(R.id.tabHost);
        tabs.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabs.clearAllTabs();

        tabs.addTab(tabs.newTabSpec("tab1").setIndicator("СМТ"), TabOneFragment.class, null);
        tabs.addTab(tabs.newTabSpec("tab2").setIndicator("КЧСМ"), TabTwoFragment.class, null);
        tabs.addTab(tabs.newTabSpec("tab3").setIndicator("КЧСМ Авто"), TabThreeFragment.class, null);
        tabs.addTab(tabs.newTabSpec("tab4").setIndicator("ТТ"), TabFourFragment.class, null);

        for(int i = 0; i < 4; i++){
            ((TextView) tabs.getTabWidget().getChildAt(i).findViewById(android.R.id.title)).setTextSize(8);
        }

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        try {

            ipAddress = sp.getString("ip_address", "192.168.0.10");
            port = sp.getInt("port", 8080);
            if ((port > 0xffff)||(port < 1024)) port = 8080;

            Log.d("Tag", ipAddress + " " + port);
        }catch (Exception e){
            Toast.makeText(this, "Ошибка загрузки настроек", Toast.LENGTH_SHORT).show();
            ipAddress = "192.168.0.10";
            port = 8080;
        }

        connection.setControles(ipAddress, port, tvResult, progressBar);
    }

    @Override
    public void onClick(View v) {


        tvResult.clearComposingText();
        switch (v.getId()){
            case R.id.buttonStartStopTest:
                if (connection.isWorking()) {
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
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuItem item = menu.add(0,1,0, "Настройки");
        item.setIntent(new Intent(this, PrefActivity.class));
        return super.onCreateOptionsMenu(menu);
    }
}
