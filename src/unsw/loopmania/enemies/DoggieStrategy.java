package unsw.loopmania.enemies;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import java.io.Serializable;

public class DoggieStrategy implements MonsterStrategy, Serializable {
    public void attack(Character c, int atk, int rd, LoopManiaWorld w){
        c.takingDamage(atk);
        if (c.ifTreeStump()){
            atk -= 5;
        }
        System.out.println("Doggie uses FALLEN COMET on Character !!!");
        if (rd < 20){
            System.out.println("Character is stunned !!!");
            c.stun(rd % 2 + 1);
        }
    }

    public void attack(Soldier s, int atk, int rd, LoopManiaWorld w){
        s.takingDamage(atk);
        System.out.println("Doggie uses GIGA IMPACT on Soldier !!!");
        if (rd < 10){
            System.out.println("Soldier is instantly killed !!!");
            s.takingDamage(9999);;
        }
    }
}
