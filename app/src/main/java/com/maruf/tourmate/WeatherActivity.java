package com.maruf.tourmate;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.maruf.tourmate.Fragments.CurrentWeatherFragment;
import com.maruf.tourmate.Fragments.ForecastWeatherFragment;
import com.maruf.tourmate.Weather.Current.CurrentResponse;
import com.maruf.tourmate.Weather.Forecast.ForecastResponse;
import com.maruf.tourmate.Weather.ResponseListener;
import com.maruf.tourmate.Weather.WeatherAPIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    public static final String WEATHER_BASE_URL = "http://api.openweathermap.org/";

    TabLayout tabLayout;
    ViewPager viewPager;
    WeatherPagerAdapter weatherPagerAdapter;

    CurrentWeatherFragment currentWeatherFragment;
    ForecastWeatherFragment forecastWeatherFragment;

    ResponseListener currentResponseListener;
    ResponseListener forecastResponseListener;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    double lat;
    double lon;

    boolean success = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("Forecast"));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        currentWeatherFragment = new CurrentWeatherFragment();
        currentResponseListener = currentWeatherFragment;

        forecastWeatherFragment = new ForecastWeatherFragment();
        forecastResponseListener = forecastWeatherFragment;

        weatherPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(weatherPagerAdapter);

        sharedPreferences = getSharedPreferences("TourMate", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        lat = getIntent().getDoubleExtra("lat", 0.0);
        lon = getIntent().getDoubleExtra("lon", 0.0);

        getData();






    }

    public void getData(){


        if(sharedPreferences.getString("city","").equals("")){

            getDataByLatLon(lat,lon);


        }
        else {
            getDataByCityName(sharedPreferences.getString("city",""));

        }

    }

    public void getDataByLatLon(double lat, double lon){

        String units = sharedPreferences.getString("units","metric");


        String currentByLanLon = String.format("data/2.5/weather?lat=%s&lon=%s&units=%s&appid=%s"
                , lat, lon, units, getString(R.string.api_key));


        String forecastByLanLon = String.format("data/2.5/forecast?lat=%s&lon=%s&units=%s&appid=%s"
                , lat, lon, units, getString(R.string.api_key));


        Retrofit weatherRetrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPIService weatherAPIService =
                weatherRetrofit.create(WeatherAPIService.class);

        getCurrentResponses(weatherAPIService, currentByLanLon, units);
        getForecastResponses(weatherAPIService, forecastByLanLon, units);

    }

    public void getDataByCityName(String city){

        String units = sharedPreferences.getString("units","metric");

        editor.putString("city",city);
        editor.commit();


        String currentByCity = String.format("data/2.5/weather?q=%s&units=%s&appid=%s"
                , city, units, getString(R.string.api_key));

        String forcastByCity = String.format("data/2.5/forecast?q=%s&units=%s&appid=%s"
                , city, units, getString(R.string.api_key));


        Retrofit weatherRetrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPIService weatherAPIService =
                weatherRetrofit.create(WeatherAPIService.class);

        getCurrentResponses(weatherAPIService, currentByCity, units);

        getForecastResponses(weatherAPIService, forcastByCity, units);



    }

    public void getCurrentResponses(WeatherAPIService weatherAPIService, String endUrl, final String units) {




        weatherAPIService.getCurrentResponse(endUrl).enqueue(new Callback<CurrentResponse>() {
            @Override
            public void onResponse(Call<CurrentResponse> call, Response<CurrentResponse> response) {

                if (response.isSuccessful()) {


                    currentResponseListener.onCurrentResponseReceived(response.body(), units);



                }
                else {
                    editor.putString("city","");
                    editor.commit();
                    Toast.makeText(WeatherActivity.this, "City Not Found", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<CurrentResponse> call, Throwable t) {

                Toast.makeText(WeatherActivity.this, "No data Found!", Toast.LENGTH_LONG).show();

            }
        });




    }

    public void getForecastResponses(WeatherAPIService weatherAPIService, String endUrl, final String units) {


        weatherAPIService.getForecastResponse(endUrl).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {

                if (response.isSuccessful()) {

                    currentResponseListener.onForecastResponseReceived(response.body(), units);
                    forecastResponseListener.onForecastResponseReceived(response.body(), units);
                    //Toast.makeText(WeatherActivity.this,response.body().getCity().getName(),Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {

               // Toast.makeText(WeatherActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


    class WeatherPagerAdapter extends FragmentPagerAdapter {


        public WeatherPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch (i) {
                case 0:

                    return currentWeatherFragment;

                case 1:


                    return forecastWeatherFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.weather_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        MenuItem locationItem = menu.findItem(R.id.action_location);

        locationItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                editor.putString("city","");
                editor.commit();
                getData();

                return false;
            }
        });

        MenuItem unitItem = menu.findItem(R.id.action_unit);

        unitItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String title = String.valueOf(item.getTitle());

                switch (title)

                {
                    case "Change to F":

                        editor.putString("units", "imperial");
                        editor.commit();
                        item.setTitle("Change to c");

                        getData();
                        break;

                    case "Change to c":

                        editor.putString("units", "metric");
                        editor.commit();
                        item.setTitle("Change to F");

                        getData();
                        break;



                }


                return false;
            }
        });


        searchView.setQueryHint("Enter City Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String city) {

                //Toast.makeText(WeatherActivity.this,query,Toast.LENGTH_SHORT).show();

                getDataByCityName(city);
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchView.setIconified(true);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}

