package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.Card;

/**
 * represents a ZombiePitCard in the backend game world
 */
public class ZombiePitCard extends Card {
    public ZombiePitCard(SimpleIntegerProperty x, SimpleIntegerProperty y, String type) {
        super(x, y, type);
        setNonPath(true);
        Image icon = new Image((new File("src/images/zombie_pit_card.png")).toURI().toString());
        setImage(icon);
        setTooltip(
                "Zombie Pit\nProduces zombies every cycle of the path completed by the Character\n spawning nearby on the path");

    }
}
