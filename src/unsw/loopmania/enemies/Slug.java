
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

public class Slug extends BasicEnemy {
    private transient SimpleIntegerProperty health;
    private transient SimpleIntegerProperty maxhealth;
    private transient SimpleIntegerProperty atk;
    private transient SimpleStringProperty type;
    private int twice;
    private int battleRadius;
    private int supportRadius;
    private MonsterStrategy attackStrategy;
    private int trance;
    String ImageUrl = "src/images/slug.png";
    String ToolTipString = "Slug\nA standard enemy type. Low health and low damage. \nThe battle radius is the same as the support radius for a slug.";

    public Slug(PathPosition position) {
        super(position);
        health = new SimpleIntegerProperty(10);
        maxhealth = new SimpleIntegerProperty(10);
        atk = new SimpleIntegerProperty(5);
        type = new SimpleStringProperty("Slug");
        twice = 0;
        battleRadius = 1;
        supportRadius = 1;
        attackStrategy = new SlugStrategy();
        trance = 0;
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setImage(icon);
        setTooltip(ToolTipString);

    }

    /**
     * returns the maxhealth of a Slug
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
     * returns the battle radius of a Slug
     */
    public int getBattleRadius() {
        return battleRadius;
    }

    /**
     * returns the support radius of a Slug
     */
    public int getSupportRadius() {
        return supportRadius;
    }

    /**
     * Movement taken for a Slug
     */
    public void move(Pair<Integer, Integer> characterPos, List<Building> buildingEntities) {
        if (twice % 2 == 0)
            moveUpPath();
        twice = (twice + 1) % 2;
    }

    /**
     * Slug attacks Character, if both alive
     */
    public void attack(Character c, int rd, LoopManiaWorld w) {
        if (!ifDead() && !c.ifDead())
            attackStrategy.attack(c, atk.get(), rd, w);
    }

    /**
     * Slug attacks Soldier, if both alive
     */
    public void attack(Soldier s, int rd, LoopManiaWorld w) {
        if (!ifDead() && !s.ifDead())
            attackStrategy.attack(s, atk.get(), rd, w);
    }

    /**
     * Slug attacks other enemy, if tranced
     * 
     * @precondition Slug is Tranced
     * @postcondition Slug trance count is reduced by 1
     */
    public void attack(BasicEnemy e, int rd, LoopManiaWorld w) {
        // if (!ifDead() && !e.ifDead()){
        if (health.get() > 0 && !e.ifDead()) {
            e.takingDamage(1);
            System.out.println("Slug is attacking enemies !!!");
            reduceTrance();
        }
    }

    /**
     * Slug is tranced
     * 
     * @precondition hit by staff special effect
     * @postcondition Slug is Tranced
     */
    public void setTrance(int t) {
        // if affected by staff
        System.out.println("Slug is tranced !!!");
        trance = t;
    }

    /**
     * Slug trance count is reduced, and a message is poped if slug exits trance
     * 
     * @precondition Slug is Tranced
     * @postcondition Slug is not Tranced
     */
    public void reduceTrance() {
        // if affected by staff, then reduce debuff each time it attacks (per turn)
        if (ifTrance()) {
            trance -= 1;
            if (!ifTrance()) {
                System.out.println("Slug exits Trance state !!!");
            }
        }
    }

    /**
     * assert if Slug is Tranced
     */
    public Boolean ifTrance() {
        return trance > 0;
    }

    /**
     * Slug takes damage
     */
    public void takingDamage(int dmg) {
        setHealth(health.get() - dmg);
    }

    /**
     * Assert if Slug is dead or tranced to prevent being attacked by Character /
     * Soldier / enemy
     * 
     * @return Boolean
     */
    public Boolean ifDead() {
        return (health.get() <= 0 || trance > 0);
    }

    /**
     * returns current health of Slug
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
     * Setup the position of just spawned Slug
     * 
     * @param x
     * @param y
     * @param slugCount
     * @return Boolean
     */
    public Boolean setSlugPosition(int x, int y, int slugCount) {
        Pair<Integer, Integer> slugPos = possiblyGetSlugSpawnPosition(x, y, slugCount);
        if (slugPos != null) {
            int indexInPath = OrderedPath.indexOf(slugPos);
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
    public Pair<Integer, Integer> possiblyGetSlugSpawnPosition(int x, int y, int slugCount) {
        // has a chance spawning a basic enemy on a tile the character isn't on or
        // immediately before or after (currently space required = 2)...
        Random rand = new Random();
        if ((slugCount < 2)) {
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
}
