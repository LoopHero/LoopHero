package unsw.loopmania.enemies;

import org.javatuples.Pair;

import java.util.Random;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.PathPosition;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;

import unsw.loopmania.building.*;

public class Zombie extends BasicEnemy {
    private SimpleIntegerProperty health;
    private SimpleIntegerProperty maxhealth;
    private SimpleIntegerProperty atk;
    private SimpleStringProperty type;
    private int battleRadius;
    private int supportRadius;
    private MonsterStrategy attackStrategy;
    private int trance;

    public Zombie(PathPosition position) {
        super(position);
        health = new SimpleIntegerProperty(15);
        maxhealth = new SimpleIntegerProperty(15);
        atk = new SimpleIntegerProperty(10);
        type = new SimpleStringProperty("Zombie");
        battleRadius = 2;
        supportRadius = 2;
        attackStrategy = new ZombieStrategy();
        trance = 0;
    }

    /**
     * set health
     */
    public void setHealth(Integer newHealth) {
        health.set(newHealth);
    }

    /**
     * returns health
     * @return SimpleIntegerProperty health
     */
    public SimpleIntegerProperty getHealth() {
        return this.health;
    }

    /**
     * returns max health
     * @return SimpleIntegerProperty maxhealth
     */
    public SimpleIntegerProperty getMaxHealth() {
        return this.maxhealth;
    }

    /**
     * returns "Zombie"
     */
    @Override
    public SimpleStringProperty getType() {
        return type;
    }

    /**
     * returns the battle radius of a Zombie
     */
    public int getBattleRadius() {
        return battleRadius;
    }

    /**
     * returns the support radius of a Zombie
     */
    public int getSupportRadius() {
        return supportRadius;
    }

    /**
     * Movement taken for a Zombie
     */
    public void move(Pair<Integer, Integer> characterPos, List<Building> buildingEntities) {
        // // this basic enemy moves in a random direction... 25% chance up or down, 50%
        // chance not at all...
        // Modified by Haoran to slow down zombie movement
        int directionChoice = (new Random(System.currentTimeMillis())).nextInt(2);
        if (directionChoice == 0) {
            moveUpPath();
        } else if (directionChoice == 1) {
            moveDownPath();
        }
    }

    /**
     * Zombie attacks Character, if both alive
     */
    public void attack(Character c, int rd, LoopManiaWorld w) {
        if (!ifDead() && !c.ifDead())
            attackStrategy.attack(c, atk.get(), rd, w);
    }

    /**
     * Zombie attacks Soldier, if both alive
     */
    public void attack(Soldier s, int rd, LoopManiaWorld w) {
        if (!ifDead() && !s.ifDead())
            attackStrategy.attack(s, atk.get(), rd, w);
    }

    /**
     * Zombie attacks other enemy, if tranced
     * @precondition Zombie is Tranced
     * @postcondition Zombie trance count is reduced by 1
     */
    public void attack(BasicEnemy e, int rd, LoopManiaWorld w){
        // if (!ifDead() && !e.ifDead()){
        if (health.get() > 0 && !e.ifDead()){
            e.takingDamage(2);
            System.out.println("Zombie is attacking enemies !!!");
            reduceTrance();
        }
    }

    /**
     * Zombie is tranced
     * @precondition hit by staff special effect
     * @postcondition Zombie is Tranced
     */
    public void setTrance(int t){
        // if affected by staff
        System.out.println("Zombie is tranced !!!");
        trance = t;
    }

    /**
     * Zombie trance count is reduced, and a message is poped if Zombie exits trance
     * @precondition Zombie is Tranced
     * @postcondition Zombie is not Tranced
     */
    public void reduceTrance(){
        // if affected by staff, then reduce debuff each time it attacks (per turn)
        if (ifTrance()){
            trance -= 1;
            if (!ifTrance()){
                System.out.println("Zombie exits Trance state !!!");
            }
        }
    }

    /**
     * assert if Zombie is Tranced
     */
    public Boolean ifTrance(){
        return trance > 0;
    }

    /**
     * Zombie takes damage
     */
    public void takingDamage(int dmg) {
        setHealth(health.get() - dmg);
    }

    /**
     * Assert if Zombie is dead or tranced to
     * prevent being attacked by Character / Soldier / enemy
     * @return Boolean
     */
    public Boolean ifDead() {
        return (health.get() <= 0 || trance > 0);
    }

    /**
     * Setup the position of just spawned Zombie
     * @param x
     * @param y
     * @param ZombieCount
     * @return Boolean
     */
    public Boolean setZombiePosition(int zombieCount, int zombiePitCount, List<Building> buildingEntities) {
        Pair<Integer, Integer> zombiePos =  possiblyGetZombieSpawnPosition(zombieCount, zombiePitCount, buildingEntities);
        if (zombiePos != null) {
            int indexInPath = OrderedPath.indexOf(zombiePos);
            setPosition(new PathPosition(indexInPath, OrderedPath));
            return true;
        }
        else
            return false;
    };

    /**
     * get a randomly generated position which could be used to spawn an enemy
     * 
     * @return null if random choice is that wont be spawning an enemy or it isn't
     *         possible, or random coordinate pair if should go ahead
     */
    public Pair<Integer, Integer> possiblyGetZombieSpawnPosition(int zombieCount, int zombiePitCount, List<Building> buildingEntities) {
        // has a chance spawning a basic enemy on a tile the character isn't on or
        // immediately before or after (currently space required = 2)...
        Random rand = new Random();
        List<Pair<Integer, Integer>> possiblePath = pathAroundZombiePit(buildingEntities);
        if ((zombieCount <= 3 * zombiePitCount) && (possiblePath.size() > 0)) {
            // // choose random choice
            Pair<Integer, Integer> spawnPosition = possiblePath.get(rand.nextInt(possiblePath.size()));
            return spawnPosition;
        }
        return null;
    }

    /**
     * find positions around ZombiePit and on Path tile
     * @param buildingEntities
     * @return
     */
    public List<Pair<Integer, Integer>> pathAroundZombiePit(List<Building> buildingEntities) {
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        for (Building building : buildingEntities)
            if (building.getType().equals("ZombiePit")) {
                ZombiePitBuilding zombiePit = (ZombiePitBuilding) building;
                if (((float) zombiePit.getMoves() / OrderedPath.size()) % (float) 1 == (float) 0)
                    path.addAll(neighboursPoses(building.getX(), building.getY()));
            }
        return path;
    }
}
