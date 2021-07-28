package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.Card;

/**
 * represents a VillageCard in the backend game world
 */
public class VillageCard extends Card {
    public VillageCard(SimpleIntegerProperty x, SimpleIntegerProperty y, String type) {
        super(x, y, type);
        setPath(true);
        Image icon = new Image((new File("src/images/village_card.png")).toURI().toString());
        setImage(icon);
    }
}
