package unsw.loopmania.items;

import unsw.loopmania.Item;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Armour extends Item {
    String ImageUrl = "src/images/armour.png";
    String ToolTipUrl = "Armour\nBody armour, provides defence and halves enemy attack";

    public Armour(String name) {
        super(name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipUrl);
    }

    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipUrl);
    }
}
