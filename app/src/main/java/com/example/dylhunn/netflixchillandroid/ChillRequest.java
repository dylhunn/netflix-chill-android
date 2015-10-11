package com.example.dylhunn.netflixchillandroid;

import android.location.Location;

/**
 * Created by dylhunn on 10/10/15.
 */
public class ChillRequest {
    public final String GENRE;
    public final MediaType TYPE;
    public final String DAY;
    public final String TIME;
    // could be null if we don't know the location
    public final Location LOCATION;


    public enum MediaType {FILM, TV_SHOW};

    public ChillRequest(String genre, MediaType type, String day, String time) {

        // Automatically fetch the last known location
        // Let's make the terrible assumption that we are being called from a ChillActivity :)
        LOCATION = ChillActivity.lastLocation;
        TYPE = type;
        GENRE = genre;
        DAY = day;
        TIME = time;
    }

    public ChillRequest(String genre, MediaType type, String day, String time, Location loc) {

        LOCATION = loc;
        TYPE = type;
        GENRE = genre;
        DAY = day;
        TIME = time;
    }
}
