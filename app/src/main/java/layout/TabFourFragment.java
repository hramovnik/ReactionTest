package layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;
import com.hramovnik.reactiontest.TaskActivityInterface;


public class TabFourFragment extends TabFragment implements TaskActivityInterface {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tab_four, container, false);
    }

    @Override
    public Session getSession() {
        return null;
    }

    private SeekBar sbInterval;
    private TextView teInterval;
    private final String intervalTag = "TT_INTERVAL";

    private SeekBar sbSubInterval;
    private TextView teSubInterval;
    private final String subintervalTag = "TT_SUBINTERVAL";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        teInterval = (TextView) getView().findViewById(R.id.teInterval);
        sbInterval = getSb(teInterval, R.id.sbInterval, 1,5,intervalTag);

        teSubInterval = (TextView) getView().findViewById(R.id.teSubInterval);
        sbSubInterval = getSb(teSubInterval, R.id.sbSubInterval, 10,30,subintervalTag);


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt(intervalTag, sbInterval.getProgress());
            ed.putInt(subintervalTag, sbSubInterval.getProgress());

            ed.apply();

        }catch (Exception e){

        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public  void onPause(){
        super.onPause();
    }



}