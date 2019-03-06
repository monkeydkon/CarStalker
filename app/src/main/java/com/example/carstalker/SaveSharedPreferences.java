package com.example.carstalker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {

    private SharedPreferences prefs;

    public SaveSharedPreferences(Context cntx) {

        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);

    }

    public void setusename(String usename) {

        prefs.edit().putString("username", usename).commit();

    }

    public String getusename() {
        String usename = prefs.getString("username","");
        return usename;
    }

    public void clearUserName()
    {
        prefs.edit().clear().commit();
    }
}
