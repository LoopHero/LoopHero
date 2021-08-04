package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;
import unsw.loopmania.allies.Character;

/**
 * this class is a dummy class demonstrating how to setup tests for the project
 * you should setup additional test classes in a similar fashion, aiming to achieve high coverage.
 * A clickable "Run Test" link should appear if you have installed the Java Extension Pack properly.
 */
public class DefenceTest extends TestSetup {
    @Test
    public void basicTest1() throws FileNotFoundException {
        // Test Armour
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipArmour();
        character.takingDamage(10);
        assertEquals(character.getMaxHealth().get() - 10 / 2, character.getHealth().get());
        tearDown();
    }

    @Test
    public void basicTest2() throws FileNotFoundException {
        // Test Helmet
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipHelmet();
        character.takingDamage(1);
        assertEquals(character.getMaxHealth().get(), character.getHealth().get());
        character.takingDamage(5);
        assertEquals(character.getMaxHealth().get(), character.getHealth().get());
        character.takingDamage(10);
        assertEquals(character.getMaxHealth().get() - (10 - 5), character.getHealth().get());
        tearDown();
    }

    @Test
    public void basicTest3() throws FileNotFoundException {
        // Test Armour && Helmet
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Character character = world.getCharacter();
        character.equipArmour();
        character.equipHelmet();
        character.takingDamage(10);
        assertEquals(character.getMaxHealth().get() - (int) Math.round(0.5 * (10 - 5)), character.getHealth().get());
        tearDown();
    }
}
