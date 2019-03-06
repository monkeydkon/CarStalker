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

    SaveSharedPreferences saveSharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        saveSharedPreferences = new SaveSharedPreferences(getApplicationContext());
      //  String loggedUser = saveSharedPreferences.getusename();

        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        final Intent i=new Intent(this, AlarmReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, intent_request_code, i, 0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
       // alarmManager.cancel(pendingIntent);


        // log out
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop alarm
                alarmManager.cancel(pendingIntent);

                //end session for logged user
                saveSharedPreferences.setusename("");

                //go to main activity
                Intent exitIntent = new Intent(LoggedInActivity.this, MainActivity.class);
                startActivity(exitIntent);

                // stop this activity
                finish();
            }
        });

        Button showMaps = findViewById(R.id.showMaps);
        showMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // stop alarm
                alarmManager.cancel(pendingIntent);

                //go to Maps activity
                Intent intent = new Intent(LoggedInActivity.this, MapsActivity.class);
                startActivity(intent);
               // finish();
            }
        });
    }

}
