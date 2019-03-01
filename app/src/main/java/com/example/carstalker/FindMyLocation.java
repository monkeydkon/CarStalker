package com.example.carstalker;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

public class FindMyLocation implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        float speed = location.getSpeed();
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LocationGetter locationGetter = new LocationGetter();

        locationGetter.setSpeed(speed);
        locationGetter.setLatitude(latitude);
        locationGetter.setLongitude(longitude);

//        LocationGetter locationGetter = new LocationGetter();
//        locationGetter.setSpeed(speed);
//        locationGetter.setLongitude(longitude);
//        locationGetter.setLatitude(latitude);

//        Log.d("hi","latitude: " + String.valueOf(latitude) + " longitude : " + String.valueOf(longitude));
     //   Toast.makeText(context,"latitude: " + String.valueOf(latitude) + " longitude : " + String.valueOf(longitude),Toast.LENGTH_SHORT).show();

    }

}
