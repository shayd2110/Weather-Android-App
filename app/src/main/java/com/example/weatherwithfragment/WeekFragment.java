package com.example.weatherwithfragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.newweatherapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class WeekFragment extends Fragment {
    Handler handler;
    private TextView dateTv;
    private TextView tempTv;
    private TextView descriptionTv;
    private TextView humidityTv;
    private TextView windTv;
    private TextView titleTv;
    private TextView day1, day2, day3, day4, day5, day6, day7;
    private TextView icon;
    private View rootView;
//    private TextView[] tvArr = new TextView[7];
    private String iconStr;


    public WeekFragment() {
        this.handler = new Handler();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_week_forecast, container, false);
        dateTv = rootView.findViewById(R.id.dateText);
        tempTv = rootView.findViewById(R.id.tempText);
        titleTv = rootView.findViewById(R.id.weather_title);
        descriptionTv = rootView.findViewById(R.id.descriptionText);
        humidityTv = rootView.findViewById(R.id.humidityText);
        windTv = rootView.findViewById(R.id.windText);
        day1 = rootView.findViewById(R.id.day1);
        day2 = rootView.findViewById(R.id.day2);
        day3 = rootView.findViewById(R.id.day3);
        day4 = rootView.findViewById(R.id.day4);
        day5 = rootView.findViewById(R.id.day5);
        day6 = rootView.findViewById(R.id.day6);
        day7 = rootView.findViewById(R.id.day7);
        icon = rootView.findViewById(R.id.icon);

//        TextView[] tempTvArr = {day1,day2,day3,day4,day5,day6,day7};
//        tvArr = Arrays.copyOf(tempTvArr);



        return rootView;
    }

    public void     changeCity(String city) {
        updateWeatherData(city);
    }

    /**
     * public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
     * super.onViewCreated(view, savedInstanceState);
     * <p>
     * view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
     *
     * @Override public void onClick(View view) {
     * NavHostFragment.findNavController(WeekFragment.this)
     * .navigate(R.id.action_First2Fragment_to_Second2Fragment);
     * }
     * });
     * }
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String icon = getArguments().getString("icon");
        iconStr = icon;
        updateWeatherData(new CityPreference(getActivity()).getCity());



    }

    private void updateWeatherData(String city) {
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON4Week(getActivity(), city);
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
                            renderWeather(json, city);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json, String city) {
        try {
            Double lat = Double.parseDouble(json.getString("lat"));
            Double lon = Double.parseDouble(json.getString("lon"));
            String countryName = Utils.getCountryName(getContext(), lat, lon);
            String cityName = Utils.getCityName(getContext(), lat, lon);
            titleTv.setText(getActivity().getString(R.string._7_days_forecast) + " in " + cityName);

            JSONObject current = json.getJSONObject("current");
            JSONArray daily = json.getJSONArray("daily");
            DateFormat df = DateFormat.getDateInstance();
            Date today = new Date(current.getLong("dt") * 1000);
            dateTv.setText("Date: " + df.format(today));

            double temp = current.getDouble("temp");
            tempTv.setText("Temperature: " + String.valueOf(temp) + "°C");

            String description = current.getJSONArray("weather").getJSONObject(0).getString("description");
            descriptionTv.setText(description);

            int humidity = Integer.parseInt(current.getString( "humidity") );
            humidityTv.setText("Humidity: " + String.valueOf(humidity) + "%");

            double wind = Double.parseDouble(current.getString("wind_speed"));
            windTv.setText("Wind speed: "+String.valueOf(wind) + "km/h");
            icon.setText(iconStr);


            for (int i=1;i <= 7; i++)
            {
                int id = getResources().getIdentifier("day"+String.valueOf(i), "id", getContext().getPackageName());
                TextView dayView = rootView.findViewById(id);
                String finalString = "";
                JSONObject thisDay = daily.getJSONObject(i);
                String dayString = Utils.getDayStringOld(new Date(thisDay.getLong("dt") * 1000));
                String dayDescription = thisDay.getJSONArray("weather").getJSONObject(0).getString("description");
                String dayMax = "Max: "  + String.valueOf(thisDay.getJSONObject("temp").getDouble("max") ) + " °C";
                String dayMin = "Min: "  + String.valueOf(thisDay.getJSONObject("temp").getDouble("min"))  + " °C";
                finalString += dayString + " - " +dayDescription+ ". " + dayMax +"  "+ dayMin;
                dayView.setText(finalString);

            }




            


        } catch (Exception e) {
            Log.e("WeekFragment", e.getMessage());
        }


    }
}