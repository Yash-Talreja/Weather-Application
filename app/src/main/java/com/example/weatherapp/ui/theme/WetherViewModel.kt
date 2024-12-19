package com.example.weatherapp.ui.theme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Api.Constant
import com.example.weatherapp.Api.NetworkResponse
import com.example.weatherapp.Api.RetrofitInstance
import com.example.weatherapp.Api.WeatherModel
import kotlinx.coroutines.launch

class WetherViewModel:ViewModel() {


    private val weatherApi=RetrofitInstance.weatherApi
    private val _weatherData= MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherData

    fun getData(city:String)
    {

        _weatherData.value= NetworkResponse.Loading

        viewModelScope.launch {

            val response=weatherApi.getWeather(Constant.apikey,city)


            try {

                if (response.isSuccessful)
                {

                    response.body().let {
                        _weatherData.value= NetworkResponse.Succsess(it!!)
                    }

                }else
                {

                    _weatherData.value= NetworkResponse.Error("Error fetching weather data")

                }
            }
            catch (e:Exception)
            {
                _weatherData.value= NetworkResponse.Error("Error fetching weather data")
            }

        }


    }

}