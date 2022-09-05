package com.example.locationweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationListenerCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    final String API_ID = "398648dd804da0a84703fb8da9f1cffd";
    final String Weatherurl = "https://api.openweathermap.org/data/2.5/weather";

    final long Min_type = 5000;
    final float Min_distance = 1000;
    final int REQUEST_CODE= 101;


    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView Nameofcity, weatherstate, Temperature;
    ImageView mweathericon;

    RelativeLayout mcityfinder;


    LocationManager mlocationmanager;
    LocationListener mlocationlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherstate = findViewById(R.id.weather);
        Temperature = findViewById(R.id.temp);
        mweathericon = findViewById(R.id.weathericon);
        mcityfinder = findViewById(R.id.CityFinder);
        Nameofcity = findViewById(R.id.cityname);


        mcityfinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, cityfinder.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {

        mlocationmanager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mlocationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat" ,Latitude);
                params.put("long",Longitude);
                params.put("apid",API_ID);
                letsdoSomeNetworking(params);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location
            }

        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mlocationmanager.requestLocationUpdates(Location_Provider, Min_type, Min_distance, mlocationlistener);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"Location get Successfully",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                //user denied the permission
            }
        }

    }


    private void letsdoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Weatherurl,params,new JsonHttpResponseHandler()
        {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainActivity.this, "Data Accessed", Toast.LENGTH_SHORT).show();

                weatherData weatherD=weatherData.fromJson(response);
                updateUI(weatherD);


//                super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    private void updateUI(weatherData weather){

        Temperature.setText(weather.getmTemperature());
        Nameofcity.setText(weather.getmCity());
        weatherstate.setText(weather.getmWeathertype());
        int resourceID=getResources().getIdentifier(weather.getmIcon(),"drawable",getPackageName());
        mweathericon.setImageResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mlocationmanager!=null){
            mlocationmanager.removeUpdates(mlocationlistener);
        }
    }
}