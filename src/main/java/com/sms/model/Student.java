package com.sms.model;

/** Model (POJO) class: one row of the "students" table. */
public class Student {
    private int id;
    private String studentId;
    private String name;
    private String department;
    private String email;
    private String phone;
    private double cgpa;

    public Student() {}

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
    public void setId(int id) { this.id = id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    @Override
    public String toString() {
        return String.format("%-4d %-12s %-20s %-8s %-25s %-14s %.2f",
                id, studentId, name, department, email, phone, cgpa);
    }
}
