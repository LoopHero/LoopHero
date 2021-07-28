package unsw.loopmania.items;

import unsw.loopmania.Item;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Sword extends Item {
    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);

        Image icon = new Image((new File("src/images/basic_sword.png")).toURI().toString());
        setIcon(icon);
    }
}
