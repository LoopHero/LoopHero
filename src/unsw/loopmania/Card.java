package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * a Card in the world which doesn't move
 */
public abstract class Card extends StaticEntity {
    private String type;
    private boolean on_path = false;
    private boolean on_non_path = false;
    private Image icon;

    public Card(SimpleIntegerProperty x, SimpleIntegerProperty y, String type) {
        super(x, y);
        this.type = type;
    }
    
    /**
     * Bind icon with card
     * @param icon
     */
    public void setImage(Image icon) {
        this.icon = icon;
    }

    /**
     * returns stored icon
     * @return
     */
    public Image getImage() {
        return this.icon;
    }

    /**
     * Assert if card is on path tile
     * @return
     */
    public boolean checkPath() {
        return this.on_path;

    }

    /**
     * set on_path for current card
     * @param path
     */
    public void setPath(boolean path) {
        this.on_path = path;
    }

    /**
     * Assert if card is on non-path tile
     * @return
     */
    public boolean checkNonPath() {
        return this.on_non_path;
    }

    /**
     * set non_path for current card
     * @param path
     */
    public void setNonPath(boolean path) {
        this.on_non_path = path;
    }

    /**
     * Returns type for this card object
     * @return
     */
    public String getType() {
        return this.type;
    }
}
