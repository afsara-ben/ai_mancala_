import java.util.ArrayList;

/**
 *
 */
public class NodeGame {

    NodeGame parent;
    private Board board;
    private ArrayList<NodeGame> children;
    private int decisionChosen;

    public NodeGame getParent() {
        return parent;
    }

    private Player player;
    private Player opponent;

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    private int heuristicValue;
    private static int nodeCount = 0;

    public NodeGame(Board passedBoard, Player player, Player opponent) {
        this(null, passedBoard, player, opponent, 0);
    }

    public NodeGame(NodeGame parent, Board passedBoard, Player player, Player opponent, int move) {
        this.parent = parent;
        board = new Board(passedBoard);
        children = new ArrayList<>();
        this.player = player.copy();
        this.opponent = opponent.copy();
        decisionChosen = move;
        nodeCount++;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getDecisionChosen() {
        return decisionChosen;
    }

    public void setHeuristicValue(int value) {
        heuristicValue = value;
    }

    public int getHeuristicValue() {
        return heuristicValue;
    }

    public Board getBoard() {
        return board;
    }

    public void createHole(boolean isMaximizingPlayer) {

        ArrayList<Integer> choicesMovement = new ArrayList<>();

        if (isMaximizingPlayer) {

            for (int i = 1; i < board.getWidth() - 1; i++) {
                if (!board.getHole(player.getSideOfBoard(), i).isEmpty()) {
                    choicesMovement.add(i);
                }
            }
            for (int move : choicesMovement) {
                NodeGame childAdded = new NodeGame(this, board, player, opponent, move);
                children.add(childAdded);

                Hole chosenHole = board.getHole(player.getSideOfBoard(), move);
                int numStones = chosenHole.getStones();

                //If the last piece you drop is in an empty hole on your side, you capture that piece and any pieces in the hole directly opposite.

                if (numStones >= board.getWidth() + (board.getWidth() - move)
                        && (childAdded.parent.getBoard().getHole(player.getSideOfBoard(), (move + numStones) % 16 + 2).getStones() == 0)) {

                    player.stone_captured = player.getBoard().getHole(player.getSideOfBoard(), move).getStones()
                            + opponent.getBoard().getHole(opponent.getSideOfBoard(), move).getStones();
                    //System.out.println(" stone captured : " + player.stone_captured);

                }

                childAdded.player.executeMove(this.player, this.opponent, move); //recursive
            }

        } else { //for min player

            for (int i = 1; i < board.getWidth() - 1; i++) {
                if (!board.getHole(opponent.getSideOfBoard(), i).isEmpty()) {
                    choicesMovement.add(i);
                }
            }
            for (int move : choicesMovement) {
                NodeGame childAdded = new NodeGame(this, board, player, opponent, move);
                children.add(childAdded);

                Hole chosenHole = board.getHole(player.getSideOfBoard(), move);
                int numStones = chosenHole.getStones();

                //If the last piece you drop is in an empty hole on your side, you capture that piece and any pieces in the hole directly opposite.

                if (numStones >= board.getWidth() + (board.getWidth() - move)
                        && (childAdded.parent.getBoard().getHole(player.getSideOfBoard(), (move + numStones) % 16 + 2).getStones() == 0)) {

                    player.stone_captured = player.getBoard().getHole(player.getSideOfBoard(), move).getStones()
                            + opponent.getBoard().getHole(opponent.getSideOfBoard(), move).getStones();
                    //System.out.println(" stone captured : " + player.stone_captured);

                }

                childAdded.opponent.executeMove(this.opponent, this.player, move); //recursive
            }
        }
    }

    public int getPlayerSideOfBoard() {
        return player.getSideOfBoard();
    }


    public ArrayList<NodeGame> getChildren() {
        return children;
    }
}
