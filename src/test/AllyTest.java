package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;
import unsw.loopmania.PathPosition;
import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.building.*;
import java.util.*;
import org.javatuples.Pair;
import unsw.loopmania.*;

public class AllyTest extends TestSetup {
    @Test
    public void soldierSlugTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        character.addSoldier(soldier);
        Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        character.soldierAttack(slug, 100, world);
        assertEquals(7, slug.getHealth().get());
        slug.setHealth(0);
        character.soldierAttack(slug, 100, world);
        assertEquals(0, slug.getHealth().get());
        tearDown();
    }

    @Test
    public void soldierZombieTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        character.addSoldier(soldier);
        Zombie zombie = new Zombie(new PathPosition(1, world.getOrderedPath()));
        character.soldierAttack(zombie, 100, world);
        assertEquals(12, zombie.getHealth().get());
        zombie.setHealth(0);
        character.soldierAttack(zombie, 100, world);
        assertEquals(0, zombie.getHealth().get());
        tearDown();
    }

    @Test
    public void soldierVampireTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        Soldier soldier = new Soldier(new PathPosition(0, world.getOrderedPath()));
        character.addSoldier(soldier);
        for (int i = 0; i < 160; i++) {
            Vampire vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
            character.soldierAttack(vampire, 100, world);
            vampire.attack(soldier, 100, world);
            character.moveDownPathSoldier();;
        }
        assertEquals(0, character.getSoldiers().size());
        tearDown();
    }

    @Test
    public void characterVampireTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Character character = world.getCharacter();
        character.equipStaff();
        Vampire vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        character.attack(vampire, 100);
        character.endBattle();
        assertEquals(-9901, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void runBattleTowerTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card card = world.loadCard("Tower");
        assertEquals(true, card instanceof TowerCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        Card campfireCard = world.loadCard("Campfire");
        Building campfireBuilding = world.convertCardToBuildingByCoordinates(campfireCard.getX(), campfireCard.getY(), 
                                orderedPath.get(1).getValue0()+2, orderedPath.get(1).getValue1());
        for (int i = 0; i < orderedPath.size(); i++) {
            Card cardVampireCastle = world.loadCard("VampireCastle");
            Building buildingVampireCastle = world.convertCardToBuildingByCoordinates(cardVampireCastle.getX(), cardVampireCastle.getY(), 
                                    orderedPath.get(i).getValue0()+1, orderedPath.get(i).getValue1());
        }
        for (int i = 0; i < orderedPath.size(); i++) {
            Card cardZombiePit = world.loadCard("ZombiePit");
            Building buildingZombiePit = world.convertCardToBuildingByCoordinates(cardZombiePit.getX(), cardZombiePit.getY(), 
                                    orderedPath.get(i).getValue0()+1, orderedPath.get(i).getValue1());
        }
        Character character = world.getCharacter();
        for (int i = 0; i < 160; i++) {
            world.possiblySpawnEnemies();
            world.runBattles();
            world.updateLoop();
        }
        assertTrue(character.getHealth().get() <= 1000);
        tearDown();
    }
}