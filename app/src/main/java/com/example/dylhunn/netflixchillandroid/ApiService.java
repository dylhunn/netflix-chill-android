package com.example.dylhunn.netflixchillandroid;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.Request;
import com.android.volley.Response;

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
        //act.uid_is_valid_and_login(uid);


        String url = "http://netflix-chill-server.herokuapp.com/verify-user-exists";

        RequestQueue queue = Volley.newRequestQueue(act.getApplicationContext());

        Map<String,String> params = new HashMap<String, String>();
        params.put("uid", uid + "");

        JSONObject jso = new JSONObject(params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jso, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("NetflixAndChill", "Server verification returned: " + response.toString());
                try {
                    if (response.get("user_exists").toString().contains("true")) {
                        act.uid_is_valid_and_login(uid);
                    }
                    else if (response.get("user_exists").toString().contains("false")) {
                        act.uid_is_invalid();
                    }
                } catch (Exception e) {
                    act.uid_check_response_error();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                act.uid_check_response_error();
            }
        });

        queue.add(jsObjRequest);
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
        //act.register_success("1");

        String url = "http://netflix-chill-server.herokuapp.com/sign-in";

        RequestQueue queue = Volley.newRequestQueue(act.getApplicationContext());

        Map<String,String> params = new HashMap<String, String>();
        params.put("nf_un", mEmail);
        params.put("nf_pw", mPassword);

        JSONObject jso = new JSONObject(params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jso, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("NetflixAndChill", "Server response to account registration: " + response.toString());
                try {
                    if (("" + response.getInt("user_id")).contains("-1")) { // :(
                        act.register_bad_credentials();
                    }
                    else {
                        act.register_success("" + response.getInt("user_id"));
                    }
                } catch (Exception e) {
                    act.register_fail();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                act.register_fail();

            }
        });

        queue.add(jsObjRequest);

    }

    /**
     * Send the server a chill request. The Chill request ID should be sent back.
     * Return -1 if something failed.
     * @param uid
     * @param request
     * @return
     */
    public static void makeChillRequest(final int uid, final ChillRequest request, final ChillActivity act) {
        //act.chillRequestSucceeded(1); // Testing

        String url = "http://netflix-chill-server.herokuapp.com/create-chill-request";

        RequestQueue queue = Volley.newRequestQueue(act.getApplicationContext());

        Map<String,String> params = new HashMap<String, String>();
        params.put("uid", "" + uid);
        params.put("genre", request.GENRE);
        params.put("type", request.TYPE.toString());
        params.put("day", request.DAY);
        params.put("time", request.TIME);
        params.put("latitude", "" + request.LOCATION.getLatitude());
        params.put("longitude", "" + request.LOCATION.getLongitude());

        JSONObject jso = new JSONObject(params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jso, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                int responseNum = -1;
                try {
                    responseNum = response.getInt("chill_request_id");
                } catch (Exception e) {
                    act.chillRequestFailed();
                }
                if (responseNum < 0) act.chillRequestFailed();
                act.chillRequestSucceeded(responseNum);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                act.chillRequestFailed();

            }
        });

        queue.add(jsObjRequest);
    }

    /**
     * Try to delete a chill request by ID. If something failed, return false.
     * @return
     */
    public static void deleteChillRequest(final int chill_id, final ChillActivity act) {
        act.chillRequestSucceeded(1); // Testing

        String url = "http://netflix-chill-server.herokuapp.com/delete-chill-request";

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
        ChillRequest cr = new ChillRequest("Horror", ChillRequest.MediaType.FILM, "Monday", "Evening", null, 2);
        ChillRequestResponseList<Person> one = new ChillRequestResponseList<>(32, cr);
        one.add(new Person("Suzie", 1, 1));
        one.add(new Person("Ben", 2, .9));
        one.add(new Person("Roger", 41, .1));
        one.add(new Person("Jamal", 6, .8));
        ChillRequest cr3 = new ChillRequest("Drama", ChillRequest.MediaType.TV_SHOW, "Friday", "Morning", null, 1);
        ChillRequestResponseList<Person> three = new ChillRequestResponseList<>(12, cr3);
        ChillRequest cr2 = new ChillRequest("Drama", ChillRequest.MediaType.TV_SHOW, "Tuesday", "Morning", null, 3);
        ChillRequestResponseList<Person> two = new ChillRequestResponseList<>(72, cr2);
        two.add(new Person("Suzie", 1, .7));
        two.add(new Person("Ben", 2, .9));
        entries.add(one);
        entries.add(three);
        entries.add(two);

        fr.populate(entries);
        return;
/*
        String url = "http://netflix-chill-server.herokuapp.com/get-chill-matches";
        RequestQueue queue = Volley.newRequestQueue(fr.getActivity().getApplicationContext());
        Map<String,String> params = new HashMap<String, String>();
        params.put("uid", "" + uid);
        JSONObject jso = new JSONObject(params);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jso, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // re-populate queue
                    fr.populate(parseMatchEntriesFromJson(response));
                } catch (Exception e) {
                    fr.populate(new ArrayList<ChillRequestResponseList<Person>>());
                    Log.e("NetflixAndChill", "Exception thrown while parsing matches.");
                }

                List<ChillRequestResponseList<Person>> entries = new ArrayList<>();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // populate the queue anyway, it will display the blank message
                fr.populate(new ArrayList<ChillRequestResponseList<Person>>());
                Log.e("NetflixAndChill", "Error retrieving matches.");
            }
        });

        queue.add(jsObjRequest);*/
    }

    private static List<ChillRequestResponseList<Person>> parseMatchEntriesFromJson(JSONObject response) {
        List<ChillRequestResponseList<Person>> entries = new ArrayList<>();
        try {
            Iterator<String> it = response.keys();
            while (it.hasNext()) {
                int chillRequestId;

                chillRequestId = Integer.parseInt(it.next());

                JSONObject chillRequestDataJson = response.getJSONObject("" + chillRequestId);
                String genre = chillRequestDataJson.getString("genre");
                String day = chillRequestDataJson.getString("day");
                String time = chillRequestDataJson.getString("time");
                String type = chillRequestDataJson.getString("type");
                int priority = chillRequestDataJson.getInt("priority");

                ChillRequest.MediaType mediaType;
                if (type.contains("tv") || type.contains("TV")) mediaType = ChillRequest.MediaType.TV_SHOW;
                else mediaType = ChillRequest.MediaType.FILM;

                ChillRequest requestData = new ChillRequest(genre, mediaType, day, time, null, priority);

                ChillRequestResponseList<Person> responseList = new ChillRequestResponseList<>(chillRequestId, requestData);

                JSONObject matches = chillRequestDataJson.getJSONObject("matches");
                Iterator<String> it2 = matches.keys();

                while (it2.hasNext()) {
                    int match_uid = Integer.parseInt(it2.next());
                    JSONObject match_data_dict = matches.getJSONObject(match_uid + "");
                    String match_email = match_data_dict.getString("email");
                    double match_priority = match_data_dict.getDouble("priority");
                    Person p = new Person(match_email, match_uid, match_priority);
                    responseList.add(p);
                }
            }
        } catch (Exception e) {
            Log.e("NetflixAndChill", "Malformed chill request match JSON.");
            return entries;
        }

        return entries;
    }
}