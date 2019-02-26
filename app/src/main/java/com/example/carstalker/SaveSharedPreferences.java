package com.example.carstalker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {

//    static final String PREF_USER_NAME = "username";
//
//    static SharedPreferences getSharedPreferances(Context context){
//        return PreferenceManager.getDefaultSharedPreferences(context);
//    }
//
//    public static void setUserName(Context context, String userName){
//        SharedPreferences.Editor editor = getSharedPreferances(context).edit();
//        editor.putString(PREF_USER_NAME, userName);
//        editor.commit();
//    }
//
//    public static String getUserName(Context context){
//        return getSharedPreferances(context).getString(PREF_USER_NAME, "");
//    }
//
//    public static void clearUserName(Context context)
//    {
//        SharedPreferences.Editor editor = getSharedPreferances(context).edit();
//        editor.clear(); //clear all stored data
//        editor.commit();
//    }

    private SharedPreferences prefs;

    public SaveSharedPreferences(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).commit();
    }

    public String getusename() {
        String usename = prefs.getString("usename","");
        return usename;
    }
}
