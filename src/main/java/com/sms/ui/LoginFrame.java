package com.sms.ui;

import com.sms.dao.AdminDAO;
import com.sms.model.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Random;

/**
 * Screen 3: Admin Login.
 * Wrong email or password -> red error message, and the Login button stays
 * visible but jumps away whenever the mouse comes on it, so it can't be clicked.
 * When the user types in the email or password box again, the button comes back.
 */
public class LoginFrame extends BaseFrame {
    private static final int BTN_X = 150, BTN_Y = 205; // normal place of the Login button

    private final JTextField emailField = new JTextField();
    private final JPasswordField passField = new JPasswordField();
    private final JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton loginBtn;
    private final Random random = new Random();
    private boolean wrong = false; // true after a wrong login

    public LoginFrame() {
        super("Admin Login", 420);
        addField("Email:", emailField, 85);
        addField("Password:", passField, 125);

        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(0, 165, FRAME_WIDTH, 25);
        add(errorLabel);

        loginBtn = addButton("Login", BTN_X, BTN_Y, 120, e -> login());
        addButton("Back", BTN_X, 315, 120, e -> goTo(new WelcomeFrame()));

        // Mouse comes on the Login button -> if the last try was wrong, jump away
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (wrong) moveButton();
            }
        });

        // User types again -> the button comes back to its place
        KeyAdapter typing = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                wrong = false;
                errorLabel.setText("");
                loginBtn.setLocation(BTN_X, BTN_Y);
            }
        };
        emailField.addKeyListener(typing);
        passField.addKeyListener(typing);
    }

    private void login() {
        if (wrong) { // email/password not changed yet
            moveButton();
            return;
        }
        try {
            Admin admin = AdminDAO.login(emailField.getText().trim(), new String(passField.getPassword()));
            if (admin != null) {
                goTo(new StudentFrame(admin)); // go straight to the Students page
                return;
            }
            errorLabel.setText("Wrong email or password!");
            wrong = true;
            moveButton();
        } catch (SQLException ex) {
            errorLabel.setText("Database error: " + ex.getMessage());
        }
    }

    /** Move the Login button to a random place far from where it was (between the error text and Back). */
    private void moveButton() {
        int x, y;
        do {
            x = 10 + random.nextInt(270);  // 10 .. 279
            y = 195 + random.nextInt(80);  // 195 .. 274
        } while (Math.abs(x - loginBtn.getX()) < 120 && Math.abs(y - loginBtn.getY()) < 40);
        loginBtn.setLocation(x, y);
    }
}
