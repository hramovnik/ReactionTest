package layout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.hramovnik.reactiontest.R;

public class ParametersActivity extends Activity implements View.OnClickListener{

    RadioButton [] radioButtonsPosition = new RadioButton[9];
    RadioButton [] radioButtonsEyeChooser = new RadioButton[3];
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        radioButtonsPosition[0] = (RadioButton)findViewById(R.id.radioButtonChoosePosition1);
        radioButtonsPosition[1] = (RadioButton)findViewById(R.id.radioButtonChoosePosition2);
        radioButtonsPosition[2] = (RadioButton)findViewById(R.id.radioButtonChoosePosition3);
        radioButtonsPosition[3] = (RadioButton)findViewById(R.id.radioButtonChoosePosition4);
        radioButtonsPosition[4] = (RadioButton)findViewById(R.id.radioButtonChoosePosition5);
        radioButtonsPosition[5] = (RadioButton)findViewById(R.id.radioButtonChoosePosition6);
        radioButtonsPosition[6] = (RadioButton)findViewById(R.id.radioButtonChoosePosition7);
        radioButtonsPosition[7] = (RadioButton)findViewById(R.id.radioButtonChoosePosition8);
        radioButtonsPosition[8] = (RadioButton)findViewById(R.id.radioButtonChoosePosition9);

        View.OnClickListener positionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (RadioButton rbutton : radioButtonsPosition) {
                    if (rbutton.getId() != v.getId()) rbutton.setChecked(false);
                }
            }
        };

        for (RadioButton rbutton : radioButtonsPosition) {
            rbutton.setOnClickListener(positionListener);
        }

        radioButtonsEyeChooser[0] = (RadioButton)findViewById(R.id.radioButtonLeft);
        radioButtonsEyeChooser[1] = (RadioButton)findViewById(R.id.radioButtonBoth);
        radioButtonsEyeChooser[2] = (RadioButton)findViewById(R.id.radioButtonRight);

        View.OnClickListener eyeChooserListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (RadioButton rbutton : radioButtonsEyeChooser) {
                    if (rbutton.getId() != v.getId()) rbutton.setChecked(false);
                }
            }
        };

        for (RadioButton rbutton : radioButtonsEyeChooser) {
            rbutton.setOnClickListener(eyeChooserListener);
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    int getPosition(){
        for (int i = 0; i < radioButtonsPosition.length; i++){
            if (radioButtonsPosition[i].isChecked()) return i;
        }
        return 4;
    }
    int getEye(){
        for (int i = 0; i < radioButtonsEyeChooser.length; i++){
            if (radioButtonsEyeChooser[i].isChecked()) return i;
        }
        return 1;
    }

    static int position = 5;
    public static int getChosenPosition(){
        if ((position > 8) || (position < 0)) position = 5;
        return position;
    }
    static int eye = 1;
    public static int getChosenEye(){
        if ((eye > 2) || (eye < 0)) eye = 1;
        return eye;
    }

    public static void loadStaticData(Context context){
        SharedPreferences sharPref = PreferenceManager.getDefaultSharedPreferences(context);
        position = sharPref.getInt("rbPosition", 4);
        eye = sharPref.getInt("rbEye", 1);
        Log.d("Tag", String.valueOf(position) + " " + String.valueOf(eye));
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            position = sp.getInt("rbPosition", 4);
            eye = sp.getInt("rbEye", 1);
        }catch (Exception e){
            sp.edit().clear().apply();
        }finally {
            radioButtonsPosition[getChosenPosition()].setChecked(true);
            radioButtonsEyeChooser[getChosenEye()].setChecked(true);
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        position = getPosition();
        eye = getEye();
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt("rbPosition", position);
            ed.putInt("rbEye", eye);
            ed.apply();
        }catch (Exception e){
            Toast.makeText(this, "Ошибка сохранения настроек в постоянную память", Toast.LENGTH_LONG).show();
        }
    }
}
