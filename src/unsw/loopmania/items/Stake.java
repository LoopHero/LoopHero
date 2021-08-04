package unsw.loopmania.items;

import unsw.loopmania.Item;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Stake extends Item {
    String ImageUrl = "src/images/stake.png";
    String ToolTipString = "Stake\nA melee weapon with lower stats than the sword, \nbut causes very high damage to vampires";

    public Stake(String name) {
        super(name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

}
