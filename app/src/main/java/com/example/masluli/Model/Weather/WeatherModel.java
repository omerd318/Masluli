package com.example.masluli.Model.Weather;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherModel {
    final public static WeatherModel instance = new WeatherModel();

    final String BASE_URL = "https://weatherapi-com.p.rapidapi.com/";
    Retrofit retrofit;
    WeatherApi weatherApi;

    private WeatherModel(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        weatherApi = retrofit.create(WeatherApi.class);
    }

    public LiveData<List<Forecastday>> getWeatherForecast(String latlng, String days){
        MutableLiveData<List<Forecastday>> data = new MutableLiveData<>();
        Call<Weather> call = weatherApi.getWeatherForecast(latlng, days);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()){
                    Weather res = response.body();
                    data.setValue(res.getForecast().getForecastday());
                }else{
                    Log.d("TAG","----- getWeatherforecast response error");
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.d("TAG","----- getWeatherforecast fail");
            }
        });
        return data;
    }
}
