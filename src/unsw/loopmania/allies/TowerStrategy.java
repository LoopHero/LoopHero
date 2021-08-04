package unsw.loopmania.allies;

import unsw.loopmania.enemies.*;
import java.io.Serializable;
/**
 * TowerStrategy implements a type of AttackStrategy;
 * It allows damage dealing to Basic Enemy, with special effect;
 */
public class TowerStrategy implements AttackStrategy, Serializable{

    /**
     * Attack Basic Enemy by Tower with a uniform 2 dmg
     * @param Character c
     * @param BasicEnemy s
     * @param int rd
     * @param Boolean buffCampfire
     */
    public void attack(Character c, BasicEnemy s, int rd, Boolean buffCampfire) {
        int atk = 2;
        s.takingDamage(atk);
        System.out.println("Tower deals " + atk + " dmg");
    };
}
