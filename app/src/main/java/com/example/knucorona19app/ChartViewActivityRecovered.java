package com.example.knucorona19app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

public class ChartViewActivityRecovered extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ArrayList<Entry> xVal_r;

    ProgressDialog progressDialog;

    LineChart lineChart_recovered;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ChartData> data;
    int lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view_recovered);

        loading();
        init();
        getData();
    }

    public void init(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        getLastUpdate();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright
        );

        xVal_r = new ArrayList<>();
        data=new ArrayList<>();
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
        lineChart_recovered=(LineChart)findViewById(R.id.lineChart_recovered);
        lineChart_recovered.getDescription().setEnabled(false);

        XAxis xaxis = lineChart_recovered.getXAxis();
        YAxis yLaxis = lineChart_recovered.getAxisLeft();
        YAxis yRaxis = lineChart_recovered.getAxisRight();

        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setValueFormatter(new MyValueFormatter(lineChart_recovered));
        xaxis.setDrawGridLines(false);

        yLaxis.setTextColor(Color.BLUE);
        yLaxis.setAxisMinimum(0);

        yRaxis.setDrawLabels(false);
        yRaxis.setDrawAxisLine(false);
        yRaxis.setDrawGridLines(false);

        LineDataSet lineData=new LineDataSet(xVal_r,"완치자 수");
        lineData.setColor(Color.DKGRAY);
        ArrayList<ILineDataSet> dataSets=new ArrayList<ILineDataSet>();
        dataSets.add(lineData);

        LineData data_r=new LineData(dataSets);

        lineChart_recovered.setData(data_r);
        lineChart_recovered.invalidate();
    }
    public void getData(){
        xVal_r.clear();
        databaseReference.child("ChartData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object cd = dataSnapshot.getValue();
                TreeMap<String, HashMap<String, String>> tm = new TreeMap<String, HashMap<String, String>>((HashMap)cd);
                Log.d("@@@@@@@@", tm.size()+"!");
                Iterator<String> iter = tm.keySet().iterator();
                while(iter.hasNext()){
                    String key = iter.next();
                    HashMap<String, String> value = tm.get(key);
                    //int xInfection = Integer.parseInt(value.get("infection"));
                    int xRecovered = Integer.parseInt(value.get("recovered"));
                    //int xDeaths = Integer.parseInt(value.get("deaths"));
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd");
                    String s1 = "20160101";
                    String s2 = value.get("date");
                    long diff=0;
                    try{
                        Date d1 = myFormat.parse(s1);
                        Date d2 = myFormat.parse(s2);
                        diff = d2.getTime()-d1.getTime();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    float days = (diff / (1000*60*60*24));
                    xVal_r.add(new Entry(days+1,xRecovered));
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
                        progressDialog = new ProgressDialog(ChartViewActivityRecovered.this);
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


    @Override
    public void onRefresh() {
        final DBAsyncTask dbAsyncTask = new DBAsyncTask(lastUpdate, this);
        dbAsyncTask.execute();
        swipeRefreshLayout.setRefreshing(false);
    }
}
