package com.sms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC connection helper.
 * Steps of JDBC:
 *   1. Load driver      -> Class.forName("org.sqlite.JDBC")
 *   2. Get connection   -> DriverManager.getConnection(URL)
 *   3. Create statement -> PreparedStatement / Statement
 *   4. Execute query    -> executeQuery() / executeUpdate()
 *   5. Close connection -> try-with-resources
 */
public class DBConnection {
    private static final String URL = "jdbc:sqlite:students.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found. Is lib/sqlite-jdbc.jar on the classpath?", e);
        }
        return DriverManager.getConnection(URL);
    }

    /** Creates the tables on first run. */
    public static void initDatabase() {
        String adminsSql = "CREATE TABLE IF NOT EXISTS admins ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "email TEXT NOT NULL UNIQUE,"
                + "phone TEXT NOT NULL,"
                + "password_hash TEXT NOT NULL)";
        String sql = "CREATE TABLE IF NOT EXISTS students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "student_id TEXT NOT NULL UNIQUE,"
                + "name TEXT NOT NULL,"
                + "department TEXT,"
                + "email TEXT,"
                + "phone TEXT,"
                + "cgpa REAL)";
        try (Connection con = getConnection(); Statement st = con.createStatement()) {
            st.execute(adminsSql);
            st.execute(sql);
            System.out.println("[DB] Connected & tables 'admins' + 'students' ready.");
        } catch (SQLException e) {
            System.err.println("[DB] Init failed: " + e.getMessage());
        }
    }
}
