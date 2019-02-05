import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 */
public class Player {


    private Board board;
    private boolean isPlayer1;
    private int sideOfBoard;
    private ArrayList<Hole> playerBoardView;
    private HashMap<Integer, Hole> indexHoleMap;
    boolean extraMove;
    int additionalMoveEarnedCount;
    int stone_captured;
    public int rootNodeCount;


    public Board getBoard() {
        return board;
    }

    public Player(Board board, boolean isPlayer1) {

        this.board = board;
        this.isPlayer1 = isPlayer1;

        if (isPlayer1) {
            sideOfBoard = 0;
        } else {
            sideOfBoard = 1;
        }



        playerBoardView = new ArrayList<>();
        indexHoleMap = new HashMap<>();
        extraMove = false;
        rootNodeCount = 0;
        initializePlayerBoardView();

    }

    public Player copy() {
        return new Player(new Board(board), isPlayer1);
    }

    public int getSideOfBoard() {
        return sideOfBoard;
    }


    //Convert 2D array to Single Array for easy bi-directional navigation
    public void initializePlayerBoardView() {

        int counter = 0;

        if (isPlayer1) {

            for (int i = board.getWidth() - 1; i >= 0; i--) {

                Hole holeToAdd = board.getHole(0, i);
                playerBoardView.add(holeToAdd);
                indexHoleMap.put(counter, holeToAdd);
                counter++;
                //System.out.println(" 1 : hole : " + holeToAdd.showPosition() + " counter : " + counter);

            }

            for (int i = 0; i < board.getWidth(); i++) {

                Hole holeToAdd = board.getHole(1, i);
                playerBoardView.add(holeToAdd);
                indexHoleMap.put(counter, holeToAdd);
                counter++;
                //System.out.println("hole : " + holeToAdd.showPosition() + " counter : " + counter);

            }
        } else {

            for (int i = 0; i < board.getWidth(); i++) {

                Hole holeToAdd = board.getHole(1, i);
                playerBoardView.add(holeToAdd);
                indexHoleMap.put(counter, holeToAdd);
                counter++;
                //System.out.println("2 : hole : " + holeToAdd.showPosition() + " counter : " + counter);

            }

            for (int i = board.getWidth() - 1; i >= 0; i--) {

                Hole holeToAdd = board.getHole(0, i);
                playerBoardView.add(holeToAdd);
                indexHoleMap.put(counter, holeToAdd);
                counter++;
                //System.out.println("hole : " + holeToAdd.showPosition());

            }
        }
    }


    public boolean move(Player myPlayer, Player opponent, int Heuristic_Choice) {

        System.out.println("heu choice : " + Heuristic_Choice);
        System.out.print("available holes to pick from : ");

        ArrayList<Integer> choiceOfMovement = new ArrayList<>();

        for (int i = 1; i < board.getWidth() - 1; i++) {

            if (!board.getHole(sideOfBoard, i).isEmpty()) {

                choiceOfMovement.add(i);
                System.out.print(i + " ");
            }
        }

        System.out.println();

        NodeGame root = new NodeGame(board, this, myPlayer);
        root.createHole(true);
        int max = -99999999; //-INF

        for (NodeGame node : root.getChildren()) {

          node.setHeuristicValue(alphaBeta(node, 9, Integer.MIN_VALUE, Integer.MAX_VALUE, false,Heuristic_Choice));
//            node.setHeuristicValue(miniMax(node, 4, false, Heuristic_Choice));

            //System.out.println("heuristic val returned : " + node.getHeuristicValue());
            if (node.getHeuristicValue() >= max) {
                max = node.getDecisionChosen(); //max is the chosen hole now
                //System.out.println("\nmax : " + max);
            }
        }
        //System.out.println("Node number : " + root.getNodeCount() + " opened.");
        rootNodeCount = root.getNodeCount();

        int choice = max;
        System.out.println("The hole choice chosen by MinMax was : " + choice);


        return executeMove(myPlayer,opponent, choice);
    }

    public boolean executeMove(Player myplayer, Player opponent, int choiceOfHole) {

        Hole chosenHole = board.getHole(sideOfBoard, choiceOfHole);
        int numStones = chosenHole.getStones();
        //System.out.println("num stones : " +numStones + "\n");
        chosenHole.removeStones();

        if (isPlayer1) { //count from opposite side from player 1
            choiceOfHole = board.getWidth() - 1 - choiceOfHole;
        }

        //extra moves
        //if(board.getWidth() - 1 - choice <= numStones )
        if(choiceOfHole + numStones == board.getWidth())
        {
            //System.out.println("num stones : " +numStones + " chosenhole : " + choice);
            myplayer.extraMove = true;
            myplayer.additionalMoveEarnedCount++;
           // System.out.println( " GOT EXTRA MOVE");
        }
        else
        {
            //System.out.println("false : num stones : " +numStones + " chosenhole : " + choice);
            myplayer.extraMove = false;
        }

        //If the last piece you drop is in an empty hole on your side, you capture that piece and any pieces in the hole directly opposite.
        //if(numStones >= board.getWidth() + (board.getWidth() - choice) && opponent.getBoard().getHole(opponent.sideOfBoard,).getStones())


        int counter = 1;
        int increment_stones = 1;

        while (increment_stones <= numStones) {

            //System.out.println("choice + counter : " + choice + counter);
            if (choiceOfHole + counter > board.getWidth() * 2 - 1) {
                counter = 0;
                choiceOfHole = 0;
            }

            Hole currentHole = indexHoleMap.get(choiceOfHole + counter);

            if (isPlayer1) {
                if (currentHole.getStorage() && !currentHole.isBlue()) {

                    counter++;
                    //System.out.println(" 1.1 : counter : " +counter);

                } else {
                    indexHoleMap.get(choiceOfHole + counter).addStone();
                    counter++;
                    increment_stones++;
                    //System.out.println(" 1.2 : counter : " +counter + "stone increment : " + increment_stones);
                }
            } else { //for player2
                if (currentHole.getStorage() && currentHole.isBlue()) {
                    counter++;
                } else {
                    indexHoleMap.get(choiceOfHole + counter).addStone();
                    counter++;
                    increment_stones++;
                }
            }
        }//while
        return myplayer.extraMove;
    }

    public int miniMax(NodeGame root, int depth, boolean isMaximizingPlayer, int heuristicChoice) {
        //System.out.println("Node number : " + root.getNodeCount() + " opened.");
        if (depth == 0 || root == null) {
            //Check if the game is done
            return root.getBoard().heuristic(root.getPlayer(), root.getOpponent(), root.getPlayerSideOfBoard(),heuristicChoice);
        }

        if (isMaximizingPlayer) {

            root.createHole(true);
            int eval = Integer.MIN_VALUE;
            int value;
            for (NodeGame node : root.getChildren()) {

                if(!extraMove) value = miniMax(node, depth - 1, false, heuristicChoice);
                else
                {
                    //System.out.println("max got extra move");
                    value = miniMax(node, depth - 1, true, heuristicChoice);
                }

                eval = Math.max(eval, value);
            }
            return eval;

        } else { /* for min */

            root.createHole(false);
            int eval = Integer.MAX_VALUE;
            int value;
            for (NodeGame node : root.getChildren()) {
                if(!extraMove) value = miniMax(node, depth - 1, true, heuristicChoice);
                else
                {
                    //System.out.println("min got extra move");
                    value = miniMax(node, depth - 1, false, heuristicChoice);
                }
                eval = Math.min(eval, value);
            }
            return eval;
        }
    }

    public int alphaBeta(NodeGame root, int depth, int alpha, int beta, boolean isMaximizingPlayer, int heuristicChoice) {
        //System.out.println("Node number : " + root.getNodeCount() + " opened.");

        if (depth == 0 || root == null) {

            //Check if the game is done
            return root.getBoard().heuristic(root.getPlayer(), root.getOpponent(), root.getPlayerSideOfBoard(), heuristicChoice);
        }

        if (isMaximizingPlayer) {

            root.createHole(true);
            int value;

            for (NodeGame node : root.getChildren()) {
                value = alphaBeta(node, depth - 1, alpha, beta, false,heuristicChoice);
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                } //for
            }
            return alpha;

        } else {

            root.createHole(false);
            int value;
            for (NodeGame node : root.getChildren()) {
                value = alphaBeta(node, depth - 1, alpha, beta, true, heuristicChoice);
                beta = Math.min(beta, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return beta;
        }//else

    }//alphaBeta()
}
