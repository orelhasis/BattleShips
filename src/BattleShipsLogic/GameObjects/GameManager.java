package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.*;
import BattleShipsLogic.GameSettings.BattleShipGame;
import BattleShipsLogic.GameSettings.BattleShipGame.Boards.Board;
import BattleShipsLogic.GameSettings.BattleShipGame.ShipTypes;

import java.util.List;

public class GameManager extends java.util.Observable{

    /* -------------- Data members -------------- */

    private Player[] players = new Player[2];
    private GameType type;
    private GameStatus status;
    private Player currentPlayer;
    private Player winnerPlayer;
    private int boardSize;
    /* -------------- Getters and setters -------------- */

    public Player getWinnerPlayer() {
        return winnerPlayer;
    }

    public void setWinnerPlayer(Player winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer=currentPlayer;
    }

    public int getBoarSize(){
        return this.boardSize;
    }
     /* -------------- Function members -------------- */

    public GameManager() {
            status = GameStatus.INIT;
    }

    public boolean LoadGame(BattleShipGame gameSettings){
        //TODO: ADD LOADING VERIFICATION LOGIC
        Boolean isLoadedSuccessfully = true;
        if (GameType.BASIC == GameType.valueOf(gameSettings.getGameType())) {
            type = GameType.BASIC;
            loadBasicGame(gameSettings);
        }
        currentPlayer = players[0];
        winnerPlayer = null;
        return isLoadedSuccessfully;
    }

    private void loadBasicGame(BattleShipGame gameSettings) {
        this.boardSize =  gameSettings.getBoardSize();
        initializePlayer(gameSettings, PlayerName.PLAYER_1);
        initializePlayer(gameSettings, PlayerName.PLAYER_2);
    }

    private void initializePlayer(BattleShipGame gameSettings, PlayerName name) {

        // Get board size.
        int boardSize = gameSettings.getBoardSize();

        // Get Player board.
        int playerIndex = 1;
        if(name == PlayerName.PLAYER_1) {
            playerIndex = 0;
        }

        // Get Game ship types and player board.
        List<ShipTypes.ShipType> shipTypesList = gameSettings.getShipTypes().getShipType();
        Board playerBoard = gameSettings.getBoards().getBoard().get(playerIndex);

        // Initiate board.
        players[playerIndex] = new Player(name, boardSize, playerBoard.getShip().size());
        initiatePlayerBattleShips(players[playerIndex], playerBoard, shipTypesList);
    }

    private void initiatePlayerBattleShips(Player player, Board playerBoard, List<ShipTypes.ShipType> shipTypes) {

        // Get player spaceships.
        List<Board.Ship> ship = playerBoard.getShip();

        for(int i=0; i < ship.size(); i++) {
            ShipDirection direction = ShipDirection.valueOf(ship.get(i).getDirection()); // Get battle ship direction.
            BattleShipsLogic.Definitions.ShipType shipType = BattleShipsLogic.Definitions.ShipType.valueOf(ship.get(i).getShipTypeId()); // Get battle ship type.
            int length = getShipLength(ship.get(i), shipTypes); // Get ship length by type.
            int score = getShipScore(ship.get(i), shipTypes); // Get ship length by type.
            int positionX = ship.get(i).getPosition().getX(); // Get x position.
            int positionY = ship.get(i).getPosition().getY(); // Get y position.

            // Create new battle ship.
            BattleShip playerShip = new BattleShip(direction, shipType,length , score, positionX, positionY);

            // Set batttle ship to user board.
            setBattleShipToUserBoard(player, playerShip);
        }
    }

    private int getShipLength(Board.Ship ship, List<ShipTypes.ShipType> shipTypes) {
        for(int i=0; i<shipTypes.size(); i++) {
            if(ShipType.valueOf(ship.getShipTypeId()) == ShipType.valueOf(shipTypes.get(i).getId()))
            {
                return shipTypes.get(i).getLength();
            }
        }
        return -1;
    }

    private int getShipScore(Board.Ship ship, List<ShipTypes.ShipType> shipTypes) {
        for(int i=0; i<shipTypes.size(); i++) {
            if(ShipType.valueOf(ship.getShipTypeId()) == ShipType.valueOf(shipTypes.get(i).getId()))
            {
                return shipTypes.get(i).getScore();
            }
        }
        return -1;
    }


    private void setBattleShipToUserBoard(Player player, BattleShip playerShip) {
        Point position = playerShip.getPosition();
        SeaItem[][] board = player.getBoard();
        for(int i = 0 ; i< playerShip.getLength(); i++) {
            // Set item to point to the battle ship.
            board[position.getX()-1][position.getY()-1] = playerShip;

            // Move to next item that should point the battle ship.
            if(playerShip.getDirection() == ShipDirection.ROW) {
                position.setY(position.getY()+1);
            }
            else {
                position.setX(position.getX() + 1);
            }
        }
    }

    public MoveResults makeMove(Point attackedPoint) {
        MoveResults result = MoveResults.Miss;
        Player attackedPlayer = players[0];
        if(currentPlayer == players[0]){
            attackedPlayer = players[1];
        }

        // Get attacked item in the attacked player grid.
        SeaItem attackedItem = attackedPlayer.getBoard()[attackedPoint.getX()][attackedPoint.getY()];
        int x = attackedPoint.getX();
        int y = attackedPoint.getY();
        if(attackedItem.IsDestroyed()){
            result = MoveResults.Used;
        }
        else if(attackedItem instanceof WaterItem)
        {
            result = MoveResults.Miss;
            attackedItem.GotHit();
        }
        else if (attackedItem instanceof BattleShip)
        {
            result = MoveResults.Hit;
            attackedItem.GotHit();
            attackedPlayer.getBoard()[x][y] = new ShipRemains(x, y);
            if(attackedItem.IsDestroyed()){
                result = MoveResults.Drowned;
                attackedPlayer.ShipDrowned();
            }
            currentPlayer.AddScore(1);
            if(attackedPlayer.IsPlayerDestroyed()) {
                winnerPlayer = currentPlayer;
                status = GameStatus.OVER;
            }
        }
        return result;
    }
}
