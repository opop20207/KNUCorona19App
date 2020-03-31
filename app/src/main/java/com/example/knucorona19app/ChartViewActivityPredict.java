package com.example.knucorona19app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class ChartViewActivityPredict extends AppCompatActivity {
    ArrayList<Entry> xVal_p;

    ProgressDialog progressDialog;

    LineChart lineChart_predict;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<ChartData> data;
    int lastUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view_predict);
        init();
        getData();
    }

    public void init(){
        loading();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        getLastUpdate();
        data=new ArrayList<>();
        xVal_p = new ArrayList<>();
    }

    public void getLastUpdate(){
        databaseReference.child("LastUpdate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp = dataSnapshot.getValue(String.class);
                lastUpdate = Integer.parseInt(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showChart(){
        lineChart_predict=(LineChart)findViewById(R.id.lineChart_predict);
        lineChart_predict.getDescription().setEnabled(false);

        XAxis xaxis = lineChart_predict.getXAxis();
        YAxis yLaxis = lineChart_predict.getAxisLeft();
        YAxis yRaxis = lineChart_predict.getAxisRight();

        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setValueFormatter(new MyValueFormatter(lineChart_predict));
        xaxis.setDrawGridLines(false);

        yLaxis.setTextColor(Color.BLUE);
        yLaxis.setAxisMinimum(0);

        yRaxis.setDrawLabels(false);
        yRaxis.setDrawAxisLine(false);
        yRaxis.setDrawGridLines(false);

        LineDataSet lineData=new LineDataSet(xVal_p,"사망자 수");
        lineData.setColor(Color.DKGRAY);
        ArrayList<ILineDataSet> dataSets=new ArrayList<ILineDataSet>();
        dataSets.add(lineData);

        LineData data_d=new LineData(dataSets);

        lineChart_predict.setData(data_d);
        lineChart_predict.invalidate();
    }
    public void getData(){
        xVal_p.clear();
        databaseReference.child("PredictData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object cd = dataSnapshot.getValue();
                TreeMap<String, HashMap<String, String>> tm = new TreeMap<String, HashMap<String, String>>((HashMap)cd);
                Iterator<String> iter = tm.keySet().iterator();
                while(iter.hasNext()){
                    String key = iter.next();
                    HashMap<String, String> value = tm.get(key);
                    int xdata = Integer.parseInt(value.get("data"));
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd");
                    String s1 = "20160101";
                    String s2 = key;
                    long diff=0;
                    try{
                        Date d1 = myFormat.parse(s1);
                        Date d2 = myFormat.parse(s2);
                        diff = d2.getTime()-d1.getTime();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    float days = (diff / (1000*60*60*24));
                    xVal_p.add(new Entry(days+1,xdata));
                }
                showChart();
                loadingEnd();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getLastUpdate();
    }
    public void loading() {
        //로딩
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog = new ProgressDialog(ChartViewActivityPredict.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("잠시만 기다려 주세요");
                        progressDialog.show();
                    }
                }, 0);
    }

    public void loadingEnd() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 0);
    }

    public void refresh(View v) {
        final DBAsyncTask dbAsyncTask = new DBAsyncTask(lastUpdate, this);
        dbAsyncTask.execute();
    }
}
