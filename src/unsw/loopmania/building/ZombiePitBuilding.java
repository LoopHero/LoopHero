package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * ZombiePitBuilding in the world
 */
public class ZombiePitBuilding extends Building {

    public ZombiePitBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y, type, moves);
        Image icon = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        setImage(icon);
        setTooltip(
                "Zombie Pit\nProduces zombies every cycle of the path completed by the Character\n spawning nearby on the path");
    }
}
