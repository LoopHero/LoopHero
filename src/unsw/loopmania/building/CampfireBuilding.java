package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * CampfireBuilding in the world
 */
public class CampfireBuilding extends Building {
    private int radius;

    /**
     * Returns the effect radius for CampfireBuilding
     * 
     * @return int radius
     */
    public int getRadius() {
        return this.radius;
    }

    public CampfireBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y, type, moves);
        this.radius = 3;
        Image icon = new Image((new File("src/images/campfire.png")).toURI().toString());
        setImage(icon);
        setTooltip("Campfire\nCharacter deals double damage \nwithin campfire battle radius");
    }
}
