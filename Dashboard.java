import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Dashboard extends JFrame implements ActionListener {

    String username;
    JButton startQuiz, viewHistory, leaderboard, logout;
    JButton admin, export, profile;
    JComboBox<String> categoryBox;

    public Dashboard(String username) {
        this.username = username;

        setTitle("Quiz Dashboard - " + username);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setBounds(150, 30, 300, 30);
        welcome.setFont(new Font("Tahoma", Font.BOLD, 20));
        welcome.setForeground(new Color(30, 144, 255));
        add(welcome);

        JLabel categoryLabel = new JLabel("Select Category:");
        categoryLabel.setBounds(100, 90, 150, 30);
        add(categoryLabel);

        //  DYNAMIC CATEGORY LOADING
        categoryBox = new JComboBox<>();
        categoryBox.setBounds(250, 90, 150, 30);

        try {
            File file = new File("data/categories.txt");

            // create file if not exists
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();

                // default categories
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write("Java\nGeneral Knowledge\nMath\n");
                bw.close();
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                categoryBox.addItem(line);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        add(categoryBox);

        // START QUIZ
        startQuiz = new JButton("Start Quiz");
        startQuiz.setBounds(150, 140, 180, 30);
        startQuiz.setBackground(new Color(30, 144, 255));
        startQuiz.setForeground(Color.WHITE);
        startQuiz.addActionListener(this);
        add(startQuiz);

        // HISTORY
        viewHistory = new JButton("View Score History");
        viewHistory.setBounds(150, 180, 180, 30);
        viewHistory.addActionListener(this);
        add(viewHistory);

        // LEADERBOARD
        leaderboard = new JButton("Leaderboard");
        leaderboard.setBounds(150, 220, 180, 30);
        leaderboard.addActionListener(this);
        add(leaderboard);

        // ADMIN PANEL
        admin = new JButton("Admin Panel");
        admin.setBounds(150, 260, 180, 30);
        admin.addActionListener(this);
        add(admin);

        // EXPORT CSV
        export = new JButton("Export CSV");
        export.setBounds(150, 300, 180, 30);
        export.addActionListener(this);
        add(export);

        // PROFILE
        profile = new JButton("Profile");
        profile.setBounds(150, 340, 180, 30);
        profile.addActionListener(this);
        add(profile);

        // LOGOUT
        logout = new JButton("Logout");
        logout.setBounds(150, 380, 180, 30);
        logout.setBackground(Color.GRAY);
        logout.setForeground(Color.WHITE);
        logout.addActionListener(this);
        add(logout);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == startQuiz) {
            String selectedCategory = (String) categoryBox.getSelectedItem();
            setVisible(false);
            new Quiz(username, selectedCategory);

        } else if (ae.getSource() == viewHistory) {
            new ScoreHistoryWriter(username);

        } else if (ae.getSource() == leaderboard) {
            new Leaderboard();

        } else if (ae.getSource() == admin) {
            new AdminPanel();

        } else if (ae.getSource() == export) {
            ExportUtil.exportCSV();
            JOptionPane.showMessageDialog(this, "Exported to CSV!");

        } else if (ae.getSource() == profile) {
            new Profile(username);

        } else if (ae.getSource() == logout) {
            setVisible(false);
            new Login();
        }
    }
}