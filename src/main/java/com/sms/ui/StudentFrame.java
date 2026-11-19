package com.sms.ui;

import com.sms.dao.StudentDAO;
import com.sms.model.Admin;
import com.sms.model.Student;
import com.sms.util.AppInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

import static com.sms.ui.BaseFrame.button;

/** Screen 4: Students page (opens after login). Admin info on top, form on the left, table on the right. */
public class StudentFrame extends JFrame {
    private final Admin admin;

    private final JTextField idField = new JTextField(15);
    private final JTextField nameField = new JTextField(15);
    private final JComboBox<String> deptBox = new JComboBox<>(new String[]{"CSE", "EEE", "BBA", "English", "Civil", "Law"});
    private final JTextField emailField = new JTextField(15);
    private final JTextField phoneField = new JTextField(15);
    private final JTextField cgpaField = new JTextField(15);
    private final JTextField searchField = new JTextField(15);

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"#", "Student ID", "Name", "Dept", "Email", "Phone", "CGPA"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable table = new JTable(model);
    private int selectedId = -1; // database id of the selected row, -1 = nothing selected

    public StudentFrame(Admin admin) {
        super("Student Management  -  Admin: " + admin.getName());
        this.admin = admin;
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(header(), BorderLayout.NORTH);
        root.add(form(), BorderLayout.WEST);
        root.add(tablePanel(), BorderLayout.CENTER);
        root.add(BaseFrame.footer(), BorderLayout.SOUTH);
        setContentPane(root);

        refresh();
    }

    // ---------------- layout ----------------

    /** Blue bar: title on the left, Admin info + Logout on the right. */
    private JComponent header() {
        JLabel title = new JLabel("Student Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel info = new JLabel("<html><b>Admin: " + admin.getName() + "</b><br>Phone: "
                + admin.getPhone() + "<br>" + admin.getEmail() + "</html>");
        info.setForeground(Color.WHITE);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(info);
        right.add(button("Logout", e -> logout()));

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(30, 64, 120));
        bar.setBorder(new EmptyBorder(8, 12, 8, 12));
        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JComponent form() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Student Information"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Student ID:", "Name:", "Department:", "Email:", "Phone:", "CGPA:"};
        JComponent[] fields = {idField, nameField, deptBox, emailField, phoneField, cgpaField};
        for (int i = 0; i < labels.length; i++) {
            g.gridy = i;
            g.gridx = 0; form.add(new JLabel(labels[i]), g);
            g.gridx = 1; form.add(fields[i], g);
        }

        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        buttons.add(button("Add", e -> save(false)));
        buttons.add(button("Update", e -> save(true)));
        buttons.add(button("Delete", e -> delete()));
        buttons.add(button("Clear", e -> clearForm()));
        g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2;
        form.add(buttons, g);
        return form;
    }

    private JComponent tablePanel() {
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Search:"));
        search.add(searchField);
        search.add(button("Search", e -> refresh()));
        search.add(button("Show All", e -> { searchField.setText(""); refresh(); }));

        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillForm();
        });

        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.add(search, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    // ---------------- actions ----------------

    /** Add a new student, or update the selected one. */
    private void save(boolean isUpdate) {
        if (isUpdate && selectedId < 0) { error("Select a student from the table first."); return; }
        Student s = readForm(isUpdate ? selectedId : 0);
        if (s == null) return;
        try {
            if (isUpdate) StudentDAO.update(s);
            else StudentDAO.insert(s);
            clearForm();
            refresh();
        } catch (SQLException ex) {
            error(ex.getMessage().contains("UNIQUE") ? "This Student ID already exists." : ex.getMessage());
        }
    }

    private void delete() {
        if (selectedId < 0) { error("Select a student from the table first."); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete this student?", "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            StudentDAO.delete(selectedId);
            clearForm();
            refresh();
        } catch (SQLException ex) {
            error(ex.getMessage());
        }
    }

    /** Reload the table (uses the search box; empty search = all students). */
    private void refresh() {
        try {
            model.setRowCount(0);
            for (Student s : StudentDAO.search(searchField.getText().trim())) {
                model.addRow(new Object[]{s.getId(), s.getStudentId(), s.getName(), s.getDepartment(),
                        s.getEmail(), s.getPhone(), String.format("%.2f", s.getCgpa())});
            }
        } catch (SQLException ex) {
            error(ex.getMessage());
        }
    }


    private void logout() {
        if (JOptionPane.showConfirmDialog(this, "Do you want to logout?", "Logout",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        System.out.println("[LOGOUT] " + admin.getEmail() + " | " + AppInfo.signature());
        new WelcomeFrame().setVisible(true);
        dispose();
    }

    // ---------------- form helpers ----------------

    /** Reads the form into a Student. Returns null (and shows an error) if something is wrong. */
    private Student readForm(int id) {
        String studentId = idField.getText().trim();
        String name = nameField.getText().trim();
        if (studentId.isEmpty() || name.isEmpty()) { error("Student ID and Name are required."); return null; }

        double cgpa;
        try {
            String text = cgpaField.getText().trim();
            cgpa = text.isEmpty() ? 0 : Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            error("CGPA must be a number."); return null;
        }
        if (cgpa < 0 || cgpa > 4) { error("CGPA must be between 0 and 4."); return null; }

        return new Student(id, studentId, name, (String) deptBox.getSelectedItem(),
                emailField.getText().trim(), phoneField.getText().trim(), cgpa);
    }

    /** Copy the selected table row into the form. */
    private void fillForm() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        selectedId = (int) model.getValueAt(r, 0);
        idField.setText((String) model.getValueAt(r, 1));
        nameField.setText((String) model.getValueAt(r, 2));
        deptBox.setSelectedItem(model.getValueAt(r, 3));
        emailField.setText((String) model.getValueAt(r, 4));
        phoneField.setText((String) model.getValueAt(r, 5));
        cgpaField.setText((String) model.getValueAt(r, 6));
    }

    private void clearForm() {
        selectedId = -1;
        for (JTextField f : new JTextField[]{idField, nameField, emailField, phoneField, cgpaField}) f.setText("");
        deptBox.setSelectedIndex(0);
        table.clearSelection();
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
