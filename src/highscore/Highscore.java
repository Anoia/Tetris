package highscore;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class Highscore extends JDialog {
    private static final String HIGHSCORE_FILE = "resources/scores.dat";
    //Streams
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;
    private ArrayList<Score> scores;

    public Highscore(int newScore) {
        loadScoreFile();
        sort();
        if (newScore != 0) {
            addScore(newScore);
        }
        createGUI();
    }

    private void sort() {
        ScoreComparator scoreComparator = new ScoreComparator();
        Collections.sort(scores, scoreComparator);
    }

    private void addScore(int newScoreInt) {
        for (int i = 0; i < 10; i++) {
            if (newScoreInt > scores.get(i).getScore()) {
                String name = (String) JOptionPane.showInputDialog(
                        this,
                        "NEW HIGHSCORE!\n"
                                + "Enter your name:",
                        "New Highscore",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);
                Score newScore = new Score(name, newScoreInt);
                scores.add(i, newScore);
                break;
            }
        }
        updateScoreFile();
    }

    @SuppressWarnings("unchecked")
    private void loadScoreFile() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
            scores = (ArrayList<Score>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void createGUI() {
        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        String text = "";
        int i = 1;
        for (Score s : scores) {
            if (i < 11) {
                text += i + ". ";
                text += s.getName();
                text += "\t\t";
                text += s.getScore();
                text += "\n";
                i++;
            }
        }
        JTextArea ta = new JTextArea(text);
        ta.setBackground(new Color(0, 16, 42));
        ta.setForeground(new Color(255, 255, 255));
        ta.setEditable(false);
        add(ta);
        pack();
        setLocationRelativeTo(null);
    }
}
