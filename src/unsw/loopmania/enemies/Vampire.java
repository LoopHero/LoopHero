
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

public class Vampire extends BasicEnemy {
    private SimpleIntegerProperty health;
    private SimpleIntegerProperty maxhealth;
    private SimpleIntegerProperty atk;
    private SimpleStringProperty type;
    private int battleRadius;
    private int supportRadius;
    private MonsterStrategy attackStrategy;
    private int trance;
    private int direction;

    public Vampire(PathPosition position) {
        super(position);
        health = new SimpleIntegerProperty(100);
        maxhealth = new SimpleIntegerProperty(100);
        atk = new SimpleIntegerProperty(10);
        type = new SimpleStringProperty("Vampire");
        battleRadius = 2;
        supportRadius = 15;
        attackStrategy = new VampireStrategy();
        trance = 0;
        direction = 1;
    }

    /**
     * set new health
     * @param newMaxHealth
     */
    public void setHealth(Integer newHealth) {
        health.set(newHealth);
    }

    /**
     * returns the health of a Vampire
     * @return SimpleIntegerProperty health
     */
    public SimpleIntegerProperty getHealth() {
        return this.health;
    }

    /**
     * returns the maxhealth of a Vampire
     * @return SimpleIntegerProperty maxhealth
     */
    public SimpleIntegerProperty getMaxHealth() {
        return this.maxhealth;
    }

    /**
     * returns Vampire
     */
    @Override
    public SimpleStringProperty getType() {
        return type;
    }

    /**
     * returns the battle radius of a Vampire
     */
    public int getBattleRadius() {
        return battleRadius;
    }

    /**
     * returns the support radius of a Vampire
     */
    public int getSupportRadius() {
        return supportRadius;
    }

    /**
     * Movement taken for a Vampire, 
     * vampire generally moves away from campfire
     */
    public void move(Pair<Integer, Integer> characterPos, List<Building> buildingEntities) {
        // if currently near campfire
        List<Pair<Integer, Integer>> camplight = pathAroundCampfire(buildingEntities);
        Pair<Integer, Integer> vp = new Pair<Integer, Integer>(this.getX(), this.getY());
        if (camplight.contains(vp)){
            direction *= -1;
        }
        // move away from campfire
        if (direction == 1){
            moveUpPath();
            moveUpPath();
        } else {
            moveDownPath();
            moveDownPath();
        }
    }

    /**
     * Vampire attacks Character, if both alive
     */
    public void attack(Character c, int rd, LoopManiaWorld w) {
        if (!ifDead() && !c.ifDead())
            attackStrategy.attack(c, atk.get(), rd, w);
    }

    /**
     * Vampire attacks Soldier, if both alive
     */
    public void attack(Soldier s, int rd, LoopManiaWorld w) {
        if (!ifDead() && !s.ifDead())
            attackStrategy.attack(s, atk.get(), rd, w);
    }

    /**
     * Vampire attacks other enemy, if tranced
     * @precondition Vampire is Tranced
     * @postcondition Vampire trance count is reduced by 1
     */
    public void attack(BasicEnemy e, int rd, LoopManiaWorld w){
        // if (!ifDead() && !e.ifDead()){
        if (health.get() > 0 && !e.ifDead()){
            e.takingDamage(5);
            System.out.println("Vampire is attacking enemies !!!");
            reduceTrance();
        }
    }

    /**
     * Vampire is tranced
     * @precondition hit by staff special effect
     * @postcondition Vampire is Tranced
     */
    public void setTrance(int t){
        // if affected by staff
        System.out.println("Vampire is tranced !!!");
        trance = t;
    }

    /**
     * Vampire trance count is reduced, and a message is poped if Vampire exits trance
     * @precondition Vampire is Tranced
     * @postcondition Vampire is not Tranced
     */
    public void reduceTrance(){
        // if affected by staff, then reduce debuff each time it attacks (per turn)
        if (ifTrance()){
            trance -= 1;
            if (!ifTrance()){
                System.out.println("Vampire exits Trance state !!!");
            }
        }
    }

    /**
     * assert if Vampire is Tranced
     */
    public Boolean ifTrance(){
        return trance > 0;
    }

    /**
     * Vampire takes damage
     */
    public void takingDamage(int dmg) {
        setHealth(health.get() - dmg);
    }

    /**
     * Assert if Vampire is dead or tranced to
     * prevent being attacked by Character / Soldier / enemy
     * @return Boolean
     */
    public Boolean ifDead() {
        return (health.get() <= 0 || trance > 0);
    }

    /**
     * Setup the position of just spawned Vampire
     * @param x
     * @param y
     * @param VampireCount
     * @return Boolean
     */
    public Boolean setVampirePosition(int vampireCount, int vampireCaslteCount, List<Building> buildingEntities) {
        Pair<Integer, Integer> vampirePos = possiblyGetVampireSpawnPosition(vampireCount, vampireCaslteCount, buildingEntities);
        if (vampirePos != null) {
            int indexInPath = OrderedPath.indexOf(vampirePos);
            setPosition(new PathPosition(indexInPath, OrderedPath));
            return true;
        }
        else
            return false;
    };

    /**
     * Returns positions adjacent to VampireCastles
     * @param buildingEntities
     * @return
     */
    public List<Pair<Integer, Integer>> pathAroundVampireCastle(List<Building> buildingEntities) {
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        for (Building building : buildingEntities)
            if (building.getType().equals("VampireCastle")) {
                VampireCastleBuilding vampireCastle = (VampireCastleBuilding) building;
                if (((float) vampireCastle.getMoves() / OrderedPath.size()) % 5 == (float) 0) {
                    path.addAll(neighboursPoses(building.getX(), building.getY()));
                }
            }
        return path;
    }

    /**
     * get a randomly generated position which could be used to spawn an enemy
     * 
     * @return null if random choice is that wont be spawning an enemy or it isn't
     *         possible, or random coordinate pair if should go ahead
     */
    public Pair<Integer, Integer> possiblyGetVampireSpawnPosition(int vampireCount, int vampireCastleCount, List<Building> buildingEntities) {
        List<Pair<Integer, Integer>> possiblePath = pathAroundVampireCastle(buildingEntities);
        Random rand = new Random();
        if ((vampireCount <= 2 * vampireCastleCount) && (possiblePath.size() > 0)) {
            // choose random choice
            Pair<Integer, Integer> spawnPosition = possiblePath.get(rand.nextInt(possiblePath.size()));

            return spawnPosition;
        }
        return null;
    }

    /**
     * Returns positions adjacent to VampireCastles
     * @param buildingEntities
     * @return
     */
    public List<Pair<Integer, Integer>> pathAroundCampfire(List<Building> buildingEntities) {
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        for (Building building : buildingEntities)
            if (building.getType().equals("Campfire")) {
                path.addAll(neighboursPoses(building.getX(), building.getY()));
            }
        return path;
    }

}
