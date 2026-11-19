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

/** All student CRUD operations (Create, Read, Update, Delete) with JDBC. */
public class StudentDAO {

    // CREATE
    public static void insert(Student s) throws SQLException {
        DBConnection.update("INSERT INTO students(student_id, name, department, email, phone, cgpa) VALUES(?,?,?,?,?,?)",
                s.getStudentId(), s.getName(), s.getDepartment(), s.getEmail(), s.getPhone(), s.getCgpa());
        log("INSERT " + s.getName());
    }

    // UPDATE
    public static void update(Student s) throws SQLException {
        DBConnection.update("UPDATE students SET student_id=?, name=?, department=?, email=?, phone=?, cgpa=? WHERE id=?",
                s.getStudentId(), s.getName(), s.getDepartment(), s.getEmail(), s.getPhone(), s.getCgpa(), s.getId());
        log("UPDATE " + s.getName());
    }

    // DELETE
    public static void delete(int id) throws SQLException {
        DBConnection.update("DELETE FROM students WHERE id=?", id);
        log("DELETE row " + id);
    }

    // READ: search by name / student ID / department. An empty keyword returns every student.
    public static List<Student> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM students WHERE name LIKE ? OR student_id LIKE ? OR department LIKE ? ORDER BY id";
        String k = "%" + keyword + "%";
        List<Student> list = new ArrayList<>(); // Java Collection
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Student(rs.getInt("id"), rs.getString("student_id"), rs.getString("name"),
                            rs.getString("department"), rs.getString("email"), rs.getString("phone"),
                            rs.getDouble("cgpa")));
                }
            }
        }
        log("SELECT " + list.size() + " record(s)");
        return list;
    }

    private static void log(String msg) {
        System.out.println("[" + msg + "] | " + AppInfo.signature());
    }
}
