package layout;

import android.content.Intent;
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
    private ResultDisplay dialogResult;
    final protected int DIALOG_RESULT = 0xfff;

    @Override
    public void onCreate(Bundle savedInstanceState){
        sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        super.onCreate(savedInstanceState);
        dialogResult = new ResultDisplay();
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

    public void setResult(String value){
        dialogResult.setTargetFragment(this, DIALOG_RESULT);
        dialogResult.setText(value);
        dialogResult.show(getFragmentManager(), "Результат");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case DIALOG_RESULT:
                saveResult();
                break;
            default:

        }
    }

    protected void saveResult(){

    }
}

