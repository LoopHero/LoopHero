package unsw.loopmania.enemies;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;

/**
 * AttackStrategy for Monsters
 */
public interface MonsterStrategy {
    public void attack(Character c, int atk, int rd, LoopManiaWorld w);

    public void attack(Soldier s, int atk, int rd, LoopManiaWorld w);
}
