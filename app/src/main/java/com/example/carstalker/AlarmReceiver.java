package com.example.carstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences(context);
        String username = saveSharedPreferences.getusename();
//        int current_speed = Integer.parseInt(intent.getStringExtra("current_speed"));
//        String longitude = intent.getStringExtra("longitude");
//        String latitude = intent.getStringExtra("latitude");

        Toast.makeText(context,username,Toast.LENGTH_SHORT).show();
    }


}
