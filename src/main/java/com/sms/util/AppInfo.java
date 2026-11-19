package com.sms.util;

/**
 * Student info: shown in every output (console + GUI).
 * Change here once and it updates everywhere.
 */
public class AppInfo {
    public static final String NAME = "MD. Lutfar Rahman";
    public static final String ID = "027232005101051";
    public static final String PROJECT = "Student Management System (JDBC + JFrame)";

    public static String signature() {
        return "Name: " + NAME + " | ID: " + ID;
    }

    /** Small text shown at the bottom of every screen. */
    public static String developedBy() {
        return "Developed by " + NAME + "  |  ID: " + ID;
    }

    /** Small grey label for the very bottom of a window. */
    public static javax.swing.JLabel footerLabel() {
        javax.swing.JLabel l = new javax.swing.JLabel(developedBy(), javax.swing.SwingConstants.CENTER);
        l.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11));
        l.setForeground(java.awt.Color.GRAY);
        return l;
    }

    public static void printHeader() {
        System.out.println("==================================================");
        System.out.println(" " + PROJECT);
        System.out.println(" " + signature());
        System.out.println("==================================================");
    }
}
