package game;

import highscore.Highscore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Tetris extends JPanel {

    private int[][] board;
    private final Dimension boardSize = new Dimension(10, 20);
    private final int tileSize = 20;
    private Shape currentShape;
    private int currentShapeX;
    private int currentShapeY;
    private Shape nextShape;
    private Timer timer;
    private final int timerDelay = 500;
    private int score = 0;
    private boolean paused = false;
    private boolean lost = false;
    private Font font;

    private final Color[] colors = {
            new Color(14, 41, 91), //NoShape
            new Color(31, 231, 231), //LineShape
            new Color(251, 218, 102), //SquareShape
            new Color(250, 127, 49),//LShape
            new Color(56, 74, 234), //MirroredLShape
            new Color(154, 55, 223), //TShape
            new Color(208, 49, 45), //ZShape
            new Color(58, 210, 76), //SShape
    };

    public Tetris(Font font) {
        this.font = font;
        initGame();
    }

    void initGame() {
        board = new int[(int) boardSize.getWidth()][(int) boardSize.getHeight()];
        //initalize Board: empty
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 0;
            }
        }

        nextShape = new Shape();
        getNextShape();

        //Focus
        setFocusable(true);
        requestFocus();

        addKeyListener(new TetrisInputHandler(this));

        ActionListener timerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateGame();
            }
        };
        timer = new Timer(timerDelay, timerAction);
        timer.start();


    }

    private void UpdateGame() {
        moveDown();
        repaint();
        checkForCompleteRow();
    }

    private void checkForCompleteRow() {
        int removedRows = 0;
        for (int i = 0; i < boardSize.getHeight(); i++) {
            //for each row
            boolean completeRow = true;
            //now check each tile in this row
            for (int j = 0; j < boardSize.getWidth(); j++) {
                if (board[j][i] == 0) {
                    completeRow = false;
                }
            }
            if (completeRow) {
                //if one row is complete
                score += 100;
                System.out.println("Score: " + score);
                //remove row and then let all rows above fall down
                for (int r = i; r > 0; r--) {
                    for (int j = 0; j < boardSize.getWidth(); j++) {
                        //jedes feld in der reihe
                        board[j][r] = board[j][r - 1];
                    }
                }
                removedRows++;
            }

        }
        //calculate score bonus
        if (removedRows > 1) {
            score += 50 * removedRows;
            System.out.println("BONUS! +" + 50 * removedRows);
            System.out.println("Score: " + score);
        }
    }

    private boolean freeSpace(int newX, int newY) { //Checks if current Shape is allowed to be placed at newX, newY
        boolean freeSpace = true;
        int[][] coords = currentShape.getCoordinates();
        for (int i = 0; i < 4; i++) {
            if (newY - coords[i][1] == boardSize.getHeight()
                    || newX + coords[i][0] == -1 || newX + coords[i][0] == boardSize.getWidth()
                    || board[newX + coords[i][0]][newY - coords[i][1]] != 0) {
                freeSpace = false;
            }
        }

        return freeSpace;
    }

    void moveDown() {
        if (freeSpace(currentShapeX, currentShapeY + 1)) {
            currentShapeY++;
            repaint();
        } else {
            //normal Speed after drop(SPACE)
            timer.setDelay(timerDelay);

            //Save position to board
            for (int i = 0; i < 4; i++) {
                int tileToSaveY = currentShapeY - currentShape.getCoordinates()[i][1];
                int tileToSaveX = currentShapeX + currentShape.getCoordinates()[i][0];
                board[tileToSaveX][tileToSaveY] = currentShape.getShape().ordinal();
            }

            //get a new Shape
            getNextShape();

        }

    }

    public void move(int direction) {
        switch (direction) {
            case 0: //Down
                if (!paused && !lost) {
                    moveDown();
                }
                break;
            case -1: //Left
                if (!paused && !lost) {
                    moveLeft();
                }
                break;
            case 1: //Right
                if (!paused && !lost) {
                    moveRight();
                }
                break;
        }
    }

    private void moveLeft() {
        if (freeSpace(currentShapeX - 1, currentShapeY)) {
            currentShapeX--;
            repaint();
        }
    }

    private void moveRight() {
        if (freeSpace(currentShapeX + 1, currentShapeY)) {
            currentShapeX++;
            repaint();
        }
    }

    public void rotate() {
        if (!paused && !lost) {
            currentShape.rotate(board, currentShapeX, currentShapeY);
            repaint();
        }
    }

    public void drop() {
        if (!paused && !lost) {
            timer.setDelay(40);
        }
    }


    private void getNextShape() {
        currentShape = nextShape;
        nextShape = new Shape();
        System.out.println(currentShape.getShape());

        currentShapeY = 1;
        currentShapeX = 4;

        if (!freeSpace(currentShapeX, currentShapeY)) {
            timer.stop();
            System.out.println("You lose. Your Score: " + score);
            lost = true;
            new Highscore(score);
        }

    }


    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();
        //fill the background
        g2d.setColor(colors[0].darker());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawTitle(g2d);
        drawScore(g2d);
        drawNextShape(g2d);

        //draw the Game
        g2d.translate(100, 100);
        drawBoard(g2d);
        drawMovingShape(g2d);
        if (paused) {
            drawPauseScreen(g2d);
        }
        g2d.dispose();

    }

    private void drawNextShape(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate((100 + boardSize.getWidth() * tileSize), 100);
        g2d.setColor(Color.white);
        g2d.setFont(font);
        g2d.drawString("Next", 15, 20);
        int x = 2;
        int y = 3;
        for (int i = 0; i < 4; i++) {
            Color c = colors[nextShape.getShape().ordinal()];
            int coordX = nextShape.getCoordinates()[i][0];
            int coordY = nextShape.getCoordinates()[i][1];
            drawTile(g2d, c, (x + coordX) * tileSize, (y - coordY) * tileSize);
        }

        g2d.dispose();

    }

    private void drawScore(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(0, 100);
        g2d.setColor(Color.white);
        g2d.setFont(font);
        g2d.drawString("Score", 8, 20);
        g2d.drawString(Integer.toString(score), 18, 50);
        g2d.dispose();

    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.setFont(font.deriveFont(72f));
        g2d.drawString("Tetris", 65, 75);

    }

    private void drawPauseScreen(Graphics2D g) {
        //draw transparent rect over everything
        g.setColor(new Color(255, 255, 255, 100));
        g.fillRect(0, 0, boardSize.width * tileSize, boardSize.height * tileSize);
        g.setColor(new Color(0, 16, 42));
        g.fillRect(20, 100, 160, 100);
        g.setColor(colors[0]);
        g.fillRect(25, 105, 150, 90);
        g.setColor(Color.white);
        font = font.deriveFont(32f);
        g.setFont(font);
        g.drawString("Paused", 31, 160);
    }

    private void drawMovingShape(Graphics2D g) {
        for (int i = 0; i < 4; i++) {
            Color c = colors[currentShape.getShape().ordinal()];
            int coordX = currentShape.getCoordinates()[i][0];
            int coordY = currentShape.getCoordinates()[i][1];
            drawTile(g, c, (currentShapeX + coordX) * tileSize, (currentShapeY - coordY) * tileSize);
        }
    }

    private void drawBoard(Graphics2D g) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Color c = colors[board[i][j]];
                if (board[i][j] == 0) {
                    drawEmptyField(g, c, i * tileSize, j * tileSize);
                } else {
                    drawTile(g, c, i * tileSize, j * tileSize);
                }
            }
        }
    }

    private void drawEmptyField(Graphics2D g, Color c, int x, int y) {
        g.setColor(c);
        g.fillRect(x, y, tileSize - 1, tileSize - 1);
        g.setColor(c.darker());
        g.drawRect(x, y, tileSize - 1, tileSize - 1);
    }

    private void drawTile(Graphics2D g, Color c, int x, int y) {
        g.setColor(c.brighter());
        g.fillRect(x, y, tileSize - 1, tileSize - 1);
        g.setColor(c);
        g.drawRect(x + 2, y + 2, tileSize - 5, tileSize - 5);
        g.setColor(c.darker());
        g.drawRect(x + 1, y + 1, tileSize - 3, tileSize - 3);
        g.setColor(c.darker());
        g.drawRect(x, y, tileSize - 1, tileSize - 1);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(boardSize.width * tileSize + 200, boardSize.height * tileSize + 150);
    }

    public void pause() {
        if (!paused && !lost) {
            paused = true;
            timer.stop();
            System.out.println("Pause");
            repaint();
        } else {
            if (!lost) {
                paused = false;
                timer.start();
                System.out.println("Go!");
                repaint();
            }
        }
    }

    public void displayHighscore() {
        if (!lost) {
            paused = true;
            timer.stop();
            System.out.println("Pause");
            repaint();
        }
        new Highscore(0);
    }
}
