package com.example.knucorona19app;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class BarChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private BarChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bar_chart);
        setTitle("BarChartActivity");
        chart=findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        ValueFormatter custom = new MyValueFormatter("명");

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private class DayAxisValueFormatter extends ValueFormatter {
        private final String[] mMonths = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        public DayAxisValueFormatter(BarChart chart) {
            super();
        }
        @Override
        public String getFormattedValue(float value) {

            int days = (int) value;

            int year = determineYear(days);

            int month = determineMonth(days);
            String monthName = mMonths[month % mMonths.length];
            String yearName = String.valueOf(year);

            if (chart.getVisibleXRange() > 30 * 6) {

                return monthName + " " + yearName;
            } else {

                int dayOfMonth = determineDayOfMonth(days, month + 12 * (year - 2016));

                String appendix = "th";

                switch (dayOfMonth) {
                    case 1:
                        appendix = "st";
                        break;
                    case 2:
                        appendix = "nd";
                        break;
                    case 3:
                        appendix = "rd";
                        break;
                    case 21:
                        appendix = "st";
                        break;
                    case 22:
                        appendix = "nd";
                        break;
                    case 23:
                        appendix = "rd";
                        break;
                    case 31:
                        appendix = "st";
                        break;
                }

                return dayOfMonth == 0 ? "" : dayOfMonth + appendix + " " + monthName;
            }
        }

        private int getDaysForMonth(int month, int year) {

            // month is 0-based

            if (month == 1) {
                boolean is29Feb = false;

                if (year < 1582)
                    is29Feb = (year < 1 ? year + 1 : year) % 4 == 0;
                else if (year > 1582)
                    is29Feb = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);

                return is29Feb ? 29 : 28;
            }

            if (month == 3 || month == 5 || month == 8 || month == 10)
                return 30;
            else
                return 31;
        }

        private int determineMonth(int dayOfYear) {

            int month = -1;
            int days = 0;

            while (days < dayOfYear) {
                month = month + 1;

                if (month >= 12)
                    month = 0;

                int year = determineYear(days);
                days += getDaysForMonth(month, year);
            }

            return Math.max(month, 0);
        }

        private int determineDayOfMonth(int days, int month) {

            int count = 0;
            int daysForMonths = 0;

            while (count < month) {

                int year = determineYear(daysForMonths);
                daysForMonths += getDaysForMonth(count % 12, year);
                count++;
            }

            return days - daysForMonths;
        }

        private int determineYear(int days) {

            if (days <= 366)
                return 2016;
            else if (days <= 730)
                return 2017;
            else if (days <= 1094)
                return 2018;
            else if (days <= 1458)
                return 2019;
            else
                return 2020;

        }
    }

    private class MyValueFormatter extends ValueFormatter {
        public MyValueFormatter(String 명) {
            super();
        }
    }
}
