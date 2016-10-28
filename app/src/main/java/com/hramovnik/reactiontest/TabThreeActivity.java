package com.hramovnik.reactiontest;

import android.app.TabActivity;
import android.os.Bundle;

public class TabThreeActivity extends TabActivity implements TaskActivityInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_three);
    }
}
