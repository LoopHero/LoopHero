package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.allies.Character;

/**
 * VillageBuilding in the world
 */
public class VillageBuilding extends Building {
    private final int GAIN_HEALTH_NUMBER = 5;

    public VillageBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y, type, moves);
        Image icon = new Image((new File("src/images/village.png")).toURI().toString());
        setImage(icon);
        setTooltip("Village\nCharacter regains health when passing through");

    }

    /**
     * replenish health of character
     */
    public void timeUpdate(LoopManiaWorld w) {
        Character Character = w.getCharacter();
        int characterX = Character.getX();
        int characterY = Character.getY();
        if (this.getX() == characterX && this.getY() == characterY) {
            Character.setHealth(
                    Math.min(Character.getHealth().get() + GAIN_HEALTH_NUMBER, Character.getMaxHealth().get()));
        }
    }

    // /**
    // * helper function to return the smaller value
    // * @param a
    // * @param b
    // * @return
    // */
    // private int min(int a, int b){
    // if (a <= b){
    // return a;
    // } else {
    // return b;
    // }
    // }

}
