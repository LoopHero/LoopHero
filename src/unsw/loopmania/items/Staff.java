package unsw.loopmania.items;

import unsw.loopmania.Item;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Staff extends Item {
    String ImageUrl = "src/images/staff.png";
    String ToolTipString = "Staff\nA melee weapon with very low stats (lower than both the sword and stake), \nwhich has a random chance of inflicting a trance, \nwhich transforms the attacked enemy into an allied soldier temporarily (and fights alongside the Character). \nIf the trance ends during the fight, the affected enemy reverts back to acting as an enemy which fights the Character. \nIf the fight ends whilst the enemy is in a trance, the enemy dies";

    public Staff(String name) {
        super(name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

}
