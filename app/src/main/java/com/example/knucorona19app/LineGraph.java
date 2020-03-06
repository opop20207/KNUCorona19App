package com.example.knucorona19app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class LineGraph extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        LineChart chart=(LineChart)findViewById(R.id.chart);

        ArrayList<Entry> xVals=new ArrayList<Entry>();
        xVals.add(new Entry(100.0f,0));
        xVals.add(new Entry(50.0f,1));
        xVals.add(new Entry(75.0f,2));
        xVals.add(new Entry(50.0f,3));

        //네모 무슨 그래프인지 표시하는거
        LineDataSet setComp1=new LineDataSet(xVals,"Company1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets=new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        ArrayList<String> valsComp1=new ArrayList<String>();

        valsComp1.add("1.Q");
        valsComp1.add("2.Q");
        valsComp1.add("3.Q");
        valsComp1.add("4.Q");

        LineData data=new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();
    }
}
