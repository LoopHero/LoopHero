package unsw.loopmania.allies;

import unsw.loopmania.enemies.*;

/**
 * StaffStrategy implements a type of AttackStrategy;
 * It allows damage dealing to Basic Enemy, with special effect;
 */
public class StaffStrategy implements AttackStrategy {

    /**
     * Attack with Staff (dmg = 2), upon having campfire buff, damage is doubled;
     * Having a 15% chance of converting hit enemy into a trace for 5 attack turns;
     * Upon battle ends, trance enemy is instantly killed.
     * @param Character c
     * @param BasicEnemy s
     * @param int rd
     * @param Boolean buffCampfire
     */
    public void attack(Character c, BasicEnemy s, int rd, Boolean buffCampfire) {
        int atk = 2;
        int chance = 85;
        if (buffCampfire) {
            atk *= 2;
        }
        s.takingDamage(atk);
        System.out.println("Player deals " + atk + " dmg with Staff");
        if (rd >= chance) {
            System.out.println("Player accidentally triggers Staff Magic !!!");
            s.setTrance(5);
            c.addToTrances(s);
        }
    }
}
