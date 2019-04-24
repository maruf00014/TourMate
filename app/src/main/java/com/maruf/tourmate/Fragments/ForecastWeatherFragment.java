package com.maruf.tourmate.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.maruf.tourmate.R;
import com.maruf.tourmate.Weather.Current.CurrentResponse;
import com.maruf.tourmate.Weather.Forecast.ForecastResponse;
import com.maruf.tourmate.Weather.ResponseListener;
import com.maruf.tourmate.Weather.TempDataFormetter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastWeatherFragment extends Fragment implements ResponseListener {

    private LineChart chart;

     ArrayList<String> date = new ArrayList<>();
     ArrayList<Entry> minTemp = new ArrayList<>();
     ArrayList<Entry> maxTemp = new ArrayList<>();

    public ForecastWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forcast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        chart = view.findViewById(R.id.lineChart);



    }

    @Override
    public void onCurrentResponseReceived(CurrentResponse currentResponse, String units) {

    }

    @Override
    public void onForecastResponseReceived(ForecastResponse forecastResponse, final String units) {

        //Toast.makeText(getActivity(),forecastResponse.getCity().getName(), Toast.LENGTH_SHORT).show();

            chart.clear();

            date.clear();
            maxTemp.clear();
            minTemp.clear();

        for (int i=0 ; i< forecastResponse.getList().size();i++){

            date.add(new SimpleDateFormat("d MMM")
                    .format(new Date(forecastResponse.getList().get(i).getDt() * 1000L)));
            minTemp.add(new Entry(i,Float.valueOf(String.valueOf(forecastResponse.getList().get(i).getMain().getTempMin()))));
            maxTemp.add(new Entry(i,Float.valueOf(String.valueOf(forecastResponse.getList().get(i).getMain().getTempMax()))+2));

        }





        LineDataSet minTempDataSet = new LineDataSet(minTemp, "Minimum Temperature");
        //minTempDataSet.setDrawFilled(true);
        minTempDataSet.setLineWidth(3f);
        minTempDataSet.setColor(getActivity().getColor(R.color.blue));
        minTempDataSet.setCircleColor(getActivity().getColor(R.color.blue));


        LineDataSet maxTempDataSet = new LineDataSet(maxTemp, "Maximum Temperature");
       // maxTempDataSet.setDrawFilled(true);
        maxTempDataSet.setLineWidth(3f);
        maxTempDataSet.setLineWidth(3f);
        maxTempDataSet.setColor(getActivity().getColor(R.color.red));
        maxTempDataSet.setCircleColor(getActivity().getColor(R.color.red));

/*
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            minTempDataSet.setFillDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue));
            maxTempDataSet.setFillDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.fade_red));

        }
*/

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return date.get((int)(value));
            }

        });




        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return  new DecimalFormat("###,###,##0").format(value)
                        + new CurrentWeatherFragment().unitChanger(units);
            }

        });


        ArrayList<ILineDataSet> idata = new ArrayList<>();

        idata.add(maxTempDataSet);
        idata.add(minTempDataSet);

        LineData data = new LineData(idata);


        data.setValueFormatter(new IValueFormatter() {

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                return new DecimalFormat("###,###,##0").format(value)
                        + new CurrentWeatherFragment().unitChanger(units);

            }
        });

        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);




    }


}



