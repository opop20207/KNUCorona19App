package com.example.knucorona19app;

import java.io.Serializable;

public class ChartData implements Serializable {
    public String date, infection, recovered, deaths;

    public ChartData(){

    }

    public ChartData(String date, String infection, String recovered, String deaths){
        this.date = date;
        this.infection = infection;
        this.recovered = recovered;
        this.deaths= deaths;
    }
}