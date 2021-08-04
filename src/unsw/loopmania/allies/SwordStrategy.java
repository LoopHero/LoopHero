package unsw.loopmania.allies;

import unsw.loopmania.enemies.*;
import java.io.Serializable;
/**
 * SwordStrategy implements a type of AttackStrategy;
 * It allows damage dealing to Basic Enemy
 */
public class SwordStrategy implements AttackStrategy, Serializable{

    /**
     * Attack with Sword (dmg = 15), upon having campfire buff, damage is doubled
     * @param Character c
     * @param BasicEnemy s
     * @param int rd
     * @param Boolean buffCampfire
     */
    public void attack(Character c, BasicEnemy s, int rd, Boolean buffCampfire) {
        int atk = 15;
        if (buffCampfire) {
            atk *= 2;
        }
        s.takingDamage(atk);
        System.out.println("Player deals " + atk + " dmg with Sword");
    };
}
