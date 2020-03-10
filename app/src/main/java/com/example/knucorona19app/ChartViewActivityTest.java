package com.example.knucorona19app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.graphics.Color;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class ChartViewActivityTest extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ArrayList<Entry> xVal_t,xVal_n;

    ProgressDialog progressDialog;

    LineChart lineChart_test;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<ChartData> data;
    SwipeRefreshLayout swipeRefreshLayout;
    int lastUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view_test);

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

        xVal_t = new ArrayList<>();
        xVal_n=new ArrayList<>();
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
        lineChart_test=(LineChart)findViewById(R.id.lineChart_test);
        lineChart_test.getDescription().setEnabled(false);

        XAxis xaxis = lineChart_test.getXAxis();
        YAxis yLaxis = lineChart_test.getAxisLeft();
        YAxis yRaxis = lineChart_test.getAxisRight();

        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setValueFormatter(new MyValueFormatter(lineChart_test));
        xaxis.setDrawGridLines(false);

        yLaxis.setTextColor(Color.BLACK);
        yLaxis.setAxisMinimum(0);

        yRaxis.setDrawLabels(false);
        yRaxis.setDrawAxisLine(false);
        yRaxis.setDrawGridLines(false);

        LineDataSet lineData=new LineDataSet(xVal_t,"검사진행자 수");
        LineDataSet lineData2=new LineDataSet(xVal_n,"검사결과 음성판단 수");
        lineData.setColor(Color.DKGRAY);
        lineData2.setColor(Color.RED);
        ArrayList<ILineDataSet> dataSets=new ArrayList<ILineDataSet>();
        dataSets.add(lineData);
        dataSets.add(lineData2);

        LineData data_r=new LineData(dataSets);
        LineData data_n=new LineData(dataSets);
        lineChart_test.setData(data_r);
        lineChart_test.setData(data_n);
        lineChart_test.invalidate();
    }

    public void getData(){
        xVal_t.clear();
        xVal_n.clear();
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
                    int xTest=Integer.parseInt(value.get("testNow"));
                    int xTestN=Integer.parseInt(value.get("testNegative"));
                    int xdTestN=Integer.parseInt(value.get("dTestNegative"));
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
                    xVal_t.add(new Entry(days+1,xTest));
                    xVal_n.add(new Entry(days+1,xTestN-xdTestN));
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
                        progressDialog = new ProgressDialog(ChartViewActivityTest.this);
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
        Log.e("@", lastUpdate+"@");
        final DBAsyncTask dbAsyncTask = new DBAsyncTask(lastUpdate, this);
        dbAsyncTask.execute();
        swipeRefreshLayout.setRefreshing(false);
    }

}
