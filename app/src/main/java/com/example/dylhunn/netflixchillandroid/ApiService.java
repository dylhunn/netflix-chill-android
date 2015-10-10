package com.example.dylhunn.netflixchillandroid;

import android.content.Context;

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
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(act.getApplicationContext());
        String url = "http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
    public static int makeChillRequest(int uid, ChillRequest request) {
        return -1;
    }

    /**
     * Try to delete a chill request by ID. If something failed, return false.
     * @return
     */
    public static boolean deleteChillRequest(int chill_id) {
        return true;
    }
}
