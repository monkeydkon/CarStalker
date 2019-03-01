package com.example.carstalker;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class LoggedInActivity extends AppCompatActivity  {

    private final int intent_request_code = 0;

    String username;

    float current_speed;
    double longitude, latitude;

    SaveSharedPreferences saveSharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

       // final SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences(getApplicationContext());

//        Intent intent = getIntent();
//        username = intent.getStringExtra("username");

        saveSharedPreferences = new SaveSharedPreferences(getApplicationContext());
        String logedUser = saveSharedPreferences.getusename();
      //  username = saveSharedPreferences.getusename();



        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                current_speed = Math.round(location.getSpeed());
//                longitude = String.valueOf(location.getLongitude());
//                latitude = String.valueOf(location.getLatitude());
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
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
//            return;
//        }


        // set exact alarm(fires once) -> at AlarmReceiver I manually make it repeating
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intentForAlarm = new Intent(getApplicationContext(), AlarmReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),intent_request_code,intentForAlarm,0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime(),pendingIntent);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.cancel(pendingIntent);
                saveSharedPreferences.setusename("");
                Intent exitIntent = new Intent(LoggedInActivity.this, MainActivity.class);
                startActivity(exitIntent);
                finish();
            }
        });



    }


}
