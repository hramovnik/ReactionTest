package layout;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.hramovnik.reactiontest.R;

import java.util.ArrayList;

/**
 * Created by gshabalev on 12/21/2016.
 */


public class ResultDisplayDiagram extends ResultDisplay {
    protected Typeface mTfLight;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setStyle(STYLE_NO_TITLE, 0);
        mTfLight = Typeface.defaultFromStyle(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getDialog().setTitle("Результат");
        View view = inflater.inflate(R.layout.fragment_result_display_diagram, null);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BarChart chart = (BarChart) getView().findViewById(R.id.chart1);

        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.animateXY(1000, 1000);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setEnabled(false);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        float groupSpace = 1f;
        float barSpace = 0f; // x4 DataSet
        float barWidth = 5f;
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = 20;
        int start = 0;
        int end = groupCount-1;


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();


        for (int i = start; i <= end; i++) {
            yVals1.add(new BarEntry(i, (float) (1*i)));
            yVals2.add(new BarEntry(i, (float) (2*i)));
        }

        BarDataSet set1, set2;

        set1 = new BarDataSet(yVals1, "A");
        set1.setColor(Color.rgb(104, 241, 175));
        set2 = new BarDataSet(yVals2, "B");
        set2.setColor(Color.rgb(164, 228, 251));


        BarData data = new BarData(set1, set2);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTypeface(mTfLight);

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

    public void show(FragmentManager manager, String tag, String information){
        super.show(manager,tag);
        setText(information);
    }

}
