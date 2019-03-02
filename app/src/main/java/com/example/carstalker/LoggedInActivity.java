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

        saveSharedPreferences = new SaveSharedPreferences(getApplicationContext());
        String loggedUser = saveSharedPreferences.getusename();

        Toast.makeText(getApplicationContext(),"jo",Toast.LENGTH_SHORT).show();

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
