package com.example.dylhunn.netflixchillandroid;

import java.util.ArrayList;

/**
 * Created by dylhunn on 10/10/15.
 */
public class ChillRequestResponseList<T> extends ArrayList<T> {

    public final int CRID;
    public final ChillRequest DATA;

    public ChillRequestResponseList(int chillRequestId, ChillRequest data) {
        CRID = chillRequestId;
        DATA = data;
    }
}
