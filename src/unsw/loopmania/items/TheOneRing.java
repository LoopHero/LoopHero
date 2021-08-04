package unsw.loopmania.items;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class TheOneRing extends RareItem {
    String ImageUrl = "src/images/the_one_ring.png";
    String ToolTipString = "The One Ring\nIf the Character is killed, it respawns with full health up to a single time";

    RareItem hidden;

    public TheOneRing(String name, int rd) {
        super(name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
        hidden = null;
        setHidden(rd);
    }

    public TheOneRing(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

    public void setHidden(int rd) {
        if (rd == 0){
            hidden = new Rapier("Rapier", -1);
        } else if (rd == 1){
            hidden = new TreeStump("TreeStump", -1);
        } else {
            hidden = null;
        }
    }

    public RareItem getHidden() {
        return hidden;
    }
}
