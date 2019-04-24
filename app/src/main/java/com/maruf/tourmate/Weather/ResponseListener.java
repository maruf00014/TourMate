package com.maruf.tourmate.Weather;

import com.maruf.tourmate.Weather.Current.CurrentResponse;
import com.maruf.tourmate.Weather.Forecast.ForecastResponse;

public interface ResponseListener {

    void onCurrentResponseReceived(CurrentResponse currentResponse, String units);

    void onForecastResponseReceived(ForecastResponse forecastResponse, String units);
}
