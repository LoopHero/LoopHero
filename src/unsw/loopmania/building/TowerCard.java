package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.Card;

/**
 * represents a TowerCard in the backend game world
 */
public class TowerCard extends Card {
    public TowerCard(SimpleIntegerProperty x, SimpleIntegerProperty y, String type) {
        super(x, y, type);
        setNonPath(true);
        Image icon = new Image((new File("src/images/tower_card.png")).toURI().toString());
        setImage(icon);
    }
}
