package com.example.carstalker;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.location.LocationManager;
import android.os.Bundle;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;


public class AlarmReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final int intent_request_code = 0;

    Context context;

    GoogleApiClient mGoogleApiClient;

    SaveSharedPreferences saveSharedPreferences;

    String loggedUser;

   // Stack<Location> locations = new Stack<>();

    private DatabaseReference mDatabase;

    Location previousLocation;



    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        // take user session
        saveSharedPreferences = new SaveSharedPreferences(this.context);
        loggedUser = saveSharedPreferences.getusename();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        setRepeatingAlarm();
    }


    // exact repeating alarm
    private void setRepeatingAlarm() {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, AlarmReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, intent_request_code, i, 0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1 * 6 * 1000, pendingIntent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        final Location mLastLocation;
        Double distance;



        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationRequest myLocationRequest = new LocationRequest();
        myLocationRequest.setInterval(10000);
        myLocationRequest.setFastestInterval(5000);
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }
        };

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,myLocationRequest, locationListener);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


//        int counter = saveSharedPreferences.getCounter();
//
//        if(counter == 0){
//            distance = 10000d;
//        }else{
//            distance = distance(mLastLocation.getLatitude(),mLastLocation.getLongitude(),previousLocation.getLatitude(),previousLocation.getLongitude(), 'K');
//        }

        previousLocation = saveSharedPreferences.getLocation();

        if(previousLocation == null){
            distance = 0.001d;
        }else{
            distance = distance(mLastLocation.getLatitude(),mLastLocation.getLongitude(),previousLocation.getLatitude(),previousLocation.getLongitude(), "K");
            Log.d("hi",previousLocation.toString());
            Log.d("hi",mLastLocation.toString());
            Log.d("hi",distance.toString());
            //Toast.makeText(context,String.valueOf(mLastLocation.getLongitude())+" "+String.valueOf(mLastLocation.getLatitude()),Toast.LENGTH_SHORT).show();
        }



//        Log.d("last location latitude", String.valueOf(mLastLocation.getLatitude()));
//
//        Log.d("prev location latitude", String.valueOf(previousLocation.getLatitude()));
//
//        Log.d("distamce",distance.toString());

        if(distance >= 0.001d) {


            if (mLastLocation != null) {
                final String latitude = (String.valueOf(mLastLocation.getLatitude()));
                final String longitude = (String.valueOf(mLastLocation.getLongitude()));

                final String altitude = (String.valueOf(mLastLocation.getAltitude()));

                final String timestamp = String.valueOf(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String dateString = simpleDateFormat.format(new Date(Long.parseLong(timestamp)));

                final float speed = (float) (distance / 60 * 1000);

                mDatabase.child("users").child(loggedUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mDatabase.child("users").child(loggedUser).child("events").child(dateString).child("latitude").setValue(latitude);
                        mDatabase.child("users").child(loggedUser).child("events").child(dateString).child("longitude").setValue(longitude);
                        mDatabase.child("users").child(loggedUser).child("events").child(dateString).child("altitude").setValue(altitude);
                        mDatabase.child("users").child(loggedUser).child("events").child(dateString).child("speed").setValue(speed);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            previousLocation = mLastLocation;
            saveSharedPreferences.setLocation(previousLocation);
        }



//        saveSharedPreferences.setCounter(1);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

//    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
//
//        double theta = lon1 - lon2;
//
//        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//
//        dist = Math.acos(dist);
//
//        dist = rad2deg(dist);
//
//        dist = dist * 60 * 1.1515;
//
//        if (unit == 'K') {
//
//            dist = dist * 1.609344;
//
//        } else if (unit == 'N') {
//
//            dist = dist * 0.8684;
//
//        }
//
//        return (dist);
//
//    }
//
//    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
//
//    /*::  This function converts decimal degrees to radians             :*/
//
//    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
//
//    private double deg2rad(double deg) {
//
//        return (deg * Math.PI / 180.0);
//
//    }
//
//    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
//
//    /*::  This function converts radians to decimal degrees             :*/
//
//    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
//
//    private double rad2deg(double rad) {
//
//        return (rad * 180.0 / Math.PI);
//
//    }
}
