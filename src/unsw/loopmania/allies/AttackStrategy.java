package unsw.loopmania.allies;

import unsw.loopmania.enemies.*;

/**
 * A interface for different offending Strategy pattern, 
 * which describe how damage and effects are applied.
 */
public interface AttackStrategy {
    public void attack(Character c, BasicEnemy s, int rd, Boolean buffCampfire);
}
