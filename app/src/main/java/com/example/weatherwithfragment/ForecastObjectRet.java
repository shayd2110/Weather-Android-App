package com.example.weatherwithfragment;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

public class ForecastObjectRet implements Serializable {

    private int windDirection;
    private double windSpeed;
    private double minTemp;
    private double maxTemp;
    private double curTemp;
    private double humidity;
    private double pressure;
    private double nightTemp;
    private double dayTimeTemp;
    private double eveningTemp;
    private double morningTemp;
    private Boolean heatIndex;
    private String cityName;
    private String day;
    private String curForecast;
    private String dayTimeForecast;
    private String eveningForecast;
    private String morningForecast;
    private String nightForecast;
    private String icon;
    private String countryName;
    private transient GeoPoint latLon;
    private long date;


    public ForecastObjectRet(int windDirection, double windSpeed, double minTemp, double maxTemp, double curTemp, double humidity, double pressure, double nightTemp, double dayTimeTemp, double eveningTemp, double morningTemp, Boolean heatIndex, String cityName, String day, String curForecast, String dayTimeForecast, String eveningForecast, String morningForecast, String nightForecast, String icon, String countryName, GeoPoint latLon, long date) {
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.curTemp = curTemp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.nightTemp = nightTemp;
        this.dayTimeTemp = dayTimeTemp;
        this.eveningTemp = eveningTemp;
        this.morningTemp = morningTemp;
        this.heatIndex = heatIndex;
        this.cityName = cityName;
        this.day = day;
        this.curForecast = curForecast;
        this.dayTimeForecast = dayTimeForecast;
        this.eveningForecast = eveningForecast;
        this.morningForecast = morningForecast;
        this.nightForecast = nightForecast;
        this.icon = icon;
        this.countryName = countryName;
        this.latLon = latLon;
        this.date = date;
    }

    public ForecastObjectRet(ForecastObjectRet forecastObj) {
        this.windDirection = forecastObj.windDirection;
        this.windSpeed = forecastObj.windSpeed;
        this.minTemp = forecastObj.minTemp;
        this.maxTemp = forecastObj.maxTemp;
        this.curTemp = forecastObj.curTemp;
        this.humidity = forecastObj.humidity;
        this.pressure = forecastObj.pressure;
        this.nightTemp = forecastObj.nightTemp;
        this.dayTimeTemp = forecastObj.dayTimeTemp;
        this.eveningTemp = forecastObj.eveningTemp;
        this.morningTemp = forecastObj.morningTemp;
        this.heatIndex = forecastObj.heatIndex;
        this.cityName = forecastObj.cityName;
        this.day = forecastObj.day;
        this.curForecast = forecastObj.curForecast;
        this.dayTimeForecast = forecastObj.dayTimeForecast;
        this.eveningForecast = forecastObj.eveningForecast;
        this.morningForecast = forecastObj.morningForecast;
        this.nightForecast = forecastObj.nightForecast;
        this.icon = forecastObj.icon;
        this.countryName = forecastObj.countryName;
        this.latLon = forecastObj.latLon;
        this.date = forecastObj.date;

    }

    public ForecastObjectRet() {

    }

    public ForecastObjectRet(ForecastObject forecastObj) {
        this.windDirection = forecastObj.getWindDirection();
        this.windSpeed = forecastObj.getWindSpeed();
        this.minTemp = forecastObj.getMinTemp();
        this.maxTemp = forecastObj.getMaxTemp();
        this.curTemp = forecastObj.getCurTemp();
        this.humidity = forecastObj.getHumidity();
        this.pressure = forecastObj.getPressure();
        this.nightTemp = forecastObj.getNightTemp();
        this.dayTimeTemp = forecastObj.getDayTimeTemp();
        this.eveningTemp = forecastObj.getEveningTemp();
        this.morningTemp = forecastObj.getMorningTemp();
        this.heatIndex = forecastObj.getHeatIndex();
        this.cityName = forecastObj.getCityName();
        this.day = forecastObj.getDay();
        this.curForecast = forecastObj.getCurForecast();
        this.dayTimeForecast = forecastObj.getDayTimeForecast();
        this.eveningForecast = forecastObj.getEveningForecast();
        this.morningForecast = forecastObj.getMorningForecast();
        this.nightForecast = forecastObj.getNightForecast();
        this.icon = forecastObj.getIcon();
        this.countryName = forecastObj.getCountryName();
        this.latLon = forecastObj.getLatLon();
        this.date = forecastObj.getDate().getSeconds();
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(double curTemp) {
        this.curTemp = curTemp;
    }

    public Boolean getHeatIndex() {
        return heatIndex;
    }

    public void setHeatIndex(Boolean heatIndex) {
        this.heatIndex = heatIndex;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getDayTimeTemp() {
        return dayTimeTemp;
    }

    public void setDayTimeTemp(double dayTimeTemp) {
        this.dayTimeTemp = dayTimeTemp;
    }

    public double getEveningTemp() {
        return eveningTemp;
    }

    public void setEveningTemp(double eveningTemp) {
        this.eveningTemp = eveningTemp;
    }

    public double getMorningTemp() {
        return morningTemp;
    }

    public void setMorningTemp(double morningTemp) {
        this.morningTemp = morningTemp;
    }

    public String getCurForecast() {
        return curForecast;
    }

    public void setCurForecast(String curForecast) {
        this.curForecast = curForecast;
    }

    public String getDayTimeForecast() {
        return dayTimeForecast;
    }

    public void setDayTimeForecast(String dayTimeForecast) {
        this.dayTimeForecast = dayTimeForecast;
    }

    public String getEveningForecast() {
        return eveningForecast;
    }

    public void setEveningForecast(String eveningForecast) {
        this.eveningForecast = eveningForecast;
    }

    public String getMorningForecast() {
        return morningForecast;
    }

    public void setMorningForecast(String morningForecast) {
        this.morningForecast = morningForecast;
    }

    public String getNightForecast() {
        return nightForecast;
    }

    public void setNightForecast(String nightForecast) {
        this.nightForecast = nightForecast;
    }

    public GeoPoint getLatLon() {
        return latLon;
    }

    public void setLatLon(GeoPoint latLon) {
        this.latLon = latLon;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getNightTemp() {
        return nightTemp;
    }

    public void setNightTemp(double nightTemp) {
        this.nightTemp = nightTemp;
    }

    public void setIcon(String icon) {

        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }


    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return "ForecastObject{" +
                "windDirection=" + windDirection +
                "\n windSpeed=" + windSpeed +
                "\n minTemp=" + minTemp +
                "\n maxTemp=" + maxTemp +
                "\n curTemp=" + curTemp +
                "\n humidity=" + humidity +
                "\n pressure=" + pressure +
                "\n nightTemp=" + nightTemp +
                "\n dayTimeTemp=" + dayTimeTemp +
                "\n eveningTemp=" + eveningTemp +
                "\n morningTemp=" + morningTemp +
                "\n heatIndex=" + heatIndex +
                "\n cityName='" + cityName + '\'' +
                "\n day='" + day + '\'' +
                "\n curForecast='" + curForecast + '\'' +
                "\n dayTimeForecast='" + dayTimeForecast + '\'' +
                "\n eveningForecast='" + eveningForecast + '\'' +
                "\n morningForecast='" + morningForecast + '\'' +
                "\n nightForecast='" + nightForecast + '\'' +
                "\n icon='" + icon + '\'' +
                "\n countryName='" + countryName + '\'' +
                "\n latLon=" + latLon +
                "\n date=" + new Date(date).toString() +
                '}';
    }
}
