package layout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;

import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.TableLayout;
import android.widget.TableRow;

import com.hramovnik.reactiontest.R;

import java.util.HashMap;
import java.util.Map;


public class ColorChooser extends DialogFragment implements View.OnClickListener {

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;
    Button colorButtons[];
    ArrayAdapter<Button> buttonAdapter= null;
    Map<Button,Integer> colorMap;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Выбор цвета");
        setColor(Color.GREEN);
        View v = inflater.inflate(R.layout.fragment_color_chooser, null);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        TableLayout layout = (TableLayout) getView().findViewById(R.id.tableColorLayout);

        colorButtons = new Button[48];
        colorMap = new HashMap<>();

        for(int i = 0 ; i < colorButtons.length; i++){
            colorButtons[i] = new Button(getContext());
            colorButtons[i].setOnClickListener(this);
        }

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(80,80);
        rowParams.setMargins(10,10,10,10);

        /*boolean portrait = false;
        if (portrait) {

            for (int i = 0; i < 8; i++) {
                TableRow row = new TableRow(getContext());

                colorButtons[i * 6].setBackgroundColor(Color.rgb(255, 0, i * 32));
                colorMap.put(colorButtons[i * 6], Color.rgb(255, 0, i * 32));
                row.addView(colorButtons[i * 6], rowParams);

                colorButtons[i * 6 + 1].setBackgroundColor(Color.rgb(255, i * 32, 0));
                colorMap.put(colorButtons[i * 6 + 1], Color.rgb(255, i * 32, 0));
                row.addView(colorButtons[i * 6 + 1], rowParams);

                colorButtons[i * 6 + 2].setBackgroundColor(Color.rgb(i * 32, 255, 0));
                colorMap.put(colorButtons[i * 6 + 2], Color.rgb(i * 32, 255, 0));
                row.addView(colorButtons[i * 6 + 2], rowParams);

                colorButtons[i * 6 + 3].setBackgroundColor(Color.rgb(0, 255, i * 32));
                colorMap.put(colorButtons[i * 6 + 3], Color.rgb(0, 255, i * 32));
                row.addView(colorButtons[i * 6 + 3], rowParams);

                colorButtons[i * 6 + 4].setBackgroundColor(Color.rgb(0, i * 32, 255));
                colorMap.put(colorButtons[i * 6 + 4], Color.rgb(0, i * 32, 255));
                row.addView(colorButtons[i * 6 + 4], rowParams);

                colorButtons[i * 6 + 5].setBackgroundColor(Color.rgb(i * 32, 0, 255));
                colorMap.put(colorButtons[i * 6 + 5], Color.rgb(i * 32, 0, 255));
                row.addView(colorButtons[i * 6 + 5], rowParams);

                layout.addView(row);

            }
        }else{*/
            TableRow [] row = new TableRow[6];

            for (int i = 0; i < 6; i++){
                row[i] = new TableRow(getContext());
            }

            for (int i = 0; i < 8; i++){
                colorButtons[i*6].setBackgroundColor(Color.rgb(255, 0, i*32));
                colorMap.put(colorButtons[i*6], Color.rgb(255, 0, i*32));
                row[0].addView(colorButtons[i*6],rowParams);

                colorButtons[i*6+1].setBackgroundColor(Color.rgb(255, i*32, 255-i*32));
                colorMap.put(colorButtons[i*6+1], Color.rgb(255, i*32, 255-i*32));
                row[1].addView(colorButtons[i*6+1],rowParams);

                colorButtons[i*6+2].setBackgroundColor(Color.rgb(i*32, 255, 0));
                colorMap.put(colorButtons[i*6+2], Color.rgb(i*32, 255, 0));
                row[2].addView(colorButtons[i*6+2],rowParams);

                colorButtons[i*6+3].setBackgroundColor(Color.rgb(255-i*32, 255, i*32));
                colorMap.put(colorButtons[i*6+3], Color.rgb(255-i*32, 255, i*32));
                row[3].addView(colorButtons[i*6+3],rowParams);

                colorButtons[i*6+4].setBackgroundColor(Color.rgb(0, i*32, 255));
                colorMap.put(colorButtons[i*6+4], Color.rgb(0, i*32, 255));
                row[4].addView(colorButtons[i*6+4],rowParams);

                colorButtons[i*6+5].setBackgroundColor(Color.rgb(i*32, 255-i*32, 255));
                colorMap.put(colorButtons[i*6+5], Color.rgb(i*32, 255-i*32, 255));
                row[5].addView(colorButtons[i*6+5],rowParams);

            }

            for (int i = 0; i < 6; i++){
                layout.addView(row[i]);
            }
        //}

        layout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));

        for(int i = 0 ; i < colorButtons.length; i++){
            TableRow.LayoutParams params = new TableRow.LayoutParams(80,80);
            params.setMargins(10,10,10,10);
            colorButtons[i].setLayoutParams(params);
        }


    }

    @Override
    public void onResume(){
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        if(colorMap.containsKey((Button) v)){
            color = colorMap.get((Button) v);
        }

        getTargetFragment().onActivityResult(getTargetRequestCode(), color, getActivity().getIntent());
        dismiss();
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

}
