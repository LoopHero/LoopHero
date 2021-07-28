package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * Barracks Building in the world
 */
public class BarracksBuilding extends Building {
    public BarracksBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y, type, moves);
        Image icon = new Image((new File("src/images/barracks.png")).toURI().toString());
        setImage(icon);
    }
}
