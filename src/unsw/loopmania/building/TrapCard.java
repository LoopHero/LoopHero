package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.Card;

/**
 * represents a TrapCard in the backend game world
 */
public class TrapCard extends Card {
    public TrapCard(SimpleIntegerProperty x, SimpleIntegerProperty y, String type) {
        super(x, y, type);
        setPath(true);
        Image icon = new Image((new File("src/images/trap_card.png")).toURI().toString());
        setImage(icon);
    }
}
