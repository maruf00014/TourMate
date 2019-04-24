package com.maruf.tourmate.Weather;

import com.maruf.tourmate.Weather.Current.CurrentResponse;
import com.maruf.tourmate.Weather.Forecast.ForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherAPIService {

    @GET
    Call<CurrentResponse> getCurrentResponse(@Url String endUrl);

    @GET
    Call<ForecastResponse> getForecastResponse(@Url String endUrl);


}
