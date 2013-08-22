package game;


public class Shape {

    protected enum TetrisShape {
        NoShape, LineShape, SquareShape, LShape, MirroredLShape, TShape, ZShape, SShape
    }

    private final int[][][] coordsTemplate = new int[][][]{
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}}, // NoShape
            {{0, -2}, {0, -1}, {0, 0}, {0, 1}}, // LineShape
            {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // SquareShape
            {{0, 1}, {0, 0}, {0, -1}, {1, -1}}, // LShape
            {{0, 1}, {0, 0}, {0, -1}, {-1, -1}}, // MirroredLShape
            {{-1, 0}, {0, 0}, {1, 0}, {0, 1}}, // TShape
            {{-1, 1}, {0, 1}, {0, 0}, {1, 0}}, // ZShape
            {{-1, 0}, {0, 0}, {0, 1}, {1, 1}} // SShape
    };

    // the current TetrisShape of the Shape
    private TetrisShape shape;
    // the current coordinates of the current Shape
    private int[][] coordinates;

    public Shape() {
        coordinates = new int[4][2];
        setRandomShape();
    }


    void setShape(TetrisShape shape) {
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates[i].length; j++) {
                coordinates[i][j] = coordsTemplate[shape.ordinal()][i][j];
            }
        }

        this.shape = shape;
    }

    public TetrisShape getShape() {
        return shape;
    }

    public int[][] getCoordinates() {
        return coordinates;
    }

    void setRandomShape() {
        int x = (int) (Math.random() * (TetrisShape.values().length - 1) + 1);
        setShape(TetrisShape.values()[x]);
    }

    public void rotate(int[][] board, int x, int y) {
        if (shape == TetrisShape.SquareShape) {
            return;
        }
        //create new Array with coordinates after rotation
        int[][] coordsAfterRoatation = new int[4][2];
        for (int i = 0; i < 4; i++) {
            coordsAfterRoatation[i][0] = coordinates[i][1];
            coordsAfterRoatation[i][1] = -coordinates[i][0];
        }

        //check if the coordsAfterRoation would be valid given the current board and position of the Shape
        boolean rotationValid = true;
        for (int i = 0; i < 4; i++) {
            if (y - coordsAfterRoatation[i][1] == board[1].length
                    || x + coordsAfterRoatation[i][0] == -1
                    || x + coordsAfterRoatation[i][0] == board.length
                    || board[x + coordsAfterRoatation[i][0]][y - coordsAfterRoatation[i][1]] != 0) {
                rotationValid = false;
            }
        }
        if (rotationValid) {
            coordinates = coordsAfterRoatation;
        }
    }

}
