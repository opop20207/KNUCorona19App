package com.example.knucorona19app;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class LineChart2 extends AppCompatActivity  {
    int DATA_RANGE_X=15;
    int DATA_RANGE_Y=7000;
    TextView txtRead;
    //ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart2);
        LineChart lineChart=(LineChart)findViewById(R.id.lineChart);
        //lineChart.setOnChartValueSelectedListener(this);
        lineChart.getDescription().setEnabled(false);
        //String[] xAxisFormatter={"1","2"};

        //myValueFormatter xAxisFormatter=new DayAxisValueFormatter(lineChart);
        XAxis xaxis = lineChart.getXAxis();
        YAxis yLaxis = lineChart.getAxisLeft();
        YAxis yRaxis = lineChart.getAxisRight();

        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawGridLines(false);
        xaxis.setGranularity(1f);
        xaxis.setLabelCount(7);

        yLaxis.setTextColor(Color.BLUE);
        yLaxis.setAxisMaximum(DATA_RANGE_Y+10);
        yLaxis.setAxisMinimum(0);

        yRaxis.setDrawLabels(false);
        yRaxis.setDrawAxisLine(false);
        yRaxis.setDrawGridLines(false);

        ArrayList<Entry> xVal=new ArrayList<Entry>();
        xVal.add(new Entry(0,0));
        xVal.add(new Entry(1,2000));
        xVal.add(new Entry(2,3050));
        xVal.add(new Entry(3,1000));
        xVal.add(new Entry(4,1500));

        LineDataSet lineData=new LineDataSet(xVal,"확진자 수");
        lineData.setColor(Color.DKGRAY);
        ArrayList<ILineDataSet> dataSets=new ArrayList<ILineDataSet>();
        dataSets.add(lineData);

        LineData data=new LineData(dataSets);

        lineChart.setData(data);
        lineChart.invalidate();
    }
}
