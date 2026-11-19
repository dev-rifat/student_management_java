package com.sms.ui;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;
import com.sms.model.User;
import com.sms.util.AppInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/** Students page (opens after Admin login): admin info on top, form on the left, table on the right. */
public class StudentFrame extends JFrame {
    private final StudentDAO dao = new StudentDAO();
    private final User admin; // the Admin who logged in

    private final JTextField tfStudentId = new JTextField(15);
    private final JTextField tfName = new JTextField(15);
    private final JComboBox<String> cbDept = new JComboBox<>(new String[]{"CSE", "EEE", "BBA", "English", "Civil", "Law"});
    private final JTextField tfEmail = new JTextField(15);
    private final JTextField tfPhone = new JTextField(15);
    private final JTextField tfCgpa = new JTextField(15);
    private final JTextField tfSearch = new JTextField(15);

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"#", "Student ID", "Name", "Dept", "Email", "Phone", "CGPA"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel(" ");
    private int selectedRowId = -1;

    public StudentFrame(User admin) {
        super("Student Management  -  Admin: " + admin.getName());
        this.admin = admin;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 600);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildForm(), BorderLayout.WEST);
        root.add(buildTable(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
        setContentPane(root);

        loadTable(null);
    }

    private JComponent buildHeader() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(new Color(30, 64, 120));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel title = new JLabel("Student Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        p.add(title, BorderLayout.WEST);

        // Admin profile info (from the users table)
        JLabel info = new JLabel("<html><b>Admin: " + admin.getName() + "</b><br>Phone: "
                + admin.getPhone() + "<br>" + admin.getEmail() + "</html>");
        info.setForeground(Color.WHITE);
        JButton logout = button("Logout", e -> logout());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(info);
        right.add(logout);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JComponent buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Student Information"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Student ID", "Name", "Department", "Email", "Phone", "CGPA"};
        JComponent[] fields = {tfStudentId, tfName, cbDept, tfEmail, tfPhone, tfCgpa};
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; form.add(new JLabel(labels[i] + ":"), g);
            g.gridx = 1; form.add(fields[i], g);
        }

        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        buttons.add(button("Add", e -> addStudent()));
        buttons.add(button("Update", e -> updateStudent()));
        buttons.add(button("Delete", e -> deleteStudent()));
        buttons.add(button("Clear", e -> clearForm()));
        g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2;
        form.add(buttons, g);
        return form;
    }

    private JComponent buildTable() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Search:"));
        search.add(tfSearch);
        search.add(button("Search", e -> loadTable(tfSearch.getText().trim())));
        search.add(button("Show All", e -> { tfSearch.setText(""); loadTable(null); }));
        p.add(search, BorderLayout.NORTH);

        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JComponent buildFooter() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(status, BorderLayout.NORTH);
        p.add(AppInfo.footerLabel(), BorderLayout.SOUTH); // small text at the very bottom
        return p;
    }

    private JButton button(String text, java.awt.event.ActionListener l) {
        JButton b = new JButton(text);
        b.addActionListener(l);
        return b;
    }

    private void logout() {
        int ans = JOptionPane.showConfirmDialog(this, "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (ans != JOptionPane.YES_OPTION) return;
        System.out.println("[LOGOUT] Admin " + admin.getEmail() + " | " + AppInfo.signature());
        new WelcomeFrame().setVisible(true);
        dispose();
    }

    // ---------------- CRUD actions ----------------

    private void addStudent() {
        Student s = readForm();
        if (s == null) return;
        try {
            if (dao.insert(s)) {
                showStatus("Student added: " + s.getName());
                clearForm();
                loadTable(null);
            }
        } catch (SQLException ex) {
            error("Insert failed (Student ID must be unique): " + ex.getMessage());
        }
    }

    private void updateStudent() {
        if (selectedRowId < 0) { error("Select a student from the table first."); return; }
        Student s = readForm();
        if (s == null) return;
        s.setId(selectedRowId);
        try {
            if (dao.update(s)) {
                showStatus("Student updated: " + s.getName());
                clearForm();
                loadTable(null);
            }
        } catch (SQLException ex) {
            error("Update failed: " + ex.getMessage());
        }
    }

    private void deleteStudent() {
        if (selectedRowId < 0) { error("Select a student from the table first."); return; }
        int ans = JOptionPane.showConfirmDialog(this, "Delete this student?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ans != JOptionPane.YES_OPTION) return;
        try {
            if (dao.delete(selectedRowId)) {
                showStatus("Student deleted.");
                clearForm();
                loadTable(null);
            }
        } catch (SQLException ex) {
            error("Delete failed: " + ex.getMessage());
        }
    }

    private void loadTable(String keyword) {
        try {
            List<Student> list = (keyword == null || keyword.isEmpty()) ? dao.getAll() : dao.search(keyword);
            model.setRowCount(0);
            for (Student s : list) {
                model.addRow(new Object[]{s.getId(), s.getStudentId(), s.getName(), s.getDepartment(),
                        s.getEmail(), s.getPhone(), String.format("%.2f", s.getCgpa())});
            }
            showStatus(list.size() + " record(s) found.");
        } catch (SQLException ex) {
            error("Load failed: " + ex.getMessage());
        }
    }

    // ---------------- helpers ----------------

    private Student readForm() {
        String sid = tfStudentId.getText().trim();
        String name = tfName.getText().trim();
        if (sid.isEmpty() || name.isEmpty()) { error("Student ID and Name are required."); return null; }
        double cgpa;
        try {
            cgpa = tfCgpa.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfCgpa.getText().trim());
            if (cgpa < 0 || cgpa > 4) { error("CGPA must be between 0 and 4."); return null; }
        } catch (NumberFormatException ex) {
            error("CGPA must be a number."); return null;
        }
        return new Student(0, sid, name, (String) cbDept.getSelectedItem(),
                tfEmail.getText().trim(), tfPhone.getText().trim(), cgpa);
    }

    private void fillFormFromTable() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        selectedRowId = (int) model.getValueAt(r, 0);
        tfStudentId.setText(String.valueOf(model.getValueAt(r, 1)));
        tfName.setText(String.valueOf(model.getValueAt(r, 2)));
        cbDept.setSelectedItem(model.getValueAt(r, 3));
        tfEmail.setText(String.valueOf(model.getValueAt(r, 4)));
        tfPhone.setText(String.valueOf(model.getValueAt(r, 5)));
        tfCgpa.setText(String.valueOf(model.getValueAt(r, 6)));
    }

    private void clearForm() {
        selectedRowId = -1;
        tfStudentId.setText(""); tfName.setText(""); tfEmail.setText("");
        tfPhone.setText(""); tfCgpa.setText(""); cbDept.setSelectedIndex(0);
        table.clearSelection();
    }

    private void showStatus(String msg) {
        status.setText(msg);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg + "\n\n" + AppInfo.signature(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
