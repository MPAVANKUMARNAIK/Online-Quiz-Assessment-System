import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Rules extends JFrame implements ActionListener {

    String name;
    JButton next;

    public Rules(String name, boolean f) {
        this.name = name;

        setLayout(null);

        // Title
        JLabel title = new JLabel("Rules");
        title.setBounds(220, 30, 200, 30);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title);

        // Rules Text
        JTextArea rulesText = new JTextArea();
        rulesText.setBounds(50, 80, 400, 150);
        rulesText.setFont(new Font("Arial", Font.PLAIN, 14));
        rulesText.setEditable(false);
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);

        rulesText.setText(
                "1. Each question carries 1 mark.\n" +
                "2. No negative marking.\n" +
                "3. You have limited time to answer each question.\n" +
                "4. Do not refresh or close the application during the quiz.\n" +
                "5. Click 'Next' to move to the quiz.\n" +
                "6. Your final score will be displayed at the end.\n"
        );

        add(rulesText);

        // Next Button
        next = new JButton("Next");
        next.setBounds(200, 260, 100, 30);
        next.addActionListener(this);
        add(next);

        // Frame Settings
        setSize(500, 400);
        setLocationRelativeTo(null); // ✅ CENTER SCREEN FIX
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        new Dashboard(name);
    }
}