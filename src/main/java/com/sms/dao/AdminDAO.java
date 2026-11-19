package com.sms.dao;

import com.sms.db.DBConnection;
import com.sms.model.Admin;
import com.sms.util.AppInfo;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HexFormat;

/** Admin Sign Up and Login with JDBC. */
public class AdminDAO {

    // SIGN UP -> INSERT
    public static void signup(Admin a, String password) throws SQLException {
        DBConnection.update("INSERT INTO admins(name, phone, email, password_hash) VALUES(?,?,?,?)",
                a.getName(), a.getPhone(), a.getEmail(), hash(password));
        System.out.println("[SIGNUP] " + a.getEmail() + " | " + AppInfo.signature());
    }

    // LOGIN -> SELECT. Returns the Admin, or null if the email/password is wrong.
    public static Admin login(String email, String password) throws SQLException {
        String sql = "SELECT name, phone, email FROM admins WHERE email=? AND password_hash=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hash(password));
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = rs.next();
                System.out.println("[LOGIN] " + email + (found ? " -> SUCCESS" : " -> WRONG") + " | " + AppInfo.signature());
                return found ? new Admin(rs.getString("name"), rs.getString("phone"), rs.getString("email")) : null;
            }
        }
    }

    // The password is saved as a SHA-256 hash, never as plain text.
    private static String hash(String password) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
