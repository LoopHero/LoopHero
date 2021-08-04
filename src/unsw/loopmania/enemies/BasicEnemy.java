package unsw.loopmania.enemies;

import org.javatuples.Pair;

import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.PathPosition;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import unsw.loopmania.building.*;

/**
 * a basic form of enemy in the world
 */
public abstract class BasicEnemy extends MovingEntity {
    // TODO = modify this, and add additional forms of enemy
    public List<Pair<Integer, Integer>> OrderedPath;

    public BasicEnemy(PathPosition position) {
        super(position);
    }

    /**
     * Enemy movement method place holder
     */
    public abstract void move(Pair<Integer, Integer> characterPos, List<Building> buildingEntities);

    /**
     * Import OrderedPath from world
     * 
     * @param OrderedPath
     */
    public void setOrderedPath(List<Pair<Integer, Integer>> OrderedPath) {
        this.OrderedPath = OrderedPath;
    };

    /**
     * Check if a pair of coordinates is in OrderedPath
     * 
     * @param x
     * @param y
     * @return
     */
    public Boolean inOrderedPath(int x, int y) {
        return OrderedPath.contains(new Pair<Integer, Integer>(x, y));
    }

    /**
     * returns neighbour postion (in OrderedPath) of a selected tile
     * 
     * @param x
     * @param y
     * @return
     */
    public List<Pair<Integer, Integer>> neighboursPoses(int x, int y) {
        List<Pair<Integer, Integer>> neighbours = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0)
                    continue;
                Integer newX = (x + i);
                Integer newY = (y + j);
                if (inOrderedPath(newX, newY)) {
                    neighbours.add(new Pair<Integer, Integer>(newX, newY));
                }
            }
        }
        return neighbours;
    }

    public abstract SimpleIntegerProperty getHealth();

    public abstract void setHealth(Integer newHealth);

    public abstract SimpleStringProperty getType();

    // added methods place holder for sub classes

    public abstract int getBattleRadius();

    public abstract int getSupportRadius();

    public abstract void attack(Character c, int rd, LoopManiaWorld w);

    public abstract void attack(Soldier s, int rd, LoopManiaWorld w);

    public abstract void attack(BasicEnemy e, int rd, LoopManiaWorld w);

    public abstract void setTrance(int t);

    public abstract void reduceTrance();

    public abstract Boolean ifTrance();

    public abstract void takingDamage(int dmg);

    public abstract Boolean ifDead();

}
