package com.example.weatherwithfragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.newweatherapp.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
//
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
    Typeface weatherFont;

    private TextView cityField;
    private TextView weatherIcon;
    private TextView updatedField;
    private TextView currentTempField;
    private TextView descriptionField;
    private TextView morningField;
    private TextView eveningField;
    private TextView afterNoonField;
    private TextView nightField;
    private TextView minTempField;
    private TextView maxTempField;
    private TextView humidField;
    private TextView pressureField;
    private TextView windField;
    private View rootView;
    private static ForecastObject forecast;

    Handler handler;
    private ImageView windIcon;

    public WeatherFragment() {
        // Required empty public constructor
        handler = new Handler();
    }

    public static ForecastObject getForecast() {
        return forecast;
    }

    public static void setForecast(ForecastObject forecast) {
        WeatherFragment.forecast = forecast;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment WeatherFragment.
     */
//    // TODO: Rename and change types and number of parameters
//    public static WeatherFragment newInstance(String param1, String param2) {
//        WeatherFragment fragment = new WeatherFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);//
        rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        updatedField = (TextView) rootView.findViewById(R.id.updated_field);
        cityField = (TextView) rootView.findViewById(R.id.city_field);
        currentTempField = (TextView) rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) rootView.findViewById(R.id.weather_icon);
        descriptionField = (TextView)rootView.findViewById(R.id.description_field);
         morningField = (TextView) rootView.findViewById(R.id.morning_field);
         afterNoonField = (TextView) rootView.findViewById(R.id.afternoon_field);
        eveningField = (TextView) rootView.findViewById(R.id.evening_field);
         nightField = (TextView) rootView.findViewById(R.id.night_field);
         minTempField = (TextView) rootView.findViewById(R.id.min_temp_field);
         maxTempField = (TextView) rootView.findViewById(R.id.max_temp_field);
         humidField = (TextView) rootView.findViewById(R.id.humidity_field);
         pressureField  = (TextView) rootView.findViewById(R.id.pressure_field);
//        detailsField = (TextView) rootView.findViewById(R.id.details_field);
        windIcon = (ImageView) rootView.findViewById(R.id.wind_icon_shdow);
        windIcon.setVisibility(View.INVISIBLE);
        windField = (TextView) rootView.findViewById(R.id.wind_field);
        weatherIcon.setTypeface(weatherFont);


        return rootView;
    }

    public void     changeCity(String city) {
        updateWeatherData(city);
    }
    public interface OnDataPass {
        public void onDataPass(ForecastObject forecast);
    }
    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }
    public void passData(ForecastObject forecast) {
        dataPasser.onDataPass(forecast);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        updateWeatherData(new CityPreference(getActivity()).getCity());
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json,city);
                        }
                    });
                }
            }
        }.start();
    }
    //old : {"temp":16.07,"feels_like":12.5,"temp_min":15,"temp_max":17.78,"pressure":1018,"humidity":67}
    private void renderWeather(JSONObject json, final String city) {
        try {
            forecast = new ForecastObject();
            Double lat = Double.parseDouble(json.getString("lat"));
            Double lon = Double.parseDouble(json.getString("lon"));
            String countryName = Utils.getCountryName(getContext(),lat,lon);
            String cityName = Utils.getCityName(getContext(),lat,lon);
            try {
                cityName = Utils.getInternationalNameFromCity(getContext(),cityName);

            }
            catch(IncorrectCityNameException cityNameException){
                String exceptionString = cityNameException.getMessage();
                Utils.showToast(exceptionString,getContext());
                return;
            }
            catch (Exception e) {
                e.printStackTrace();
            }



            cityField.setText(Utils.capitalize(cityName) +
                    ", " + countryName
            );
            GeoPoint coordinates = new GeoPoint(lat,lon);
            forecast.setLatLon(coordinates); //add coordinates to Object

            forecast.setCityName(cityName); // add city
            forecast.setCountryName(countryName);



            JSONObject current = json.getJSONObject("current");
            JSONObject todayJsonObj = json.getJSONArray("daily").getJSONObject(0);

            JSONObject todayTemps = todayJsonObj.getJSONObject("temp");
            JSONObject details = current.getJSONArray("weather").getJSONObject(0);

            descriptionField.setText(details.getString("description"));
            morningField.setText("Morning: " +todayTemps.getString("morn") + "°C");
            afterNoonField.setText("Afternoon: " + todayTemps.getString("day") + "°C");
            eveningField.setText("Evening: " + todayTemps.getString("eve") + "°C");
            nightField.setText("Night: " + todayTemps.getString("night") + "°C");
            minTempField.setText("Min Temp: " + todayTemps.getString("min") + "°C");
            maxTempField.setText( "Max Temp: " + todayTemps.getString("max") + "°C");
            humidField.setText( "Humidity: " + current.getString("humidity") + "%");
            pressureField.setText("Pressure: " + current.getString("pressure") + "Hpa");



//            detailsField.setText(
//                            "\n" + "Morning " + todayTemps.getString("morn") + "°C" +
//                            "\n" + "Afternoon " + todayTemps.getString("day") + "°C" +
//                            "\n" + "Evening " + todayTemps.getString("eve") + "°C" +
//                            "\n" + "Night " + todayTemps.getString("night") + "°C" +
//                            "\n" + "Min Temp " + todayTemps.getString("min") + "°C"+
//                            "\n" + "Max Temp " + todayTemps.getString("max") + "°C"+
//                            "\n" + "Humidity " + current.getString("humidity") + "%" +
//                            "\n" +   "Pressure " + current.getString("pressure") + "Hpa"
//            );

            forecast.setCurForecast(details.getString("description"));
            forecast.setMinTemp(Double.parseDouble(todayTemps.getString("min"))); //add min to Object
            forecast.setMaxTemp(Double.parseDouble(todayTemps.getString("max"))); //add max to Object
            forecast.setHumidity(Double.parseDouble(current.getString("humidity") )); //add humidity to Object
            forecast.setPressure(Double.parseDouble(current.getString("pressure") )); //add pressure to Object
            forecast.setMorningTemp(Double.parseDouble(todayTemps.getString("morn"))); //add morning temp to Object
            forecast.setDayTimeTemp(Double.parseDouble(todayTemps.getString("day"))); //add day temp to Object
            forecast.setEveningTemp(Double.parseDouble(todayTemps.getString("eve"))); //add Evening temp to Object
            forecast.setNightTemp(Double.parseDouble(todayTemps.getString("night")));       //add Night temp to Object

            currentTempField.setText(
                    String.format("%.2f", current.getDouble("temp")) + "°C"
            );
            forecast.setCurTemp(current.getDouble("temp"));//add Current temp to Object

            DateFormat df = DateFormat.getDateInstance();

            windField.setText("Wind Speed " + current.getString("wind_speed") + "m/s N");
            /////////////////////////////////////
            forecast.setWindSpeed(Double.parseDouble(current.getString("wind_speed")));//add Wind Speed to Object
            int deg  =current.getInt("wind_deg") +180;
            forecast.setWindDirection(deg); //add Wind Direction to Object

            Date today = new Date(current.getLong("dt") * 1000);

            String updatedOn = df.format(today);
            long dt = current.getLong("dt");
            RotateAnimation rotate = new RotateAnimation(0f, deg, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setFillAfter(true);
            windIcon.setAnimation(rotate);
            windIcon.setVisibility(View.VISIBLE);
            updatedField.setText("Last update: " + updatedOn);
            forecast.setDay(Utils.getDayStringOld(today)); //add Day of the week to Object

            String icon = setWeatherIcon(details.getInt("id"),
                    current.getLong("sunrise") * 1000,
                    current.getLong("sunset") * 1000,
                    today);
            forecast.setIcon(icon);//add icon to Object

            new Thread() {
                public void run() {
                    final JSONObject jsonHistorical = RemoteFetch.getJSONHistorical(getActivity(), city,dt);
                    if (json == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), getActivity().getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                handleDescriptionGetter(json,jsonHistorical,city/**,forecast*/);
                            }
                        });
                    }
                }
            }.start();


        } catch (Exception e) {
            Log.e("SimpleWeather", "Field"+e.getMessage().toString()+" not presnt in Json Recevied");
        }
    }



    private void handleDescriptionGetter(JSONObject json, JSONObject jsonHistorical, String city/**, ForecastObject forecast*/) {
        String[] weatherDescription = new String[4];
        try {
            JSONArray hourly = json.getJSONArray("hourly");
            int morningTime = 5;
            int dayTime = 11;
            int evening = 17;
            int night = 23;
            String morningDescription;
            String dayTimeDescription;
            String eveningDescription;
            String nightDescription;
            Date fromEpoch = new Date((hourly.getJSONObject(0).getLong("dt")/**+2*3600*/) * 1000); // to make it Israel time
            forecast.setDate( new Timestamp(fromEpoch) );//add Date temp to Object
            int hours = fromEpoch.getHours();

            // claculate morning description
            morningDescription = calcDescriptionAtTime(hours, morningTime,json,jsonHistorical,hourly);
            if(morningDescription != null) {
                morningField.setText(morningField.getText().toString() + " , " + morningDescription);
                forecast.setMorningForecast(morningDescription); //add morning Description to Object
            }
            // claculate dayTime description
            dayTimeDescription = calcDescriptionAtTime(hours, dayTime,json,jsonHistorical,hourly);
            if(dayTimeDescription != null) {
                afterNoonField.setText(afterNoonField.getText().toString()  + " , " + dayTimeDescription);
                forecast.setDayTimeForecast(dayTimeDescription);//add DayTime Description to Object
            }
            // claculate evening description
            eveningDescription = calcDescriptionAtTime(hours, evening,json,jsonHistorical,hourly);
            if(eveningDescription != null) {
                eveningField.setText(eveningField.getText().toString()  + " , " + eveningDescription);
                forecast.setEveningForecast(eveningDescription);//add evening Description to Object
            }
            // claculate night description
            nightDescription = calcDescriptionAtTime(hours, night,json,jsonHistorical,hourly);
            if(nightDescription != null) {
                nightField.setText(nightField.getText().toString()  + " ,  "  + nightDescription);
                forecast.setNightForecast(nightDescription);//add night Description to Object
            }

            addData2Db(forecast);
            passData(forecast);

            }
        catch (JSONException e)
        {
            Log.e("SimpleWeather", "Field"+e.getMessage().toString()+" not presnt in Json Recevied");
        }


        /////////////////////////////////////

    }

    private String calcDescriptionAtTime(int hours,int timeAtDay,JSONObject json,JSONObject jsonHistorical,JSONArray hourly  )
    {
        try{

            String timeAtDayDescription;
            if (hours > timeAtDay ) {
                int hoursAfterTimeAtDay = hours - timeAtDay;
                JSONArray hourlyHistorical = jsonHistorical.getJSONArray("hourly");
                JSONObject jsonObjInLastTimeAtDay = hourlyHistorical.getJSONObject(hourlyHistorical.length() - hoursAfterTimeAtDay  -1);
                timeAtDayDescription = jsonObjInLastTimeAtDay.getJSONArray("weather").getJSONObject(0).getString("description");


            }
            else if (hours < timeAtDay){
                int hoursUntilTimeAtDay = timeAtDay-hours ;
                JSONObject jsonObjInTimeAtDay = hourly.getJSONObject(hoursUntilTimeAtDay);
                timeAtDayDescription = jsonObjInTimeAtDay.getJSONArray("weather").getJSONObject(0).getString("description");
            }
            else {
                timeAtDayDescription =json.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description");
            }
            return timeAtDayDescription;
        }
        catch (JSONException e){
            Log.e("calcDescriptionAtTime" + timeAtDay,e.getMessage());
            return null;
        }

    }




    private String setWeatherIcon(int actualId, long sunrise, long sunset, Date today) {

        int id = actualId / 100;
        int color,shadowColor;
        String icon = "";
        if (actualId == 800) {
            long currentTime = today.getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
                rootView.setBackgroundResource(R.drawable.clear_day);
                color =ContextCompat.getColor(getContext(), R.color.azure);
                        //Color.parseColor("#01148C");//ContextCompat.getColor(getContext(), R.color.cold);
                //#001CD1
                shadowColor =  ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
                        // Color.parseColor("#F33A00");
                //ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);

                changeAllTextColor(color,shadowColor);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
                rootView.setBackgroundResource(R.drawable.clear_sky_night);
                color = ContextCompat.getColor(getContext(), R.color.star_yellow);
                shadowColor =  ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);
                changeAllTextColor(color,shadowColor);
            }
        } else {
            switch (id) {
                case 2:
                    icon = getActivity().getString(R.string.weather_thunder);
                    rootView.setBackgroundResource(R.drawable.thunder);
                    color = ContextCompat.getColor(getContext(), R.color.aquamarine);
                    shadowColor =  ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);
                    changeAllTextColor(color,shadowColor);
                    break;
                case 3:
                    icon = getActivity().getString(R.string.weather_drizzle);
                    rootView.setBackgroundResource(R.drawable.drizzle);
                    color = ContextCompat.getColor(getContext(), R.color.indigo);
                    shadowColor =  ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);
                    changeAllTextColor(color,shadowColor);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.weather_rainy);
                    rootView.setBackgroundResource(R.drawable.rainy);
                    color = ContextCompat.getColor(getContext(), R.color.my_teal_200);
                    shadowColor =  ContextCompat.getColor(getContext(), R.color.my_red);
                    changeAllTextColor(color,shadowColor);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.weather_snowy);
                    rootView.setBackgroundResource(R.drawable.snowy);
                    color= ContextCompat.getColor(getContext(), R.color.black);
                    shadowColor =  ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);
                    changeAllTextColor(color,shadowColor);
                    break;
                case 7:
                    icon = getActivity().getString(R.string.weather_foggy);
                    rootView.setBackgroundResource(R.drawable.foggy);
                    color = Color.parseColor("#FF1744");//ContextCompat.getColor(getContext(), R.color.sky_blue);
                    //#001CD1
                    shadowColor =  Color.parseColor("#001CD1");//ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);
                    changeAllTextColor(color,shadowColor);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.weather_cloudy);
                    rootView.setBackgroundResource(R.drawable.cloudy3);
                    color = ContextCompat.getColor(getContext(), R.color.blanched_almond);
                    shadowColor =  ContextCompat.getColor(getContext(), R.color.aqua);
                    changeAllTextColor(color,shadowColor);
                    break;


            }
        }
        weatherIcon.setText(icon);
        return icon;
    }




    private void changeAllTextColor(int color, int shadowColor)
    {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.layotId);
        ViewGroup viewGroupScroll = (ViewGroup) rootView.findViewById(R.id.scroll_relative);//windLayout
        ViewGroup viewGroupWind = (ViewGroup) rootView.findViewById(R.id.windLayout);
        for (int i = 0; i < viewGroup.getChildCount() ; i++)
        {
            View childView = viewGroup.getChildAt(i);


            if (childView instanceof TextView)
            {
                TextView childTv = (TextView) childView;
                childTv.setTextColor(color);
                if (shadowColor != 0 ) {
                    childTv.setShadowLayer(25,0,0,shadowColor);

                }
            }

        }
        for (int i = 0; i < viewGroupScroll.getChildCount() ; i++)
        {
            View childView = viewGroupScroll.getChildAt(i);


            if (childView instanceof TextView)
            {
                TextView childTv = (TextView) childView;
                childTv.setTextColor(color);
                if (shadowColor != 0 ) {
                    childTv.setShadowLayer(25,0,0,shadowColor);

                }
            }

        }
        for (int i = 0; i < viewGroupWind.getChildCount() ; i++)
        {
            View childView = viewGroupWind.getChildAt(i);


            if (childView instanceof TextView)
            {
                TextView childTv = (TextView) childView;
                childTv.setTextColor(color);
                if (shadowColor != 0 ) {
                    childTv.setShadowLayer(25,0,0,shadowColor);
                }
            }


        }
    }
    private void addData2Db(ForecastObject forecast)
    {

        DbHandler handler = new DbHandler();
        handler.addForecast(forecast);

    }

    public void renderFromRes(ForecastObjectRet forecastObj){
        String cityName = forecastObj.getCityName();
        String countryName = forecastObj.getCountryName();
        cityField.setText(Utils.capitalize(cityName) +
                ", " + countryName
        );
        Date date = new Date(forecastObj.getDate() * 1000);
        updatedField.setText(date.toString());
        String icon = forecastObj.getIcon();
        weatherIcon.setText(icon);
        final String sunnyWeather = getActivity().getString(R.string.weather_sunny);
        setBackground(icon);
//        String details = "\n" + "Morning " + forecastObj.getMorningTemp()+ "°C" +
//                "\n" + "Afternoon " + forecastObj.getDayTimeTemp() + "°C" +
//                "\n" + "Evening " + forecastObj.getEveningTemp() + "°C" +
//                "\n" + "Night " + forecastObj.getNightTemp() + "°C" +
//                "\n" + "Min Temp " + forecastObj.getMinTemp() + "°C"+
//                "\n" + "Max Temp " + forecastObj.getMaxTemp() + "°C"+
//                "\n" + "Humidity " + forecastObj.getHumidity() + "%" +
//                "\n" +   "Pressure " + forecastObj.getPressure() + "Hpa";
        descriptionField.setText(forecastObj.getCurForecast());
//        detailsField.setText(details);
        morningField.setText("Morning: " +forecastObj.getMorningTemp() + "°C" + ", " +forecastObj.getMorningForecast());
        afterNoonField.setText("Afternoon: " + forecastObj.getDayTimeTemp() + "°C" + ", " +forecastObj.getDayTimeForecast());
        eveningField.setText("Evening: " + forecastObj.getEveningTemp() + "°C" + ", " +forecastObj.getEveningForecast());
        nightField.setText("Night: " + forecastObj.getNightTemp() + "°C" + ", " +forecastObj.getNightTemp());
        minTempField.setText("Min Temp: " + forecastObj.getMinTemp() + "°C");
        maxTempField.setText( "Max Temp: " + forecastObj.getMaxTemp() + "°C");
        humidField.setText( "Humidity: " + forecastObj.getHumidity() + "%");
        pressureField.setText("Pressure: " + forecastObj.getPressure() + "Hpa");
        //TODO: continue rendering
        windField.setText("Wind Speed " + forecastObj.getWindSpeed() + "m/s N");
        int deg  = forecastObj.getWindDirection();
        RotateAnimation rotate = new RotateAnimation(0f, deg, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setFillAfter(true);
        windIcon.setAnimation(rotate);
        windIcon.setVisibility(View.VISIBLE);
        currentTempField.setText(forecastObj.getCurTemp() + "°C");

    }

    private void setBackground(String icon) {
        int color = 0 ,shadowColor = 0;
        if(icon.equals(getActivity().getString(R.string.weather_sunny))){

            rootView.setBackgroundResource(R.drawable.clear_day);
            color =ContextCompat.getColor(getContext(), R.color.azure);
            shadowColor =  ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        }

        else if(icon.equals(getActivity().getString(R.string.weather_clear_night))){

            rootView.setBackgroundResource(R.drawable.clear_sky_night);
            color = ContextCompat.getColor(getContext(), R.color.star_yellow);
            shadowColor =  ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);
        }

        else if(icon.equals(getActivity().getString(R.string.weather_foggy))){

            rootView.setBackgroundResource(R.drawable.foggy);
            color = Color.parseColor("#FF1744");//ContextCompat.getColor(getContext(), R.color.sky_blue);
            shadowColor =  Color.parseColor("#001CD1");//ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);

        }

        else if(icon.equals(getActivity().getString(R.string.weather_cloudy))){

            rootView.setBackgroundResource(R.drawable.cloudy3);
            color = ContextCompat.getColor(getContext(), R.color.blanched_almond);
            shadowColor =  ContextCompat.getColor(getContext(), R.color.aqua);

        }

        else if(icon.equals(getActivity().getString(R.string.weather_rainy))){
            rootView.setBackgroundResource(R.drawable.rainy);
            color = ContextCompat.getColor(getContext(), R.color.my_teal_200);
            shadowColor =  ContextCompat.getColor(getContext(), R.color.my_red);
        }

        else if(icon.equals(getActivity().getString(R.string.weather_snowy))){

            rootView.setBackgroundResource(R.drawable.snowy);
            color= ContextCompat.getColor(getContext(), R.color.black);
            shadowColor =  ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);
        }

        else if(icon.equals(getActivity().getString(R.string.weather_thunder))){

            rootView.setBackgroundResource(R.drawable.thunder);
            color = ContextCompat.getColor(getContext(), R.color.aquamarine);
            shadowColor =  ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);
        }

        else if(icon.equals(getActivity().getString(R.string.weather_drizzle))){

            rootView.setBackgroundResource(R.drawable.drizzle);
            color = ContextCompat.getColor(getContext(), R.color.indigo);
            shadowColor =  ContextCompat.getColor(getContext(), R.color.very_light_sky_blue);
        }
        changeAllTextColor(color,shadowColor);

    }
}
