package com.sms.dao;

import com.sms.db.DBConnection;
import com.sms.model.User;
import com.sms.util.AppInfo;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HexFormat;

/** Admin Signup and Login with JDBC (table: admins). */
public class UserDAO {

    // SIGNUP -> INSERT a new user
    public void signup(String name, String email, String phone, String password) throws SQLException {
        String sql = "INSERT INTO admins(name, email, phone, password_hash) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, hash(password));
            ps.executeUpdate();
            System.out.println("[SIGNUP] Admin " + email + " | " + AppInfo.signature());
        }
    }

    // LOGIN -> SELECT the user with this email and password. Returns null if wrong.
    public User login(String email, String password) throws SQLException {
        String sql = "SELECT name, email, phone FROM admins WHERE email=? AND password_hash=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hash(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("[LOGIN] Admin " + email + " -> SUCCESS | " + AppInfo.signature());
                    return new User(rs.getString("name"), rs.getString("email"), rs.getString("phone"));
                }
            }
        }
        System.out.println("[LOGIN] " + email + " -> WRONG | " + AppInfo.signature());
        return null;
    }

    // The password is saved as a SHA-256 hash, not as plain text.
    private String hash(String password) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
            return HexFormat.of().formatHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
