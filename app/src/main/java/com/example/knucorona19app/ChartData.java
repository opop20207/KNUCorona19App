package com.example.knucorona19app;

import java.io.Serializable;

public class ChartData implements Serializable {
    public String date, infection, recovered, deaths;
    public String testNow, testNegative;
    public String dInfection, dRecovered, dDeaths, dTestNow, dTestNegative;

    public ChartData(){

    }

    public ChartData(String date, String infection, String recovered, String deaths){
        this.date = date;
        this.infection = infection;
        this.recovered = recovered;
        this.deaths= deaths;
    }

    public ChartData(String[] s){
        this.date = s[0];
        this.infection = s[1];
        this.recovered = s[2];
        this.deaths = s[3];
        this.testNow = s[4];
        this.testNegative = s[5];
        this.dInfection = s[6];
        this.dRecovered = s[7];
        this.dDeaths = s[8];
        this.dTestNow = s[9];
        this.dTestNegative = s[10];
    }
}