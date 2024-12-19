package com.example.weatherapp.ui.theme

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.weatherapp.Api.NetworkResponse
import com.example.weatherapp.Api.WeatherModel
import com.example.weatherapp.R

@Composable
fun WeatherPage(viewmodel: WetherViewModel) {
    var city by remember {
        mutableStateOf("")
    }

    val weatherResult = viewmodel.weatherResult.observeAsState()

    val keyboardcontroller = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Image(
            painter = painterResource(id = R.drawable.weather),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = city,
                    onValueChange = {
                        city = it
                    },
                    label = {
                        Text(text = "Search for any location")
                    }
                )

                IconButton(onClick = {
                    viewmodel.getData(city)
                    keyboardcontroller?.hide()
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            }

            when (val result = weatherResult.value) {
                is NetworkResponse.Error -> {
                    Text(text = "Error: ${result.message}")
                }

                NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                is NetworkResponse.Succsess -> {
                    WeatherDetails(data = result.data)
                }

                null -> {}
            }
        }
    }
}
@Composable
fun WeatherDetails(data: WeatherModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        )
        {

            Icon(
                imageVector = Icons.Default.LocationOn, contentDescription = "Location",
                modifier = Modifier.size(40.dp)
            )

            Text(text = data.location.name, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color.Blue)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = data.location.country,
                fontSize = 20.sp,
                color = Color.Magenta,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${data.current.temp_c}° c",
            fontSize = 62.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            modifier = Modifier.size(250.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "condition icon"
        )
        
       Text(text = data.current.condition.text, fontSize = 25.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.fillMaxWidth()) {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {

                        WeatherKey("Humidity", data.current.humidity )
                        WeatherKey("Wind Speed", data.current.wind_kph )

                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {

                    WeatherKey("UV", data.current.uv )
                    WeatherKey("Percipitation", data.current.precip_mm )

                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    WeatherKey("Local Time", data.location.localtime.split(" ")[1])
                    WeatherKey("Local Date", data.location.localtime.split(" ")[0])
                }

            }

        }


    }
}

@Composable
fun WeatherKey(key:String, value: String) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold)
    }
}
