package unsw.loopmania.enemies;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import java.io.Serializable;

public class ElonStrategy implements MonsterStrategy, Serializable{
    public void attack(Character c, int atk, int rd, LoopManiaWorld w){
        c.takingDamage(atk);
        if (c.ifTreeStump()){
            atk -= 20;
        }
        System.out.println("Elon uses BitCoin Flare on Character !!!");
    }

    public void attack(Soldier s, int atk, int rd, LoopManiaWorld w){
        s.takingDamage((int) atk/10);
        System.out.println("Elon damages Soldiers with unkown weaken AOE effect !!!");
    }
}
