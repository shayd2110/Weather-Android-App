package com.example.weatherwithfragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Utils {
    public static String getInternationalNameFromCity(Context context, String city) throws Exception{
        Locale aLocale = new Locale.Builder()
                .setLanguage("en")
                .setRegion("IL")
                .build();
        Geocoder coder = new Geocoder(context,aLocale);
        List<Address> address;
        try {
            address = coder.getFromLocationName(city, 1);
            if (address == null) {
                return null;
            }
            if (address.size() == 0 )
            {
                throw new IncorrectCityNameException(city + " Not Found");
            }

            Address location = address.get(0);

           String adressStr =  address.get(0).getAddressLine(0);
            String adressFeature =  location.getFeatureName();
           if(location.getFeatureName() != null){
               adressFeature =  location.getFeatureName();
           }
           else{
               String[] strPlit =adressStr.split(",");
               adressFeature =  strPlit[0];
           }

            return adressFeature;
        } catch (Exception e) {
            throw  e;
        }
    }
    public static  String getLocationFromAddress(Context context, String strAddress) throws Exception {
        Locale aLocale = new Locale.Builder()
                .setLanguage("en")
                .setRegion("IL")
                .build();
        Geocoder coder = new Geocoder(context,aLocale);

        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);

            double lat = location.getLatitude();
            double lng = location.getLongitude();

            return lat + "," + lng;
        } catch (Exception e) {
            throw  e;
        }

    }

    public static String getCountryName(Context context, double latitude, double longitude) {
        Locale aLocale = new Locale.Builder()
                .setLanguage("en")
                .setRegion("IL")
                .build();
        Geocoder geocoder = new Geocoder(context,aLocale);
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
            return addresses.get(0).getCountryName();

        } catch (IOException ignored) {
            //do something
        }return null;

    }

    public static String getCityName(Context context, double latitude, double longitude) {
        Locale aLocale = new Locale.Builder()
                .setLanguage("en")
                .setRegion("IL")
                .build();
        Geocoder geocoder = new Geocoder(context,aLocale);
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getLocality();
            }
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL


        } catch (IOException ignored) {
            //do something
        }return null;

    }

    public static String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
    public static String getDayStringOld(Date date) {
        DateFormat formatter = new SimpleDateFormat("EEEE");
        return formatter.format(date);
    }

    public static void showToast(String s, Context context) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }
}
