package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * The moving entity
 */
public abstract class MovingEntity extends Entity {

    /**
     * object holding position in the path
     */
    private PathPosition position;

    /**
     * Create a moving entity which moves up and down the path in position
     * @param position represents the current position in the path
     */
    public MovingEntity(PathPosition position) {
        super();
        this.position = position;
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
    //  * check closest way to get the goal position
    //  */
    // public int downOrUp(Pair<Integer, Integer> goalPosition) {
    //     int downPathLength = position.calculateDownPath(goalPosition);
    //     int upPathLength = position.calculateUpPath(goalPosition);
    //     if (downPathLength > upPathLength)
    //         return 0;
    //     else
    //         return 1;
    // }

    /**
     * Returns x position of Moving Entity
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty x() {
        return position.getX();
    }

    /**
     * Returns y position of Moving Entity
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty y() {
        return position.getY();
    }

    /**
     * Returns x position of Moving Entity
     * @return int
     */
    public int getX() {
        return x().get();
    }

    /**
     * Returns y position of Moving Entity
     * @return int
     */
    public int getY() {
        return y().get();
    }

    /**
     * returns the position index of Moving entity on PathTile
     * @param position
     */
    public void setPosition(PathPosition position) {
        this.position = position;
    }
}
