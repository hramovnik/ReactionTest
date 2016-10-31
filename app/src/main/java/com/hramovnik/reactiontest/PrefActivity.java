package com.hramovnik.reactiontest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class PrefActivity extends Activity{

    private SharedPreferences sp;
    EditText textIP;
    EditText textPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        Log.d("Pref", "Pref Create");

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        textIP = (EditText)findViewById(R.id.teIP);
        textPort = (EditText)findViewById(R.id.tePort);

    }

    public void onResume(){
        super.onResume();
            try {
                textPort.setText(String.valueOf(sp.getInt("port", 8080)));
                textIP.setText(sp.getString("ip_address", "192.168.0.10"));
            }catch (Exception e){
                textPort.setText("8080");
                textIP.setText("192.168.0.10");
                sp.edit().clear().apply();
                Log.d("Tag", "Settings loading fail");
            }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuItem mi = menu.add(0,1,0,"Настройки");
        mi.setIntent(new Intent(this, PrefActivity.class));
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public void onPause(){
        super.onPause();
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt("port", Integer.parseInt(textPort.getText().toString()));
            ed.putString("ip_address", textIP.getText().toString());
            ed.apply();
            Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Настройки не сохранены", Toast.LENGTH_SHORT).show();
        }

    }

}
