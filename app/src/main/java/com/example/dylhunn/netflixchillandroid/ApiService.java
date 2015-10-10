package com.example.dylhunn.netflixchillandroid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by richard on 10/10/15.
 */
public class ApiService {
    public static final int MODULO = 1_000_000_007;

    public static enum UID_STATUS {VALID, INVALID, CONNECTION_FAILURE};

    /**
     * Query the server to check if this is still a valid uid.
     * @param uid
     * @return
     */
    public static UID_STATUS isUilStillValid(int uid) {
        return null;
    }

    /**
     * Query the server, asking for the user ID associated with this account.
     * Return null if the server says these are bad Netflix credentials.
     * Return -1 if we couldn't connect to the server at all.
     * @param mEmail
     * @param mPassword
     * @return
     */
    public static Integer l34ogin(String mEmail, String mPassword) {
        Document doc = Jsoup.connect("")
        return null;
    }
}
