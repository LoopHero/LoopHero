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

public class ElonMask extends BossEnemy {
    private transient SimpleIntegerProperty health;
    private transient SimpleIntegerProperty maxhealth;
    private transient SimpleIntegerProperty atk;
    private transient SimpleStringProperty type;
    private int battleRadius;
    private int supportRadius;
    private MonsterStrategy attackStrategy;
    private int trance;
    String ImageUrl = "src/images/ElanMuske.png";
    String ToolTipString = "Elon Muskeeeeeeeeeee! To the moon!\nAn incredibly tough boss which, when appears, causes the price of DoggieCoin to \nincrease drastically. Defeating this boss causes the price of DoggieCoin to plummet. \nElan has the ability to heal other enemy NPCs. The battle and support radii are the same as for slugs";

    public ElonMask(PathPosition position) {
        super(position);
        health = new SimpleIntegerProperty(5000);
        maxhealth = new SimpleIntegerProperty(5000);
        atk = new SimpleIntegerProperty(80);
        type = new SimpleStringProperty("ElonMask");
        battleRadius = 1;
        supportRadius = 1;
        attackStrategy = new ElonStrategy();
        trance = 0;
        Image icon = new Image((new File(ImageUrl)).toURI().toString());
        setImage(icon);
        setTooltip(ToolTipString);
    }

    public void triggerDefeatFlag(LoopManiaWorld w) {
        w.ElonDefeated();
    }

    /**
     * only spawn after 40 loops, and char exp > 10^4
     */
    public Boolean canSpawn(LoopManiaWorld w) {
        return ((int) w.getCurrentLoop().get()) >= 40 && w.getCharacterExp().get() > 10000;
        // return ((int) w.getCurrentLoop().get()) >= 2 && w.getCharacterExp().get() >
        // 100;
    }

    /**
     * returns the maxhealth of a Elon
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
     * returns the battle radius of a Elon
     */
    public int getBattleRadius() {
        return battleRadius;
    }

    /**
     * returns the support radius of a Elon
     */
    public int getSupportRadius() {
        return supportRadius;
    }

    /**
     * Elon does not move
     */
    public void move(Pair<Integer, Integer> characterPos, List<Building> buildingEntities) {
        // if (twice % 2 == 0)
        // moveDownPath();
        // twice = (twice + 1) % 2;
    }

    /**
     * Elon attacks Character, if both alive
     */
    public void attack(Character c, int rd, LoopManiaWorld w) {
        if (!ifDead() && !c.ifDead())
            attackStrategy.attack(c, atk.get(), rd, w);
    }

    /**
     * Elon attacks Soldier, if both alive
     */
    public void attack(Soldier s, int rd, LoopManiaWorld w) {
        if (!ifDead() && !s.ifDead())
            attackStrategy.attack(s, atk.get(), rd, w);
    }

    /**
     * Elon can't be tranced
     * 
     * @precondition Elon is Tranced
     * @postcondition Elon trance count is reduced by 1
     */
    public void attack(BasicEnemy e, int rd, LoopManiaWorld w) {
        // not gonna happen
    }

    /**
     * Elon can't be tranced
     * 
     * @precondition hit by staff special effect
     * @postcondition Elon is Tranced
     */
    public void setTrance(int t) {
    }

    /**
     * Elon can't be tranced
     * 
     * @precondition Elon is Tranced
     * @postcondition Elon is not Tranced
     */
    public void reduceTrance() {
    }

    /**
     * assert if Elon is Tranced
     */
    public Boolean ifTrance() {
        return false;
    }

    /**
     * Elon takes damage
     */
    public void takingDamage(int dmg) {
        setHealth(health.get() - dmg);
    }

    /**
     * Assert if Elon is dead or tranced to prevent being attacked by Character /
     * Soldier / enemy
     * 
     * @return Boolean
     */
    public Boolean ifDead() {
        return health.get() <= 0;
    }

    /**
     * returns current health of Elon
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
     * Setup the position of just spawned Elon
     * 
     * @param x
     * @param y
     * @param ElonCount
     * @return Boolean
     */
    public Boolean setElonPosition(int x, int y, int ElonCount) {
        Pair<Integer, Integer> ElonPos = possiblyGetElonSpawnPosition(x, y, ElonCount);
        if (ElonPos != null) {
            int indexInPath = OrderedPath.indexOf(ElonPos);
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
    public Pair<Integer, Integer> possiblyGetElonSpawnPosition(int x, int y, int ElonCount) {
        // has a chance spawning a basic enemy on a tile the character isn't on or
        // immediately before or after (currently space required = 2)...
        Random rand = new Random();
        if ((ElonCount < 1)) {
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
        // do nothing for Elon
        List<BasicEnemy> eList = w.getEnemies();
        for (BasicEnemy e : eList) {
            // all basic enemy regen 5 health, can surpass maxhealth
            if (!(e instanceof BossEnemy) && !e.ifDead()) {
                e.takingDamage(-5);
            }
        }
    }
}
