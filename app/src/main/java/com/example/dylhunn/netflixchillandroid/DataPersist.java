package com.example.dylhunn.netflixchillandroid;


import android.content.SharedPreferences;
import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by dylhunn on 10/10/15.
 */
public class DataPersist {

    public final String PREFS_LOGIN_USERID_KEY = "__USERNAME__" ;
    private Context c;

    public DataPersist(Context c) {
        this.c = c;
    }

    // Loads the UserID from shared prefs, if we know it. Else, returns null.
    public Integer getUserId() {
        int uid = Integer.parseInt(getFromPrefs(c, PREFS_LOGIN_USERID_KEY, "-1"));
        if (uid == -1) return null;
        return uid;
    }

    public void setUserId(int uid) {
        saveToPrefs(c, PREFS_LOGIN_USERID_KEY, "" + uid);
    }

    public void clearStoredUserId() {
        deletePref(c, PREFS_LOGIN_USERID_KEY);
    }

    // STORED PREFERENCE HANDLING

    /**
     * Called to save supplied value in shared preferences against given key.
     * @param context Context of caller activity
     * @param key Key of value to save against
     * @param value Value to save
     */
    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     * @param context Context of caller activity
     * @param key Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static void deletePref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            final SharedPreferences.Editor editor = prefs.edit();
            editor.remove(key);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
