package com.sms.model;

/** Model class: one row of the "students" table. */
public class Student {
    private final int id; // database row id (0 for a new student)
    private final String studentId;
    private final String name;
    private final String department;
    private final String email;
    private final String phone;
    private final double cgpa;

    public Student(int id, String studentId, String name, String department,
                   String email, String phone, double cgpa) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.cgpa = cgpa;
    }

    public int getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public double getCgpa() { return cgpa; }
}
