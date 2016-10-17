package com.sethu.andpopularmoviesstage1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

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
    public static void saveInSharedPref(HashMap<String,BeanMovies> favoritesList,Context mcontext) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoritesList);
        prefsEditor.putString("favorites", json);
        prefsEditor.commit();
    }

    public static HashMap<String,BeanMovies> getFavoritesList(Context mContext){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("favorites", "");
        Log.d("","Favorites json"+json);
//        ArrayList<BeanMovies> movieList = gson.fromJson(json,new ArrayList<BeanMovies>());
        Type type = new TypeToken<HashMap<String,BeanMovies>>(){}.getType();
        HashMap<String,BeanMovies> favoriteList= gson.fromJson(json, type);
        if(favoriteList==null){
            favoriteList= new HashMap<>();
        }
        return favoriteList;
    }
}
