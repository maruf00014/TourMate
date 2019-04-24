package com.maruf.tourmate.Weather;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.maruf.tourmate.Fragments.CurrentWeatherFragment;
import com.maruf.tourmate.Fragments.ForecastWeatherFragment;

import java.text.DecimalFormat;



public class TempDataFormetter implements IValueFormatter {

    String tempUnit = "";
    CurrentWeatherFragment cwf = new CurrentWeatherFragment();
    private DecimalFormat mFormat;

    public TempDataFormetter(String units) {

        mFormat = new DecimalFormat("###,###,##0");
        tempUnit = cwf.unitChanger(units);
    }
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here

        return mFormat.format(value) + tempUnit; // e.g. append a dollar-sign
    }
}
