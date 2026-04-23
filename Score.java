
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Score extends JFrame implements ActionListener {
    String name;

    Score(String name, int score) {
        this.name = name;

        setBounds(400, 150, 750, 550);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("Thank you " + name);
        heading.setBounds(45, 30, 700, 30);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 26));
        add(heading);

        JLabel lblscore = new JLabel("Your score is " + score);
        lblscore.setBounds(350, 200, 300, 30);
        lblscore.setFont(new Font("Tahoma", Font.PLAIN, 26));
        add(lblscore);

        JButton submit = new JButton("Go to Dashboard");
        submit.setBounds(380, 270, 180, 30);
        submit.addActionListener(this);
        add(submit);

        try(BufferedWriter w=new BufferedWriter(new FileWriter("data/score_history.txt",true))){
            w.write(name+","+score); w.newLine();
        }catch(Exception e){}

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Dashboard(name);
    }
}
