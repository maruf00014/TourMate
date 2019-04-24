package com.maruf.tourmate.Fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maruf.tourmate.R;
import com.maruf.tourmate.Weather.Current.CurrentResponse;
import com.maruf.tourmate.Weather.Forecast.ForecastResponse;
import com.maruf.tourmate.Weather.ResponseListener;
import com.maruf.tourmate.WeatherActivity;
import com.maruf.tourmate.databinding.FragmentCurrentBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherFragment extends Fragment implements ResponseListener {


    FragmentCurrentBinding binding;
    Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_current, container, false);


        return binding.getRoot();
    }



    @Override
    public void onCurrentResponseReceived(CurrentResponse currentResponse, String units) {

       // Toast.makeText(getActivity(),currentResponse.getName(),Toast.LENGTH_SHORT).show();
        String tempUnit = unitChanger(units);

        binding.cityTV.setText(currentResponse.getName());

        binding.dateTimeTV.setText(new SimpleDateFormat("EEE, d MMM, hh:mm aaa")
                .format(new Date(currentResponse.getDt() * 1000L)));

        binding.sunriseTV.setText(new SimpleDateFormat("hh:mm aaa")
                .format(new Date(currentResponse.getSys().getSunrise() * 1000L)));

        binding.sunsetTV.setText(new SimpleDateFormat("hh:mm aaa")
                .format(new Date(currentResponse.getSys().getSunset() * 1000L)));

        binding.humadityTV.setText(String.format(String.valueOf(currentResponse.getMain().getHumidity())+"%%"));

        Picasso.get().load("http://openweathermap.org/img/w/"
                + currentResponse.getWeather().get(0).getIcon() + ".png").into(binding.iconIV);

        binding.tempTV.setText(String.valueOf(Math.round(currentResponse.getMain().getTemp()))+tempUnit);

    }

    @Override
    public void onForecastResponseReceived(ForecastResponse forecastResponse, String units) {

         //Toast.makeText(getActivity(),forecastResponse.getCity().getName(),Toast.LENGTH_SHORT).show();

        String tempUnit = unitChanger(units);

        binding.hourTime1.setText(new SimpleDateFormat("hh aaa")
                .format(new Date(forecastResponse.getList().get(0).getDt() * 1000L)));
        Picasso.get().load("http://openweathermap.org/img/w/"
                + forecastResponse.getList().get(0).getWeather().get(0).getIcon() + ".png").into(binding.hourIcon1);
        binding.hourtempTV1.setText(
                String.valueOf(Math.round(forecastResponse.getList().get(0).getMain().getTemp()))+tempUnit);


        binding.hourTime2.setText(new SimpleDateFormat("hh aaa")
                .format(new Date(forecastResponse.getList().get(1).getDt() * 1000L)));
        Picasso.get().load("http://openweathermap.org/img/w/"
                + forecastResponse.getList().get(1).getWeather().get(0).getIcon() + ".png").into(binding.hourIcon2);
        binding.hourtempTV2.setText(
                String.valueOf(Math.round(forecastResponse.getList().get(1).getMain().getTemp()))+tempUnit);

        binding.hourTime3.setText(new SimpleDateFormat("hh aaa")
                .format(new Date(forecastResponse.getList().get(2).getDt() * 1000L)));
        Picasso.get().load("http://openweathermap.org/img/w/"
                + forecastResponse.getList().get(2).getWeather().get(0).getIcon() + ".png").into(binding.hourIcon3);
        binding.hourtempTV3.setText(
                String.valueOf(Math.round(forecastResponse.getList().get(2).getMain().getTemp()))+tempUnit);


        binding.hourTime4.setText(new SimpleDateFormat("hh aaa")
                .format(new Date(forecastResponse.getList().get(3).getDt() * 1000L)));
        Picasso.get().load("http://openweathermap.org/img/w/"
                + forecastResponse.getList().get(3).getWeather().get(0).getIcon() + ".png").into(binding.hourIcon4);
        binding.hourtempTV4.setText(
                String.valueOf(Math.round(forecastResponse.getList().get(3).getMain().getTemp()))+tempUnit);

    }

    public String unitChanger(String units){

        if(units.equals("metric")){

            return (char) 0x00B0+"c";

        } else {

            return (char) 0x00B0+"F";

        }


    }


}
