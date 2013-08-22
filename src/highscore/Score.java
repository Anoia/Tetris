package highscore;

import java.io.Serializable;

class Score implements Serializable {
    private final int score;
    private final String name;

    public Score(String name, int score) {
        this.score = score;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}