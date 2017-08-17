package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.ShipDirection;
import BattleShipsLogic.Definitions.ShipType;

public class BattleShip extends SeaItem {

    /* -------------- Data members -------------- */

    private ShipDirection direction;
    private ShipType type;
    private int length;

    /* -------------- Getters and setters -------------- */

    public ShipDirection getDirection() {
        return direction;
    }

    public void setDirection(ShipDirection direction) {
        this.direction = direction;
    }

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /* -------------- Function members -------------- */

    public BattleShip(ShipDirection direction, ShipType type, int length, int x, int y) {
        super(x,y);
        this.direction=direction;
        this.type=type;
        this.length=length;

        if(type == ShipType.shipTypeA)
        {
            setItemChar('A');
        }
        else
        {
            setItemChar('B');
        }
    }

}
