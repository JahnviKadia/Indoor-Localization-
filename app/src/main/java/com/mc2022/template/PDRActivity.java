package com.mc2022.template;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PDRActivity extends AppCompatActivity {

    private LocationManager locationManager;
    LocationListener locListener;
    double lat, lon;
    TextView txt4;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdractivity);

        txt4 = findViewById(R.id.tv_angle);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locListener = new ResultActivity.MyLocationListener() {
            @Override
            public void onLocationChanged (@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.i("Location values",lat + " " + lon);
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
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locListener);

        double dis = calculateDistance( MainActivity.stepCount, MainActivity.res);
        // double bearing = calculateBearing(lat, lon);
        computeStep(lat,lon,dis, MainActivity.azimuth);
    }

    private void computeStep (double lat, double lon, double dis, double bear) {
        /*
        β = atan2(X,Y),
        For  variable Y = sin(toRadians(lo2-lo1)) *  cos(toRadians(la2))
        and variable X = cos(toRadians(la1))*sin(toRadians(la2)) – sin(toRadians(la1))*cos(toRadians(la2))*cos(toRadians(lo2-lo1))
        */

        double bearRad = Math.toRadians(bear);
        double latRad = Math.toRadians(lat);
        double lonRad = Math.toRadians(lon);
        int earthRadius = 6378;
        double calDist = dis / earthRadius;
        double latRes = Math.asin(Math.sin(latRad) * Math.cos(calDist) + Math.cos(latRad) * Math.sin(calDist) * Math.cos(bearRad));
        double a = Math.atan2(Math.sin(bearRad) * Math.sin(calDist) * Math.cos(latRad), Math.cos(calDist) - Math.sin(latRad) * Math.sin(latRes));
        double lonRes = (lonRad + a + 3 * Math.PI) % (2 *Math.PI ) - Math.PI;
        lat= Math.toDegrees(latRes) ;
        lon= Math.toDegrees(lonRes);
        Log.i("Latitude "+lat,"Longtitude"+lon);
        txt4.setText(lat + "\n\t" + lon);
    }

    private double calculateDistance (int stepCount, float res) {
        //distance / stride length = steps (in miles)
        //https://www.omnicalculator.com/sports/steps-to-miles
        double distance = (stepCount * res) * 1609.34; // in meters
        return distance;
    }


}