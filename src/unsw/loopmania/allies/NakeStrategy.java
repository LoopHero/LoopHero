package unsw.loopmania.allies;
import java.io.Serializable;
/**
 * NakeStrategy implements a type of DefenseStrategy;
 * It works as a place holder while no armour is worn;
 */
public class NakeStrategy implements DefenseStrategy, Serializable{

    /** 
     * damage taken is not reduced for this method
     */
    public int defence (int dmg){
        return dmg;
    }
}
