package com.sms.ui;

/** Screen 1: Welcome. */
public class WelcomeFrame extends BaseFrame {

    public WelcomeFrame() {
        super("Admin Panel", 300);
        addButton("Admin Login", 60, 120, 140, e -> goTo(new LoginFrame()));
        addButton("Admin Sign Up", 220, 120, 140, e -> goTo(new SignupFrame()));
    }
}
