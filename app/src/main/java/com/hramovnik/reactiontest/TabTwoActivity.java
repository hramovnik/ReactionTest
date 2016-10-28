package com.hramovnik.reactiontest;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabTwoActivity extends Fragment implements TaskActivityInterface {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tab_two, container, false);
    }

 /*   @Override
    public void onResume(){
        Log.d("Tag", "Tab2 on resume");
        super.onResume();
    }*/
}
