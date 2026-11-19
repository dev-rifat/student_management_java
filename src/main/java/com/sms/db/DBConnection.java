package com.sms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC in 5 steps:
 *   1. Load driver      -> Class.forName("org.sqlite.JDBC")
 *   2. Get connection   -> DriverManager.getConnection(URL)
 *   3. Create statement -> con.prepareStatement(sql)
 *   4. Execute query    -> executeUpdate() / executeQuery()
 *   5. Close connection -> try-with-resources closes it automatically
 */
public class DBConnection {
    private static final String URL = "jdbc:sqlite:students.db";

    // Step 1: load the driver (runs once, when the class is first used)
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] SQLite driver not found. Is lib/sqlite-jdbc.jar on the classpath?");
        }
    }

    // Step 2: open a connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Runs an INSERT / UPDATE / DELETE. Each ? in the SQL gets the next value. */
    public static void update(String sql, Object... values) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {   // step 3
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            ps.executeUpdate();                                     // step 4
        }                                                           // step 5
    }

    /** Creates the tables the first time the app runs. */
    public static void initDatabase() {
        try (Connection con = getConnection(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS admins ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "email TEXT NOT NULL UNIQUE,"
                    + "phone TEXT NOT NULL,"
                    + "password_hash TEXT NOT NULL)");
            st.execute("CREATE TABLE IF NOT EXISTS students ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "student_id TEXT NOT NULL UNIQUE,"
                    + "name TEXT NOT NULL,"
                    + "department TEXT,"
                    + "email TEXT,"
                    + "phone TEXT,"
                    + "cgpa REAL)");
            System.out.println("[DB] Connected & tables ready.");
        } catch (SQLException e) {
            System.err.println("[DB] Init failed: " + e.getMessage());
        }
    }
}
