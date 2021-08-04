package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents an equipped or unequipped armour in the backend world
 */
public class Item extends StaticEntity {
    private String name;
    private transient Image icon;

    public Item(String name) {
        this.name = name;
    }

    public Item(SimpleIntegerProperty x, SimpleIntegerProperty y, String name) {
        super(x, y);
        this.name = name;
    }

    /**
     * Returns the name of Item
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Bind icon with Item
     * 
     * @param icon
     */
    public void setIcon(Image icon) {
        this.icon = icon;
    }

    /**
     * Returns binded icon on Item
     * 
     * @return
     */
    public Image getIcon() {
        return this.icon;
    }
}
