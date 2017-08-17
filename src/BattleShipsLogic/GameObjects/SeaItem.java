package BattleShipsLogic.GameObjects;

public abstract class SeaItem {

    /* -------------- Data members -------------- */

    private Point position;

    private char itemChar;

    /* -------------- Getters and setters -------------- */
    SeaItem(int x, int y) {
        this.position = new Point(x,y);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public char getItemChar() {
        return itemChar;
    }

    public void setItemChar(char itemChar) {
        this.itemChar = itemChar;
    }
}
