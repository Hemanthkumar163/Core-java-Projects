package com.quizapp.main;

import com.quizapp.model.Question;
import com.quizapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class QuizAppHibernate extends JFrame implements ActionListener {

    List<Question> questions;
    JLabel questionLabel, prizeLabel, nameLabel;
    JRadioButton[] optionButtons = new JRadioButton[4];
    JButton nextButton, audienceButton, fiftyButton, quitButton;
    ButtonGroup optionGroup;
    JPanel quizPanel, startPanel;
    JTextField nameField;
    JButton startButton;

    int currentQ = 0, won = 0;
    boolean audienceUsed = false, fiftyUsed = false;
    String name;

    public QuizAppHibernate() {
        setTitle("🎮 Java Quiz Challenge - Hibernate Edition");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(72, 61, 139));

        nameLabel = new JLabel("Welcome!", JLabel.CENTER);
        nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        nameLabel.setForeground(Color.CYAN);

        prizeLabel = new JLabel("Prize: ₹0", JLabel.CENTER);
        prizeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        prizeLabel.setForeground(Color.YELLOW);

        topPanel.add(nameLabel);
        topPanel.add(prizeLabel);
        add(topPanel, BorderLayout.NORTH);

        createStartScreen();
        createQuizPanel();

        setVisible(true);
    }

    void loadQuestionsFromDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("FROM Question ORDER BY qno"); // old-compatible syntax
        questions = query.list();
        session.close();
        System.out.println("✅ Loaded " + questions.size() + " questions using Hibernate");
    }

    void createStartScreen() {
        startPanel = new JPanel(new BorderLayout());
        startPanel.setBackground(new Color(240, 248, 255));

        JTextArea instructions = new JTextArea(
                "🎮 Welcome to Java Quiz Challenge 🎮\n\n" +
                "📝 Instructions:\n" +
                "- 10 questions loaded from database (Hibernate ORM).\n" +
                "- Lifelines: Audience, 50-50, and Quit.\n" +
                "- Select A/B/C/D and click Next.\n"
        );
        instructions.setEditable(false);
        instructions.setFont(new Font("Arial", Font.PLAIN, 16));
        instructions.setBackground(new Color(240, 248, 255));
        startPanel.add(instructions, BorderLayout.NORTH);

        JPanel namePanel = new JPanel();
        namePanel.setBackground(new Color(240, 248, 255));
        namePanel.add(new JLabel("Enter your name:"));
        nameField = new JTextField(15);
        namePanel.add(nameField);
        startButton = new JButton("Start Quiz");
        namePanel.add(startButton);
        startPanel.add(namePanel, BorderLayout.CENTER);

        add(startPanel, BorderLayout.CENTER);

        startButton.addActionListener(e -> {
            name = nameField.getText().isEmpty() ? "Player" : nameField.getText();
            nameLabel.setText("Welcome, " + name + " 🎉");
            loadQuestionsFromDB();
            remove(startPanel);
            add(quizPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
            loadQuestion();
        });
    }

    void createQuizPanel() {
        quizPanel = new JPanel(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        centerPanel.setBackground(new Color(240, 248, 255));

        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        questionLabel.setForeground(new Color(0, 51, 102));
        centerPanel.add(questionLabel);

        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setBackground(Color.WHITE);
            optionButtons[i].setForeground(Color.BLACK);
            optionButtons[i].setFont(new Font("Arial", Font.BOLD, 16));
            optionGroup.add(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
        }
        quizPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(230, 230, 250));

        audienceButton = new JButton("📞 Audience");
        audienceButton.addActionListener(this);

        fiftyButton = new JButton("💡 50-50");
        fiftyButton.addActionListener(this);

        quitButton = new JButton("🏳️ Quit");
        quitButton.addActionListener(this);

        nextButton = new JButton("➡️ Next");
        nextButton.addActionListener(this);

        bottomPanel.add(audienceButton);
        bottomPanel.add(fiftyButton);
        bottomPanel.add(quitButton);
        bottomPanel.add(nextButton);
        quizPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    void loadQuestion() {
        if (currentQ < questions.size()) {
            Question q = questions.get(currentQ);
            questionLabel.setText("Q" + (currentQ + 1) + ": " + q.getQuestion());
            optionGroup.clearSelection();
            optionButtons[0].setText("A. " + q.getOptionA());
            optionButtons[1].setText("B. " + q.getOptionB());
            optionButtons[2].setText("C. " + q.getOptionC());
            optionButtons[3].setText("D. " + q.getOptionD());
        } else {
            JOptionPane.showMessageDialog(this, "🏁 Game Over! " + name + ", you won ₹" + won);
            goToStartScreen();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Question q = questions.get(currentQ);

        if (e.getSource() == nextButton) {
            char chosen = ' ';
            for (int i = 0; i < 4; i++) {
                if (optionButtons[i].isSelected()) chosen = (char) ('A' + i);
            }

            if (chosen == ' ') {
                JOptionPane.showMessageDialog(this, "⚠️ Please select an option!");
                return;
            }

            if (String.valueOf(chosen).equalsIgnoreCase(q.getCorrect())) {
                won = q.getPrize();
                prizeLabel.setText("Prize: ₹" + won);
                JOptionPane.showMessageDialog(this, "✅ Correct! You won ₹" + won);
                currentQ++;
                loadQuestion();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Wrong! You take home ₹" + won);
                goToStartScreen();
            }
        }

        if (e.getSource() == audienceButton) {
            if (audienceUsed) JOptionPane.showMessageDialog(this, "❌ Already used!");
            else {
                audienceUsed = true;
                audienceButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "📞 Audience suggests: Option " + q.getCorrect());
            }
        }

        if (e.getSource() == fiftyButton) {
            if (fiftyUsed) JOptionPane.showMessageDialog(this, "❌ Already used!");
            else {
                fiftyUsed = true;
                fiftyButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "💡 50-50 Hint includes: " + q.getCorrect());
            }
        }

        if (e.getSource() == quitButton) {
            JOptionPane.showMessageDialog(this, "🏳️ You quit with ₹" + won);
            goToStartScreen();
        }
    }

    void goToStartScreen() {
        currentQ = 0;
        won = 0;
        audienceUsed = false;
        fiftyUsed = false;
        prizeLabel.setText("Prize: ₹0");

        remove(quizPanel);
        add(startPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new QuizAppHibernate();
    }
}
