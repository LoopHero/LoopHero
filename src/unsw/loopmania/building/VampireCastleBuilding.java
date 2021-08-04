package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * VampireCastleBuilding in the world
 */
public class VampireCastleBuilding extends Building {
    // TODO = add more types of building, and make sure buildings have effects on
    // entities as required by the spec

    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y, type, moves);
        Image icon = new Image(
                (new File("src/images/vampire_castle_building_purple_background.png")).toURI().toString());
        setImage(icon);
        setTooltip(
                "Vampire castle\nProduces vampires every 5 cycles of the path completed by the Character,\n spawning nearby on the path");

    }
}
