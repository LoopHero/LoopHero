package unsw.loopmania.allies;

import java.lang.Math;
import java.io.Serializable;
/**
 * ArmourStrategy implements a type of DefenseStrategy;
 * It allows damage mitigation
 */
public class ArmourStrategy implements DefenseStrategy, Serializable {

    /**
     * Reduce damage taken to half (50%), damage taken is rounded up to Integer;
     * e.g.:
     * 6 -> 3;
     * 5 -> 3;
     * @param int damage
     */
    public int defence (int dmg){
        System.out.println("Player dmg taken reduced by armour to " + (int) Math.round(0.5*dmg));
        return (int) Math.round(0.5*dmg);
    }
}
