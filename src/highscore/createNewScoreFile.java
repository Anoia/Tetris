package highscore;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class createNewScoreFile {

    private static final String HIGHSCORE_FILE = "resources/scores.dat";

    //Streams
    private static ObjectOutputStream outputStream = null;

    public static void main(String[] arg) {
        ArrayList<Score> scores = new ArrayList<Score>();
        for (int i = 0; i < 10; i++) {
            scores.add(new Score("hans", 5));
        }

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
}
