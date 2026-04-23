// Login.java
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;
import javax.swing.*;

public class Login extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn, registerBtn;
    JCheckBox darkMode;
    JPanel formPanel;

    public Login() {
        setTitle("Quiz Master Pro - Login");
        setSize(600, 500);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Ensure data folder and files exist
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();
        createIfMissing("data/score_history.txt");
        createIfMissing("data/leaderboard.txt");
        createIfMissing("data/users.txt");

        JPanel background = new JPanel();
        background.setLayout(null);
        background.setBounds(0, 0, 600, 500);
        background.setBackground(Color.WHITE);
        add(background);

        JLabel title = new JLabel("Welcome to Quiz Master Pro");
        title.setFont(new Font("Verdana", Font.BOLD, 22));
        title.setBounds(130, 30, 400, 30);
        background.add(title);

        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icons/login.jpeg"));
        Image logoImg = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds(20, 20, 80, 80);
        background.add(logoLabel);

        formPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        formPanel.setLayout(null);
        formPanel.setBounds(100, 120, 400, 280);
        formPanel.setBackground(new Color(240, 248, 255));
        background.add(formPanel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(40, 40, 100, 30);
        formPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(140, 40, 200, 30);
        styleTextField(usernameField);
        formPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(40, 90, 100, 30);
        formPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 90, 200, 30);
        styleTextField(passwordField);
        formPanel.add(passwordField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(40, 150, 140, 35);
        styleButton(loginBtn, new Color(34, 139, 34));
        loginBtn.addActionListener(this);
        formPanel.add(loginBtn);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(200, 150, 140, 35);
        styleButton(registerBtn, new Color(70, 130, 180));
        registerBtn.addActionListener(this);
        formPanel.add(registerBtn);

        darkMode = new JCheckBox("Dark Mode");
        darkMode.setBounds(270, 10, 120, 25);
        darkMode.setBackground(Color.WHITE);
        darkMode.addActionListener(e -> toggleDarkMode());
        background.add(darkMode);

        setVisible(true);
    }

    private void createIfMissing(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toggleDarkMode() {
        boolean isDark = darkMode.isSelected();
        Color bg = isDark ? Color.DARK_GRAY : Color.WHITE;
        Color fg = isDark ? Color.LIGHT_GRAY : Color.BLACK;
        getContentPane().setBackground(bg);
        formPanel.setBackground(isDark ? new Color(60, 63, 65) : new Color(240, 248, 255));

        for (Component c : formPanel.getComponents()) {
            if (c instanceof JLabel || c instanceof JTextField || c instanceof JPasswordField || c instanceof JCheckBox) {
                c.setForeground(fg);
                c.setBackground(isDark ? new Color(90, 90, 90) : Color.WHITE);
            } else if (c instanceof JButton) {
                c.setBackground(isDark ? new Color(70, 70, 130) : new Color(30, 144, 255));
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
        if (ae.getSource() == loginBtn) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields are required.");
                return;
            }

            try {
                File file = new File("data/users.txt");
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this, "No users found. Please register first.");
                    return;
                }

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                boolean userFound = false;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2 && parts[0].equals(username)) {
                        String hashedPassword = hashPassword(password);
                        if (parts[1].equals(hashedPassword)) {
                            userFound = true;
                            break;
                        } else {
                            JOptionPane.showMessageDialog(this, "Incorrect password.");
                            reader.close();
                            return;
                        }
                    }
                }
                reader.close();

                if (userFound) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    setVisible(false);
                    new Rules(username, true);
                } else {
                    JOptionPane.showMessageDialog(this, "Username not found.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (ae.getSource() == registerBtn) {
            setVisible(false);
            new Register();
        }
    }

    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) hex.append(String.format("%02x", b));
        return hex.toString();
    }

    public static void main(String[] args) {
        new Login();
    }
}
