package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class TetrisInputHandler implements KeyListener {

    private final Tetris tetris;

    public TetrisInputHandler(Tetris tetris) {
        this.tetris = tetris;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 37: //left
                tetris.move(-1);
                break;
            case 39: //right
                tetris.move(1);
                break;
            case 40: //down
                tetris.move(0);
                break;
            case 38: //up -> rotate
                tetris.rotate();
                break;
            case 32: //space: drop
                tetris.drop();
                break;
            case 80: //P - pause
                tetris.pause();
                break;
            case 72: //H - Highscore
                tetris.displayHighscore();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
