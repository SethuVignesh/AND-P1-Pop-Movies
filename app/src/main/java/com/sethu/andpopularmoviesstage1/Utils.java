package com.sethu.andpopularmoviesstage1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by sethugayu on 9/14/16.
 */
public class Utils {
    public static boolean isNetworkAvailable(Context cxt) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void saveInSharedPref(ArrayList<String> favoritesList,Context mcontext) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoritesList);
        prefsEditor.putString("favorites", json);
        prefsEditor.commit();
    }

    public static ArrayList<String> getFavoritesList(Context mContext){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("favorites", "");
//        ArrayList<BeanMovies> movieList = gson.fromJson(json,new ArrayList<BeanMovies>());
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> favoriteList= gson.fromJson(json, type);
        if(favoriteList==null){
            favoriteList= new ArrayList<String>();
        }
        return favoriteList;
    }
}
