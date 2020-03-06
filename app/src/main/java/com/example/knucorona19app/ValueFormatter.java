package com.example.knucorona19app;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

class ValueFormatter implements IValueFormatter, IAxisValueFormatter {

    public String getFormattedValue(float value) {
        return String.valueOf(value);
    }

    /**
     * Used to draw axis labels, calls {@link #getFormattedValue(float)} by default.
     *
     * @param value float to be formatted
     * @param axis  axis being labeled
     * @return formatted string label
     */
    public String getAxisLabel(float value, AxisBase axis) {
        return getFormattedValue(value);
    }

    /**
     * Used to draw bar labels, calls {@link #getFormattedValue(float)} by default.
     *
     * @param barEntry bar being labeled
     * @return formatted string label
     */
    public String getBarLabel(BarEntry barEntry) {
        return getFormattedValue(barEntry.getY());
    }

    /**
     * Used to draw stacked bar labels, calls {@link #getFormattedValue(float)} by default.
     *
     * @param value        current value to be formatted
     * @param stackedEntry stacked entry being labeled, contains all Y values
     * @return formatted string label
     */
    public String getBarStackedLabel(float value, BarEntry stackedEntry) {
        return getFormattedValue(value);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return null;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return null;
    }

    /**
     * Used to draw line and scatter labels, calls {@link #getFormattedValue(float)} by default.
     *
     * @param entry point being labeled, contains X value
     * @return formatted string label
     */


}
