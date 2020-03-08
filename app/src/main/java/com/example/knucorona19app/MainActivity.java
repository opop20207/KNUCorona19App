package com.example.knucorona19app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button) findViewById(R.id.button);
    }

    public void Main2(View v){
        Intent intent=null;
        switch (v.getId()){
            case R.id.button:
                intent=new Intent(this, ChartViewActivity.class);
                break;
            case R.id.btnRecovered:
                intent=new Intent(this,ChartViewActivityRecovered.class);
                break;
        }
        startActivity(intent);
    }
}

