package layout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.SessionResultActionInterface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gshabalev on 12/21/2016.
 */


public class ResultDisplayGraphic extends Activity implements View.OnClickListener {


    @Override
    public void onClick(View v) {
        finish();
    }



    @Override
    public void onResume(){
        super.onResume();

        if ((dataList == null)||(colors == null)){
            ((TextView) findViewById(R.id.tvDiagramTextResult)).setText("Отсутствуют валидные данные");
            return;
        }


    }

    private LineChart [] mChart = new LineChart[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display_graphic);
        Button buttonSave = ((Button) findViewById(R.id.frameResultButtonSave));
        buttonSave.setOnClickListener(this);


        mChart[0] = (LineChart) findViewById(R.id.lineChart1);
        mChart[1] = (LineChart) findViewById(R.id.lineChart2);

        for(int i = 0; i < 2; i++){
            mChart[i].getDescription().setEnabled(false);

            mChart[i].setDragDecelerationFrictionCoef(0.9f);
            mChart[i].setDragEnabled(false);
            mChart[i].setScaleEnabled(false);
            mChart[i].setDrawGridBackground(false);
            mChart[i].setPinchZoom(false);
            mChart[i].setBackgroundColor(Color.WHITE);

            mChart[i].animateX(2500);

            Legend l = mChart[i].getLegend();

            l.setForm(Legend.LegendForm.LINE);
            l.setTextSize(11f);
            l.setTextColor(Color.BLACK);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);

            XAxis xAxis = mChart[i].getXAxis();
            xAxis.setTextSize(10f);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);

            YAxis leftAxis = mChart[i].getAxisLeft();
            leftAxis.setTextColor(Color.rgb(0xff,0x80, 0));
            leftAxis.setAxisMaximum(200f);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setDrawGridLines(true);
            leftAxis.setGranularityEnabled(true);

            mChart[i].getAxisRight().setEnabled(false);


            int range = 100;
            int count = 20;
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();

            for (int k = 0; k < count; k++) {
                float mult = range / 2f;
                float val = (float) (Math.random() * mult) + 50;
                yVals1.add(new Entry(k, val));
            }

            ArrayList<Entry> yVals2 = new ArrayList<Entry>();

            for (int k = 0; k < count-1; k++) {
                float mult = range;
                float val = (float) (Math.random() * mult) + 150;
                yVals2.add(new Entry(k, val));
            }

            LineDataSet set1, set2;

            if (mChart[i].getData() != null &&
                    mChart[i].getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart[i].getData().getDataSetByIndex(0);
                set2 = (LineDataSet) mChart[i].getData().getDataSetByIndex(1);
                set1.setValues(yVals1);
                set2.setValues(yVals2);
                mChart[i].getData().notifyDataChanged();
                mChart[i].notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                set1 = new LineDataSet(yVals1, "Правый глаз");

                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(ColorTemplate.getHoloBlue());
                set1.setCircleColor(Color.BLACK);
                set1.setLineWidth(2f);
                set1.setCircleRadius(3f);
                set1.setFillAlpha(65);
                set1.setFillColor(ColorTemplate.getHoloBlue());
                set1.setHighLightColor(Color.rgb(244, 117, 117));
                set1.setDrawCircleHole(false);


                // create a dataset and give it a type
                set2 = new LineDataSet(yVals2, "Левый глаз");
                set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
                set2.setColor(Color.RED);
                set2.setCircleColor(Color.BLACK);
                set2.setLineWidth(2f);
                set2.setCircleRadius(3f);
                set2.setFillAlpha(65);
                set2.setFillColor(Color.RED);
                set2.setDrawCircleHole(false);
                set2.setHighLightColor(Color.rgb(244, 117, 117));
                //set2.setFillFormatter(new MyFillFormatter(900f));

                // create a data object with the datasets
                LineData data = new LineData(set1, set2);
                data.setValueTextColor(Color.GRAY);
                data.setValueTextSize(9f);

                // set data
                mChart[i].setData(data);
            }


        }




    }


    public static void setData(LinkedList<Pair<Integer, Integer>> [] dataList, int [] colors, SessionResultActionInterface action){
        ResultDisplayGraphic.dataList = dataList;
        ResultDisplayGraphic.colors = colors;
        ResultDisplayGraphic.action = action;
    }

    private static LinkedList<Pair<Integer, Integer>> [] dataList = null;
    private static int [] colors = null;
    private static SessionResultActionInterface action = null;

}
