package layout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;
import com.hramovnik.reactiontest.TaskActivityInterface;


public class TabFiveFragment extends TabFragment implements TaskActivityInterface {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_five, container, false);
    }

    @Override
    public Session getSession() {
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            SharedPreferences.Editor ed = sp.edit();
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
