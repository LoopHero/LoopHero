package unsw.loopmania.enemies;

import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import unsw.loopmania.LoopManiaWorld;
import java.io.Serializable;

/**
 * AttackStrategy for Vampire
 */
public class VampireStrategy implements MonsterStrategy, Serializable{

    /**
     * AttackStrategy for Vampire on Character;
     * Having a random chance of inflicting a VampireBite debuff
     * lasting for a random number of turns, making player taking
     * random amount of additional damage.
     */
    public void attack(Character c, int atk, int rd, LoopManiaWorld w){
        System.out.println("Rolled chance = " + rd);
        int chance = 20;
        if (c.ifShield()){
            chance = 8;
        }
        if (rd >= chance && !c.ifBitten()){
            System.out.println("Vampire hits Player!!!!!");
            c.takingDamage(atk);
        } else if (rd < 20) {
            // crit
            // additional 10~20 damage per hit
            System.out.println("Vampire hits Player a CRIT!!!!!");
            c.vampireBitten(rd % 4 + 2);
            System.out.println("Scar remains for " + c.getBitten() + " hits");
        }
        if (c.ifBitten()){
            System.out.println("Player taking additional damage due to CRIT!!!!!");
            c.takingDamage(atk + rd % 11 + 10);
        }
    }

    /**
     * AttackStrategy for Vampire on Sodlier;
     * Having a random chance of inflicting a VampireBite debuff
     * lasting for a random number of turns, making player taking
     * random amount of additional damage.
     */
    public void attack(Soldier s, int atk, int rd, LoopManiaWorld w){
        // Random rdd = new Random(rd);
        if (rd >= 20 && !s.ifBitten()){
            System.out.println("Vampire attacked Soldier !!!");
            s.takingDamage(atk);
        } else if (rd < 20){
            // crit
            // additional 10~20 damage per hit
            System.out.println("Vampire infects Soldier !!!");
            s.vampireBitten(rd % 4 + 2);
        }
        if (s.ifBitten()){
            System.out.println("Vampire attacked Soldier for additional damage !!!");
            s.takingDamage(atk + rd % 11 + 10);
        }
    }
}
