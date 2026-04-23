import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Quiz extends JFrame implements ActionListener {

    String questions[][] = new String[10][5];
    String answers[][] = new String[10][2];
    String useranswers[][] = new String[10][1];

    JLabel qno, timerLabel;
    JTextArea question;
    JRadioButton opt1, opt2, opt3, opt4;
    ButtonGroup groupoptions;
    JButton next, submit, lifeline;

    int timer = 15;
    int count = 0;
    int score = 0;
    Timer quizTimer;

    String name, category;

    public Quiz(String name, String category) {
        this.name = name;
        this.category = category;

        setBounds(50, 0, 1440, 850);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // IMAGE
        ImageIcon i1 = new ImageIcon("icons/quiz.jpg");
        JLabel image = new JLabel(i1);
        image.setBounds(0, 0, 1440, 350);
        add(image);

        // MAIN PANEL
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 350, 1440, 500);
        panel.setBackground(new Color(245, 245, 245));
        add(panel);

        // QUESTION NUMBER
        qno = new JLabel();
        qno.setBounds(100, 40, 50, 30);
        qno.setFont(new Font("Arial", Font.BOLD, 22));
        qno.setForeground(Color.BLACK);
        panel.add(qno);

        // QUESTION TEXT (FIXED)
        question = new JTextArea();
        question.setBounds(150, 20, 900, 100);
        question.setFont(new Font("Arial", Font.BOLD, 20));
        question.setForeground(Color.BLACK);
        question.setBackground(new Color(245, 245, 245));
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setEditable(false);
        panel.add(question);

        // TIMER
        timerLabel = new JLabel("Time left - 15 seconds");
        timerLabel.setBounds(1050, 40, 350, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        timerLabel.setForeground(Color.RED);
        panel.add(timerLabel);

        // OPTIONS
        opt1 = new JRadioButton();
        opt2 = new JRadioButton();
        opt3 = new JRadioButton();
        opt4 = new JRadioButton();

        JRadioButton[] options = {opt1, opt2, opt3, opt4};

        int y = 140;
        for (JRadioButton opt : options) {
            opt.setBounds(170, y, 900, 40);
            opt.setFont(new Font("Arial", Font.PLAIN, 18));
            opt.setBackground(new Color(245, 245, 245));
            opt.setForeground(Color.BLACK);
            opt.setFocusPainted(false);
            panel.add(opt);
            y += 60;
        }

        groupoptions = new ButtonGroup();
        groupoptions.add(opt1);
        groupoptions.add(opt2);
        groupoptions.add(opt3);
        groupoptions.add(opt4);

        // BUTTONS
        next = new JButton("Next");
        next.setBounds(1100, 200, 200, 45);
        next.setBackground(new Color(30, 144, 255));
        next.setForeground(Color.WHITE);
        next.setFont(new Font("Arial", Font.BOLD, 16));
        next.addActionListener(this);
        panel.add(next);

        lifeline = new JButton("50-50 Lifeline");
        lifeline.setBounds(1100, 270, 200, 45);
        lifeline.setFont(new Font("Arial", Font.BOLD, 14));
        lifeline.addActionListener(this);
        panel.add(lifeline);

        submit = new JButton("Submit");
        submit.setBounds(1100, 340, 200, 45);
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        submit.addActionListener(this);
        submit.setEnabled(false);
        panel.add(submit);

        setupQuestions(category);
        start(count);
        startTimer();

        setVisible(true);
    }

    // 🔥 FIXED QUESTION LOADING
    void setupQuestions(String category) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/questions.txt"));
            String line;
            int i = 0;

            while ((line = br.readLine()) != null && i < 10) {
                String[] p = line.split(",");

                // Skip invalid lines
                if (p.length < 7) continue;

                if (p[0].equalsIgnoreCase(category)) {
                    questions[i][0] = p[1];
                    questions[i][1] = p[2];
                    questions[i][2] = p[3];
                    questions[i][3] = p[4];
                    questions[i][4] = p[5];

                    answers[i][1] = p[Integer.parseInt(p[6])];
                    i++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void start(int count) {
        qno.setText((count + 1) + ". ");
        question.setText(questions[count][0]);

        opt1.setText(questions[count][1]);
        opt1.setActionCommand(questions[count][1]);

        opt2.setText(questions[count][2]);
        opt2.setActionCommand(questions[count][2]);

        opt3.setText(questions[count][3]);
        opt3.setActionCommand(questions[count][3]);

        opt4.setText(questions[count][4]);
        opt4.setActionCommand(questions[count][4]);

        groupoptions.clearSelection();
    }

    void startTimer() {
        quizTimer = new Timer(1000, e -> {
            timer--;
            timerLabel.setText("Time left - " + timer + " seconds");

            if (timer <= 0) {
                quizTimer.stop();
                handleAnswer();

                if (count == 9) {
                    showScore();
                } else {
                    count++;
                    start(count);
                    timer = 15;
                    startTimer();
                }

                if (count == 8) {
                    next.setEnabled(false);
                    submit.setEnabled(true);
                }
            }
        });
        quizTimer.start();
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == next) {
            quizTimer.stop();
            handleAnswer();

            if (count == 8) {
                next.setEnabled(false);
                submit.setEnabled(true);
            }

            count++;
            start(count);
            timer = 15;
            startTimer();

        } else if (ae.getSource() == lifeline) {

            opt2.setEnabled(false);
            opt3.setEnabled(false);
            lifeline.setEnabled(false);

        } else if (ae.getSource() == submit) {
            quizTimer.stop();
            handleAnswer();
            showScore();
        }
    }

    void handleAnswer() {
        if (groupoptions.getSelection() == null) {
            useranswers[count][0] = "";
        } else {
            useranswers[count][0] = groupoptions.getSelection().getActionCommand();
        }
    }

    void showScore() {
        for (int i = 0; i < useranswers.length; i++) {
            if (useranswers[i][0] != null &&
                useranswers[i][0].equals(answers[i][1])) {
                score += 10;
            }
        }

        setVisible(false);
        new Score(name, score);
    }
}