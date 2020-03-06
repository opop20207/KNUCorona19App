package com.example.knucorona19app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ChartViewActivity extends AppCompatActivity {
    int DATA_RANGE_X=15;
    int DATA_RANGE_Y=7000;
    TextView txtRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view);

        LineChart lineChart=(LineChart)findViewById(R.id.lineChart);
        //lineChart.setOnChartValueSelectedListener(this);
        lineChart.getDescription().setEnabled(false);
        //String[] xAxisFormatter={"1","2"};

        //myValueFormatter xAxisFormatter=new DayAxisValueFormatter(lineChart);
        XAxis xaxis = lineChart.getXAxis();
        YAxis yLaxis = lineChart.getAxisLeft();
        YAxis yRaxis = lineChart.getAxisRight();

        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setValueFormatter(new MyValueFormatter(lineChart));
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
        xVal.add(new Entry(20200301,0));
        xVal.add(new Entry(20200302,2000));
        xVal.add(new Entry(20200303,3050));
        xVal.add(new Entry(20200304,1000));
        xVal.add(new Entry(20200311,1500));

        LineDataSet lineData=new LineDataSet(xVal,"확진자 수");
        lineData.setColor(Color.DKGRAY);
        ArrayList<ILineDataSet> dataSets=new ArrayList<ILineDataSet>();
        dataSets.add(lineData);

        LineData data=new LineData(dataSets);

        lineChart.setData(data);
        lineChart.invalidate();
    }
}
