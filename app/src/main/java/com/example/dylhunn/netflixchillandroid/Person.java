package com.example.dylhunn.netflixchillandroid;

/**
 * Created by dylhunn on 10/10/15.
 */
public class Person {

    public final String NAME;
    public final int UID;
    public final double PRIORITY;

    // priority is a double, higher values indicate better matches
    public Person(String name, int uid, double priority) {
        NAME = name;
        UID = uid;
        PRIORITY = priority;
    }
}
