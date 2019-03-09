package com.example.carstalker;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
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

    public void setLocation(Location location){
        if (location == null) {
            prefs.edit().remove("LOCATION_LAT").apply();
            prefs.edit().remove("LOCATION_LON").apply();
            prefs.edit().remove("LOCATION_PROVIDER").apply();
        } else {
            prefs.edit().putString("LOCATION_LAT", String.valueOf(location.getLatitude())).apply();
            prefs.edit().putString("LOCATION_LON", String.valueOf(location.getLongitude())).apply();
            prefs.edit().putString("LOCATION_PROVIDER", location.getProvider()).apply();
        }
    }

    public Location getLocation(){
        String lat = prefs.getString("LOCATION_LAT", null);
        String lon = prefs.getString("LOCATION_LON", null);
        Location location = null;
        if (lat != null && lon != null) {
            String provider = prefs.getString("LOCATION_PROVIDER", null);
            location = new Location(provider);
            location.setLatitude(Double.parseDouble(lat));
            location.setLongitude(Double.parseDouble(lon));
        }
        return location;
    }
}
