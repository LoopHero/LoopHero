package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;


import unsw.loopmania.*;
import unsw.loopmania.items.*;

public class ItemTest extends TestSetup {
    @Test
    public void potionTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Potion");
        assertEquals(true, item instanceof Potion);
        tearDown();
    }

    @Test
    public void swordTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Sword");
        assertEquals(true, item instanceof Sword);
        tearDown();
    }

    @Test
    public void stakeTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Stake");
        assertEquals(true, item instanceof Stake);
        tearDown();
    }

    @Test
    public void armourTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Armour");
        assertEquals(true, item instanceof Armour);
        tearDown();
    }

    @Test
    public void staffTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Staff");
        assertEquals(true, item instanceof Staff);
        tearDown();
    }

    @Test
    public void theOneRingTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("TheOneRing");
        assertEquals(true, item instanceof TheOneRing);
        tearDown();
    }
    
    @Test
    public void shieldTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Shield");
        assertEquals(true, item instanceof Shield);
        tearDown();
    }

    @Test
    public void helmetTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Helmet");
        assertEquals(true, item instanceof Helmet);
        tearDown();
    }
}
