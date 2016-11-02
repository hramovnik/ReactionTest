package layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;
import com.hramovnik.reactiontest.SessionSensomotoric;
import com.hramovnik.reactiontest.TaskActivityInterface;

public class TabTwoFragment extends Fragment implements TaskActivityInterface, View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_two, container, false);
    }

    @Override
    public Session getSession() {
        return null;
    }

    private DialogFragment dialogChooseColor;

    private Button buttonChooseColor;

    public static final int DIALOG_ONE = 1;

    private SharedPreferences sp;

    private int color = Color.GREEN;

    private SeekBar sbBrightness;
    private TextView teBrightness;

    private SeekBar sbRoundSize;
    private TextView teRoundSize;

    private SeekBar sbFrequency;
    private TextView teFrequency;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonChooseColor = (Button) getView().findViewById(R.id.buttonColor);
        buttonChooseColor.setOnClickListener(this);
        buttonChooseColor.setBackgroundColor(color);


        teRoundSize = (TextView) getView().findViewById(R.id.teRoundSize);
        sbRoundSize = (SeekBar) getView().findViewById(R.id.sbRoundSize);
        sbRoundSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) sbRoundSize.setProgress(1);
                else teRoundSize.setText(String.valueOf(i));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sbRoundSize.setProgress(sp.getInt("KCHSM_Round_SIZE", 3));
        teRoundSize.setText(String.valueOf(sp.getInt("KCHSM_Round_SIZE", 3)));


        teBrightness = (TextView) getView().findViewById(R.id.teBrightness);
        sbBrightness = (SeekBar) getView().findViewById(R.id.sbBrightness);
        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) sbBrightness.setProgress(1);
                else teBrightness.setText(String.valueOf(i));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sbBrightness.setProgress(sp.getInt("KCHSM_BRIGHTNESS", 3));
        teBrightness.setText(String.valueOf(sp.getInt("KCHSM_BRIGHTNESS", 3)));

        teFrequency = (TextView) getView().findViewById(R.id.teFrequency);
        sbFrequency = (SeekBar) getView().findViewById(R.id.sbFrequency);
        sbFrequency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) sbFrequency.setProgress(1);
                else teFrequency.setText(String.valueOf(i));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sbFrequency.setProgress(sp.getInt("KCHSM_FREQUENCY", 3));
        teFrequency.setText(String.valueOf(sp.getInt("KCHSM_FREQUENCY", 3)));


        dialogChooseColor = new ColorChooser();


    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        super.onCreate(savedInstanceState);

        color = sp.getInt("KCHSM_Color_ONE", Color.GREEN);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt("SMT_Color_ONE", colorOne);
            ed.putInt("SMT_Color_TWO", colorTwo);
            ed.putInt("SMT_Round_SIZE", sbSizeChooser.getProgress());
            ed.putInt("SMT_QUANTITY_REP", sbQuantityRep.getProgress());

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.buttonColorOneChooser):
                dialogChooseColorOne.setTargetFragment(this, DIALOG_ONE);
                dialogChooseColorOne.show(getFragmentManager(), "Выберите цвет");
                break;
            case (R.id.buttonColorTwoChooser):
                dialogChooseColorTwo.setTargetFragment(this, DIALOG_TWO);
                dialogChooseColorTwo.show(getFragmentManager(), "Выберите цвет");
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case DIALOG_ONE:
                colorOne = resultCode;
                buttonChooseColorOne.setBackgroundColor(colorOne);
                break;
            case DIALOG_TWO:
                colorTwo = resultCode;
                buttonChooseColorTwo.setBackgroundColor(colorTwo);
                break;
        }
    }



}
