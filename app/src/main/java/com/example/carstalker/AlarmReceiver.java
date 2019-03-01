package com.example.carstalker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

public class AlarmReceiver extends BroadcastReceiver{

    private final int intent_request_code = 0;


    Context context;

    float speed;
    double longitude;
    double latitude = 1d;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences(context);
        String username = saveSharedPreferences.getusename();

        Intent serviceIntent = new Intent(context,FindMyLocation.class);
        context.startService(serviceIntent); //start service for get location

        // manual repeating alarm
       setRepeatingAlarm();

        LocationGetter locationGetter = new LocationGetter();

        speed = locationGetter.getSpeed();
        longitude = locationGetter.getLongitude();
        latitude = locationGetter.getLatitude();

        Log.d("hi","user: "+username+" latitude: " + String.valueOf(latitude) + " longitude : " + String.valueOf(longitude));
        //Toast.makeText(context,"latitude: " + String.valueOf(latitude) + " longitude : " + String.valueOf(longitude),Toast.LENGTH_SHORT).show();

    }


    private void setRepeatingAlarm(){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intentForAlarm = new Intent(context, AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context,intent_request_code,intentForAlarm,0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, 3*1000,pendingIntent);
    }
}
