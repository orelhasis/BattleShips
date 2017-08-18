package BattleShipsUI;

import BattleShipsLogic.Definitions.GameStatus;
import BattleShipsLogic.Definitions.MoveResults;
import BattleShipsLogic.GameObjects.GameManager;
import BattleShipsLogic.GameObjects.Player;
import BattleShipsLogic.GameObjects.Point;
import BattleShipsLogic.GameSettings.BattleShipGame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Observer;

public abstract class BattleShipUI implements Observer{
    static final String GAME_SETTINGS_FILE_PATH = "src\\BattleShipsLogic\\GameSettings\\battleShip_5_basic.xml";
    static final int LOAD_GAME = 1;
    static final int START_GAME = 2;
    static final int GET_GAME_STATUS = 3;
    static final int MAKE_A_MOVE = 4;
    static final int GET_STATISTICS = 5;
    static final int QUIT = 6;
    static final int EXIT_GAME = 7;
    protected GameManager theGame;


    protected abstract void showWelcomeMessage();
    protected abstract void showGameLoadedMessage();
    protected abstract void showGameLoadFailedMessage();
    public abstract void StartGame();
    protected abstract void printGameStartsMessage();
    protected abstract void publishWinnerResults();
    protected abstract void printStatistics();

    public BattleShipUI() {
        theGame = new GameManager();
        theGame.addObserver(this);
    }

    // Load game settings and initiate a game.
    protected Boolean loadGame() {
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

    protected Boolean isLegalChoice(int choice){
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

    protected Boolean isLegalPoint(Point p){
        return p.getX() >= 0 && p.getX() <  theGame.getBoarSize() && p.getY()>=0 && p.getY() < theGame.getBoarSize();
    }

    protected void showBoards(Player player, GameManager battleShipGame) {

        if(battleShipGame.getPlayers()[0] == player) {
            showPrimaryGrid(battleShipGame.getPlayers()[0]);
            showTrackingGrid(battleShipGame.getPlayers()[1]);
        }
        else {
            showPrimaryGrid(battleShipGame.getPlayers()[1]);
            showTrackingGrid(battleShipGame.getPlayers()[0]);
        }
    }

    protected MoveResults attackAPoint(Point pointToAttack) {
        return theGame.makeMove(pointToAttack);
    }

    protected abstract void showPrimaryGrid(Player player);

    protected abstract void showTrackingGrid(Player player);

    protected abstract void showBoard(char[][] board);
}
