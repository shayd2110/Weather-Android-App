package com.example.weatherwithfragment;

import android.content.Context;
import android.net.Uri;

import com.example.newweatherapp.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteFetch {
    //    private  static final String OPEN_WEATHER_MAP_API =
//            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";



    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/onecall?lat=";

    public static JSONObject getJSON(Context context, String city) {
        try {
            String lanLat = Utils.getLocationFromAddress(context,city);
            String[] separated = lanLat.split(",");
            String lat = separated[0];
            String lon = separated[1];

//            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            //"http://api.openweathermap.org/data/2.5/onecall?
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.openweathermap.org")
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("onecall")
                    .appendQueryParameter("lat",lat)
                    .appendQueryParameter("lon",lon)
//                    .appendQueryParameter("lang", "he")
                    .appendQueryParameter("units","metric")
                    .appendQueryParameter("exclude","minutely")
                    .appendQueryParameter("appid",context.getString(R.string.open_weather_maps_app_id))

                    .build();

            URL url = new URL(builder.toString());
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.open_weather_maps_app_id));

           String prop =  connection.getRequestProperties().toString();


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

//            if (data.getInt("cod") != 200) {
//                return null;
//            }

            return data;
        } catch (Exception e) {
            return null;
        }
    }
    public static JSONObject getJSONHistorical(Context context, String city,long dt) {
        try {
            String lanLat = Utils.getLocationFromAddress(context,city);
            String[] separated = lanLat.split(",");
            String lat = separated[0];
            String lon = separated[1];

//            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            //"http://api.openweathermap.org/data/2.5/onecall/timemachine?
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.openweathermap.org")
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("onecall")
                    .appendPath("timemachine")
                    .appendQueryParameter("lat",lat)
                    .appendQueryParameter("lon",lon)
                    //.appendQueryParameter("lang", "he")
                    .appendQueryParameter("units","metric")
                    .appendQueryParameter("exclude","minutely")
                    .appendQueryParameter("appid",context.getString(R.string.open_weather_maps_app_id))
                    .appendQueryParameter("dt",Long.toString(dt))

                    .build();

            //GOOD:   1615046824
            //PROBLEM 1615112343
            //CUR     1615112462

            URL url = new URL(builder.toString());
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.open_weather_maps_app_id));

            String prop =  connection.getRequestProperties().toString();


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

//            if (data.getInt("cod") != 200) {
//                return null;
//            }

            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONObject getJSON4Week(Context context, String city) {
        try {
            String lanLat = Utils.getLocationFromAddress(context,city);
            String[] separated = lanLat.split(",");
            String lat = separated[0];
            String lon = separated[1];

//            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            //"http://api.openweathermap.org/data/2.5/onecall?
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.openweathermap.org")
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("onecall")
                    .appendQueryParameter("lat",lat)
                    .appendQueryParameter("lon",lon)
//                    .appendQueryParameter("lang", "he")
                    .appendQueryParameter("units","metric")
                    .appendQueryParameter("exclude","minutely,hourly")
                    .appendQueryParameter("appid",context.getString(R.string.open_weather_maps_app_id))

                    .build();

            URL url = new URL(builder.toString());
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.open_weather_maps_app_id));

            String prop =  connection.getRequestProperties().toString();


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

//            if (data.getInt("cod") != 200) {
//                return null;
//            }

            return data;
        } catch (Exception e) {
            return null;
        }
    }


}
