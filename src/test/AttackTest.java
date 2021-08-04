package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;
import unsw.loopmania.PathPosition;
import unsw.loopmania.allies.Character;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Vampire;

/**
 * this class is a dummy class demonstrating how to setup tests for the project
 * you should setup additional test classes in a similar fashion, aiming to achieve high coverage.
 * A clickable "Run Test" link should appear if you have installed the Java Extension Pack properly.
 */
public class AttackTest extends TestSetup {
    @Test
    public void basicTest1() throws FileNotFoundException {
        // Test Sword
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipSword();
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        character.attack(vampire, 20);
        assertEquals(vampire.getMaxHealth().get() - 15, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void basicTest2() throws FileNotFoundException {
        // Test Stake
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipStake();
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        character.attack(vampire, 20);
        assertEquals(vampire.getMaxHealth().get() - 10 * 5, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void basicTest3() throws FileNotFoundException {
        // Test Staff
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipStaff();
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        character.attack(vampire, 20);
        assertEquals(vampire.getMaxHealth().get() - 2, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void basicTest4() throws FileNotFoundException {
        // Test Fist
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        character.attack(vampire, 20);
        assertEquals(vampire.getMaxHealth().get() - 5, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void complexTest1() throws FileNotFoundException {
        // Test Sword + Helmet (Attack)
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipHelmet();
        character.equipSword();
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        character.attack(vampire, 20);
        assertEquals(vampire.getMaxHealth().get() - 15, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void complexTest2() throws FileNotFoundException {
        // Test Sword + Helmet (Miss)
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipHelmet();
        character.equipSword();
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        character.attack(vampire, 10);
        assertEquals(vampire.getMaxHealth().get(), vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void complexTest3() throws FileNotFoundException {
        // Test Sword + Campfire
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipSword();
        character.setBuffCampfire(true);
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        character.attack(vampire, 20);
        assertEquals(vampire.getMaxHealth().get() - 2 * 15, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void complexTest4() throws FileNotFoundException {
        // Test Staff Magic + Campfire
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipStaff();
        character.setBuffCampfire(true);
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        character.attack(vampire, 90);
        assertEquals(vampire.getMaxHealth().get() - 2 * 2, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void complexTest5() throws FileNotFoundException {
        // Test Fist + Campfire
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.setBuffCampfire(true);
        Vampire vampire = new Vampire(new PathPosition(0, world.getOrderedPath()));
        character.attack(vampire, 20);
        assertEquals(vampire.getMaxHealth().get() - 2 * 5, vampire.getHealth().get());
        tearDown();
    }

    @Test
    public void complexTest6() throws FileNotFoundException {
        // Test Stake + Campfire
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipStake();
        character.setBuffCampfire(true);
        Slug slug = new Slug(new PathPosition(0, world.getOrderedPath()));
        character.attack(slug, 20);
        assertEquals(slug.getMaxHealth().get() - 2 * 5, slug.getHealth().get());
        tearDown();
    }
}
