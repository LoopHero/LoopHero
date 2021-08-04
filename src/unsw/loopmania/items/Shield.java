package unsw.loopmania.items;

import unsw.loopmania.Item;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Shield extends Item {
    String ImageUrl = "src/images/shield.png";
    String ToolTipString = "Shield\nDefends against enemy attacks,\ncritical vampire attacks have a 60% lower chance of occurring";

    public Shield(String name) {
        super(name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

}
