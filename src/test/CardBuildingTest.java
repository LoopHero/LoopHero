package test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.building.*;
import unsw.loopmania.*;

import java.util.*;
import org.javatuples.Pair;

public class CardBuildingTest extends TestSetup {
    @Test
    public void villageTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card card = world.loadCard("Village");
        assertEquals(true, card instanceof VillageCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        for (int i = 0; i <= 16; i++) world.runTickMoves();
        assertEquals(true, building instanceof VillageBuilding);
        tearDown();
    }

    @Test
    public void trapTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card card = world.loadCard("Trap");
        assertEquals(true, card instanceof TrapCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        assertEquals(true, building instanceof TrapBuilding);
        for (int i = 0; i <= 16; i++) world.runTickMoves();
        tearDown();
    }

    @Test
    public void towerTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card card = world.loadCard("Tower");
        assertEquals(true, card instanceof TowerCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        assertEquals(true, building instanceof TowerBuilding);
        TowerBuilding tower = (TowerBuilding) building;
        assertEquals(3, tower.getRadius());
        tearDown();
    }

    @Test
    public void zombiePitTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card card = world.loadCard("ZombiePit");
        assertEquals(true, card instanceof ZombiePitCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        assertEquals(true, building instanceof ZombiePitBuilding);
        tearDown();
    }

    @Test
    public void vampireCastleTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card card = world.loadCard("VampireCastle");
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        assertEquals(true, building instanceof VampireCastleBuilding);
        tearDown();
    }

    @Test
    public void campfireTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card card = world.loadCard("Campfire");
        assertEquals(true, card instanceof CampfireCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        assertEquals(true, building instanceof CampfireBuilding);
        CampfireBuilding campfire = (CampfireBuilding) building;
        assertEquals(3, campfire.getRadius());
        tearDown();
    }

    @Test
    public void barracksTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card card = world.loadCard("Barracks");
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        assertEquals(true, building instanceof BarracksBuilding);
        tearDown();
    }

    @Test
    public void heroCastleTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        SimpleIntegerProperty x = new SimpleIntegerProperty(orderedPath.get(0).getValue0());
        SimpleIntegerProperty y = new SimpleIntegerProperty(orderedPath.get(0).getValue1());
        Building building = new HeroCastleBuilding(x, y, "HeroCastle", 0);
        assertEquals(true, building instanceof HeroCastleBuilding);
        tearDown();
    }

    @Test
    public void zombiePitTrapTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card zombiePitCard = world.loadCard("ZombiePit");
        Card trapCard = world.loadCard("Trap");
        assertEquals(true, zombiePitCard instanceof ZombiePitCard);
        assertEquals(true, trapCard instanceof TrapCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building zombiePit = world.convertCardToBuildingByCoordinates(zombiePitCard.getX(), zombiePitCard.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        Building trap = world.convertCardToBuildingByCoordinates(trapCard.getX(), trapCard.getY(), 
                            orderedPath.get(1).getValue0(), orderedPath.get(1).getValue1());
        for (int i = 0; i <= 160; i++) {
            world.runTickMoves();
            world.possiblySpawnEnemies();
        }
        tearDown();
    }

    @Test
    public void zombiePitTowerTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card zombiePitCard = world.loadCard("ZombiePit");
        Card towerCard = world.loadCard("Tower");
        assertEquals(true, zombiePitCard instanceof ZombiePitCard);
        assertEquals(true, towerCard instanceof TowerCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building zombiePit = world.convertCardToBuildingByCoordinates(zombiePitCard.getX(), zombiePitCard.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        Building tower = world.convertCardToBuildingByCoordinates(towerCard.getX(), towerCard.getY(), 
                            orderedPath.get(1).getValue0(), orderedPath.get(1).getValue1());
        for (int i = 0; i <= 160; i++) {
            world.runTickMoves();
            world.possiblySpawnEnemies();
            world.runBattles();
        }
        tearDown();
    }

    @Test
    public void positionTakenTest1() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card zombiePitCard = world.loadCard("ZombiePit");
        Card towerCard = world.loadCard("Tower");
        assertEquals(true, zombiePitCard instanceof ZombiePitCard);
        assertEquals(true, towerCard instanceof TowerCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building zombiePit = world.convertCardToBuildingByCoordinates(zombiePitCard.getX(), zombiePitCard.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        assertEquals(false, world.canBuildingPlace(towerCard.getX(), towerCard.getX(), zombiePit.getX(), zombiePit.getY()));
        tearDown();
    }

    @Test
    public void positionTakenTest2() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card zombiePitCard = world.loadCard("ZombiePit");
        Card towerCard = world.loadCard("Tower");
        assertEquals(true, zombiePitCard instanceof ZombiePitCard);
        assertEquals(true, towerCard instanceof TowerCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building zombiePit = world.convertCardToBuildingByCoordinates(zombiePitCard.getX(), zombiePitCard.getY(), 
                                orderedPath.get(0).getValue0()+1, orderedPath.get(0).getValue1()+1);
        zombiePit.destroy();                     
        assertEquals(true, world.canBuildingPlace(towerCard.getX(), towerCard.getX(), zombiePit.getX(), zombiePit.getY()));
        tearDown();
    }

    @Test
    public void positionTakenTest3() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card zombiePitCard1 = world.loadCard("ZombiePit");
        Card zombiePitCard2 = world.loadCard("ZombiePit");
        assertEquals(true, zombiePitCard1 instanceof ZombiePitCard);
        assertEquals(true, zombiePitCard2 instanceof ZombiePitCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building zombiePit1 = world.convertCardToBuildingByCoordinates(zombiePitCard1.getX(), zombiePitCard1.getY(), 
                                orderedPath.get(0).getValue0()+1, orderedPath.get(0).getValue1()+1);                    
        assertEquals(false, world.canBuildingPlace(zombiePitCard2.getX(), zombiePitCard2.getY(), 
                                zombiePit1.getX(), zombiePit1.getY()));
        tearDown();
    }

    @Test
    public void positionTakenTest4() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        Card zombiePitCard = world.loadCard("ZombiePit");
        Card towerCard = world.loadCard("Tower");
        assertEquals(true, zombiePitCard instanceof ZombiePitCard);
        assertEquals(true, towerCard instanceof TowerCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building zombiePit = world.convertCardToBuildingByCoordinates(zombiePitCard.getX(), zombiePitCard.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());                   
        assertEquals(false, world.canBuildingPlace(towerCard.getX(), towerCard.getX(),
                                orderedPath.get(1).getValue0(), orderedPath.get(1).getValue1()));
        tearDown();
    }

    @Test
    public void maxCardsTest() throws FileNotFoundException {
        
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_basic_test1.json");
        for (int i = 0; i < 10; i++) {
            Card card = world.loadCard("Tower");
            assertEquals(true, card instanceof TowerCard);
        }
        tearDown();
    }
}
