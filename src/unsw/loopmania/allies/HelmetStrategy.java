package unsw.loopmania.allies;
import java.io.Serializable;
/**
 * HelmetStrategy implements a type of DefenseStrategy;
 * It allows damage mitigation
 */
public class HelmetStrategy implements DefenseStrategy, Serializable{

    /**
     * Reduce damage taken by a fixed amount, damage taken is always >= 0;
     * e.g.:
     * 6 -> 1;
     * 4 -> 0;
     * @param int damage
     */
    public int defence(int dmg){
        System.out.println("Player dmg taken reduced by helmet to " + (dmg > 5 ? (dmg - 5) : 0));
        return dmg > 5 ? (dmg - 5) : 0;
    }
}
