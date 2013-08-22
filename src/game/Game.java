package game;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Game extends JFrame {
    private Font font;

    private Game() {
        super("Tetris");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loadFont();
        add(new Tetris(font));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void loadFont() {
        File f = new File("resources/BITING.TTF");
        FileInputStream in;
        Font ttFont = null;
        try {
            in = new FileInputStream(f);
            ttFont = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ttFont != null) {
            font = ttFont.deriveFont(24f);
        }
    }

    public static void main(String[] arg) {
        new Game();
    }

}
