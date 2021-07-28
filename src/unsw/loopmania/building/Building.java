package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.StaticEntity;

/**
 * a basic form of building in the world
 */
public class Building extends StaticEntity {
    // TODO = add more types of building, and make sure buildings have effects on
    // entities as required by the spec
    private String type;
    private int moves;
    private Image icon;

    /**
     * Bind building with icon
     * @param icon
     */
    public void setImage(Image icon) {
        this.icon = icon;
    }

    /**
     * Returns the icon of this class
     * @return
     */
    public Image getImage() {
        return this.icon;
    }

    /**
     * Initiate a Building object
     * @param x
     * @param y
     * @param type
     * @param moves
     */
    public Building(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y);
        this.type = type;
        this.moves = moves;
    }

    /**
     * Placeholder for new Building
     * @see timeUpdate(LoopManiaWorld w) in sub class
     * @param w
     */
    public void timeUpdate(LoopManiaWorld w) {
    }

    /**
     * return the type (String name) of this building
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * return the moves taken since this building is deployed
     * @return
     */
    public int getMoves() {
        return this.moves;
    }

    /**
     * Increment moves count for building
     */
    public void addMoves() {
        this.moves++;
    }

    /**
     * Assert if the position dragged to is taken or not
     * @param cardNodeX
     * @param cardNodeY
     * @param buildingNodeX
     * @param buildingNodeY
     * @return
     */
    public Boolean positionTaken(int buildingNodeX, int buildingNodeY) {
        if (!this.shouldExist().get()) {
            return false;
        }
        int buildingX = this.getX();
        int buildingY = this.getY();
        if (buildingX == buildingNodeX && buildingY == buildingNodeY) {
            return true;
        }
        return false;
    }
}
