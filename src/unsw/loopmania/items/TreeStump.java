package unsw.loopmania.items;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class TreeStump extends RareItem {
    String ImageUrl = "src/images/tree_stump.png";
    String ToolTipString = "Tree Stump\nAn especially powerful shield, which provides higher defence against bosses";

    RareItem hidden;

    public TreeStump(String name, int rd) {
        super(name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
        hidden = null;
        setHidden(rd);
    }

    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y, name);
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setIcon(icon);
        setTooltip(ToolTipString);
    }

    public void setHidden(int rd) {
        if (rd == 0){
            hidden = new TheOneRing("TheOneRing", -1);
        } else if (rd == 1){
            hidden = new Rapier("Rapier", -1);
        } else {
            hidden = null;
        }
    }

    public RareItem getHidden() {
        return hidden;
    }
}
