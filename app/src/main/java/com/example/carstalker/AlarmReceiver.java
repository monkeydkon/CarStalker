package com.example.carstalker;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;

import java.util.Stack;


public class AlarmReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final int intent_request_code = 0;


    protected Context context;

    GoogleApiClient mGoogleApiClient;

    SaveSharedPreferences saveSharedPreferences;

    String loggedUser;

    Stack<Location> locations = new Stack<>();


    @Override
    public void onReceive(Context context, Intent intent) {



        this.context = context;

        saveSharedPreferences = new SaveSharedPreferences(this.context);
        loggedUser = saveSharedPreferences.getusename();

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


    private void setRepeatingAlarm() {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, AlarmReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, intent_request_code, i, 0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 20000,

                pendingIntent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation;
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        // TODO: 4/3/2019  
//        locations.push(mLastLocation);
//        
//        Location first = locations.pop();
//        
//        if(!locations.empty()){
//            Location second = locations.pop();
//            
//            String speed = String.valueOf(first)
//        }
        
        

        float speed = mLastLocation.getSpeed();
       // mLastLocation = LocationServices.FusedLocationApi.removeLocationUpdates()
        if (mLastLocation != null) {
            String latitude = (String.valueOf(mLastLocation.getLatitude()));
            String longitude = (String.valueOf(mLastLocation.getLongitude()));

            Toast.makeText(this.context,"usename :" +loggedUser+ " latitude : " + latitude + " longitude : " + longitude + " speed: " + String.valueOf(speed),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
