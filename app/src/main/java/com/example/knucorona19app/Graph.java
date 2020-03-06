package com.example.knucorona19app;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class Graph extends AppCompatActivity {
   private LineChart chart;
   private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        chart=(LineChart) findViewById(R.id.chart);

        //차트의 아래 Axis
        XAxis xAxis=chart.getXAxis(); //XAxis위치는 아래쪽
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);
        //차트의 왼쪽 Axis
        YAxis leftAxis=chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);  //leftAxis의 그리드라인을 없앰
        //차트의 오른쪽 Axis
        YAxis rightAxis=chart.getAxisRight();
        rightAxis.setEnabled(false);   //ledrAxis를 비활성화

        LineData data=new LineData();
        chart.setData(data);

        feedMultiple();
    }

    private void feedMultiple(){
        if(thread!=null)
            thread.interrupt();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable(){
           @Override
           public void run(){
               while(true){
                   runOnUiThread(runnable);
                   try{
                       Thread.sleep(100);
                   }catch(InterruptedException ie){
                       ie.printStackTrace();
                   }
               }
           }
        });
        thread.start();
    }
    private void addEntry(){
        LineData data=chart.getData();
        if(data!=null){
            ILineDataSet set=data.getDataSetByIndex(0);
            if(set==null){
                set=createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(),(float)(Math.random()*10)+20f),0);
            data.notifyDataChanged();

            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(10);
            chart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet(){
        LineDataSet set=new LineDataSet(null,"Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.RED);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244,117,117));
        set.setDrawValues(false);
        return set;
    }
}
