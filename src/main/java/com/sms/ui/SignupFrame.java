package com.sms.ui;

import com.sms.dao.UserDAO;
import com.sms.util.AppInfo;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/** Screen 2: Sign Up. Every new account is an Admin. */
public class SignupFrame extends JFrame {
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JPasswordField passField = new JPasswordField();

    public SignupFrame() {
        setTitle("Admin Sign Up");
        setSize(420, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Create Admin Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBounds(0, 20, 420, 30);
        add(title);


        addRow("User Name:", nameField, 80);
        addRow("Phone:", phoneField, 120);
        addRow("Email:", emailField, 160);
        addRow("Password:", passField, 200);

        JLabel footer = AppInfo.footerLabel();
        footer.setBounds(0, 340, 420, 20);
        add(footer);

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(70, 280, 120, 35);
        add(signupBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(230, 280, 120, 35);
        add(backBtn);

        signupBtn.addActionListener(e -> signup());
        backBtn.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
    }

    private void addRow(String text, JComponent field, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(40, y, 90, 25);
        add(label);
        field.setBounds(130, y, 240, 25);
        add(field);
    }

    private void signup() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
        try {
            new UserDAO().signup(name, email, phone, password);
            JOptionPane.showMessageDialog(this, "Admin account created! Now please login.");
            new LoginFrame().setVisible(true);
            dispose();
        } catch (SQLException ex) {
            // email column is UNIQUE, so the same email can't sign up twice
            JOptionPane.showMessageDialog(this, "This email is already used.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
