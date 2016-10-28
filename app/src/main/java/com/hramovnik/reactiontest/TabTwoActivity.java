package com.hramovnik.reactiontest;

import android.app.TabActivity;
import android.os.Bundle;

public class TabTwoActivity extends TabActivity implements TaskActivityInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_two);
    }
}
