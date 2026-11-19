package com.sms.ui;

import com.sms.util.AppInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Parent class of the small screens (Welcome, Sign Up, Login).
 * It gives every screen the same size, a big title on top and
 * "Developed by ..." at the bottom, so that code is written only once.
 */
abstract class BaseFrame extends JFrame {
    static final int FRAME_WIDTH = 420;

    BaseFrame(String title, int height) {
        super(title);
        setSize(FRAME_WIDTH, height);
        setLayout(null); // every item is placed with setBounds(x, y, width, height)
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel heading = new JLabel(title, SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setBounds(0, 25, FRAME_WIDTH, 30);
        add(heading);

        JLabel footer = footer();
        footer.setBounds(0, height - 60, FRAME_WIDTH, 20);
        add(footer);
    }

    /** A label on the left and an input box on the right. */
    void addField(String text, JComponent field, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(40, y, 90, 25);
        add(label);
        field.setBounds(130, y, 240, 25);
        add(field);
    }

    JButton addButton(String text, int x, int y, int width, ActionListener action) {
        JButton b = button(text, action);
        b.setBounds(x, y, width, 35);
        add(b);
        return b;
    }

    /** Close this screen and open the next one. */
    void goTo(JFrame next) {
        next.setVisible(true);
        dispose();
    }

    static JButton button(String text, ActionListener action) {
        JButton b = new JButton(text);
        b.addActionListener(action);
        return b;
    }

    /** Small grey "Developed by ..." text for the very bottom of a window. */
    static JLabel footer() {
        JLabel l = new JLabel(AppInfo.developedBy(), SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(Color.GRAY);
        return l;
    }
}
