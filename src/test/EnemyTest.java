package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;

import unsw.loopmania.PathPosition;
import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import unsw.loopmania.enemies.BasicEnemy;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.enemies.ElonMask;
import unsw.loopmania.enemies.Doggie;
import unsw.loopmania.building.*;

import java.util.*;
import org.javatuples.Pair;
import unsw.loopmania.*;
public class EnemyTest extends TestSetup {
    @Test
    public void slugTranceTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        character.equipStaff();
        Slug slug = new Slug(new PathPosition(0, world.getOrderedPath()));
        Vampire vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        assertEquals(false, slug.ifDead());
        character.attack(slug, 100);
        for (int i = 0; i <= 6; i++) {
            character.tranceAttack(vampire, 0, world);
        }
        assertEquals(slug.getMaxHealth().get() - 2, slug.getHealth().get());
        tearDown();
    }

    @Test
    public void slugSoldierTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        slug.attack(soldier, 100, world);
        assertEquals(soldier.getMaxHealth().get() - 5, soldier.getHealth().get());
        tearDown();
    }

    @Test
    public void zombieBasicTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Zombie zombie = new Zombie(new PathPosition(0, world.getOrderedPath()));
        assertEquals(true, zombie.getType().get().equals("Zombie"));
        assertEquals(2, zombie.getBattleRadius());
        assertEquals(2, zombie.getSupportRadius());
    }

    @Test
    public void zombieTranceTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        character.equipStaff();
        Zombie zombie = new Zombie(new PathPosition(0, world.getOrderedPath()));
        Zombie zombie1 = new Zombie(new PathPosition(2, world.getOrderedPath()));
        Zombie zombie2 = new Zombie(new PathPosition(4, world.getOrderedPath()));
        assertEquals(false, zombie.ifDead());
        character.attack(zombie, 100);
        for (int i = 0; i <= 10; i++) {
            character.tranceAttack(zombie1, 0, world);
            character.tranceAttack(zombie2, 0, world);
        }
        assertEquals(zombie.getMaxHealth().get() - 2, zombie.getHealth().get());
        tearDown();
    }

    @Test
    public void zombieSpawnTest() throws FileNotFoundException {
        
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Card card = world.loadCard("ZombiePit");
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        buildings.add(building);
        for (int i = 0; i <= 2; i++) {
            world.possiblySpawnEnemies();

        }
        int zombieCount = 0;
        for (BasicEnemy e: world.getEnemies()) {
            if (e instanceof Zombie)
                zombieCount++;
        }
        assertEquals(5, world.getEnemies().size());
        tearDown();
    }

    @Test
    public void zombieSoldierTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        Zombie zombie = new Zombie(new PathPosition(1, world.getOrderedPath()));
        zombie.attack(soldier, 100, world);
        assertEquals(soldier.getMaxHealth().get() - 9999, soldier.getHealth().get());
        tearDown();
    }

    @Test
    public void zombieSoldierMoreTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        Zombie zombie = new Zombie(new PathPosition(1, world.getOrderedPath()));
        zombie.attack(soldier, 0, world);
        assertEquals(90, soldier.getHealth().get());
        tearDown();
    }

    @Test
    public void zombieEnemyTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Zombie zombie = new Zombie(new PathPosition(1, world.getOrderedPath()));
        Doggie doggie = new Doggie(new PathPosition(1, world.getOrderedPath()));
        zombie.attack(doggie, 100, world);
        assertEquals(998, doggie.getHealth().get());
        tearDown();
    }

    @Test
    public void vampireBasicTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        assertEquals(true, vampire.getType().get().equals("Vampire"));
        assertEquals(2, vampire.getBattleRadius());
        assertEquals(15, vampire.getSupportRadius());
    }

    @Test
    public void vampireCampfireTest() throws FileNotFoundException {
        
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Vampire vampire = new Vampire(new PathPosition(3, world.getOrderedPath()));
        Card card = world.loadCard("Campfire");
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        vampire.setOrderedPath(orderedPath);
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        buildings.add(building);
        vampire.move(new Pair<Integer, Integer> (character.getX(), character.getY()), buildings);
        assertEquals(1, vampire.getX());
        assertEquals(0, vampire.getY());
        vampire.move(new Pair<Integer, Integer> (character.getX(), character.getY()), buildings);
        assertEquals(2, vampire.getX());
        assertEquals(1, vampire.getY());
        vampire.move(new Pair<Integer, Integer> (character.getX(), character.getY()), buildings);
        assertEquals(1, vampire.getX());
        assertEquals(2, vampire.getY());
    }

    @Test
    public void vampireCharacterTest() throws FileNotFoundException {
        
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Vampire vampire = new Vampire(new PathPosition(3, world.getOrderedPath()));
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        vampire.setOrderedPath(orderedPath);
        vampire.attack(character, 100, world);
        assertEquals(990, character.getHealth().get());
    }
    
    @Test
    public void vampireCharacterMoreTest() throws FileNotFoundException {
        
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Vampire vampire = new Vampire(new PathPosition(3, world.getOrderedPath()));
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        vampire.setOrderedPath(orderedPath);
        character.equipShield();
        vampire.attack(character, 5, world);
        assertEquals(975, character.getHealth().get());
    }

    @Test
    public void vampireTranceTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        character.equipStaff();
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        Vampire vampire1 = new Vampire(new PathPosition(2, world.getOrderedPath()));
        Vampire vampire2 = new Vampire(new PathPosition(4, world.getOrderedPath()));
        assertEquals(false, vampire.ifDead());
        character.attack(vampire, 100);
        for (int i = 0; i <= 10; i++) {
            character.tranceAttack(vampire1, 0, world);
            character.tranceAttack(vampire2, 0, world);
        }
        assertEquals(vampire.getMaxHealth().get() - 2, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void vampireSoldierTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        Vampire vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        vampire.attack(soldier, 100, world);
        assertEquals(soldier.getMaxHealth().get() - 10, soldier.getHealth().get());
        tearDown();
    }

    @Test
    public void vampireSoldierMoreTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        Vampire vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        vampire.attack(soldier, 5, world);
        assertEquals(75, soldier.getHealth().get());
        tearDown();
    }

    @Test
    public void vampireSpawnTest() throws FileNotFoundException {
        
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Vampire vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        Card card = world.loadCard("VampireCastle");
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        buildings.add(building);
        for (int i = 0; i <= 3; i++) {
            world.possiblySpawnEnemies();
        }
        int vampireCount = 0;
        for (BasicEnemy e: world.getEnemies()) {
            if (e instanceof Vampire)
                vampireCount++;
        }
        assertEquals(3, vampireCount);
        tearDown();
    }

    @Test
    public void doggieSoldierTest() throws FileNotFoundException {
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        Doggie doggie = new Doggie(new PathPosition(1, world.getOrderedPath()));
        doggie.attack(soldier, 100, world);
        doggie.attack(soldier, 5, world);
        doggie.attack(doggie, 10, world);
        doggie.setTrance(0);
        doggie.reduceTrance();
        doggie.ifTrance();
        assertTrue(soldier.ifDead());
        assertFalse(doggie.ifDead());
        doggie.setHealth(0);
        assertTrue(doggie.ifDead());
    }

    @Test
    public void doggieSpawnTest() throws FileNotFoundException {
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        for (int i = 0; i < 400; i++) {
            world.runTickMoves();
            world.possiblySpawnBosses();
            world.updateLoop();
        }
        int count = 0;
        for (BasicEnemy e : world.getEnemies()) {
            if (e instanceof Doggie)
                count++;
        }
        assertEquals(1, count);
    }

    @Test
    public void ElonBasicTest() throws FileNotFoundException {
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        ElonMask elonMask = new ElonMask(new PathPosition(1, world.getOrderedPath()));
        assertEquals(5000, elonMask.getMaxHealth().get());
        assertEquals("ElonMask", elonMask.getType().get());
        assertEquals(1, elonMask.getBattleRadius());
        assertEquals(1, elonMask.getSupportRadius());
        character.attack(elonMask, 100);
        assertEquals(4995, elonMask.getHealth().get());
    }

    @Test
    public void ElonAuraTest() throws FileNotFoundException {
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        ElonMask elonMask = new ElonMask(new PathPosition(1, world.getOrderedPath()));
        world.possiblySpawnEnemies();
        elonMask.presenceAura(world);
        for (BasicEnemy e : world.getEnemies())
            assertEquals(15, e.getHealth().get());
    }

    @Test
    public void ElonSpawnTest() throws FileNotFoundException {
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        character.setExp(20000);
        for (int i = 0; i < 800; i++) {
            world.runTickMoves();
            world.possiblySpawnBosses();
            world.updateLoop();
        }
        int count = 0;
        for (BasicEnemy e : world.getEnemies()) {
            if (e instanceof ElonMask)
                count++;
        }
        assertEquals(1, count);
    }

    @Test
    public void ElonAttackTest() throws FileNotFoundException {
        List<Building> buildings = new ArrayList<Building>();
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        ElonMask elonMask = new ElonMask(new PathPosition(1, world.getOrderedPath()));
        elonMask.attack(soldier, 100, world);
        elonMask.attack(soldier, 5, world);
        elonMask.attack(character, 100, world);
        elonMask.attack(elonMask, 10, world);
        elonMask.setTrance(0);
        elonMask.reduceTrance();
        elonMask.ifTrance();
        assertFalse(soldier.ifDead());
        assertFalse(character.ifDead());
        assertFalse(elonMask.ifDead());
        elonMask.setHealth(0);
        assertTrue(elonMask.ifDead());
        elonMask.triggerDefeatFlag(world);
    }
}
