package test;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.LoopManiaWorldControllerLoader;
import unsw.loopmania.LoopManiaWorldLoader;

/**
 * this class is a dummy class demonstrating how to setup tests for the project
 * you should setup additional test classes in a similar fashion, aiming to achieve high coverage.
 * A clickable "Run Test" link should appear if you have installed the Java Extension Pack properly.
 */
public class TestSetup {
    LoopManiaWorldLoader world_loader = null;
    LoopManiaWorld world = null;

    /*
    * To configure LoopManiaWorld for each test
    */
    void setup(String filename) throws FileNotFoundException {
        // load a new LoopManiaWorld
        world_loader = new LoopManiaWorldControllerLoader(filename);
        world = world_loader.load();
    }

    @AfterEach
    void tearDown() {
        world_loader = null;
        world = null;
    }
}
