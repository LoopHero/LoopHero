package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;

import unsw.loopmania.Card;
import unsw.loopmania.building.*;
import unsw.loopmania.enemies.*;
import unsw.loopmania.items.*;

/**
 * this class is a dummy class demonstrating how to setup tests for the project
 * you should setup additional test classes in a similar fashion, aiming to achieve high coverage.
 * A clickable "Run Test" link should appear if you have installed the Java Extension Pack properly.
 */
public class WorldTest extends TestSetup {
    @Test
    public void Test1() throws FileNotFoundException {
        // Test Hero Castle Position
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.getHeroCastlePosition();
        Pair<Integer, Integer> pair = new Pair<Integer, Integer>(0, 0);
        assertEquals(pair.getValue0(), world.getHeroCastlePosition().getValue0());
        assertEquals(pair.getValue1(), world.getHeroCastlePosition().getValue1());
        tearDown();
    }

    @Test
    public void Test2() throws FileNotFoundException {
        // Test Current Loop
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(0, world.getCurrentLoop().get());
        tearDown();
    }

    
    @Test
    public void Test3() throws FileNotFoundException {
        // Test Height
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(3, world.getHeight());
        tearDown();
    }

    @Test
    public void Test4() throws FileNotFoundException {
        // Test Game Over (Goal achieved)
        JFXPanel jfxPanel = new JFXPanel();
        setup("goal_initial_achived.json");
        world.updateGoal();
        world.gameOver();
        assertEquals(true, world.goalAchieved());
        tearDown();
    }

    @Test
    public void Test5() throws FileNotFoundException {
        // Test Game Over (Goal unachieved)
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.updateGoal();
        world.gameOver();
        assertEquals(false, world.goalAchieved());
        tearDown();
    }

    @Test
    public void Test6() throws FileNotFoundException {
        // Test Clean Enemy
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(new ArrayList<BasicEnemy>(), world.cleanEnemies());
        tearDown();
    }

    @Test
    public void Test7() throws FileNotFoundException {
        // Test Add & Remove Unequiped Item By Coordinates
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        for (int i = 0; i < 18; i++) {
            assertEquals(true, world.addUnequipedItem("Sword") instanceof Sword);
        }
        world.removeUnequippedInventoryItemByCoordinates(0, 0);
        tearDown();
    }

    @Test
    public void Test8() throws FileNotFoundException {
        // Test Set Buildings
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Card zombiePitCard = world.loadCard("ZombiePit");
        assertEquals(true, zombiePitCard instanceof ZombiePitCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building zombiePit = world.convertCardToBuildingByCoordinates(zombiePitCard.getX(), zombiePitCard.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        List<Building> buildings = new ArrayList<Building>();
        buildings.add(zombiePit);
        world.setBuildings(buildings);
        tearDown();
    }

    @Test
    public void Test9() throws FileNotFoundException {
        // Test Add & Remove Equiped Item
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Sword sword = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1), "Sword");
        world.addEquippedInventoryItem(sword);
        assertEquals(true, sword instanceof Sword);
        world.removeEquippedInventoryItem(sword);
        tearDown();
    }

    @Test
    public void Test10() throws FileNotFoundException {
        // Test Add & Remove Equiped Weapon By Coordinates
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Sword sword = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1), "Sword");
        assertEquals(true, sword instanceof Sword);
        world.addEquippedInventoryItem(sword);
        world.removeEquippedInventoryItemByCoordinates(0, 1);
        tearDown();
    }

    @Test
    public void Test11() throws FileNotFoundException {
        // Test Add & Remove Equiped Armour By Coordinates
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Armour armour = new Armour(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1), "Armour");
        assertEquals(true, armour instanceof Armour);
        world.addEquippedInventoryItem(armour);
        world.removeEquippedInventoryItemByCoordinates(1, 1);
        tearDown();
    }

    @Test
    public void Test12() throws FileNotFoundException {
        // Test Add & Remove Equiped Helmet By Coordinates
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Helmet helmet = new Helmet(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0), "Helmet");
        assertEquals(true, helmet instanceof Helmet);
        world.addEquippedInventoryItem(helmet);
        world.removeEquippedInventoryItemByCoordinates(1, 0);
        tearDown();
    }

    @Test
    public void Test13() throws FileNotFoundException {
        // Test Add & Remove Equiped Shield By Coordinates
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Shield shield = new Shield(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1), "Shield");
        assertEquals(true, shield instanceof Shield);
        world.addEquippedInventoryItem(shield);
        world.removeEquippedInventoryItemByCoordinates(2, 1);
        tearDown();
    }

    @Test
    public void Test14() throws FileNotFoundException {
        // Test Remove Unexisting Item By Coordinates
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(null, world.getEquippedInventoryItemEntityByCoordinates(0, 0));
        tearDown();
    }

    @Test
    public void Test15() throws FileNotFoundException {
        // Test Character Stats 
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals("1000/1000", world.getCharacterHealth().get());
        assertEquals(0, world.getCharacterGold().get());
        assertEquals(0, world.getCharacterExp().get());
        assertEquals(false, world.isCharacterDead());
        tearDown();
    }

    @Test
    public void Test16() throws FileNotFoundException {
        // Test Use Health Potion
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.addUnequipedItem("Sword");
        world.useHealthPotion();
        world.addUnequipedItem("Potion");
        world.useHealthPotion();
        world.removeItem("Potion");
        assertEquals("1000/1000", world.getCharacterHealth().get());
        tearDown();
    }

    @Test
    public void Test17() throws FileNotFoundException {
        // Test Equip Items
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.equipItem(new Armour(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1), "Armour"));
        world.equipItem(new Helmet(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0), "Helmet"));
        world.equipItem(new Shield(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1), "Shield"));
        world.equipItem(new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1), "Shield"));
        world.equipItem(new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1), "Staff"));
        world.equipItem(new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1), "Stake"));
        world.equipItem(new Rapier(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1), "Rapier"));
        world.equipItem(new TreeStump(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1), "TreeStump"));
        tearDown();
    }

    @Test
    public void Test18() throws FileNotFoundException {
        // Test Spawn Soldiers
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(0, world.getSoldiers().size());
        world.possiblySpawnSoldiers();
        world.spawnZombieOnSoldier();
        tearDown();
    }

    @Test
    public void Test19() throws FileNotFoundException {
        // Test Whether Hero is at Castle
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(true, world.isAtHeroCastle());
        world.characterHealthLoss(0);
        tearDown();
    }

    @Test
    public void Test20() throws FileNotFoundException {
        // Test Use One Ring
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(false, world.useOneRing());
        assertEquals(true, world.addUnequipedItem("TheOneRing") instanceof TheOneRing);
        assertEquals(true, world.useOneRing());
        tearDown();
    }

    @Test
    public void Test21() throws FileNotFoundException {
        // Test Shop
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(true, world.shouldShopAppear());
        assertEquals(false, world.shouldShopAppear());
        tearDown();
    }

    @Test
    public void Test22() throws FileNotFoundException {
        // Test Equip Slot
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Armour armour = new Armour(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1), "Armour");
        Helmet helmet = new Helmet(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0), "Helmet");
        Shield shiled = new Shield(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1), "Shield");
        Sword sword = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1), "Shield");
        Staff staff = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1), "Staff");
        Stake stake = new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1), "Stake");
        assertEquals(true, world.checkEquipSlot(armour, 1, 1, null));
        assertEquals(true, world.checkEquipSlot(helmet, 1, 0, null));
        assertEquals(true, world.checkEquipSlot(shiled, 2, 1, null));
        assertEquals(true, world.checkEquipSlot(sword, 0, 1, null));
        assertEquals(true, world.checkEquipSlot(staff, 0, 1, null));
        assertEquals(true, world.checkEquipSlot(stake, 0, 1, null));
        assertEquals(false, world.checkEquipSlot(armour, 0, 0, null));
        tearDown();
    }

    @Test
    public void Test23() throws FileNotFoundException {
        // Test Elon Defeated
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.showBossState();
        assertEquals(false, world.doDoggieCoinPriceDrop());
        world.ElonDefeated();
        assertEquals(true, world.doDoggieCoinPriceDrop());
        world.doggieDefeated();
        world.showBossState();
        tearDown();
    }

    @Test
    public void Test24() throws FileNotFoundException {
        // Test Game Mode
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.showGameMode();
        world.setGameModeStandard();
        world.showGameMode();
        assertEquals(0, world.getMode());
        assertEquals(false, world.isBerserkerMode());
        world.setGameModeBerserker();
        world.showGameMode();
        assertEquals(true, world.isBerserkerMode());
        assertEquals(false, world.isSurvivalMode());
        world.setGameModeSurvival();
        world.showGameMode();
        assertEquals(true, world.isSurvivalMode());
        assertEquals(false, world.isConfusionMode());
        world.setGameModeConfusion();
        world.showGameMode();
        assertEquals(true, world.isConfusionMode());
        tearDown();
    }

    @Test
    public void Test25() throws FileNotFoundException {
        // Test Add Unequipped Rare Item & Confusion Mode
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.setGameModeConfusion();
        assertEquals(true, world.addUnequipedItem("TheOneRing") instanceof RareItem);
        tearDown();
    }

    @Test
    public void Test26() throws FileNotFoundException {
        // Test Load Unexisting Cards & Unexisting Items
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(null, world.loadCard("error"));
        assertEquals(null, world.addUnequipedItem("error"));
        tearDown();
    }

    @Test
    public void Test27() throws FileNotFoundException {
        // Test Convert Unexisting Buildings & Unexisting Cards
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(null, world.convertTypetoBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0), "error"));
        assertEquals(true, world.loadCard("Village") instanceof VillageCard);
        assertEquals(null, world.convertCardIndextoCard(-1, -1));
        tearDown();
    }

    @Test
    public void Test28() throws FileNotFoundException {
        // Test Is Hero at Castle
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        for (int i = 0; i < 6; i++) {
            world.runTickMoves();
            assertEquals(false, world.isAtHeroCastle());
        }
        tearDown();
    }

    @Test
    public void Test29() throws FileNotFoundException {
        // Test Spawn Elon Mask (can not spawn)
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.ElonDefeated();
        for (int i = 0; i < 200; i++) {
            assertEquals(0, world.possiblySpawnBosses().size());
        }
        tearDown();
    }

    @Test
    public void Test30() throws FileNotFoundException {
        // Test Spawn Elon Mask (can spawn)
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        for (int i = 0; i < 400; i++) {
            world.runTickMoves();
        }
        world.characterExpGain(20000);
        for (int i = 0; i < 200; i++) {
            assertEquals(true, world.possiblySpawnBosses() != null);
        }
        world.ElonDefeated();
        for (int i = 0; i < 200; i++) {
            assertEquals(true, world.possiblySpawnBosses() != null);
        }
        tearDown();
    }

    @Test
    public void Test31() throws FileNotFoundException {
        // Test Clean Dead Enemies
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.spawnSlug(world.getEnemies());
        for (BasicEnemy enemy : world.getEnemies()) {
            enemy.setHealth(-1);
        }
        world.cleanEnemies();
        assertEquals(1, world.getEnemies().size());
        tearDown();
    }

    @Test
    public void Test32() throws FileNotFoundException {
        // Test Clean Alive Enemies
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.spawnSlug(world.getEnemies());
        world.cleanEnemies();
        assertEquals(2, world.getEnemies().size());
        tearDown();
    }

    @Test
    public void Test33() throws FileNotFoundException {
        // Test Use One Ring (Confusion Mode)
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        assertEquals(true, world.addUnequipedItem("Potion") instanceof Potion);
        assertEquals(true, world.addUnequipedItem("Rapier") instanceof Rapier);
        assertEquals(false, world.useOneRing());
        world.setGameModeConfusion();
        for (int i = 0; i < 20; i++) {
            assertEquals(true, world.addUnequipedItem("Rapier") instanceof Rapier);
            assertEquals(true, world.addUnequipedItem("TreeStump") instanceof TreeStump);
        }
        assertEquals(true, world.useOneRing());
        tearDown();
    }

    @Test
    public void Test34() throws FileNotFoundException {
        // Test Get Equipped Inventory ItemEntity By Coordinates (Rare Items)
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.addEquippedInventoryItem(world.addUnequipedItem("Potion"));
        world.addEquippedInventoryItem(world.addUnequipedItem("TheOneRing"));
        world.addEquippedInventoryItem(world.addUnequipedItem("Rapier"));
        world.addEquippedInventoryItem(world.addUnequipedItem("TreeStump"));
        assertEquals(true, world.getEquippedInventoryItemEntityByCoordinates(0, 0) == null);
        tearDown();
    }

    @Test
    public void Test35() throws FileNotFoundException {
        // Test Get Equipped Inventory ItemEntity By Coordinates (Confusion Mode & Rare Items)
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.setGameModeConfusion();
        world.addEquippedInventoryItem(world.addUnequipedItem("Potion"));
        for (int i = 0; i < 20; i++) {
            world.addEquippedInventoryItem(world.addUnequipedItem("TheOneRing"));
            world.addEquippedInventoryItem(world.addUnequipedItem("Rapier"));
            world.addEquippedInventoryItem(world.addUnequipedItem("TreeStump"));
        }
        assertEquals(true, world.getEquippedInventoryItemEntityByCoordinates(0, 0) == null);
        assertEquals(false, world.getEquippedInventoryItemEntityByCoordinates(2, 1) == null);
        assertEquals(true, world.getEquippedInventoryItemEntityByCoordinates(2, 0) == null);
        assertEquals(false, world.getEquippedInventoryItemEntityByCoordinates(0, 1) == null);
        assertEquals(true, world.getEquippedInventoryItemEntityByCoordinates(-1, 1) == null);
        assertEquals(true, world.getEquippedInventoryItemEntityByCoordinates(0, -1) == null);
        tearDown();
    }

    @Test
    public void Test36() throws FileNotFoundException {
        // Test Check Equip Slot (Rare Items)
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        world.setGameModeConfusion();
        boolean rapier = false;
        boolean treeStump = false;
        for (int i = 0; i < 20; i++) {
            if (world.checkEquipSlot(world.addUnequipedItem("Rapier"), 2, 1, new SimpleBooleanProperty(false))) {
                rapier = true;
            }
            if (world.checkEquipSlot(world.addUnequipedItem("TreeStump"), 0, 1, new SimpleBooleanProperty(false))) {
                treeStump = true;
            }
        }
        assertEquals(true, rapier);
        assertEquals(true, treeStump);
        tearDown();
    }

    @Test
    public void Test37() throws FileNotFoundException {
        // Test Possibly Spawn Soldiers
        JFXPanel jfxPanel = new JFXPanel();
        setup("basic_world_with_player.json");
        Card barracksCard = world.loadCard("Barracks");
        assertEquals(true, barracksCard instanceof BarracksCard);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Building barracks = world.convertCardToBuildingByCoordinates(barracksCard.getX(), barracksCard.getY(), 
                                orderedPath.get(0).getValue0(), orderedPath.get(0).getValue1());
        List<Building> buildings = new ArrayList<Building>();
        buildings.add(barracks);
        world.setBuildings(buildings);
        assertEquals(true, world.possiblySpawnSoldiers() != null);
        tearDown();
    }
}
