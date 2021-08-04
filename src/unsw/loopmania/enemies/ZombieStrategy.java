package unsw.loopmania.enemies;

import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import unsw.loopmania.LoopManiaWorld;
import java.io.Serializable;

/**
 * AttackStrategy for Zombie
 */
public class ZombieStrategy implements MonsterStrategy, Serializable{

    /**
     * AttackStrategy for Zombie on Character
     */
    public void attack(Character c, int atk, int rd, LoopManiaWorld w){
        System.out.println("Zombie scratches Player!!!!!");
        c.takingDamage(atk);
    }

    /**
     * AttackStrategy for Zombie on Soldier;
     * Having a 80% chance to instantly kill a soldier;
     * and spawn a zombie at character position, which
     * joins battle immediately
     */
    public void attack(Soldier s, int atk, int rd, LoopManiaWorld w){
        if (rd < 20){
            s.takingDamage(atk);
        } else {
            // instant kill soldier, and 
            System.out.println("Zombie converted Soldier to Zombie!");
            s.takingDamage(9999);
            w.spawnZombieOnSoldier();
        }
    }
}
