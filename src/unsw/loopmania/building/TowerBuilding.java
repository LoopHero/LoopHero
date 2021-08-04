package unsw.loopmania.building;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.allies.AttackStrategy;
import unsw.loopmania.allies.TowerStrategy;
import unsw.loopmania.enemies.BasicEnemy;

/**
 * TowerBuilding in the world
 */
public class TowerBuilding extends Building {
    int radius;
    private AttackStrategy attackMethod;

    /**
     * Trigger tower attacks enemy
     * 
     * @param e
     * @param rd
     */
    public void attack(BasicEnemy e, int rd) {
        attackMethod.attack(null, e, rd, false);
    }

    /**
     * Returns the effect radius for CampfireBuilding
     * 
     * @return int radius
     */
    public int getRadius() {
        return this.radius;
    }

    public TowerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y, type, moves);
        this.radius = 3;
        this.attackMethod = new TowerStrategy();
        Image icon = new Image((new File("src/images/tower.png")).toURI().toString());
        setImage(icon);
        setTooltip("Tower\nDuring a battle within its shooting radius, \nenemies will be attacked by the tower");

    }
}
