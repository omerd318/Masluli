package com.example.masluli;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.masluli.Model.Weather.Forecastday;
import com.example.masluli.Model.Weather.WeatherModel;

import java.util.List;

public class WeatherListViewModel extends ViewModel {

    private LiveData<List<Forecastday>> data;

    public WeatherListViewModel(String latLng) {
        data = WeatherModel.instance.getWeatherForecast(latLng, "3");
    }

    LiveData<List<Forecastday>> getData(){
        return data;
    }

}
