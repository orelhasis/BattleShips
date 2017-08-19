package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.PlayerName;

public class Player {

    /* -------------- Data members -------------- */
    private PlayerName name;
    private int boardSize;
    private SeaItem[][] board;
    private PlayerStatistics statistics = new PlayerStatistics();

    /* -------------- Getters and setters -------------- */

    public PlayerName getName() {
        return name;
    }

    public void setName(PlayerName name) {
        this.name = name;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public SeaItem[][] getBoard() {
        return board;
    }

    public void setBoard(SeaItem[][] board) {
        this.board = board;
    }

    public PlayerStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(PlayerStatistics statistics) {
        this.statistics = statistics;
    }

    /* -------------- Function members -------------- */
    public Player(PlayerName name, int boardSize) {

        this.name = name;
        this.boardSize = boardSize;
        setEmptyGrid();
    }

    private void setEmptyGrid() {
        this.board = new SeaItem[boardSize][boardSize];
        int i, j;
        for (i = 0; i < boardSize; i++) {
            for (j = 0; j < boardSize; j++) {
                board[i][j] = new WaterItem(i + 1, j + 1);
            }
        }
    }

    public char[][] getPlayerPrimaryGrid() {

        char[][] primary = getEmptyBoardForPrint(boardSize);

        int i,j;
        for (i = 0; i < boardSize; i++) {
            for (j = 0; j < boardSize; j++) {
                if(board[i][j]!=null)
                {
                    primary[i+1][j+1] = board[i][j].getItemChar();
                }
                else
                {
                    primary[i+1][j+1] = 'X';
                }
            }
        }

        return primary;
    }

    public char[][] getPlayerTrackingGrid() {

        char[][] tracking = getEmptyBoardForPrint(boardSize);

        int i,j;
        for (i = 0; i < boardSize; i++) {
            for (j = 0; j < boardSize; j++) {
                if(board[i][j]!=null)
                {
                    tracking[i+1][j+1] = ' ';
                }
                else
                {
                    tracking[i+1][j+1] = 'X';
                }
            }
        }

        return tracking;
    }

    private char[][] getEmptyBoardForPrint(int boardSize) {

        char[][] newBoard = new char[boardSize + 1][boardSize + 1];
        char currentNumber = '1', currentLetter = 'A';
        int i, j;

        newBoard[0][0] = ' ';

        for (i = 1; i < boardSize + 1; i++) {
            newBoard[0][i] = currentLetter;
            currentLetter+=1;
        }

        for (i = 1; i < boardSize + 1; i++) {
            newBoard[i][0] = currentNumber;
            currentNumber+=1;
        }

        return newBoard;
    }
}
