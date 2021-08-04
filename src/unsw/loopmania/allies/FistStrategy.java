package unsw.loopmania.allies;

import unsw.loopmania.enemies.*;
import java.io.Serializable;
/**
 * FistStrategy implements a type of AttackStrategy;
 * It allows damage dealing to Basic Enemy
 */
public class FistStrategy implements AttackStrategy, Serializable {
    
    /**
     * Attack with fist (dmg = 5), upon having campfire buff, damage is doubled
     * @param Character c
     * @param BasicEnemy s
     * @param int rd
     * @param Boolean buffCampfire
     */
    public void attack(Character c, BasicEnemy s, int rd, Boolean buffCampfire) {
        int atk = 5;
        if (buffCampfire) {
            atk *= 2;
        }
        s.takingDamage(atk);
        System.out.println("Player deals " + atk + " dmg with Fist");
    };
}
