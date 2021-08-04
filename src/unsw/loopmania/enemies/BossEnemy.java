package unsw.loopmania.enemies;

import unsw.loopmania.PathPosition;
import unsw.loopmania.LoopManiaWorld;

public abstract class BossEnemy extends BasicEnemy {
    
    public BossEnemy(PathPosition position) {
        super(position);
    }

    /**
     * After defeated, a world flag will be triggered to 
     * allow special events to happen
     * @param w
     */
    public abstract void triggerDefeatFlag(LoopManiaWorld w);

    /**
     * Assert if current boss can spawn based on loop completed
     * by character; doggie -> 20; Elon -> 40;
     * @param w
     */
    public abstract Boolean canSpawn(LoopManiaWorld w);

    public abstract void presenceAura(LoopManiaWorld w);

}
