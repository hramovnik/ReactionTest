package layout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by gshabalev on 11/3/2016.
 */


public class TabFragment extends Fragment {
    protected SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState){
        sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        super.onCreate(savedInstanceState);
    }


    protected SeekBar getSb(final TextView textView, int sbID, final int minimumValue, final int maximumValue, final String tag){
        final SeekBar result = (SeekBar) getView().findViewById(sbID);
        result.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < minimumValue) result.setProgress(minimumValue);
                else textView.setText(String.valueOf(i));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        result.setProgress(sp.getInt(tag, minimumValue));
        result.setMax(maximumValue);
        textView.setText(String.valueOf(sp.getInt(tag, minimumValue)));
        return result;
    }
}

