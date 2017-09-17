package com.sawyerd.weatherapp;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {
    TextView tempTextView;
    TextView cityTextView;
    TextView weatherDTextView;
    TextView dateTextView;
    ImageView weatherImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = (TextView) findViewById(R.id.tempTextView);
        cityTextView = (TextView) findViewById(R.id.cityTextView);
        weatherDTextView = (TextView) findViewById(R.id.weatherDTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        weatherImageView = (ImageView) findViewById(R.id.weatherImageView);
        //weatherImageView.setImageResource(R.drawable.icon_clearsky);

        dateTextView.setText(getCurrentDate());


        String url = "http://api.openweathermap.org/data/2.5/weather?q=Moscow&appid=cd58c6378939f33f0231d9bf186c70c0&units=metric";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

       @Override
         public void onResponse(JSONObject response) {
            //tempTextView.setText("Response: " + response.toString());

           try {
               JSONObject mainJSONObject = response.getJSONObject("main");
               JSONArray weatherArray = response.getJSONArray("weather");
               JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

               String temp = Integer.toString((int) Math.round(mainJSONObject.getDouble("temp")));
               String weatherDescription = firstWeatherObject.getString("description");
               String city = response.getString("name");


               tempTextView.setText(temp);
               weatherDTextView.setText(weatherDescription);
               cityTextView.setText(city);

               int iconResourceID = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", ""), "drawable", getPackageName());
               weatherImageView.setImageResource(iconResourceID);

           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
        }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {

             }
        });

// Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);

    }

    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }
}
