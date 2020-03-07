package com.example.knucorona19app;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Locale;

public class MyValueFormatter extends ValueFormatter {
    private final BarLineChartBase<?> chart;

    public MyValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {
        int v = (int) value;
        String y = Integer.toString(v/10000);
        String m = Integer.toString((v%10000)/100);
        String d = Integer.toString(v%100);
        return m+"/"+d;
    }
}
