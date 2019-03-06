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


    protected Context context;

    GoogleApiClient mGoogleApiClient;

    SaveSharedPreferences saveSharedPreferences;

    String loggedUser;

    Stack<Location> locations = new Stack<>();

    private DatabaseReference mDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {



        this.context = context;

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


    private void setRepeatingAlarm() {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, AlarmReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, intent_request_code, i, 0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 2000,

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
      //  locations.push()
        
        



       // mLastLocation = LocationServices.FusedLocationApi.removeLocationUpdates()
        if (mLastLocation != null) {
            final String latitude = (String.valueOf(mLastLocation.getLatitude()));
            final String longitude = (String.valueOf(mLastLocation.getLongitude()));

            final String altitude = (String.valueOf(mLastLocation.getAltitude()));

            final String timestamp = String.valueOf(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String dateString = simpleDateFormat.format(new Date(Long.parseLong(timestamp)));

            final float speed = mLastLocation.getSpeed();

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



            //Toast.makeText(this.context,"usename :" +loggedUser+ " latitude : " + latitude + " longitude : " + longitude + " speed: " + String.valueOf(speed),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
