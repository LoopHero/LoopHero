package unsw.loopmania;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a non-moving entity unlike the moving entities, this can be placed
 * anywhere on the game map
 */
public abstract class StaticEntity extends Entity {
    /**
     * x and y coordinates represented by IntegerProperty, so ChangeListeners can be
     * added
     */
    private transient IntegerProperty x, y;
    public String toolTip;

    public StaticEntity() {
    }

    public StaticEntity(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super();
        this.x = x;
        this.y = y;
        this.toolTip = "";
    }

    public IntegerProperty x() {
        return x;
    }

    public IntegerProperty y() {
        return y;
    }

    public int getX() {
        return x().get();
    }

    public int getY() {
        return y().get();
    }

    /**
     * set the String of tool tip
     * 
     * @param tip
     */
    public void setTooltip(String tip) {
        this.toolTip = tip;
    }

    /**
     * return the tooltip in this entity
     * 
     * @return tooltip
     */
    public String getTooltip() {
        return this.toolTip;
    }
}
