package com.example.dylhunn.netflixchillandroid;

import android.location.Location;

/**
 * Created by dylhunn on 10/10/15.
 */
public class ChillRequest {
    public final String GENRE;
    public final MediaType TYPE;
    // could be null if we don't know the location
    public final Location LOCATION;


    public enum MediaType {FILM, TV_SHOW};

    public ChillRequest(String genre, MediaType type) {

        // Automatically fetch the last known location
        // Let's make the terrible assumption that we are being called from a ChillActivity :)
        LOCATION = ChillActivity.lastLocation;
        TYPE = type;
        GENRE = genre;
    }
}
