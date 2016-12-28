package layout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.hramovnik.reactiontest.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class PrefActivity extends Activity implements View.OnClickListener, View.OnTouchListener{

    private SharedPreferences sp;
    private EditText teName;
    private EditText teSurname;
    private EditText teSecondName;
    private EditText teDate;
    private Button buttonOk;
    private static String profileResult = new String();
    public static String getProfileResult(){
        if (profileResult.isEmpty()){return "NoName";}
        return profileResult;
    }

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pref);
        Log.d("Pref", "Pref Create");

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        teName = (EditText) findViewById(R.id.tePersonName);
        teSurname = (EditText) findViewById(R.id.tePersonSurname);
        teSecondName = (EditText) findViewById(R.id.tePersonSecondName);
        teDate = (EditText) findViewById(R.id.tePersonDate);
        teDate.setOnClickListener(this);
        teDate.setOnTouchListener(this);

        buttonOk = (Button) findViewById(R.id.buttonProfileOk);
        buttonOk.setOnClickListener(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);
        Date lastDate = null;
        try {
            lastDate = dateFormatter.parse(sp.getString("PersonDate", "1-1-1990"), new ParsePosition(0));

        }catch (Exception e){
            sp.edit().clear().apply();
            lastDate = null;
        }


        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                teDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, lastDate == null ? newCalendar.get(Calendar.YEAR) : lastDate.getYear()+1900,
                lastDate == null ? newCalendar.get(Calendar.MONTH) : lastDate.getMonth(),
                lastDate == null ? newCalendar.get(Calendar.DAY_OF_MONTH) : lastDate.getDay());

    }

    public static String getCurrentDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);
        return (formatter.format(new Date()));
    }


    public void onResume(){
        super.onResume();
            try {
                teName.setText(sp.getString("PersonName", ""));
                teSurname.setText(sp.getString("PersonSurname", ""));
                teSecondName.setText(sp.getString("PersonSecondName", ""));
                teDate.setText(sp.getString("PersonDate", ""));
            }catch (Exception e){
                sp.edit().clear().apply();
            }

        Log.d("Date", getCurrentDate());
    }

    public static void loadStaticData(Context context){
        SharedPreferences sharPref = PreferenceManager.getDefaultSharedPreferences(context);
        profileData = new LinkedList<String>();

        StringBuilder builder = new StringBuilder();
        builder.append(sharPref.getString("PersonSurname", ""));
        profileData.add(sharPref.getString("PersonSurname", ""));
        builder.append(" ");
        builder.append(sharPref.getString("PersonName", ""));
        profileData.add(sharPref.getString("PersonName", ""));
        builder.append(" ");
        builder.append(sharPref.getString("PersonSecondName", ""));
        profileData.add(sharPref.getString("PersonSecondName", ""));
        builder.append(" ");
        builder.append(sharPref.getString("PersonDate", ""));
        profileData.add(sharPref.getString("PersonDate", ""));
        profileResult = builder.toString();
    }

    public static LinkedList<String> getProfileData() {
        return profileData;
    }

    private static LinkedList<String> profileData = null;


    @Override
    public void onPause(){
        super.onPause();
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putString("PersonName", teName.getText().toString());
            ed.putString("PersonSurname", teSurname.getText().toString());
            ed.putString("PersonSecondName", teSecondName.getText().toString());
            ed.putString("PersonDate", teDate.getText().toString());
            ed.apply();
        }catch (Exception e){
            Toast.makeText(this, "Профиль не сохранён", Toast.LENGTH_LONG).show();
        }
        profileData = new LinkedList<String>();
        StringBuilder builder = new StringBuilder();
        builder.append(teSurname.getText().toString());
        profileData.add(teSurname.getText().toString());
        builder.append(" ");
        builder.append(teName.getText().toString());
        profileData.add(teName.getText().toString());
        builder.append(" ");
        builder.append(teSecondName.getText().toString());
        profileData.add(teSecondName.getText().toString());
        builder.append(" ");
        builder.append(teDate.getText().toString());
        profileData.add(teDate.getText().toString());
        profileResult = builder.toString();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buttonProfileOk:
                this.finish();
                break;
            case R.id.tePersonDate:
                datePickerDialog.show();
                break;
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        datePickerDialog.show();
        return false;
    }
}
