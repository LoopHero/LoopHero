package unsw.loopmania.allies;

/**
 * NakeStrategy implements a type of DefenseStrategy;
 * It works as a place holder while no armour is worn;
 */
public class NakeStrategy implements DefenseStrategy {

    /** 
     * damage taken is not reduced for this method
     */
    public int defence (int dmg){
        return dmg;
    }
}
