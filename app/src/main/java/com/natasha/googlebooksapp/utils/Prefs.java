package com.natasha.googlebooksapp.utils;

import android.app.Activity;
import android.content.SharedPreferences;

//This class will save a searched book so whenever a user open the app again
//the last book that was searched will show
public class Prefs {
    SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {
        sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void setSearch(String search){
        sharedPreferences.edit().putString("search", search).commit();
    }

    //this will be the default, if the user doesn't want to search for anything
    public String getSearch(){
        return sharedPreferences.getString("search", "programming");
    }
}
