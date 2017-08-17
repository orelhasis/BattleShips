package BattleShipsUI;

import BattleShipsLogic.Definitions.GameStatus;
import BattleShipsLogic.GameObjects.GameManager;
import BattleShipsLogic.GameObjects.Player;
import BattleShipsLogic.GameObjects.Point;
import BattleShipsLogic.GameSettings.BattleShipGame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Scanner;

public class Main {

    private static final String GAME_SETTINGS_FILE_PATH = "C:\\Users\\user\\IdeaProjects\\BattleShips\\src\\BattleShipsLogic\\GameSettings\\battleShip_5_basic.xml";
    private static final int GET_GAME_STATUS = 3;
    private static final int MAKE_A_MOVE = 4;
    private static final int GET_STATISTICS = 5;
    private static final int QUIT = 6;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int num;
        GameManager theGame = handleUserChoicesUntilGameBegins();

        while(theGame.getStatus() != GameStatus.OVER)
        {
            printOptionsInGame();
            System.out.print("Your choice: ");
            num = in.nextInt();

            System.out.println("----------------------------");
            switch(num) {
                case GET_GAME_STATUS:
                    System.out.println(theGame.getCurrentPlayer().getName().toString() + " it's your turn.");
                    printBoardsForGameMove(theGame.getCurrentPlayer(), theGame);
                    System.out.println("Current score: " + theGame.getCurrentPlayer().getScore() + ".");
                    break;
                case MAKE_A_MOVE:
                    makeMove(theGame);
                    if(theGame.getStatus() == GameStatus.OVER)
                    {
                        publishWinnerResults(theGame);
                    }
                    break;
                case GET_STATISTICS:
                    break;
                case QUIT:
                    theGame.setStatus(GameStatus.OVER);
                    System.out.println("Thank you and Good-Bye!");
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }

            System.out.println("----------------------------");
        }
    }

    // Gets Battle Ship game object and output winner message.
    private static void publishWinnerResults(GameManager theGame) {

        System.out.println("Congratulations " + theGame.getWinnerPlayer().getName().toString() + ", you are the winner!");
        System.out.println("Results:");
        System.out.println(theGame.getPlayers()[0].getName().toString() + ": " + theGame.getPlayers()[0].getScore());
        System.out.println(theGame.getPlayers()[1].getName().toString() + ": " + theGame.getPlayers()[1].getScore());
    }

    // Gets Battle Ship game object and make a move.
    private static void makeMove(GameManager battleShipGame) {

        Point attackedPoint = getAttackedPoint();
        battleShipGame.makeMove(attackedPoint);
    }

    // Gets Input from user and return point to attack.
    private static Point getAttackedPoint() {

        Scanner in = new Scanner(System.in);
        System.out.print("Insert row to attack:");
        int row = in.nextInt();
        System.out.print("Insert column to attack:");
        char column = in.next().charAt(0);

        int attackedRow = row-1;
        int attackedCol = column-'A';

        Point attackedPoint = new Point(attackedRow, attackedCol);
        return attackedPoint;
    }

    // Handle user choices until the game begins.
    private static GameManager handleUserChoicesUntilGameBegins() {

        Scanner in = new Scanner(System.in);
        GameManager theGame;

        printWelcomeMessage();
        int num = in.nextInt();

        while(num!=1) {
            num = in.nextInt();
        }
        theGame = loadGame();
        printStartAndNewLoadOptions();

        while(num!=2) {

            if(num==1)
            {
                theGame = loadGame();
            }

            num = in.nextInt();
        }

        return theGame;
    }

    // Load game settings and initiate a game.
    private static GameManager loadGame() {

        try {

            // Extract game settings.
            File file = new File(GAME_SETTINGS_FILE_PATH);
            JAXBContext jaxbContext = JAXBContext.newInstance(BattleShipGame.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BattleShipGame gameSettings = (BattleShipGame) jaxbUnmarshaller.unmarshal(file);

            // Initialize game.
            GameManager theGame = new GameManager(gameSettings);
            return theGame;

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Output welcome message.
    private static void printWelcomeMessage() {
        System.out.println("Welcome to the Battle Ships game!");
        System.out.println("Choose one of the following options:");
        System.out.println("1 - Load Game.");
    }

    // Output menu with start and load game options.
    private static void printStartAndNewLoadOptions() {
        System.out.println("Choose one of the following options:");
        System.out.println("1 - Load Game.");
        System.out.println("2 - Start Game.");
    }

    // Output menu options when game is running.
    private static void printOptionsInGame() {
        System.out.println("Choose one of the following options:");
        System.out.println("3 - Show game status.");
        System.out.println("4 - Make a move.");
        System.out.println("5 - Get statistics.");
        System.out.println("6 - Quit.");
    }

    // Gets current player and game objects, output the grid and tracking grids for the current player.
    private static void printBoardsForGameMove(Player player, GameManager battleShipGame) {

        if(battleShipGame.getPlayers()[0] == player) {
            printPrimaryGrid(battleShipGame.getPlayers()[0]);
            printTrackingGrid(battleShipGame.getPlayers()[1]);
        }
        else {
            printPrimaryGrid(battleShipGame.getPlayers()[1]);
            printTrackingGrid(battleShipGame.getPlayers()[0]);
        }
    }

    // Output primary grid of the input player.
    private static void printPrimaryGrid(Player player) {

        int i,j;
        char[][] playerPrimaryGrid = player.getPlayerPrimaryGrid();
        System.out.println("Primary grid of " + player.getName().toString());
        printBoard(playerPrimaryGrid);
    }

    // Output tracking grid of the input player (the one that the second player can see).
    private static void printTrackingGrid(Player player) {

        int i,j;
        System.out.println("Tracking grid of " + player.getName().toString());
        char[][] playerTrackingGrid = player.getPlayerTrackingGrid();
        printBoard(playerTrackingGrid);
    }

    // Output the input board.
    private static void printBoard(char[][] board) {

        int i,j;
        for (i = 0; i < board.length; i++) {
            for (j = 0; j < board[i].length; j++) {
                System.out.print(" "+board[i][j]+" ");

                if(j==board[i].length-1)
                {
                    System.out.println(" ");
                }
            }
        }
    }

}
