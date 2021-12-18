package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static int count = 4;
    private String city = "";
    Double[] temp = new Double[count];
    String[] time = new String[count], weather = new String[count];
    int[] ic_weather = new int[count];
    JSONObject all_ic_weather = new JSONObject();

    private static String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private static String APPID = "15b3a3e1a7e9e22418d8578337d0f4ab";
    private static String units = "imperial"; //fahrenheit

    public MainActivity() throws JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            all_ic_weather.put("Clear", R.drawable.ic_clear_sky);
            all_ic_weather.put("Clouds", R.drawable.ic_broken_clouds);
            all_ic_weather.put("Mist", R.drawable.ic_mist);
            all_ic_weather.put("Rain", R.drawable.ic_shower_rain);
            all_ic_weather.put("Snow", R.drawable.ic_snow);
            all_ic_weather.put("Thunderstorm", R.drawable.ic_thunderstorm);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        EditText searchParam = findViewById(R.id.searchParam);
        Button searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    makeAPIRequest(searchParam.getText().toString().replaceAll("\\s", ""));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void parseData(JSONObject weatherDataObj) {
        JSONArray weatherData = new JSONArray();
        try {
            weatherData = weatherDataObj.getJSONArray("list");
            city = weatherDataObj.getJSONObject("city").getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<count; i++) {
            try {
                temp[i] = weatherData.getJSONObject(i).getJSONObject("main").getDouble("temp");
                weather[i] = weatherData.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                time[i] = weatherData.getJSONObject(i).getString("dt_txt").substring(12,19);
                ic_weather[i] = all_ic_weather.getInt(
                        weatherData.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main")
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void makeAPIRequest(String parameter){
        TextView cityName = findViewById(R.id.cityName);
        TextView errorMsg = findViewById(R.id.errorMessage);
        RecyclerView weatherReport = findViewById(R.id.weatherReport);
        JSONObject weatherData = new JSONObject();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = BASE_URL + parameter + "&cnt=" + count + "&appid=" + APPID + "&units=" + units;

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        parseData(response);
                        errorMsg.setText(null);
                        cityName.setText("Weather in " + city);
                        // setting up the recyclerView
                        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(temp, time, weather, ic_weather);
                        weatherReport.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        weatherReport.setAdapter(recyclerAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherReport.setAdapter(null);
                cityName.setText(null);
                errorMsg.setText("Oops! Address not found.");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}