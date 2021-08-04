package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.Card;

/**
 * represents a CampfireCard in the backend game world
 */
public class CampfireCard extends Card {
    public CampfireCard(SimpleIntegerProperty x, SimpleIntegerProperty y, String type) {
        super(x, y, type);
        setNonPath(true);
        Image icon = new Image((new File("src/images/campfire_card.png")).toURI().toString());
        setImage(icon);
        setTooltip("Campfire\nCharacter deals double damage \nwithin campfire battle radius");

    }
}
