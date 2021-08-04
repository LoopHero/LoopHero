package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * HeroCastleBuilding in the world
 */
public class HeroCastleBuilding extends Building {
    String ImageUrl = "src/images/heros_castle.png";
    String ToolTipString = "Hero Castle\nwhen the Character enters this castle, \nthe Human Player is offered a window to purchase items at the Hero's Castle";

    public HeroCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y, type, moves);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setImage(icon);
        setTooltip(ToolTipString);
    }
}
