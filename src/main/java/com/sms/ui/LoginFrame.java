package com.sms.ui;

import com.sms.dao.UserDAO;
import com.sms.model.User;
import com.sms.util.AppInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Random;

/**
 * Screen 3: Admin Login. Whoever logs in here is an Admin.
 * Wrong email or password -> red error message, and the Login button stays
 * visible but moves away every time the mouse comes near, so it can't be clicked.
 * When the user types in the email or password box again, the button comes back.
 */
public class LoginFrame extends JFrame {
    private final JTextField emailField = new JTextField();
    private final JPasswordField passField = new JPasswordField();
    private final JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton loginBtn = new JButton("Login");
    private final Random random = new Random();
    private boolean wrong = false; // true after a wrong login

    public LoginFrame() {
        setTitle("Admin Login");
        setSize(420, 420);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Admin Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBounds(0, 20, 420, 30);
        add(title);

        JLabel footer = AppInfo.footerLabel();
        footer.setBounds(0, 360, 420, 20);
        add(footer);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(40, 85, 90, 25);
        add(emailLabel);
        emailField.setBounds(130, 85, 240, 25);
        add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(40, 125, 90, 25);
        add(passLabel);
        passField.setBounds(130, 125, 240, 25);
        add(passField);

        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(0, 165, 420, 25);
        add(errorLabel);

        loginBtn.setBounds(150, 205, 120, 35);
        add(loginBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(150, 320, 120, 30);
        add(backBtn);

        // Click on Login
        loginBtn.addActionListener(e -> login());

        // Mouse comes on the Login button -> if the last try was wrong, move away
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (wrong) moveButton();
            }
        });

        // User types again -> button comes back to its place
        KeyAdapter typing = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                wrong = false;
                errorLabel.setText("");
                loginBtn.setLocation(150, 205);
            }
        };
        emailField.addKeyListener(typing);
        passField.addKeyListener(typing);

        backBtn.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
    }

    private void login() {
        if (wrong) { // still the same wrong email/password
            moveButton();
            return;
        }
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword());

        try {
            User user = new UserDAO().login(email, password);
            if (user == null) {
                errorLabel.setText("Wrong email or password!");
                wrong = true;
                moveButton();
            } else {
                new StudentFrame(user).setVisible(true); // go straight to the Students page
                dispose();
            }
        } catch (SQLException ex) {
            errorLabel.setText("Database error: " + ex.getMessage());
        }
    }

    /** Move the Login button to a random place (below the error message, above Back). */
    private void moveButton() {
        int oldX = loginBtn.getX();
        int oldY = loginBtn.getY();
        int x, y;
        do {
            x = 10 + random.nextInt(270); // 10 .. 280
            y = 200 + random.nextInt(80); // 200 .. 279 (between the error message and Back)
        } while (Math.abs(x - oldX) < 120 && Math.abs(y - oldY) < 40); // must go far from the mouse
        loginBtn.setLocation(x, y);
    }
}
