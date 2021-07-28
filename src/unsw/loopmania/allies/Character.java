package unsw.loopmania.allies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.PathPosition;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.enemies.*;
import unsw.loopmania.LoopManiaWorld;

/**
 * represents the main character in the backend of the game world
 */
public class Character extends MovingEntity {
    private SimpleIntegerProperty health;
    private SimpleIntegerProperty maxhealth;
    private SimpleIntegerProperty gold;
    private SimpleIntegerProperty exp;
    private SimpleStringProperty healthTxt;
    private Boolean inBattle;
    // set to a positive number if bitten (critical)
    // , reduced for each next vampire attack
    private int vampiteBite;
    private FloatProperty loop;
    private AttackStrategy attackMethod;
    private Boolean shield;
    private DefenseStrategy helmet;
    private DefenseStrategy armour;
    private List<Soldier> soldiers;
    private List<BasicEnemy> trances;
    private Boolean buffCampfire;
    // private Boolean buffTower;

    // TODO = potentially implement relationships between this class and other
    // classes
    public Character(PathPosition position) {
        super(position);
        health = new SimpleIntegerProperty(1000);
        maxhealth = new SimpleIntegerProperty(1000);
        gold = new SimpleIntegerProperty(0);
        exp = new SimpleIntegerProperty(0);
        healthTxt = new SimpleStringProperty(health.get() + "/" + maxhealth.get());
        inBattle = false;
        vampiteBite = 0;
        attackMethod = new FistStrategy();
        helmet = new NakeStrategy();
        armour = new NakeStrategy();
        soldiers = new ArrayList<Soldier>();
        shield = false;
        buffCampfire = false;
        // buffTower = false;
        loop = new SimpleFloatProperty(0);
        trances = new ArrayList<BasicEnemy>();
    }

    /**
     * Set the Campfire buff for Character
     * 
     * @param Boolean b
     */
    public void setBuffCampfire(Boolean b) {
        this.buffCampfire = b;
    }

    /**
     * Changes current health to target newHealth;
     * 
     * @precondition newHealth <= maxHealth
     * @postcondition health <= maxHealth
     * @param Integer newHealth
     */
    public void setHealth(Integer newHealth) {
        newHealth = Math.min(maxhealth.get(), newHealth);
        health.set(newHealth);
        setHealthTxt();
    }

    /**
     * Changes current gold to target newGold;
     * 
     * @precondition newGold >= 0
     * @postcondition gold >= 0
     * @param Integer newHealth
     */
    public void setGold(Integer newGold) {
        gold.set(newGold);
    }

    /**
     * Changes current exp to target newExp;
     * 
     * @precondition newExp >= exp
     * @postcondition exp >= 0
     * @param Integer newHealth
     */
    public void setExp(Integer newExp) {
        exp.set(newExp);
    }

    /**
     * Updates current hp text display;
     */
    public void setHealthTxt() {
        healthTxt.set(health.get() + "/" + maxhealth.get());
    }

    /**
     * updates current loop (as float) into character
     */
    public void setLoop(FloatProperty loop) {
        this.loop = loop;
    }

    /**
     * get current health
     * 
     * @return SimpleIntegerProperty health
     */
    public SimpleIntegerProperty getHealth() {
        return this.health;
    }

    /**
     * get current maxHealth
     * 
     * @return SimpleIntegerProperty maxHealth
     */
    public SimpleIntegerProperty getMaxHealth() {
        return this.maxhealth;
    }

    /**
     * get current gold
     * 
     * @return SimpleIntegerProperty gold
     */
    public SimpleIntegerProperty getGold() {
        return this.gold;
    }

    /**
     * get current exp
     * 
     * @return SimpleIntegerProperty exp
     */
    public SimpleIntegerProperty getExp() {
        return this.exp;
    }

    /**
     * get current exp
     * 
     * @return FloatProperty loop
     */
    public FloatProperty getLoop() {
        return loop;
    }

    /**
     * get current text for health
     * 
     * @return SimpleStringProperty healthTxt
     */
    public SimpleStringProperty healthProperty() {
        return healthTxt;
    }

    /**
     * Character takes reduced damage according to equipments Character wears
     * 
     * @param int dmg
     */
    public void takingDamage(int dmg) {
        // dmg is reduced by helmet first, then by armour
        setHealth(getHealth().get() - armour.defence(helmet.defence(dmg)));
    }

    /**
     * Character take critical bit from Vampire and have a debuff for rd attacks
     * 
     * @param int rd
     */
    public void vampireBitten(int rd) {
        this.vampiteBite = rd;
    }

    /**
     * Upon taking dmg from Vampire, assert if in debuff duration; if so, the debuff
     * is reduced, and dmg taken is increased.
     * 
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
     * Get the remaining rounds left for vampire bits to take effect
     * 
     * @return int vampireBite
     */
    public int getBitten() {
        return vampiteBite;
    }

    /**
     * Assert if character is currently in battle
     * 
     * @return Boolean inBattle
     */
    public Boolean ifInBattle() {
        return inBattle;
    }

    /**
     * Change character's battle state, allows enemy with large support tradius to
     * join battle
     */
    public void triggerBattle() {
        inBattle = true;
    }

    /**
     * Turn off character's battle state, kill all trance enemies, clear vampireBite
     * debuff;
     */
    public void endBattle() {
        vampiteBite = 0;
        for (BasicEnemy be : trances) {
            // kill enemy still in trance
            System.out.println("Tranced " + be.getType().get() + " is instantly killed !!!");
            be.takingDamage(9999);
        }
        inBattle = false;
        while (trances.size() > 0) {
            trances.remove(0);
        }
    }

    /**
     * Assert if character is Dead or not (hp <= 0 or not)
     * 
     * @return Boolean
     */
    public Boolean ifDead() {
        return getHealth().get() <= 0;
    }

    /**
     * triggers attack from soldier to BasicEnemy e; attack can miss (15%) if a
     * helmet is worn.
     * 
     * @param BasicEnemy e
     * @param int        rd
     */
    public void attack(BasicEnemy e, int rd) {
        if (!ifDead() && !e.ifDead()) {
            // helmet brings a 15% chance of miss
            if (helmet instanceof HelmetStrategy && rd >= 15)
                attackMethod.attack(this, e, rd, buffCampfire);
            else if (helmet instanceof NakeStrategy)
                attackMethod.attack(this, e, rd, buffCampfire);
            else
                System.out.println("Player miss the attack !!! (" + rd + ")");
        }
    }

    /**
     * Character equips Helmet and changes one of the defense Strategy
     */
    public void equipHelmet() {
        System.out.println("Player equips Helmet");
        helmet = new HelmetStrategy();
    }

    /**
     * Character equips Armour and changes one of the defense Strategy
     */
    public void equipArmour() {
        System.out.println("Player equips Armour");
        armour = new ArmourStrategy();
    }

    /**
     * Character unequips Helmet and changes defense Strategy to NakeStrategy
     */
    public void unequipHelmet() {
        System.out.println("Player unequips Helmet");
        helmet = new NakeStrategy();
    }

    /**
     * Character unequips Armour and changes defense Strategy to NakeStrategy
     */
    public void unequipArmour() {
        System.out.println("Player unequips Armour");
        armour = new NakeStrategy();
    }

    /**
     * Character equips Shield and reduces chance of vampireBite
     */
    public void equipShield() {
        System.out.println("Player equips Shield");
        shield = true;
    }

    /**
     * Character unequips Shield and restores chance of vampireBite
     */
    public void unequipShield() {
        System.out.println("Player unequips Shield");
        shield = false;
    }

    /**
     * Assert if character has sheild
     * 
     * @return Boolean shield
     */
    public Boolean ifShield() {
        return shield;
    }

    /**
     * EquipSword on Character, modifies AttackStrategy
     * to SwordStrategy
     */
    public void equipSword() {
        System.out.println("Player equips Sword");
        attackMethod = new SwordStrategy();
    }

    /**
     * EquipStake on Character, modifies AttackStrategy
     * to StakeStrategy
     */
    public void equipStake() {
        System.out.println("Player equips Stake");
        attackMethod = new StakeStrategy();
    }

    /**
     * EquipStaff on Character, modifies AttackStrategy
     * to StaffStrategy
     */
    public void equipStaff() {
        System.out.println("Player equips Staff");
        attackMethod = new StaffStrategy();
    }

    /**
     * UnEquip Weapon on Character, modifies AttackStrategy
     * to FistStrategy
     */
    public void unequipWeapon() {
        System.out.println("Player unequips weapon");
        attackMethod = new FistStrategy();
    }

    /**
     * Returns current number of soldier following character
     * @return int soldiers.size()
     */
    public int soldierNumber() {
        return soldiers.size();
    }

    /**
     * Added new created Soldier to character's soldier list
     * @param Soldier sol
     */
    public void addSoldier(Soldier sol) {
        soldiers.add(sol);
    }

    /**
     * Let tower near character to attack enemy
     * @param e
     * @param rd
     * @param buildings
     */
    public void towerAttack(BasicEnemy e, int rd, List<Building> buildings) {
        for (Building b : buildings) {
            if (b instanceof TowerBuilding) {
                TowerBuilding Tower = (TowerBuilding) b;
                int radius = Tower.getRadius();
                int buildingX = Tower.getX();
                int buildingY = Tower.getY();
                int charX = this.getX();
                int charY = this.getY();
                if ((buildingX - radius <= charX) && (buildingX + radius >= charX) && (buildingY - radius <= charY)
                        && (buildingY + radius >= charY)) {
                    System.out.println("In Tower Radius, Hit!");
                    Tower.attack(e, rd);
                }
            }
        }

    }

    /**
     * Let soldier following character to attack enemy
     * @param e
     * @param rd
     * @param w
     */
    public void soldierAttack(BasicEnemy e, int rd, LoopManiaWorld w) {
        // note: monster do attack soldier corpse
        for (Soldier s : soldiers) {
            s.attack(e, rd);
            if (e instanceof Slug) {
                Slug sl = (Slug) e;
                if (!sl.ifDead()) {
                    sl.attack(s, rd, w);
                }
            } else if (e instanceof Zombie) {
                Zombie zo = (Zombie) e;
                if (!zo.ifDead()) {
                    zo.attack(s, rd, w);
                }
            } else {
                Vampire va = (Vampire) e;
                if (!va.ifDead()) {
                    va.attack(s, rd, w);
                }
            }
        }
    }

    /**
     * Make soldier strictly follows character;
     * In case, the first one dies, the second and the third
     * would move one additional step to stay right after
     * character icon.
     * Also, removes dead soldier from list.
     */
    public void moveDownPathSoldier() {
        int n = 1;
        int i = 0;
        while (i < soldiers.size()) {
            if (!soldiers.get(i).ifDead()) {
                soldiers.get(i).moveDown(n);
                i += 1;
            } else {
                soldiers.get(i).shouldExist().set(false);
                soldiers.remove(i);
                n += 1;
            }
        }
    }

    /**
     * returns a list of soldiers
     * @return
     */
    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    /**
     * Add tanced enemy to Trances list
     * @param e
     */
    public void addToTrances(BasicEnemy e) {
        trances.add(e);
    }

    /**
     * Let Trance enemy to attack other enemy;
     * Remove enemy from list if enemy exits Trance state
     * @param e
     * @param rd
     * @param w
     */
    public void tranceAttack(BasicEnemy e, int rd, LoopManiaWorld w) {
        for (BasicEnemy be : trances) {
            be.attack(e, rd, w);
        }
        int i = 0;
        while (i < trances.size()) {
            if (trances.get(i).ifTrance()) {
                i++;
            } else {
                trances.remove(i);
            }
        }
    }

    /**
     * Activate/Deactivate BuffCampfire upon distance between campfire and character
     * @param Buildings
     * @return Boolean
     */
    public Boolean checkBuffCampfire(List<Building> Buildings) {
        for (Building b : Buildings) {
            if (b instanceof CampfireBuilding) {
                int radius = ((CampfireBuilding) b).getRadius();
                int buildingX = b.getX();
                int buildingY = b.getY();
                int charX = this.getX();
                int charY = this.getY();
                if ((buildingX - radius <= charX) && (buildingX + radius >= charX) && (buildingY - radius <= charY)
                        && (buildingY + radius >= charY)) {
                    System.out.println("In Campfire Radius, Double Damage");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Run battle against enemy;
     * However vampire attacks first;
     * Character usually attack first against other enemy;
     * Vampire with its larger support radius can ambush player, by attacking first twice;
     * @param e
     * @param rd
     * @param w
     * @param buildingEntities
     */
    public void runBattle(BasicEnemy e, Random rd, LoopManiaWorld w, List<Building> buildingEntities) {
        int br; // battle radius
        int sr; // support radius
        if (e.getType().get().equals("Vampire")) {
            Vampire v = (Vampire) e;
            br = v.getBattleRadius();
            sr = v.getSupportRadius();
            if (!v.ifDead() && !this.ifDead()
                    && Math.pow((this.getX() - e.getX()), 2) + Math.pow((this.getY() - e.getY()), 2) <= (br * br)) {
                // vampire attacks before this
                while (!v.ifDead() && !this.ifDead()) {
                    this.triggerBattle();
                    v.attack(this, rd.nextInt(100), w);
                    this.attack(v, rd.nextInt(100));
                    this.soldierAttack(e, rd.nextInt(100), w);
                    this.towerAttack(e, rd.nextInt(100), buildingEntities);
                    this.tranceAttack(e, rd.nextInt(100), w);
                }
            } else if (!v.ifDead() && !this.ifDead()
                    && Math.pow((this.getX() - e.getX()), 2) + Math.pow((this.getY() - e.getY()), 2) <= (sr * sr)
                    && this.ifInBattle()) {
                System.out.println("Vampire passing by joined battle, and suprised Player");
                // vampire ambushes this
                v.attack(this, rd.nextInt(100), w);
                while (!v.ifDead() && !this.ifDead()) {
                    this.triggerBattle();
                    v.attack(this, rd.nextInt(100), w);
                    this.attack(v, rd.nextInt(100));
                    this.soldierAttack(e, rd.nextInt(100), w);
                    this.towerAttack(e, rd.nextInt(100), buildingEntities);
                    this.tranceAttack(e, rd.nextInt(100), w);
                }
            }
        } else {
            br = e.getBattleRadius();
            sr = e.getSupportRadius();
            if (!e.ifDead() && !this.ifDead()
                    && Math.pow((this.getX() - e.getX()), 2) + Math.pow((this.getY() - e.getY()), 2) <= (br * br)) {
                // other enemy attacks after this
                while (!e.ifDead() && !this.ifDead()) {
                    this.triggerBattle();
                    this.attack(e, rd.nextInt(100));
                    e.attack(this, rd.nextInt(100), w);
                    this.soldierAttack(e, rd.nextInt(100), w);
                    this.towerAttack(e, rd.nextInt(100), buildingEntities);
                    this.tranceAttack(e, rd.nextInt(100), w);
                }
            }
        }
    }

}
