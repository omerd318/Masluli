package com.example.masluli;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.masluli.Model.Weather.Forecastday;
import com.example.masluli.Model.Weather.WeatherModel;

import java.util.List;

public class WeatherListViewModel extends ViewModel {

    // TODO: change latlng to not be hard coded - from view maslul
    private LiveData<List<Forecastday>> data = WeatherModel.instance.getWeatherForecast("31.318543799474224,35.36236301064491", "3");

    LiveData<List<Forecastday>> getData(){
        return data;
    }

}
