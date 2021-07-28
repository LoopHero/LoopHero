package unsw.loopmania.items;

import unsw.loopmania.Item;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Potion extends Item {
    public Potion(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);

        Image icon = new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString());
        setIcon(icon);
    }
}
