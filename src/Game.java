import java.util.Random;

//driver class

public class Game {

    private Board board;
    private Player player1;
    private Player player2;


    public Game() {

        board = new Board();

        //set constant values randomly in  each game
        board.setCONST_VALUE_W1(new Random().nextInt(100));
        board.setCONST_VALUE_W2(new Random().nextInt(100));
        board.setCONST_VALUE_W3(new Random().nextInt(100));
        board.setCONST_VALUE_W4(new Random().nextInt(100));

        player1 = new Player(board, true);
        player2 = new Player(board, false);

    }


    public int startPlay(int heuristic_choice, int heuristic_choice_2) {

        Random r = new Random();
        int startingPlayer = r.nextInt(2);

        boolean isPlayer_1_turn = false;
        boolean isPlayer_2_turn = true;

        board.printBoard();

        if (startingPlayer == 0) {

            System.out.println("Player 1 Starts");
            //player1.move(player1, heuristic_choice);
            boolean extra_move = player1.move(player1, player2, heuristic_choice);
            if (extra_move) {
                isPlayer_1_turn = true;
                isPlayer_2_turn = false;
            } else {
                isPlayer_1_turn = false;
                isPlayer_2_turn = true;
            }

        } else {

            System.out.println("Player 2 Starts");
            //player2.move(player2, heuristic_choice);
            //isPlayer_1_turn = true;
            boolean extra_move = player2.move(player2, player1, heuristic_choice_2);
            if (extra_move) {
                isPlayer_1_turn = false;
                isPlayer_2_turn = true;
            } else {
                isPlayer_1_turn = true;
                isPlayer_2_turn = false;
            }
        }

        board.printBoard();

        int itr = 10;
        while (!board.isGameFinished()) {
            //while (itr != 0) {

            if (isPlayer_1_turn) {

                //System.out.println("Before Player 1 goes:");
                //board.printBoard();

                System.out.println("Player 1 Turn");
                boolean extra_move = player1.move(player1, player2, heuristic_choice);
                if (extra_move) {
                    isPlayer_1_turn = true;
                    isPlayer_2_turn = false;
                } else {
                    isPlayer_1_turn = false;
                    isPlayer_2_turn = true;
                }
                //System.out.println("\nAfter Player 1 goes:");
                board.printBoard();

            } else if (isPlayer_2_turn) {

                //System.out.println("Before Player 2 goes:");
                //board.printBoard();

                System.out.println("Player 2 Turn");
                boolean extra_move = player2.move(player2,player1, heuristic_choice_2);
                if (extra_move) {
                    isPlayer_1_turn = false;
                    isPlayer_2_turn = true;
                } else {
                    isPlayer_1_turn = true;
                    isPlayer_2_turn = false;
                }

                //System.out.println("\nAfter Player 2 goes:");
                board.printBoard();
            }
            itr--;
        }

        boolean winner = board.findWinner();
        if (winner) {
            System.out.println("Player 1 is the winner, Total stones is: " + board.countFinalStones()[0]);
            board.printBoard();
            System.out.println("nodes expanded by winner: " + player1.rootNodeCount);
            return 1;
        } else {
            System.out.println("Player 2 is the winner, Total stones is: " + board.countFinalStones()[1]);
            board.printBoard();
            System.out.println("nodes expanded by winner : " + player1.rootNodeCount);
            return 2;
        }
    }

    public static void main(String[] args) {

        int player1WinCount = 0;
        int player2WinCount = 0;

        int PlayerChoice1 = 3; //choice of heuristic
        int PlayerChoice2 = 1;
        //for (int i = 0; i < 10; i++) {
        for (int i = 0; i < 1; i++) {
            Game game = new Game();
            int ret = game.startPlay(PlayerChoice1, PlayerChoice2);
            if(ret == 1) player1WinCount ++;
            else if(ret == 2) player2WinCount++;
            else System.out.println("error");
        }

        System.out.println("PLAYER 1 WON :" +player1WinCount + "\n PLAYER 2 WON : " + player2WinCount);
    }
}
