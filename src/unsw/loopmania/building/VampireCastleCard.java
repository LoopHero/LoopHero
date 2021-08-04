package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.Card;

/**
 * represents a vampire castle card in the backend game world
 */
public class VampireCastleCard extends Card {
    public VampireCastleCard(SimpleIntegerProperty x, SimpleIntegerProperty y, String type) {
        super(x, y, type);
        setNonPath(true);
        Image icon = new Image((new File("src/images/vampire_castle_card.png")).toURI().toString());
        setImage(icon);
        setTooltip(
                "Vampire castle\nProduces vampires every 5 cycles of the path completed by the Character,\n spawning nearby on the path");
    }
}
