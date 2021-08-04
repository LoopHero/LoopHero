package unsw.loopmania.enemies;

import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import unsw.loopmania.LoopManiaWorld;
import java.io.Serializable;

/**
 * AttackStrategy for Slug
 */
public class SlugStrategy implements MonsterStrategy, Serializable{

    /**
     * AttackStrategy for Slug on Character
     */
    public void attack(Character c, int atk, int rd, LoopManiaWorld w){
        System.out.println("Slug tackles Player!!!!!");
        c.takingDamage(atk);
    }

    /**
     * AttackStrategy for Slug on Soldier
     */
    public void attack(Soldier s, int atk, int rd, LoopManiaWorld w){
        s.takingDamage(atk);
    }
}
