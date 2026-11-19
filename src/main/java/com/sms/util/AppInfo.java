package com.sms.util;

/** Developer name & ID. Change it here once and it updates everywhere. */
public class AppInfo {
    public static final String NAME = "MD. Lutfar Rahman";
    public static final String ID = "0272320005101051";

    public static String signature() {
        return "Name: " + NAME + " | ID: " + ID;
    }

    public static String developedBy() {
        return "Developed by " + NAME + "  |  ID: " + ID;
    }
}
