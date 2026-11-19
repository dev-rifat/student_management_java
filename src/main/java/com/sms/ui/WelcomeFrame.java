package com.sms.ui;

import com.sms.util.AppInfo;

import javax.swing.*;
import java.awt.*;

/** Screen 1: Welcome. */
public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Welcome - Admin Panel");
        setSize(420, 300);
        setLayout(null); // we place every item ourselves with setBounds(x, y, width, height)
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Admin Panel", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setBounds(0, 40, 420, 35);
        add(title);


        JLabel footer = AppInfo.footerLabel();
        footer.setBounds(0, 240, 420, 20);
        add(footer);

        JButton loginBtn = new JButton("Admin Login");
        loginBtn.setBounds(60, 150, 140, 40);
        add(loginBtn);

        JButton signupBtn = new JButton("Admin Sign Up");
        signupBtn.setBounds(220, 150, 140, 40);
        add(signupBtn);

        loginBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        signupBtn.addActionListener(e -> {
            new SignupFrame().setVisible(true);
            dispose();
        });
    }
}
