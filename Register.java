import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;
import javax.swing.*;

public class Register extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton registerBtn, loginBtn;
    JCheckBox darkMode;

    public Register() {
        setTitle("Register");
        setSize(500, 400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        // Logo
        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icons/login.jpeg"));
        Image logoImg = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds(10, 10, 60, 60);
        add(logoLabel);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(100, 100, 100, 30);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 100, 180, 30);
        styleTextField(usernameField);
        add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(100, 150, 100, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 150, 180, 30);
        styleTextField(passwordField);
        add(passwordField);

        // Buttons
        registerBtn = new JButton("Register");
        registerBtn.setBounds(100, 210, 120, 30);
        styleButton(registerBtn, new Color(30, 144, 255));
        registerBtn.addActionListener(this);
        add(registerBtn);

        loginBtn = new JButton("Go to Login");
        loginBtn.setBounds(240, 210, 120, 30);
        styleButton(loginBtn, new Color(100, 100, 255));
        loginBtn.addActionListener(this);
        add(loginBtn);

        // Dark Mode
        darkMode = new JCheckBox("Dark Mode");
        darkMode.setBounds(360, 10, 120, 25);
        darkMode.setBackground(Color.WHITE);
        darkMode.addActionListener(e -> toggleDarkMode());
        add(darkMode);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void toggleDarkMode() {
        boolean isDark = darkMode.isSelected();
        Color bg = isDark ? Color.DARK_GRAY : Color.WHITE;
        Color fg = isDark ? Color.LIGHT_GRAY : Color.BLACK;
        getContentPane().setBackground(bg);

        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JLabel || c instanceof JTextField || c instanceof JPasswordField || c instanceof JCheckBox) {
                c.setForeground(fg);
                c.setBackground(bg);
            } else if (c instanceof JButton) {
                c.setBackground(isDark ? new Color(80, 80, 160) : new Color(30, 144, 255));
            }
        }
    }

    private void styleButton(JButton button, Color baseColor) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(baseColor, 1, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
    }

    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == registerBtn) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields are required.");
                return;
            }

            try {
                File file = new File("data/users.txt");
                file.getParentFile().mkdirs();
                if (!file.exists()) file.createNewFile();

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(username)) {
                        JOptionPane.showMessageDialog(this, "Username already exists.");
                        reader.close();
                        return;
                    }
                }
                reader.close();

                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.write(username + "," + hashPassword(password));
                writer.newLine();
                writer.close();

                JOptionPane.showMessageDialog(this, "Registration successful!");
                setVisible(false);
                new Login();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == loginBtn) {
            setVisible(false);
            new Login();
        }
    }

    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) hex.append(String.format("%02x", b));
        return hex.toString();
    }
}
