package layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;
import com.hramovnik.reactiontest.SessionFlicker;
import com.hramovnik.reactiontest.TaskActivityInterface;

public class TabThreeFragment extends TabFragment implements TaskActivityInterface, View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_three, container, false);
    }

    @Override
    public Session getSession() {
        return new SessionFlicker(false,color,3,sbBrightness.getProgress(),sbFrequency.getProgress(),75);
    }

    private DialogFragment dialogChooseColor;
    private Button buttonChooseColor;
    public static final int DIALOG_ONE = 1;

    private int color = Color.GREEN;
    String colorTag = "KCHSM2_Color_ONE";

    private SeekBar sbBrightness;
    private TextView teBrightness;
    private final String brightntssTag = "KCHSM2_BRIGHTNESS";


    private SeekBar sbFrequency;
    private TextView teFrequency;
    private final String frequencyTag = "KCHSM2_FREQUENCY";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonChooseColor = (Button) getView().findViewById(R.id.buttonColor);
        buttonChooseColor.setOnClickListener(this);
        buttonChooseColor.setBackgroundColor(color);

        teBrightness = (TextView) getView().findViewById(R.id.teBrightness);
        sbBrightness = getSb(teBrightness, R.id.sbBrightness, 1,6,brightntssTag);

        teFrequency = (TextView) getView().findViewById(R.id.teFrequency);
        sbFrequency = getSb(teFrequency, R.id.sbFrequency, 10,75,frequencyTag);

        dialogChooseColor = new ColorChooser();

    }


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        color = sp.getInt(colorTag, Color.GREEN);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt(colorTag, color);
            ed.putInt(frequencyTag, sbFrequency.getProgress());
            ed.putInt(brightntssTag, sbBrightness.getProgress());

            ed.apply();

        }catch (Exception e){}
    }

    @Override
    public void onClick(View view) {
        if (R.id.buttonColor == view.getId()){
            dialogChooseColor.setTargetFragment(this, DIALOG_ONE);
            dialogChooseColor.show(getFragmentManager(), "Выберите цвет");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DIALOG_ONE) {
            color = resultCode;
            buttonChooseColor.setBackgroundColor(color);
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

}
