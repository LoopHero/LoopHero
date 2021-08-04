package unsw.loopmania.items;

import unsw.loopmania.Item;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class DoggieCoin extends Item {
    String ImageUrl = "src/images/doggiecoin.png";
    String ToolTipString = "DoggieCoin\nA revolutionary asset type, which randomly fluctuates in sellable price to an extraordinary extent.\n Can sell at shop";

    public DoggieCoin(String name) {
        super(name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

    public DoggieCoin(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }
}
