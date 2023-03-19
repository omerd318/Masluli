package com.example.masluli.Model.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WeatherApi {
    @Headers({
            "X-RapidAPI-Key: 1b623d61fcmshc8fca2b71b6082ep1b870ejsn8f087a75ddc8",
            "X-RapidAPI-Host: weatherapi-com.p.rapidapi.com"
    })
    @GET("/forecast.json")
    Call<Weather> getWeatherForecast(@Query("q") String latlng, @Query("days") String days);
}
