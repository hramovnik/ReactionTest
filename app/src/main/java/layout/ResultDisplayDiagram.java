package layout;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.SessionResultActionInterface;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by gshabalev on 12/21/2016.
 */


public class ResultDisplayDiagram extends Activity implements View.OnClickListener {


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.frameDiagramResultButtonSave){
            if (action != null){
                try {
                    action.doSomething();
                    Toast.makeText(this.getBaseContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(this.getBaseContext(), "Ошибка. Не удалось сохранить данные", Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(this.getBaseContext(), "Отсутстствует обработчик сохранения", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result_display_diagram);

        Button buttonSave = ((Button) findViewById(R.id.frameDiagramResultButtonSave));
        buttonSave.setOnClickListener(this);
        Button buttonOk = ((Button) findViewById(R.id.frameDiagramResultButtonOk));
        buttonOk.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();

        if ((dataList == null)||(dataList.isEmpty())||(colors == null)){
            ((TextView) findViewById(R.id.tvDiagramTextResult)).setText("Отсутствуют валидные данные");
            return;
        }

        BarChart chart = (BarChart) findViewById(R.id.chart1);

        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.animateXY(1000, 1000);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        float groupSpace = 1f;
        float barSpace = 0f;
        float barWidth = 5f;

        int groupCount = dataList.size();
        int start = 0;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

        for (int i = start; i < groupCount; i++) {
            Pair<Integer, Integer> pair = dataList.get(i);
            if ((pair.first >= 0)&&(pair.second >= 0)) {
                yVals1.add(new BarEntry(i, (float) (pair.first)));
                yVals2.add(new BarEntry(i, (float) (pair.second)));
            }
        }

        BarDataSet set1, set2;


        set1 = new BarDataSet(yVals1, "R");
        set1.setColor(colors[0]);
        set2 = new BarDataSet(yVals2, "L");
        set2.setColor(colors[1]);

        BarData data = new BarData(set1, set2);
        data.setValueFormatter(new LargeValueFormatter());

        chart.setData(data);
        set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
        set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
        set1.setValues(yVals1);
        set2.setValues(yVals2);
        chart.getData().notifyDataChanged();

        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinimum(start);

        chart.getXAxis().setAxisMaximum(start + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(start, groupSpace, barSpace);
        chart.invalidate();
        chart.notifyDataSetChanged();
    }

    public static void setData(LinkedList<Pair<Integer, Integer>> dataList, int [] colors, SessionResultActionInterface action){
        ResultDisplayDiagram.dataList = dataList;
        ResultDisplayDiagram.colors = colors;
        ResultDisplayDiagram.action = action;
    }

    private static LinkedList<Pair<Integer, Integer>> dataList = null;
    private static int [] colors = null;
    private static SessionResultActionInterface action = null;

}
