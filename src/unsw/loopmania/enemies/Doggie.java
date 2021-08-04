package unsw.loopmania.enemies;

import java.io.File;
import java.util.*;

import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import unsw.loopmania.building.*;

import org.javatuples.Pair;

import unsw.loopmania.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import unsw.loopmania.LoopManiaWorld;

public class Doggie extends BossEnemy {
    private transient SimpleIntegerProperty health;
    private transient SimpleIntegerProperty maxhealth;
    private transient SimpleIntegerProperty atk;
    private transient SimpleStringProperty type;
    private int twice;
    private int battleRadius;
    private int supportRadius;
    private MonsterStrategy attackStrategy;
    private int trance;
    String ImageUrl = "src/images/doggie.png";
    String ToolTipString = "Doggie!!\nWow much coin how money so crypto plz mine v rich very currency\nA special boss which spawns the DoggieCoin upon defeat, which randomly fluctuates \nin sellable price to an extraordinary extent. It has high health and can stun the character, \nwhich prevents the character from making an attack temporarily. \nThe battle and support radii are the same as for slugs";

    public Doggie(PathPosition position) {
        super(position);
        health = new SimpleIntegerProperty(1000);
        maxhealth = new SimpleIntegerProperty(1000);
        atk = new SimpleIntegerProperty(40);
        type = new SimpleStringProperty("Doggie");
        battleRadius = 1;
        supportRadius = 1;
        attackStrategy = new DoggieStrategy();
        twice = 0;
        trance = 0;
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setImage(icon);
        setTooltip(ToolTipString);
    }

    public void triggerDefeatFlag(LoopManiaWorld w) {
        w.doggieDefeated();
    }

    public Boolean canSpawn(LoopManiaWorld w) {
        return ((int) w.getCurrentLoop().get()) >= 20;
    }

    /**
     * returns the maxhealth of a Doggie
     * 
     * @return SimpleIntegerProperty maxhealth
     */
    public SimpleIntegerProperty getMaxHealth() {
        return this.maxhealth;
    }

    @Override
    public SimpleStringProperty getType() {
        return type;
    }

    /**
     * returns the battle radius of a Doggie
     */
    public int getBattleRadius() {
        return battleRadius;
    }

    /**
     * returns the support radius of a Doggie
     */
    public int getSupportRadius() {
        return supportRadius;
    }

    /**
     * Movement taken for a Doggie
     */
    public void move(Pair<Integer, Integer> characterPos, List<Building> buildingEntities) {
        if (twice % 2 == 0)
            moveDownPath();
        twice = (twice + 1) % 2;
    }

    /**
     * Doggie attacks Character, if both alive
     */
    public void attack(Character c, int rd, LoopManiaWorld w) {
        if (!ifDead() && !c.ifDead())
            attackStrategy.attack(c, atk.get(), rd, w);
    }

    /**
     * Doggie attacks Soldier, if both alive
     */
    public void attack(Soldier s, int rd, LoopManiaWorld w) {
        if (!ifDead() && !s.ifDead())
            attackStrategy.attack(s, atk.get(), rd, w);
    }

    /**
     * Doggie can't be tranced
     * 
     * @precondition Doggie is Tranced
     * @postcondition Doggie trance count is reduced by 1
     */
    public void attack(BasicEnemy e, int rd, LoopManiaWorld w) {
        // not gonna happen
    }

    /**
     * Doggie can't be tranced
     * 
     * @precondition hit by staff special effect
     * @postcondition Doggie is Tranced
     */
    public void setTrance(int t) {
    }

    /**
     * Doggie can't be tranced
     * 
     * @precondition Doggie is Tranced
     * @postcondition Doggie is not Tranced
     */
    public void reduceTrance() {
    }

    /**
     * assert if Doggie is Tranced
     */
    public Boolean ifTrance() {
        return false;
    }

    /**
     * Doggie takes damage
     */
    public void takingDamage(int dmg) {
        setHealth(health.get() - dmg);
    }

    /**
     * Assert if Doggie is dead or tranced to prevent being attacked by Character /
     * Soldier / enemy
     * 
     * @return Boolean
     */
    public Boolean ifDead() {
        return health.get() <= 0;
    }

    /**
     * returns current health of Doggie
     * 
     * @return SimpleIntegerProperty health
     */
    public SimpleIntegerProperty getHealth() {
        return this.health;
    };

    /**
     * Set health to new value
     */
    public void setHealth(Integer health) {
        this.health.set(health);
    };

    /**
     * Setup the position of just spawned Doggie
     * 
     * @param x
     * @param y
     * @param DoggieCount
     * @return Boolean
     */
    public Boolean setDoggiePosition(int x, int y, int DoggieCount) {
        Pair<Integer, Integer> DoggiePos = possiblyGetDoggieSpawnPosition(x, y, DoggieCount);
        if (DoggiePos != null) {
            int indexInPath = OrderedPath.indexOf(DoggiePos);
            setPosition(new PathPosition(indexInPath, OrderedPath));
            return true;
        } else
            return false;
    };

    /**
     * get a randomly generated position which could be used to spawn an enemy
     * 
     * @return null if random choice is that wont be spawning an enemy or it isn't
     *         possible, or random coordinate pair if should go ahead
     */
    public Pair<Integer, Integer> possiblyGetDoggieSpawnPosition(int x, int y, int DoggieCount) {
        // has a chance spawning a basic enemy on a tile the character isn't on or
        // immediately before or after (currently space required = 2)...
        Random rand = new Random();
        if ((DoggieCount < 1)) {
            List<Pair<Integer, Integer>> OrderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = OrderedPath.indexOf(new Pair<Integer, Integer>(x, y));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + OrderedPath.size()) % OrderedPath.size();
            int endNotAllowed = (indexPosition + 3) % OrderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i = endNotAllowed; i != startNotAllowed; i = (i + 1) % OrderedPath.size()) {
                OrderedPathSpawnCandidates.add(OrderedPath.get(i));
            }

            // choose random choice
            Pair<Integer, Integer> spawnPosition = OrderedPathSpawnCandidates
                    .get(rand.nextInt(OrderedPathSpawnCandidates.size()));

            return spawnPosition;
        }
        return null;
    }

    /**
     * Boss's presence effect to the world
     */
    public void presenceAura(LoopManiaWorld w) {
        // do nothing for doggie
    }
}
