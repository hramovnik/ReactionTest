package layout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.hramovnik.reactiontest.MainActivity;
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
        if (v.getId() == R.id.frameGraphicResultButtonSave){
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
    public void onResume(){
        super.onResume();

        if ((dataList == null)||(colors == null)){
            ((TextView) findViewById(R.id.tvGraphicsTextResult)).setText("Отсутствуют валидные данные");
            return;
        }
    }

    private LineChart [] mChart = new LineChart[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.activity_result_display_graphic);

            Button buttonSave = ((Button) findViewById(R.id.frameGraphicResultButtonSave));
            buttonSave.setOnClickListener(this);
            Button buttonOk = ((Button) findViewById(R.id.frameGraphicResultButtonOk));
            buttonOk.setOnClickListener(this);

            if ((dataList == null)||(colors == null)){
                ((TextView) findViewById(R.id.tvGraphicsTextResult)).setText("Отсутствуют валидные данные");
                return;
            }else if((dataList.first == null)||(dataList.second == null)){
                ((TextView) findViewById(R.id.tvGraphicsTextResult)).setText("Отсутствуют валидные данные в массиве");
                return;
            }else if(dataList.first.isEmpty()||dataList.second.isEmpty()){
                ((TextView) findViewById(R.id.tvGraphicsTextResult)).setText("Не обнаружено входных данных для графика");
            }

            mChart[0] = (LineChart) findViewById(R.id.lineChart1);
            mChart[1] = (LineChart) findViewById(R.id.lineChart2);

            for(int i = 0; i < 2; i++){
                int [] localColors= new int[2];

                float [] hsvColor = new float[3];
                Color.colorToHSV(colors[i], hsvColor);

                hsvColor[1] = 1f;
                hsvColor[2] = 1f;
                localColors[0] = Color.HSVToColor(hsvColor);

                hsvColor[1] = 0.15f;
                hsvColor[2] = 0.85f;
                localColors[1] = Color.HSVToColor(hsvColor);

                mChart[i].getDescription().setEnabled(true);

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

                mChart[i].getAxisRight().setEnabled(false);

                LinkedList<Pair<Integer, Integer> > inputData = ((i == 0)? dataList.first : dataList.second);

                ArrayList<Entry> yVals1 = new ArrayList<Entry>();
                ArrayList<Entry> yVals2 = new ArrayList<Entry>();

                int scale = 200;

                int avarageRight = 0;
                int avarageLeft = 0;
                int counterRight = 0;
                int counterLeft = 0;

                for (int k = 0; k < inputData.size(); k++){
                    Pair<Integer, Integer> pair = inputData.get(k);

                    float val = (float) ((pair.first >= 0)? pair.first : 1000);
                    yVals1.add(new Entry(k, val));
                    scale = (((int)val > scale)?(int)val:scale);
                    if (val < 1000){
                        avarageRight += (int)val;
                        counterRight++;
                    }

                    val = (float) ((pair.second >= 0)? pair.second : 1000);
                    yVals2.add(new Entry(k, val));
                    scale = (((int)val > scale)?(int)val:scale);

                    if (val < 1000){
                        avarageLeft += (int)val;
                        counterLeft++;
                    }

                }
                avarageRight /= counterRight;
                avarageLeft /= counterLeft;

                scale /= 100;
                scale *= 100;
                if(scale < 1000) {scale+=100;}


                YAxis leftAxis = mChart[i].getAxisLeft();
                leftAxis.setTextColor(Color.rgb(0xff,0x80, 0));
                leftAxis.setAxisMaximum((float)scale);
                leftAxis.setAxisMinimum(0f);
                leftAxis.setDrawGridLines(true);
                leftAxis.setGranularityEnabled(true);

                LineDataSet set1, set2;
                String postfixRight = "";
                if (avarageRight != 0) postfixRight = " (среднее значение = " + String.valueOf(avarageRight) + " мс)";

                set1 = new LineDataSet(yVals1, "Правая рука" + postfixRight);

                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(localColors[0]);
                set1.setCircleColor(Color.BLACK);
                set1.setLineWidth(2f);
                set1.setCircleRadius(3f);
                set1.setFillAlpha(65);
                set1.setFillColor(localColors[0]);
                set1.setHighLightColor(Color.rgb(244, 117, 117));
                set1.setDrawCircleHole(false);

                String postfixLeft = "";
                if (avarageRight != 0) postfixLeft = " (среднее значение = " + String.valueOf(avarageLeft) + " мс)";

                set2 = new LineDataSet(yVals2, "Левая рука" + postfixLeft);
                set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                set2.setColor(localColors[1]);
                set2.setCircleColor(Color.BLACK);
                set2.setLineWidth(2f);
                set2.setCircleRadius(3f);
                set2.setFillAlpha(65);
                set2.setFillColor(localColors[1]);
                set2.setDrawCircleHole(false);
                set2.setHighLightColor(Color.rgb(244, 117, 117));

                LineData data = new LineData(set1, set2);
                data.setValueTextColor(Color.GRAY);
                data.setValueTextSize(9f);


                mChart[i].setData(data);
            }

        }catch (Throwable e){
            finish();
            MainActivity.display("Ошибка генерации графика " + e.getStackTrace()[0].toString());
        }
    }


    public static void setData(Pair<LinkedList<Pair<Integer, Integer> > , LinkedList<Pair<Integer, Integer> > > dataList, int [] colors, SessionResultActionInterface action){
        ResultDisplayGraphic.dataList = dataList;
        ResultDisplayGraphic.colors = colors;
        ResultDisplayGraphic.action = action;
    }

    private static Pair<LinkedList<Pair<Integer, Integer> > , LinkedList<Pair<Integer, Integer> > > dataList = null;
    private static int [] colors = null;
    private static SessionResultActionInterface action = null;

}
