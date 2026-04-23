import javax.swing.*;
import java.awt.*;
import java.io.*;

public class AdminPanel extends JFrame {

    JTextField q, o1, o2, o3, o4, ans, category;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(400, 450);
        setLayout(new GridLayout(9,1));

        category = new JTextField();
        category.setBorder(BorderFactory.createTitledBorder("Category"));

        q = new JTextField();
        q.setBorder(BorderFactory.createTitledBorder("Question"));

        o1 = new JTextField("Option 1");
        o2 = new JTextField("Option 2");
        o3 = new JTextField("Option 3");
        o4 = new JTextField("Option 4");

        ans = new JTextField();
        ans.setBorder(BorderFactory.createTitledBorder("Correct Option (1-4)"));

        JButton addQ = new JButton("Add Question");
        JButton addCategory = new JButton("Add Category");

        add(category);
        add(q); add(o1); add(o2); add(o3); add(o4);
        add(ans);
        add(addQ);
        add(addCategory);

        // ADD QUESTION
        addQ.addActionListener(e -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/questions.txt", true))) {
                bw.write(category.getText() + "," + q.getText() + "," +
                        o1.getText() + "," + o2.getText() + "," +
                        o3.getText() + "," + o4.getText() + "," +
                        ans.getText());
                bw.newLine();
                JOptionPane.showMessageDialog(this, "Question Added!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ADD CATEGORY
        addCategory.addActionListener(e -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/categories.txt", true))) {
                bw.write(category.getText());
                bw.newLine();
                JOptionPane.showMessageDialog(this, "Category Added!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}