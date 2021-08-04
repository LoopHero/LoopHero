package unsw.loopmania.items;


import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Rapier extends RareItem {
    String ImageUrl = "src/images/anduril_flame_of_the_west.png";
    String ToolTipString = "Anduril, Flame of the West\nA very high damage sword which causes triple damage against bosses";

    RareItem hidden;

    public Rapier(String name, int rd) {
        super(name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
        hidden = null;
        setHidden(rd);
    }

    public Rapier(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

    public void setHidden(int rd) {
        if (rd == 0){
            hidden = new TheOneRing("TheOneRing", -1);
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
