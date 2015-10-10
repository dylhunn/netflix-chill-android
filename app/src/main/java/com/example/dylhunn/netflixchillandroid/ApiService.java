package com.example.dylhunn.netflixchillandroid;


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
    public static UID_STATUS isUidStillValid(int uid) {
        if (uid == 9) return UID_STATUS.VALID;
        return UID_STATUS.INVALID;
    }

    /**
     * Query the server, asking for the user ID associated with this account.
     * Return null if the server says these are bad Netflix credentials.
     * Return -1 if we couldn't connect to the server at all.
     * @param mEmail
     * @param mPassword
     * @return
     */
    public static Integer login(String mEmail, String mPassword) {
        return 9;
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
