package unsw.loopmania.items;

import unsw.loopmania.Item;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Helmet extends Item {
    String ImageUrl = "src/images/helmet.png";
    String ToolTipString = "Helmet\nDefends against enemy attacks, enemy attacks are reduced by a scalar value.\nThe damage inflicted by the Character against enemies is reduced (since it is harder to see)";

    public Helmet(String name) {
        super(name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }
}
