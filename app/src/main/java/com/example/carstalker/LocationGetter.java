package com.example.carstalker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LocationGetter {//implements LocationListener {

    float speed;
    double longitude, latitude;


    public void setSpeed(float speed){
        this.speed = speed;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public  void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public float getSpeed(){
        return  this.speed;
    }

    public double getLongitude(){
        return this.latitude;
    }

    public double getLatitude(){
        return this.latitude;
    }




//    @Override
//    public void onLocationChanged(Location location) {
//        if(location.getLatitude()>1){
////            SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences();
////            String username = saveSharedPreferences.getusename();
//           // Log.d("creation",String.valueOf(location.getLatitude()));
//        }
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
}
