package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * HeroCastleBuilding in the world
 */
public class HeroCastleBuilding extends Building {
    public HeroCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y, type, moves);
        Image icon = new Image((new File("src/images/heros_castle.png")).toURI().toString());
        setImage(icon);
    }
}
