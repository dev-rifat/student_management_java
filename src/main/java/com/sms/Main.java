package com.sms;

import com.sms.db.DBConnection;
import com.sms.ui.WelcomeFrame;
import com.sms.util.AppInfo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        AppInfo.printHeader();
        DBConnection.initDatabase();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            new WelcomeFrame().setVisible(true);
        });
    }
}
