package com.example.dylhunn.netflixchillandroid;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by richard on 10/10/15.
 */
public class ApiService {

    /**
     * Query the server to check if this is still a valid uid.
     * @param uid
     * @return
     */
    public static void confirmUidAndLogin(final int uid, final LoginActivity act) {
        // Testing only
        act.uid_is_valid_and_login(uid);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(act.getApplicationContext());
        String url = "http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) act.uid_is_valid_and_login(uid);
                        else if (response.contains("false")) act.uid_is_invalid();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 act.uid_check_response_error();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Query the server, asking for the user ID associated with this account.
     * Return null if the server says these are bad Netflix credentials.
     * Return -1 if we couldn't connect to the server at all.
     * @param mEmail
     * @param mPassword
     * @return
     */
    public static void registerOrLookup(String mEmail, String mPassword, final LoginActivity act) {
        // Testing only
        act.register_success("1");


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(act.getApplicationContext());
        String url = "http://netflix-chill-server.herokuapp.com/sign-in";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("NetflixAndChill", "Server response: " + response);
                        if (response.contains("-1")) act.register_bad_credentials();
                        else act.register_success(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                act.register_fail();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Send the server a chill request. The Chill request ID should be sent back.
     * Return -1 if something failed.
     * @param uid
     * @param request
     * @return
     */
    public static void makeChillRequest(int uid, final ChillRequest request, final ChillActivity act) {
        act.chillRequestSucceeded(1); // Testing
        /*
        String url = "http://netflix-chill-server.herokuapp.com/sign-in";

        RequestQueue queue = Volley.newRequestQueue(act.getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int responseNum = -1;
                try {
                    responseNum = Integer.parseInt(response);
                } catch (Exception e) {
                    // malformed data returned
                }
                if (responseNum < 0) act.chillRequestFailed();
                act.chillRequestSucceeded(responseNum);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                act.chillRequestFailed();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("genre", request.GENRE);
                params.put("type", request.TYPE.toString());
                params.put("day", request.DAY);
                params.put("time", request.TIME);
                params.put("latitude", "" + request.LOCATION.getLatitude());
                params.put("longitude", "" + request.LOCATION.getLongitude());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
*/
    }

    /**
     * Try to delete a chill request by ID. If something failed, return false.
     * @return
     */
    public static void deleteChillRequest(final int chill_id, final ChillActivity act) {
        act.chillRequestSucceeded(1); // Testing

        String url = "http://netflix-chill-server.herokuapp.com/sign-in";

        RequestQueue queue = Volley.newRequestQueue(act.getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) act.chillDeleteSuccess();
                else act.chillDeleteFailure();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                act.chillDeleteFailure();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("chill_id", "" + chill_id);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    public static void fetchMatches(final int uid, final ItemFragment fr) {
        // mock
        List<ChillRequestResponseList<Person>> entries = new ArrayList<>();
        ChillRequest cr = new ChillRequest("Horror", ChillRequest.MediaType.FILM, "Monday", "Evening", null);
        ChillRequestResponseList<Person> one = new ChillRequestResponseList<>(32, cr);
        one.add(new Person("Suzie", 1));
        one.add(new Person("Ben", 2));
        one.add(new Person("Roger", 41));
        one.add(new Person("Jamal", 6));
        ChillRequest cr3 = new ChillRequest("Drama", ChillRequest.MediaType.TV_SHOW, "Friday", "Morning", null);
        ChillRequestResponseList<Person> three = new ChillRequestResponseList<>(12, cr3);
        ChillRequest cr2 = new ChillRequest("Drama", ChillRequest.MediaType.TV_SHOW, "Tuesday", "Morning", null);
        ChillRequestResponseList<Person> two = new ChillRequestResponseList<>(72, cr2);
        two.add(new Person("Suzie", 1));
        two.add(new Person("Ben", 2));
        entries.add(one);
        entries.add(three);
        entries.add(two);

        fr.populate(entries);
    }
}