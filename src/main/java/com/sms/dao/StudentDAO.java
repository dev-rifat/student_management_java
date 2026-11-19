package com.sms.dao;

import com.sms.db.DBConnection;
import com.sms.model.Student;
import com.sms.util.AppInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DAO (Data Access Object): all JDBC CRUD operations live here. */
public class StudentDAO {

    // CREATE
    public boolean insert(Student s) throws SQLException {
        String sql = "INSERT INTO students(student_id, name, department, email, phone, cgpa) VALUES(?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getDepartment());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setDouble(6, s.getCgpa());
            boolean ok = ps.executeUpdate() > 0;
            log("INSERT", s.getName(), ok);
            return ok;
        }
    }

    // READ (all) -> returns a Java Collection (List / ArrayList)
    public List<Student> getAll() throws SQLException {
        return query("SELECT * FROM students ORDER BY id", null);
    }

    // READ (search by name / student id / department)
    public List<Student> search(String keyword) throws SQLException {
        return query("SELECT * FROM students WHERE name LIKE ? OR student_id LIKE ? OR department LIKE ? ORDER BY id",
                "%" + keyword + "%");
    }

    // UPDATE
    public boolean update(Student s) throws SQLException {
        String sql = "UPDATE students SET student_id=?, name=?, department=?, email=?, phone=?, cgpa=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getDepartment());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setDouble(6, s.getCgpa());
            ps.setInt(7, s.getId());
            boolean ok = ps.executeUpdate() > 0;
            log("UPDATE", s.getName(), ok);
            return ok;
        }
    }

    // DELETE
    public boolean delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE id=?")) {
            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            log("DELETE", "row id " + id, ok);
            return ok;
        }
    }

    private List<Student> query(String sql, String keyword) throws SQLException {
        List<Student> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (keyword != null) {
                ps.setString(1, keyword);
                ps.setString(2, keyword);
                ps.setString(3, keyword);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Student(
                            rs.getInt("id"),
                            rs.getString("student_id"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getDouble("cgpa")));
                }
            }
        }
        System.out.println("[SELECT] " + list.size() + " record(s) loaded | " + AppInfo.signature());
        return list;
    }

    private void log(String op, String target, boolean ok) {
        System.out.println("[" + op + "] " + target + " -> " + (ok ? "SUCCESS" : "FAILED")
                + " | " + AppInfo.signature());
    }
}
