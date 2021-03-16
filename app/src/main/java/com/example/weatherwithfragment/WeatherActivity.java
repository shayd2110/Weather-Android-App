package com.example.weatherwithfragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.newweatherapp.R;
import com.google.android.material.navigation.NavigationView;

public class WeatherActivity extends AppCompatActivity implements WeatherFragment.OnDataPass, LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 200 * 10 * 1; // 2 seconds
    private final boolean isGPSEnabled = false;
    // flag for network status
    private final boolean isNetworkEnabled = false;
    // flag for GPS status
    private final boolean canGetLocation = false;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected String provider;
    protected DbHandler handler;
    protected double latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    private DrawerLayout mDrawer;
    private WeatherFragment wf;
    private WeekFragment weekFragment;
    private String cityMain;
    private ForecastObject _forecast;
    private FragmentManager fragmentManager;
    private Location location;        // location
    private Toolbar toolbar;
    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;
    private static final int REQUEST_OK = 1010;
    private static final int RESULT_OK = 1;
    private static final int RESULT_NOT_EXIST = -1;
    private static final int RESULT_CANCELED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContent, new WeatherFragment())
                    .commit();
        }


        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        NavigationView nvDrawer = findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        handler = new DbHandler(this);


        /**previous
         if (savedInstanceState == null)
         {
         getSupportFragmentManager().beginTransaction()
         .add(R.id.container, new WeatherFragment())
         .commit();
         }
         */
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @Override
    public void onDataPass(ForecastObject forecast) {
        this._forecast = forecast;

    }

    public void onDataPass(String data) {
        Log.d("LOG", "hello " + data);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = WeatherFragment.class;
                fragment = fragment;

                break;
            case R.id.nav_second_fragment:
                fragmentClass = WeekFragment.class;
                break;

            default:
                fragmentClass = WeatherFragment.class;
                break;

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            if (fragmentClass == WeekFragment.class) {
                weekFragment = (WeekFragment) fragment;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        String icon = null;
        if (_forecast == null) {
            Toast.makeText(this, "Make sure that there is data in forecast", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "👍👍👍👍", Toast.LENGTH_SHORT).show();
            icon = _forecast.getIcon();
        }

        Bundle bundle = new Bundle();
        bundle.putString("icon", icon);
        weekFragment.setArguments(bundle);
        // Insert the fragment by replacing any existing fragment
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .commit();
        Fragment mainFragment = fragment;

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

    }

    private void changeCity(String city) {
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContent);
            if (fragment instanceof WeatherFragment) {
                WeatherFragment wf = (WeatherFragment) fragment;
                wf.changeCity(city);
            } else {
                WeekFragment weekFragment = (WeekFragment) fragment;
                weekFragment.changeCity(city);
            }


            new CityPreference(this).setCity(city);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
//            case android.R.id.home:
//                mDrawer.openDrawer(GravityCompat.START);
//                return true;
            case R.id.change_city:
                showInputDialog4ChangeCity();
                break;
            case R.id.insert_data:
                if (_forecast == null) {
                    Toast.makeText(this, "Make sure that there is data in forecast", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "👍👍👍👍", Toast.LENGTH_SHORT).show();
                }

                handler.addForecast(_forecast);
                break;
            case R.id.cur_loc_id:
                getCurrentLocation();
                String city = Utils.getCityName(this, latitude, longitude);
                changeCity(city);
                break;

            case R.id.get_all_forecast_by_city:
                showInputDialog4GetAllCity();
                break;

            case R.id.delete_city:
            {
                showInputDialog4DeleteCity();
            }

        }


        /**prev
         if (item.getItemId() == R.id.change_city)
         showInputDialog4ChangeCity();
         if (item.getItemId() == R.id.insert_data) {

         handler.addForecast(_forecast);
         }

         if (item.getItemId() == R.id.cur_loc_id) {
         getCurrentLocation();
         String city = Utils.getCityName(this, latitude, longitude);
         changeCity(city);

         }
         */
        return super.onOptionsItemSelected(item);

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(WeatherActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void showInputDialog4ChangeCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change City");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cityMain = (input.getText().toString().trim());
                try {
                    String cityToSend = Utils.getInternationalNameFromCity(WeatherActivity.this,cityMain);
                    changeCity(cityToSend);

                }
                catch(IncorrectCityNameException cityNameException){
                    String exceptionString = cityNameException.getMessage();
                    Utils.showToast(exceptionString,WeatherActivity.this);
                    return;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        builder.show();
    }

    private void showInputDialog4GetAllCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose City to select date from list");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cityName = (input.getText().toString().trim());
                try {
                    String cityToSend = Utils.getInternationalNameFromCity(WeatherActivity.this,cityName);
                    startChooseFromCityNameActivity(cityToSend);
                }
                catch(IncorrectCityNameException cityNameException){
                    String exceptionString = cityNameException.getMessage();
                    Utils.showToast(exceptionString,WeatherActivity.this);
                    return;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });
        builder.show();
    }

    private void showInputDialog4DeleteCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setIcon(R.drawable.)
        builder.setTitle("Choose City to Delete");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cityName = (input.getText().toString().trim());
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                builder.setTitle("Delete Alert")
                        .setMessage("You are going to DELETE  " + cityName + " FROM DB!\nAre you SURE you want to do that?! ")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String cityToSend = null;
                                try {
                                    cityToSend = Utils.getInternationalNameFromCity(WeatherActivity.this,cityName);
                                    handler.deleteCity(cityToSend,WeatherActivity.this);

                                }
                                catch(IncorrectCityNameException cityNameException){
                                    String exceptionString = cityNameException.getMessage();
                                    Utils.showToast(exceptionString,WeatherActivity.this);
                                    return;
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        })
                        .setNegativeButton("No! Take me Back!",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();



//                startChooseFromCityNameActivity(cityName);



            }
        });
        builder.show();
    }

    public void getResponseFromDelete(boolean isFound, String cityName){
        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
        if(isFound)
        {
            builder.setTitle("City Not Found ")
                    .setMessage( cityName +" Not Found In Our DataBase\n");
            builder.setPositiveButton("OK :(", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
        }
        else{
            builder.setTitle("City Not Found ")
                    .setMessage( cityName +" Deleted\n");
            builder.setPositiveButton("OK :)", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });

        }
        builder.show();

    }
    private void startChooseFromCityNameActivity(String cityName) {
        Intent i = new Intent(this,ChooseFromCityName.class);
        i.putExtra("city_name",cityName);
        startActivityForResult(i,REQUEST_OK);

    }

    /** alertDatePicker
     * Show AlertDialog with date picker.

    public void alertDatePicker() {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.city_and_date_picker, null, false);

        // the time picker on the alert dialog, this is how to get the value
        final DatePicker myDatePicker = view.findViewById(R.id.myDatePicker);
        EditText etCityName = view.findViewById(R.id.etCityName);
        etCityName.setInputType(InputType.TYPE_CLASS_TEXT);

        // so that the calendar view won't appear
        myDatePicker.setCalendarViewShown(false);


        // the alert dialog
        new AlertDialog.Builder(WeatherActivity.this).setView(view)
                .setTitle("Set Date")
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        int month = myDatePicker.getMonth() + 1;
                        int day = myDatePicker.getDayOfMonth();
                        int year = myDatePicker.getYear();

                        cityMain = etCityName.getText().toString().trim();

                        showToast(month + "/" + day + "/" + year + " at " + cityMain);

                        dialog.cancel();

                    }

                }).show();
    }
*/



/**showInputDialog4GetCity
    private void showInputDialog4GetCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Get Forecast by city name (from db");
        builder.setMessage("Notice! you will get the latest forecast\n " +
                "if you want another date pick this option from the menu\n" +
                "('Get By Ciy and Date')");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = (input.getText().toString().trim());
                handler.getForecastByCity(city);
            }
        });
        builder.show();
    }
*/


    public void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkLocationPermission();
        provider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Location loc = locationManager.getLastKnownLocation(provider);
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getAltitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d("Latitude", "enable");

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d("Latitude", "disable");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (resultCode){
            case RESULT_OK:
                    ForecastObjectRet returnedObj = (ForecastObjectRet) data.getSerializableExtra("forecastObj");
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContent);
                    WeatherFragment wf = (WeatherFragment) fragment;
                    wf.renderFromRes(returnedObj);
                break;
            case RESULT_NOT_EXIST:
                Toast.makeText(WeatherActivity.this, "Sorry", Toast.LENGTH_SHORT).show();;
                break;

            case RESULT_CANCELED:
                return;


        }

    }

}