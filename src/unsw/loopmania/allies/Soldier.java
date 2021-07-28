package unsw.loopmania.allies;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.PathPosition;

import unsw.loopmania.enemies.BasicEnemy;

/**
 * represents a soldier following the character in the backend of the game world
 */
public class Soldier extends MovingEntity {

    private SimpleIntegerProperty health;
    private SimpleIntegerProperty maxhealth;
    private int vampiteBite;
    private AttackStrategy attackMethod;

    /**
     * Createa Soldier right at the end of character's soldier list; Let it follows
     * character movement.
     */
    public Soldier(PathPosition position) {
        super(position);
        health = new SimpleIntegerProperty(100);
        maxhealth = new SimpleIntegerProperty(100);
        vampiteBite = 0;
        attackMethod = new FistStrategy();
    };

    /**
     * Changes current health to target newHealth; 
     * @precondition newHealth <= maxHealth
     * @postcondition health <= maxHealth
     * @param Integer newHealth
     */
    public void setHealth(Integer newHealth) {
        health.set(newHealth);
    }

    /**
     * get current health
     * @return SimpleIntegerProperty health
     */
    public SimpleIntegerProperty getHealth() {
        return this.health;
    }

    /**
     * get current maxHealth
     * @return SimpleIntegerProperty maxHealth
     */
    public SimpleIntegerProperty getMaxHealth() {
        return this.maxhealth;
    }

    /**
     * Soldier take damage and reduce health by dmg amount
     * @param int dmg
     */
    public void takingDamage(int dmg) {
        setHealth(getHealth().get() - dmg);
    }

    /**
     * Soldier take critical bit from Vampire and have a debuff for rd attacks
     * @param int rd
     */
    public void vampireBitten(int rd) {
        this.vampiteBite = rd;
    }

    /**
     * Upon taking dmg from Vampire, assert if in debuff duration; 
     * if so, the debuff is reduced, and dmg taken is increased.
     * @return Boolean
     */
    public Boolean ifBitten() {
        if (this.vampiteBite <= 0) {
            return false;
        } else {
            // reduce debuff length upon attacked by vampire
            this.vampiteBite -= 1;
            return true;
        }
    }

    /**
     * Assert if soldier is Dead or not (hp <= 0 or not)
     * @return Boolean
     */
    public Boolean ifDead() {
        return getHealth().get() <= 0;
    }

    /**
     * triggers attack from soldier to BasicEnemy e
     * @param BasicEnemy e
     * @param int rd
     */
    public void attack(BasicEnemy e, int rd) {
        if (!ifDead() && !e.ifDead()){
            // attackMethod.attack(null, e, rd, false);
            System.out.println("Soldier deals 3 dmg with Fist");
            e.takingDamage(3);
        }
    }

    /**
     * Soldier move to the next position, and strictly follows character;
     * by taking the previous position, if soldier at previous position is dead;
     * @param int n
     */
    public void moveDown(int n) {
        for (int i = 0; i < n; i++) {
            moveDownPath();
        }
    }
}
