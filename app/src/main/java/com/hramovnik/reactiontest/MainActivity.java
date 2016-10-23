package com.hramovnik.reactiontest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener{

    TextView tvResult;
    TextView tvDescription;
    Button buttonStartStop;

    RadioButton rbSensomotor;
    RadioButton rbKChSM;
    RadioButton rbKChSM2;

    Connection connection = null;
    int task = 0;
    private SharedPreferences sp;
    int port;
    String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = (TextView)findViewById(R.id.tvResult);
        tvDescription = (TextView)findViewById(R.id.tvDescription);
        buttonStartStop = (Button) findViewById(R.id.buttonStartStopTest);;
        buttonStartStop.setOnClickListener(this);

        rbSensomotor = (RadioButton) findViewById(R.id.radioButton1);
        rbSensomotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDescription.setText("Описание Сенсомоторного теста");
                task = 0;
            }
        });
        rbKChSM = (RadioButton) findViewById(R.id.radioButton2);
        rbKChSM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDescription.setText("Описание теста КЧСМ");
                task = 1;
            }
        });
        rbKChSM2 = (RadioButton) findViewById(R.id.radioButton3);
        rbKChSM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDescription.setText("Описание теста КЧСМ - 2");
                task = 2;
            }
        });
        rbSensomotor.callOnClick();
        Log.d("Tag", "onCreate");

        sp = PreferenceManager.getDefaultSharedPreferences(this);



    }

    @Override
    public void onResume(){
        super.onResume();

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


        if(connection == null) {connection = new Connection(ipAddress, port, tvResult);}

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonStartStopTest:
                if (connection.isWorking()) {
                    return;
                }
                switch (task) {
                    case 0:
                        connection.sendSession(new SessionSensomotoric(5));
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onDestroy(){

        try {
            if (connection != null){
                connection.close();
                connection=null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuItem item = menu.add(0,1,0, "Настройки");
        item.setIntent(new Intent(this, PrefActivity.class));
        return super.onCreateOptionsMenu(menu);
    }
}
