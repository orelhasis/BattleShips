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

    private static final String GAME_SETTINGS_FILE_PATH = "src\\BattleShipsLogic\\GameSettings\\battleShip_5_basic.xml";
    private static final int LOAD_GAME = 1;
    private static final int START_GAME = 2;
    private static final int GET_GAME_STATUS = 3;
    private static final int MAKE_A_MOVE = 4;
    private static final int GET_STATISTICS = 5;
    private static final int QUIT = 6;
    private static final int EXIT_GAME = 7;
    private static GameManager theGame;

    public static void main(String[] args) {
        theGame = new GameManager();
        int choice = 0;
        printWelcomeMessage();
        while(choice != EXIT_GAME)
        {
            printChoices();
            choice = getUserInput();
            System.out.println("----------------------------");
            handleUserInput(choice);
            System.out.println("----------------------------");
        }
    }

    private static void handleUserInput(int choice){
        switch(choice) {
            case LOAD_GAME:
                if(loadGame()){
                    theGame.setStatus(GameStatus.LOADED);
                    System.out.println("Game Was Successfully Loaded");
                }
                else{
                    System.out.println("Failed To Load:");
                    //TODO ADD LOGIC TO PRINT REASON FOR LOADING ERROR
                }
                break;
            case START_GAME:
                if(theGame.getStatus() == GameStatus.OVER){
                    loadGame();
                }

                theGame.setStatus(GameStatus.RUN);
                System.out.println("Let The Battle Begin!!!");
                break;
            case GET_GAME_STATUS:
                System.out.println(theGame.getCurrentPlayer().getName().toString() + " it's your turn.");
                printBoardsForGameMove(theGame.getCurrentPlayer(), theGame);
                System.out.println("Current score: " + theGame.getCurrentPlayer().getScore() + ".");
                break;
            case MAKE_A_MOVE:
                makeMove(theGame);
                if(theGame.getStatus() == GameStatus.OVER) {
                    publishWinnerResults(theGame);
                }
                break;
            case GET_STATISTICS:
                printStatistics();
                break;
            case QUIT:
                System.out.println("You Just Surrendered!");
                theGame.setStatus(GameStatus.OVER);
                break;
            case EXIT_GAME:
                System.out.println("Thank you and Good-Bye!");
            break;
        }
    }

    private static  void printStatistics(){
        System.out.println("All Kind of statistics to be printed here");
    }

    private static int getUserInput(){
        Scanner in = new Scanner(System.in);
        while(!in.hasNextInt()){
            System.out.println("Invalid Input");
            in.nextLine();
            System.out.print("Your Choice:");
        }
        int choice = in.nextInt();
        while(!isLegalChoice(choice)){
            System.out.println("----------------------------");
            System.out.println("Invalid Choice, Please try again");
            System.out.println("----------------------------");
            printChoices();
            choice = -1;
            while(in.hasNextLine() && choice == -1){
                in.nextLine();
                if(!in.hasNextInt()) {
                    System.out.println("----------------------------");
                    System.out.println("Invalid Input");
                    System.out.println("----------------------------");
                    printChoices();
                }
                else{
                    choice = in.nextInt();
                }
            }
        }
        return choice;
    }
    private static Boolean isLegalChoice(int choice){
        Boolean isLegalInput= false;
        switch(theGame.getStatus()){
            case INIT:
                isLegalInput = choice == LOAD_GAME || choice == EXIT_GAME;
                break;
            case LOADED:
                isLegalInput = choice == LOAD_GAME || choice == START_GAME || choice == EXIT_GAME;
                break;
            case RUN:
                isLegalInput = choice == GET_GAME_STATUS || choice == MAKE_A_MOVE || choice == GET_STATISTICS || choice == QUIT || choice == EXIT_GAME;
                break;
            case OVER:
                isLegalInput = choice == LOAD_GAME || choice == START_GAME || choice == EXIT_GAME;
                break;
        }
        return isLegalInput;
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
        Point attackedPoint;
        do{
            attackedPoint = getAttackedPoint();
        }while(!isLegalPoint(attackedPoint, battleShipGame));
        battleShipGame.makeMove(attackedPoint);
    }

    private static Boolean isLegalPoint(Point p, GameManager battleShipGame){
        return p.getX() >= 0 && p.getX() <  battleShipGame.getBoarSize() && p.getY()>=0 && p.getY() < battleShipGame.getBoarSize();
    }

    // Gets Input from user and return point to attack.
    private static Point getAttackedPoint() {
        Point resPoint = new Point(0,0);
        Scanner in = new Scanner(System.in);
        System.out.print("Insert Point  To attach (i.e B5):");
        String playerInput = in.nextLine();
        if(playerInput.length() != 2){
            resPoint.setX(-1);
            resPoint.setY(-1);
        }
        else{
            resPoint.setX(playerInput.toCharArray()[0] - 'A');
            resPoint.setY(playerInput.toCharArray()[1] - '0' - 1);
        }
        return resPoint;
    }

    // Load game settings and initiate a game.
    private static Boolean loadGame() {
        boolean isLoadedSuccessfully;
        try {
            // Extract game settings.
            File file = new File(GAME_SETTINGS_FILE_PATH);
            JAXBContext jaxbContext = JAXBContext.newInstance(BattleShipGame.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BattleShipGame gameSettings = (BattleShipGame) jaxbUnmarshaller.unmarshal(file);
            isLoadedSuccessfully = theGame.LoadGame(gameSettings);
        } catch (JAXBException e) {
            e.printStackTrace();
            isLoadedSuccessfully = false;
        }
        return isLoadedSuccessfully;
    }

    // Output welcome message.
    private static void printWelcomeMessage() {
        System.out.println("Welcome to the Battle Ships game!");
    }

    // Output menu options when game is running.
    private static void printChoices() {
        System.out.println("Choose one of the following options:");
        switch(theGame.getStatus()){
            case INIT:
                System.out.println("1 - Load Game.");
                System.out.println("7 - Exit Game.");
                break;
            case LOADED:
                System.out.println("1 - Load Game.");
                System.out.println("2 - Start Game.");
                System.out.println("7 - Exit Game.");
                break;
            case RUN:
                System.out.println("3 - Show game status.");
                System.out.println("4 - Make a move.");
                System.out.println("5 - Get statistics.");
                System.out.println("6 - Surrender");
                System.out.println("7 - Exit Game.");
                break;
            case OVER:
                System.out.println("1 - Load Game.");
                System.out.println("2 - Restart Game.");
                System.out.println("7 - Exit Game.");
                break;
        }
        System.out.print("Your choice: ");
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
