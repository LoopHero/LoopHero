package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;

/**
 * this class is a dummy class demonstrating how to setup tests for the project
 * you should setup additional test classes in a similar fashion, aiming to achieve high coverage.
 * A clickable "Run Test" link should appear if you have installed the Java Extension Pack properly.
 */
public class GoalTest extends TestSetup {
    @Test
    public void basicTest1() throws FileNotFoundException {
        // Test 2 cycles
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        assertEquals(false, world.goalAchieved());
        for (int i = 0; i <= 16; i++) world.runTickMoves();
        assertEquals(true, world.goalAchieved());
        world.getGoal().removeSubgoal(world.getGoal());
        world.getGoal().addSubgoal(world.getGoal());
        world.getGoal().getSubgoals();
        tearDown();
    }

    @Test
    public void basicTest2() throws FileNotFoundException {
        // Test 2 exp
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test2.json");
        assertEquals(false, world.goalAchieved());
        world.characterExpGain(2);
        assertEquals(true, world.goalAchieved());
        tearDown();
    }

    @Test
    public void basicTest3() throws FileNotFoundException {
        // Test 2 gold
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test3.json");
        assertEquals(false, world.goalAchieved());
        world.characterGoldGain(2);
        assertEquals(true, world.goalAchieved());
        tearDown();
    }

    @Test
    public void complexTest1() throws FileNotFoundException {
        // Test 2 cycles && 2 exp
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_complex_test.json");
        assertEquals(false, world.goalAchieved());
        for (int i = 0; i <= 16; i++) world.runTickMoves();
        world.characterExpGain(2);
        assertEquals(true, world.goalAchieved());
        tearDown();
    }

    @Test
    public void complexTest2() throws FileNotFoundException {
        // Test 2 cycles && 2 gold
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_complex_test.json");
        assertEquals(false, world.goalAchieved());
        for (int i = 0; i <= 16; i++) world.runTickMoves();
        world.characterGoldGain(2);
        assertEquals(true, world.goalAchieved());
        tearDown();
    }

    @Test
    public void complexTest3() throws FileNotFoundException {
        // Test 2 cycles && (2 exp || 2 gold)
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_complex_test.json");
        assertEquals(false, world.goalAchieved());
        for (int i = 0; i <= 16; i++) world.runTickMoves();
        world.characterExpGain(2);
        world.characterGoldGain(2);
        assertEquals(true, world.goalAchieved());
        assertEquals("(AND 2 Cycles (OR 2 Exp 2 Gold))", world.getGoal().toString());
        assertEquals(true, world.getGoal().getSatisfiedProperty().get());
        tearDown();
    }
}
