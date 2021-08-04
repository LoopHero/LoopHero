package unsw.loopmania.building;

import java.io.File;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.BasicEnemy;

/**
 * TrapBuilding in the world
 */
public class TrapBuilding extends Building {
    private final int TRAP_DAMAGE = 5;

    public TrapBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type, int moves) {
        super(x, y, type, moves);
        Image icon = new Image((new File("src/images/trap.png")).toURI().toString());
        setImage(icon);
        setTooltip(
                "Trap\nWhen an enemy steps on a trap, the enemy is damaged \n(and potentially killed if it loses all health) and the trap is destroyed");

    }

    /**
     * Check trap collition with enemy
     */
    public void timeUpdate(LoopManiaWorld w) {
        if (!this.shouldExist().get()) {
            return;
        }
        List<BasicEnemy> enemies = w.getEnemies();
        for (BasicEnemy enemy : enemies) {
            int enemyX = enemy.getX();
            int enemyY = enemy.getY();
            if (this.getX() == enemyX && this.getY() == enemyY) {
                int health = enemy.getHealth().get();
                enemy.setHealth(health - TRAP_DAMAGE);
                this.destroy();
                System.out.println("A Trap is Destroyed");
                return;
            }
        }
    }
}
