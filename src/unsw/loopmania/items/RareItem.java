package unsw.loopmania.items;

import unsw.loopmania.Item;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class RareItem extends Item {

    public RareItem(String name) {
        super(name);
    }

    public RareItem(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
    }

    /**
     * Set the hidden property of a rare item, based on a random number
     * rd = Random.nextInt(2);
     * Ranges from 0 to 1;
     * let rd = -1 to negate the effect;
     * @param rd
     */
    public abstract void setHidden(int rd);

    /**
     * returns the hidden RareItem type of current rare item
     * @return
     */
    public abstract RareItem getHidden();
}
