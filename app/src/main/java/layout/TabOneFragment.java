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

import com.hramovnik.reactiontest.CommandSensomotoric;
import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;
import com.hramovnik.reactiontest.SessionSensomotoric;
import com.hramovnik.reactiontest.TaskActivityInterface;

public class TabOneFragment extends TabFragment implements TaskActivityInterface, View.OnClickListener {

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

    private int colorOne = Color.GREEN;
    private int colorTwo = Color.GREEN;

    private SeekBar sbSizeChooser;
    private TextView teSizeChooser;
    private SeekBar sbQuantityRep;
    private TextView teQuantityRep;
    private SeekBar sbQuantityDelay;
    private TextView teQuantityDelay;

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
        sbSizeChooser = getSb(teSizeChooser, R.id.sbRoundSize, 1, 30, "SMT_Round_SIZE");

        teQuantityRep = (TextView) getView().findViewById(R.id.teQuantityRep);
        sbQuantityRep = getSb(teQuantityRep, R.id.sbQuantityRep, 1,20, "SMT_QUANTITY_REP");

        teQuantityDelay = (TextView) getView().findViewById(R.id.teQuantityDelay);
        sbQuantityDelay = getSb(teQuantityDelay, R.id.sbQuantityDelay, 1,10, "SMT_QUANTITY_DELAY");


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
            ed.putInt("SMT_QUANTITY_REP", sbQuantityRep.getProgress());

            ed.apply();

        }catch (Exception e){

        }
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
            default:
                super.onActivityResult(requestCode,resultCode,data);

        }
    }

    @Override
    public Session getSession() {
        CommandSensomotoric.setDelay(sbQuantityDelay.getProgress());
        return new SessionSensomotoric(sbQuantityRep.getProgress(), sbSizeChooser.getProgress(), colorOne, colorTwo);
    }
}


