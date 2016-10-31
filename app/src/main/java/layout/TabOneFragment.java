package layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;



import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.TaskActivityInterface;

public class TabOneFragment extends Fragment implements TaskActivityInterface, View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tab_one, container, false);
    }


    private DialogFragment dialogChooseColorOne;
    private DialogFragment dialogChooseColorTwo;
    private Button buttonChooseColorOne;
    private Button buttonChooseColorTwo;
    public static final int DIALOG_ONE = 1;
    public static final int DIALOG_TWO = 2;
    private SharedPreferences sp;
    private int colorOne = Color.GREEN;
    private int colorTwo = Color.GREEN;
    private SeekBar sbSizeChooser;
    private TextView teSizeChooser;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonChooseColorOne = (Button) getView().findViewById(R.id.buttonColorOneChooser);
        buttonChooseColorOne.setOnClickListener(this);
        buttonChooseColorOne.setBackgroundColor(colorOne);

        buttonChooseColorTwo = (Button) getView().findViewById(R.id.buttonColorTwoChooser);
        buttonChooseColorTwo.setOnClickListener(this);
        buttonChooseColorTwo.setBackgroundColor(colorTwo);

        teSizeChooser = (TextView) getView().findViewById(R.id.teRoundSize);
        sbSizeChooser = (SeekBar) getView().findViewById(R.id.sbRoundSize);
        sbSizeChooser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) sbSizeChooser.setProgress(1);
                else teSizeChooser.setText(String.valueOf(i));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sbSizeChooser.setProgress(sp.getInt("SMT_Round_SIZE", 3));
        teSizeChooser.setText(String.valueOf(sp.getInt("SMT_Round_SIZE", 3)));

        dialogChooseColorOne = new ColorChooser();
        dialogChooseColorTwo = new ColorChooser();

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        super.onCreate(savedInstanceState);

        colorOne = sp.getInt("SMT_Color_ONE", Color.GREEN);
        colorTwo = sp.getInt("SMT_Color_TWO", Color.RED);


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt("SMT_Color_ONE", colorOne);
            ed.putInt("SMT_Color_TWO", colorTwo);
            ed.putInt("SMT_Round_SIZE", sbSizeChooser.getProgress());

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


