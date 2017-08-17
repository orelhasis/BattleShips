package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.PlayerName;

public class Player {

    /* -------------- Data members -------------- */
    private PlayerName name;
    private int boardSize;
    private int score;
    private SeaItem[][] board;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public SeaItem[][] getBoard() {
        return board;
    }

    public void setBoard(SeaItem[][] board) {
        this.board = board;
    }


    /* -------------- Function members -------------- */
    public Player(PlayerName name, int boardSize) {

        this.name = name;
        this.boardSize = boardSize;
        this.score = 0;
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
