package unsw.loopmania.items;

import unsw.loopmania.Item;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Staff extends Item {
    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);

        Image icon = new Image((new File("src/images/staff.png")).toURI().toString());
        setIcon(icon);
    }
}
