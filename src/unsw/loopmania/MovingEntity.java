package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * The moving entity
 */
public abstract class MovingEntity extends Entity {

    /**
     * object holding position in the path
     */
    private PathPosition position;

    /**
     * The tool Tip for this Entity
     */
    private String toolTip;
    private transient Image icon;

    /**
     * Bind building with icon
     * 
     * @param icon
     */
    public void setImage(Image icon) {
        this.icon = icon;
    }

    /**
     * Returns the icon of this class
     * 
     * @return
     */
    public Image getImage() {
        return this.icon;
    }

    /**
     * Create a moving entity which moves up and down the path in position
     * 
     * @param position represents the current position in the path
     */
    public MovingEntity(PathPosition position) {
        super();
        this.position = position;
        this.toolTip = "";
    }

    /**
     * move clockwise through the path
     */
    public void moveDownPath() {
        position.moveDownPath();
    }

    /**
     * move anticlockwise through the path
     */
    public void moveUpPath() {
        position.moveUpPath();
    }

    // /**
    // * check closest way to get the goal position
    // */
    // public int downOrUp(Pair<Integer, Integer> goalPosition) {
    // int downPathLength = position.calculateDownPath(goalPosition);
    // int upPathLength = position.calculateUpPath(goalPosition);
    // if (downPathLength > upPathLength)
    // return 0;
    // else
    // return 1;
    // }

    /**
     * Returns x position of Moving Entity
     * 
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty x() {
        return position.getX();
    }

    /**
     * Returns y position of Moving Entity
     * 
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty y() {
        return position.getY();
    }

    /**
     * Returns x position of Moving Entity
     * 
     * @return int
     */
    public int getX() {
        return x().get();
    }

    /**
     * Returns y position of Moving Entity
     * 
     * @return int
     */
    public int getY() {
        return y().get();
    }

    /**
     * returns the position index of Moving entity on PathTile
     * 
     * @param position
     */
    public void setPosition(PathPosition position) {
        this.position = position;
    }

    /**
     * set the String of tool tip
     * 
     * @param tip
     */
    public void setTooltip(String tip) {
        this.toolTip = tip;
    }

    /**
     * return the tooltip in this entity
     * 
     * @return tooltip
     */
    public String getTooltip() {
        return this.toolTip;
    }
}
