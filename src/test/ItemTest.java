package test;

import static org.junit.jupiter.api.Assertions.*;

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
        Potion potion = new Potion("Potion");
        assertEquals(true, item instanceof Potion);
        assertEquals(true, item.getIcon() != null);
        assertEquals(true, item.getTooltip() != null);
        tearDown();
    }

    @Test
    public void swordTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Sword");
        Sword sword = new Sword("Sword");
        assertEquals(true, item instanceof Sword);
        tearDown();
    }

    @Test
    public void stakeTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Stake");
        Stake stake = new Stake("Stake");
        assertEquals(true, item instanceof Stake);
        tearDown();
    }

    @Test
    public void armourTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Armour");
        Armour armour = new Armour("Armour");
        assertEquals(true, item instanceof Armour);
        tearDown();
    }

    @Test
    public void staffTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Staff");
        Staff staff = new Staff("Staff");
        assertEquals(true, item instanceof Staff);
        tearDown();
    }

    @Test
    public void theOneRingTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("TheOneRing");
        TheOneRing theOneRing = new TheOneRing("TheOneRing", 100);
        theOneRing.setHidden(0);
        assertTrue(theOneRing.getHidden() instanceof Rapier);
        theOneRing.setHidden(1);
        assertTrue(theOneRing.getHidden() instanceof TreeStump);
        theOneRing.setHidden(-1);
        assertEquals(true, item instanceof TheOneRing);
        tearDown();
    }
    
    @Test
    public void shieldTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Shield");
        Shield shield = new Shield("Shield");
        assertEquals(true, item instanceof Shield);
        tearDown();
    }

    @Test
    public void helmetTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Helmet");
        Helmet helmet = new Helmet("Helmet");
        assertEquals(true, item instanceof Helmet);
        tearDown();
    }

    @Test
    public void rapierTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("Rapier");
        Rapier Rapier = new Rapier("Rapier", 100);
        Rapier.setHidden(0);
        assertTrue(Rapier.getHidden() instanceof TheOneRing);
        Rapier.setHidden(1);
        assertTrue(Rapier.getHidden() instanceof TreeStump);
        Rapier.setHidden(-1);
        assertEquals(true, item instanceof Rapier);
        tearDown();
    }

    @Test
    public void treeStumpTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("TreeStump");
        TreeStump TreeStump = new TreeStump("TreeStump", 100);
        TreeStump.setHidden(0);
        assertTrue(TreeStump.getHidden() instanceof TheOneRing);
        TreeStump.setHidden(1);
        assertTrue(TreeStump.getHidden() instanceof Rapier);
        TreeStump.setHidden(-1);
        assertEquals(true, item instanceof TreeStump);
        tearDown();
    }

    @Test
    public void doggieCoinTest() throws FileNotFoundException {

        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Item item = world.addUnequipedItem("DoggieCoin");
        DoggieCoin DoggieCoin = new DoggieCoin("DoggieCoin");
        assertEquals(true, item instanceof DoggieCoin);
        tearDown();
    }

}
