import java.util.HashMap;

/**
 * MANCALA OFFLINE-ASSIGNMENT 4 ADVERSARIAL SEARCH
 */

public class Board {

    public static int DEFAULT_FILLED_CIRCLES = 6;
    public static int DEFAULT_EMPTY_CIRCLES = 2;
    public static int HEIGHT = 2;
    public static int DEFAULT_STONES = 4;
    public static int EMPTY = 0;
    public int CONST_VALUE_W1;
    public int CONST_VALUE_W2;
    public int CONST_VALUE_W3;
    public int CONST_VALUE_W4;

    public void setCONST_VALUE_W1(int CONST_VALUE_W1) {
        this.CONST_VALUE_W1 = CONST_VALUE_W1;
        System.out.println("W1 : " + CONST_VALUE_W1);
    }

    public void setCONST_VALUE_W2(int CONST_VALUE_W2) {
        this.CONST_VALUE_W2 = CONST_VALUE_W2;
        System.out.println("W2 : " + CONST_VALUE_W2);
    }

    public void setCONST_VALUE_W3(int CONST_VALUE_W3) {
        this.CONST_VALUE_W3 = CONST_VALUE_W3;
        System.out.println("W3 : " + CONST_VALUE_W3);
    }

    public void setCONST_VALUE_W4(int CONST_VALUE_W4) {
        this.CONST_VALUE_W4 = CONST_VALUE_W4;
        System.out.println("W4 : " + CONST_VALUE_W4);
    }

    private String[][] board; //2d array of strings
    private HashMap<String, Hole> map;
    private int width;
    private int starting_stone_count;

    public Board() {
        this(DEFAULT_FILLED_CIRCLES, DEFAULT_STONES);
    }


    //constructor1
    //numCircles is total circles in board except empty circles

    public Board(int numCircles, int stones) {
        width = numCircles + DEFAULT_EMPTY_CIRCLES;
        board = new String[HEIGHT][width];
        map = new HashMap<>();
        starting_stone_count = stones;

        initBoard();
    }


    //constructor2

    public Board(Board passedBoard) {
        width = passedBoard.width;
        board = new String[Board.HEIGHT][width];
        map = new HashMap<>();
        starting_stone_count = passedBoard.starting_stone_count;
        for (String key : passedBoard.map.keySet()) {
            Hole copiedHole = passedBoard.map.get(key).copy();
            this.map.put(key, copiedHole);
        }
    }

    //Create a mapping of Location in Board (2D Array) => Number of Stones

    private void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Hole temp = new Hole(starting_stone_count, i, j, false);
                map.put(toString(i, j), temp);
            }
        }
        initMancalaValues();
    }

    public int getWidth() {
        return width;
    }

    private void initMancalaValues() { //(1,0) and (0,7) are unused

        //Mancala on left side of board
        map.put(toString(0, 0), new Hole(EMPTY, 0, 0, true));
        map.put(toString(1, 0), new Hole(EMPTY, 1, 0, true, true));

        //Mancala on right side of the board
        map.put(toString(0, width - 1), new Hole(EMPTY, 0, width - 1, true));
        map.put(toString(1, width - 1), new Hole(EMPTY, 1, width - 1, true, true));
    }

    public int heuristic(Player player, Player opponent, int isPlayer1, int choiceOfHeuristic) {

        //calculating stones in storage
        int storageP1Left = map.get(toString(0, 0)).getStones();
        int storageP1Right = map.get(toString(0, width - 1)).getStones();
        int storageP2Left = map.get(toString(1, 0)).getStones();
        int storageP2Right = map.get(toString(1, width - 1)).getStones();

        //calculate stones in 6 bins
        int P1_sideStones = 0;
        int P2_sideStones = 0;

        for (int i = 1; i < player.getBoard().getWidth(); i++) {

            P1_sideStones = player.getBoard().getHole(isPlayer1, i).getStones();
            P2_sideStones = opponent.getBoard().getHole(isPlayer1, i).getStones();
        }

        if (choiceOfHeuristic == 1) {
            //System.out.println("heuristic 1 called\n");
            if (isPlayer1 == 0) {
                return (storageP1Left + storageP1Right) - (storageP2Left + storageP2Right);
            } else {
                return (storageP2Left + storageP2Right) - (storageP1Left + storageP1Right);
            }
        } else if (choiceOfHeuristic == 2) {
            //System.out.println("heuristic 2 called\n");
            if (isPlayer1 == 0) {

                return CONST_VALUE_W1 * ((storageP1Left + storageP1Right) - (storageP2Left + storageP2Right))
                        + CONST_VALUE_W2 * (P1_sideStones - P2_sideStones);
            } else {
                return CONST_VALUE_W2 * ((storageP2Left + storageP2Right) - (storageP1Left + storageP1Right))
                        + CONST_VALUE_W2 * (P2_sideStones - P1_sideStones);
            }
        } else if (choiceOfHeuristic == 3) {
            //System.out.println("heuristic 3 called\n");
            if (isPlayer1 == 0) {
                return CONST_VALUE_W1 * ((storageP1Left + storageP1Right) - (storageP2Left + storageP2Right))
                        + CONST_VALUE_W2 * (P1_sideStones - P2_sideStones)
                        + CONST_VALUE_W3 * player.additionalMoveEarnedCount;
            } else {
                return CONST_VALUE_W2 * ((storageP2Left + storageP2Right) - (storageP1Left + storageP1Right))
                        + CONST_VALUE_W2 * (P2_sideStones - P1_sideStones)
                        + CONST_VALUE_W3 * player.additionalMoveEarnedCount;
            }
        } else if (choiceOfHeuristic == 4) {
            //System.out.println("heuristic 4 called\n");
            if (isPlayer1 == 0) {
                return CONST_VALUE_W1 * ((storageP1Left + storageP1Right) - (storageP2Left + storageP2Right))
                        + CONST_VALUE_W2 * (P1_sideStones - P2_sideStones)
                            + CONST_VALUE_W3 * player.additionalMoveEarnedCount
                                + CONST_VALUE_W4 * player.stone_captured;
            } else {
                return CONST_VALUE_W2 * ((storageP2Left + storageP2Right) - (storageP1Left + storageP1Right))
                        + CONST_VALUE_W2 * (P2_sideStones - P1_sideStones)
                            + CONST_VALUE_W3 * player.additionalMoveEarnedCount
                                + CONST_VALUE_W4 * player.stone_captured;
            }

        } else {
            System.out.println("Wrong choice of Heuristic\n");
        }
        return 0;
    }


    //Helper Method: //converts row and column value to a joined string
    public String toString(int row, int column) {
        return "[" + row + "][" + column + "]";
    }

    //Returns a hole on the board
    public Hole getHole(int row, int column) {
        return map.get(toString(row, column));
    }

    //Prints out the board
    public void printBoard() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < width; j++) {
                if (j != width - 1) {
                    System.out.print(getHole(i, j) + "-");
                } else {
                    System.out.print(getHole(i, j));
                }
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    public boolean findWinner() {
        boolean isPlayer1Winner = false;
        Integer[] scoreHolder = countFinalStones();
        int player1Score = scoreHolder[0];
        int player2Score = scoreHolder[1];
        if (player1Score > player2Score) {
            isPlayer1Winner = true;
        }
        return isPlayer1Winner;
    }

    //Returns an array of Integers where Index: 0 Player1Score , 1 => Player2Score
    public Integer[] countFinalStones() {
        Integer[] scoreHolder = new Integer[2];

        //count score for player1
        scoreHolder[0] = getHole(0, 0).getStones() + getHole(0, width - 1).getStones();

        //count score for player2
        scoreHolder[1] = getHole(1, 0).getStones() + getHole(1, width - 1).getStones();
        return scoreHolder;
    }

    //Checks if the game is done by checking if all holes on any side excluding Mancalas are empty
    public boolean isGameFinished() {

        boolean isDone = false;

        //Check player1 holes
        for (int i = 1; i < width - 1; i++) {
            if (getHole(0, i).isEmpty()) {
                isDone = true;
            } else {
                isDone = false;
                break;
            }
        }

        if (isDone) {
            return isDone;
        }

        //Check Player2 holes
        for (int i = 1; i < width - 1; i++) {
            if (getHole(1, i).isEmpty()) {
                isDone = true;
            } else {
                isDone = false;
                break;
            }
        }
        return isDone;
    }
}
