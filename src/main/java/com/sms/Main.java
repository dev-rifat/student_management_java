package com.sms;

import com.sms.db.DBConnection;
import com.sms.ui.WelcomeFrame;
import com.sms.util.AppInfo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Program starts here: prepare the database, then open the Welcome screen. */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Student Management System | " + AppInfo.signature() + " ===");
        DBConnection.initDatabase();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            new WelcomeFrame().setVisible(true);
        });
    }
}
