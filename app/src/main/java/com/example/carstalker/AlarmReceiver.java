package com.example.carstalker;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AlarmReceiver extends BroadcastReceiver {

    private final int intent_request_code = 0;


    protected Context context;

    float speed;
    double longitude;
    double latitude = 1d;

    private GeofencingClient geofencingClient;




    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences(context);
        String username = saveSharedPreferences.getusename();

       // findTheLocation();

        setRepeatingAlarm();

    //    geofencingClient = LocationServices.getGeofencingClient(context);




        //Toast.makeText(context,"latitude: " + String.valueOf(latitude) + " longitude : " + String.valueOf(longitude),Toast.LENGTH_SHORT).show();

    }


    private void setRepeatingAlarm(){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intentForAlarm = new Intent(context, AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context,intent_request_code,intentForAlarm,0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, 3*1000,pendingIntent);
    }

//    private void findTheLocation() {
//
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        android.location.LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                Toast.makeText(context, String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location loc = locationManager
//                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//
//    }


}
