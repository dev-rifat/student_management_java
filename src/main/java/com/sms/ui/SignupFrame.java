package com.sms.ui;

import com.sms.dao.AdminDAO;
import com.sms.model.Admin;

import javax.swing.*;
import java.sql.SQLException;

/** Screen 2: Admin Sign Up. */
public class SignupFrame extends BaseFrame {
    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passField = new JPasswordField();

    public SignupFrame() {
        super("Admin Sign Up", 400);
        addField("User Name:", nameField, 80);
        addField("Phone:", phoneField, 120);
        addField("Email:", emailField, 160);
        addField("Password:", passField, 200);
        addButton("Sign Up", 70, 265, 120, e -> signup());
        addButton("Back", 230, 265, 120, e -> goTo(new WelcomeFrame()));
    }

    private void signup() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword());

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
        try {
            AdminDAO.signup(new Admin(name, phone, email), password);
            JOptionPane.showMessageDialog(this, "Admin account created! Now please login.");
            goTo(new LoginFrame());
        } catch (SQLException ex) {
            // the email column is UNIQUE, so the same email can't sign up twice
            String msg = ex.getMessage().contains("UNIQUE") ? "This email is already used." : ex.getMessage();
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
