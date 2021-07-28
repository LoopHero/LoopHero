package unsw.loopmania.allies;

import unsw.loopmania.enemies.*;

/**
 * StakeStrategy implements a type of AttackStrategy;
 * It allows damage dealing to Basic Enemy, with special effect;
 */
public class StakeStrategy implements AttackStrategy {

    /**
     * Attack with Stake (dmg = 5), upon having campfire buff, damage is doubled;
     * Attack is further increased (*10) while attacking vampire
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
        if (s instanceof Vampire){
            atk *= 10;
            System.out.println("Stake is supper effective against Vampire !!!");
        }
        s.takingDamage(atk);
        System.out.println("Player deals " + atk + " dmg with Stake");
    }
}
