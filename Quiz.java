package com.gqt.corejava.quizapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class Quiz extends JFrame {
    private JLabel questionLabel, prizeLabel;
    private JPanel[] optionCards = new JPanel[4];
    private JLabel[] optionLabels = new JLabel[4];
    private JButton lifelineAudience, lifeline5050, lifelineQuit;
    private int currentQuestion = 0;
    private int prize = 0;
    private int step = 10000;
    private int guaranteed = 0;
    private boolean usedAudience = false;
    private boolean used5050 = false;
    private boolean usedQuit = false;

    private String[] questions = {
        "Which of the following is not a Java keyword?",
        "What is the size of an int variable in Java?",
        "Which of these is used to define a package in Java?",
        "Which keyword is used to inherit a class in Java?",
        "Which company developed Java?",
        "Which of these is a wrapper class in Java?",
        "Which of these is not an OOP principle?",
        "Which method is the entry point in Java?",
        "Which of these is not a Java feature?",
        "Which of these is used to handle exceptions?"
    };

    private String[][] options = {
        {"static", "Boolean", "void", "private"},
        {"8 bits", "16 bits", "32 bits", "64 bits"},
        {"pkg", "package", "import", "define"},
        {"this", "super", "extends", "implements"},
        {"Sun Microsystems", "Oracle", "Microsoft", "IBM"},
        {"int", "Integer", "float", "char"},
        {"Encapsulation", "Polymorphism", "Compilation", "Inheritance"},
        {"start()", "main()", "run()", "init()"},
        {"Object-Oriented", "Use of pointers", "Platform Independent", "Robust"},
        {"try-catch", "throw", "finally", "All of the above"}
    };

    private int[] correctAnswers = {2, 3, 2, 3, 1, 2, 3, 2, 2, 4};

    private int[][] polls = {
        {20, 45, 25, 10},
        {20, 25, 45, 10},
        {10, 55, 25, 10},
        {10, 20, 60, 10},
        {50, 30, 10, 10},
        {15, 55, 20, 10},
        {25, 20, 40, 15},
        {15, 55, 20, 10},
        {15, 50, 20, 15},
        {20, 10, 20, 50}
    };

    public Quiz() {
        setTitle("Quiz Application");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(15, 32, 80));

        // Question label
        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setFont(new Font("Serif", Font.BOLD, 24));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(questionLabel, BorderLayout.NORTH);

        // Prize label
        prizeLabel = new JLabel("Prize: 0/-", JLabel.CENTER);
        prizeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        prizeLabel.setForeground(new Color(255, 215, 0));
        add(prizeLabel, BorderLayout.SOUTH);

        // Options panel
        JPanel optionPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        optionPanel.setBackground(new Color(15, 32, 80));
        for (int i = 0; i < 4; i++) {
            optionCards[i] = new JPanel();
            optionCards[i].setBackground(new Color(30, 144, 255));
            optionCards[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 3, true));
            optionCards[i].setLayout(new BorderLayout());

            optionLabels[i] = new JLabel("", JLabel.CENTER);
            optionLabels[i].setFont(new Font("Arial", Font.BOLD, 18));
            optionLabels[i].setForeground(Color.WHITE);

            optionCards[i].add(optionLabels[i], BorderLayout.CENTER);
            final int index = i;

            optionCards[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleAnswer(index + 1);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    optionCards[index].setBackground(new Color(65, 105, 225));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    optionCards[index].setBackground(new Color(30, 144, 255));
                }
            });

            optionPanel.add(optionCards[i]);
        }
        add(optionPanel, BorderLayout.CENTER);

        // Lifeline panel (vertical)
        JPanel lifelinePanel = new JPanel();
        lifelinePanel.setLayout(new GridLayout(3, 1, 10, 10));
        lifelinePanel.setBackground(new Color(15, 32, 80));

        lifelineAudience = new JButton("Audience Poll");
        lifeline5050 = new JButton("50-50");
        lifelineQuit = new JButton("Quit");

        JButton[] lifelines = {lifelineAudience, lifeline5050, lifelineQuit};
        for (JButton lifeline : lifelines) {
            lifeline.setFont(new Font("Arial", Font.BOLD, 16));
            lifeline.setBackground(new Color(255, 140, 0));
            lifeline.setForeground(Color.BLACK);
            lifeline.setFocusPainted(false);
            lifeline.addActionListener(this::handleLifeline);
            lifelinePanel.add(lifeline);
        }
        add(lifelinePanel, BorderLayout.EAST);

        loadQuestion();
        setVisible(true);
    }

    private void loadQuestion() {
        if (currentQuestion >= questions.length) {
            JOptionPane.showMessageDialog(this, "🎉 Congratulations! You won " + prize + "/-");
            System.exit(0);
        }
        questionLabel.setText("Q" + (currentQuestion + 1) + ": " + questions[currentQuestion]);
        for (int i = 0; i < 4; i++) {
            optionLabels[i].setText(options[currentQuestion][i]);
            optionCards[i].setBackground(new Color(30, 144, 255));
            optionCards[i].setEnabled(true);
        }
        prizeLabel.setText("Prize: " + prize + "/-");
    }

    private void handleAnswer(int choice) {
        if (choice == correctAnswers[currentQuestion]) {
            prize += step;

            // Update guaranteed prize at milestones
            if (currentQuestion == 4) guaranteed = 50000;   // after Q5
            if (currentQuestion == 7) guaranteed = 70000;   // after Q8

            optionCards[choice - 1].setBackground(Color.GREEN);
            JOptionPane.showMessageDialog(this, "✅ Correct! You won " + prize + "/-");
            currentQuestion++;
            loadQuestion();
        } else {
            optionCards[choice - 1].setBackground(Color.RED);
            optionCards[correctAnswers[currentQuestion] - 1].setBackground(Color.GREEN);
            int finalPrize = guaranteed;
            JOptionPane.showMessageDialog(this, "❌ Wrong Answer! Game Over.\nFinal Prize: " + finalPrize + "/-");
            System.exit(0);
        }
    }

    private void handleLifeline(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        if (src == lifelineAudience) {
            if (!usedAudience) {
                usedAudience = true;
                StringBuilder poll = new StringBuilder("📊 Audience Poll:\n");
                for (int i = 0; i < 4; i++)
                    poll.append((i + 1) + ") " + options[currentQuestion][i] + " - " + polls[currentQuestion][i] + "%\n");
                JOptionPane.showMessageDialog(this, poll.toString());
            } else JOptionPane.showMessageDialog(this, "Audience Poll already used.");
        } 
        else if (src == lifeline5050) {
            if (!used5050) {
                used5050 = true;
                ArrayList<Integer> wrong = new ArrayList<>();
                for (int i = 0; i < 4; i++) 
                    if (i + 1 != correctAnswers[currentQuestion]) wrong.add(i);
                Collections.shuffle(wrong);

                // Disable 2 wrong answers visually
                optionLabels[wrong.get(0)].setText("");
                optionCards[wrong.get(0)].setBackground(Color.DARK_GRAY);
                optionLabels[wrong.get(1)].setText("");
                optionCards[wrong.get(1)].setBackground(Color.DARK_GRAY);
            } else JOptionPane.showMessageDialog(this, "50-50 already used.");
        } 
        else if (src == lifelineQuit) {
            if (!usedQuit) {
                usedQuit = true;
                JOptionPane.showMessageDialog(this, "🚪 You quit the game.\nFinal Prize: " + prize + "/-");
                System.exit(0);
            } else JOptionPane.showMessageDialog(this, "Quit already used.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Quiz::new);
    }
}
