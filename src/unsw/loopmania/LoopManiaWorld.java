package unsw.loopmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import unsw.loopmania.items.*;
import unsw.loopmania.enemies.*;
import unsw.loopmania.allies.Character;
import unsw.loopmania.allies.Soldier;
import unsw.loopmania.building.*;
import unsw.loopmania.goal.*;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one entity
 * can occupy the same square.
 */
public class LoopManiaWorld {
    // TODO = add additional backend functionality

    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;

    /**
     * width of the world in GridPane cells
     */
    private int width;

    /**
     * height of the world in GridPane cells
     */
    private int height;

    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;

    private Character character;

    // TODO = add more lists for other entities, for equipped inventory items,
    // etc...
    private List<Soldier> soldiers;
    // TODO = expand the range of enemies
    private List<BasicEnemy> enemies;

    // TODO = expand the range of cards
    private List<Card> cardEntities;

    // TODO = expand the range of items
    List<Entity> unequippedInventoryItems;
    List<Entity> equippedInventoryItems;

    // TODO = expand the range of buildings
    private List<Building> buildingEntities;

    private Goal goal;
    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse
     * them
     */
    private List<Pair<Integer, Integer>> orderedPath;

    public Pair<Integer, Integer> getHeroCastlePosition() {
        return orderedPath.get(0);
    }

    // added loop counter
    private FloatProperty loop;
    private int moves;
    private int loopForShop; // shop appears for longer and longer interval
    private int loopSinceLastShop; // shop appears for longer and longer interval

    // mode flag
    private int mode;

    /**
     * create the world (constructor)
     * 
     * @param width       width of world in number of cells
     * @param height      height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing
     *                    position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        this.width = width;
        this.height = height;
        nonSpecifiedEntities = new ArrayList<>();
        character = null;
        enemies = new ArrayList<>();
        soldiers = new ArrayList<>();
        cardEntities = new ArrayList<>();
        unequippedInventoryItems = new ArrayList<>();
        equippedInventoryItems = new ArrayList<>();
        this.orderedPath = orderedPath;
        buildingEntities = new ArrayList<>();
        loop = new SimpleFloatProperty(0);
        this.goal = null;
        moves = 0;
        loopForShop = 1;
        loopSinceLastShop = 0;
        mode = -1;
    }

    /**
     * Display game mode in terminal
     */
    public void showGameMode(){
        switch (mode){
            case 0:
                System.out.println("Gamemode: StandardMode");
                break;
            case 1:
                System.out.println("Gamemode: BerserkerMode");
                break;
            case 2:
                System.out.println("Gamemode: SurvivalMode");
                break;
            default:
                System.out.println("Gamemode: Not initialized");
                break;
        }
    }

    /**
     * setGameMode to Standard
     */
    public void setGameModeStandard(){
        mode = 0;
    }

    /**
     * setGameMode to Berserker
     */
    public void setGameModeBerserker(){
        mode = 1;
    }

    /**
     * setGameMode to Survival
     */
    public void setGameModeSurvival(){
        mode = 2;
    }

    /**
     * assert if gamemode is Berserker
     * @return
     */
    public Boolean isBerserkerMode(){
        return mode == 1;
    }

    /**
     * assert if gamemode is Survival
     * @return
     */
    public Boolean isSurvivalMode(){
        return mode == 2;
    }

    /**
     * Returns gamemode code, check if gamemode is selected or not
     * @return
     */
    public int getMode(){
        return this.mode;
    }

    /**
     * returns current list of enemy
     * @return
     */
    public List<BasicEnemy> getEnemies() {
        return this.enemies;
    }

    /**
     * bind list of buildings to world
     * @param buildings
     */
    public void setBuildings(List<Building> buildings) {
        this.buildingEntities = buildings;
    }

    /**
     * returns binded building list
     * @return
     */
    public List<Building> getBuildings() {
        return this.buildingEntities;
    }

    /**
     * get current loop number
     * @return
     */
    public FloatProperty getCurrentLoop() {
        return loop;
    }

    /**
     * update loop number
     */
    public void updateLoop() {
        loop.set((float) moves / orderedPath.size());
    }

    /**
     * updateBuildingSkill
     */
    public void updateBuildingSkill() {
        for (Building Building : buildingEntities) {
            Building.timeUpdate(this);
        }

    }

    /**
     * updates character move count for building
     */
    public void updateBuildingLoop() {
        for (Building building : buildingEntities)
            building.addMoves();
    }

    /**
     * get world width
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * get world heigtht
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * returns player move path
     * @return
     */
    public List<Pair<Integer, Integer>> getOrderedPath() {
        return orderedPath;
    }

    /**
     * returns character position
     * @return
     */
    public Pair<Integer, Integer> getCharacterPos() {
        return new Pair<Integer, Integer>(character.getX(), character.getY());
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity
     * out of the file
     * 
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return this.character;
    }

    /**
     * bind goal to world
     * @return
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    /**
     * return goal
     * @return
     */
    public Goal getGoal() {
        return goal;
    }

    /**
     * update goal
     */
    public void updateGoal() {
        goal.update();
    }

    /**
     * game over
     */
    public void gameOver() {
        System.out.println("Game Over!");
        if (goalAchieved())
            System.out.println("Congrat! You finished the game!");
        else
            System.out.println("Oppps. You failed. Please retry!");
    }

    /**
     * goal achieved
     * @return
     */
    public boolean goalAchieved() {
        return this.goal.isSatisfied();
    }

    /**
     * add a generic entity (without it's own dedicated method for adding to the
     * world)
     * 
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        // TODO = if more specialised types being added from main menu, add more methods
        // like this with specific input types...
        nonSpecifiedEntities.add(entity);
    }

    /**
     * spawnSlug
     * @param spawningEnemies
     */
    public void spawnSlug(List<BasicEnemy> spawningEnemies) {
        Slug slug = new Slug(new PathPosition(0, orderedPath));
        slug.setOrderedPath(orderedPath);
        Boolean slugSpawned = slug.setSlugPosition(character.getX(), character.getY(), countSlugs());
        if (slugSpawned) {
            enemies.add(slug);
            spawningEnemies.add(slug);
        }
        return;
    }

    /**
     * spawnZombie
     * @param spawningEnemies
     */
    public void spawnZombie(List<BasicEnemy> spawningEnemies) {
        for (int i = 0; i < buildingEntities.size(); i++) {
            if (buildingEntities.get(i) instanceof ZombiePitBuilding) {
                Zombie zombie = new Zombie(new PathPosition(0, orderedPath));
                zombie.setOrderedPath(orderedPath);
                Boolean zombieSpawned =  zombie.setZombiePosition(countZombies(), countZombiePits(), buildingEntities);
                if (zombieSpawned) {
                    enemies.add(zombie);
                    spawningEnemies.add(zombie);
                }
            }
        }
        return;
    }

    /**
     * spawnVampire
     * @param spawningEnemies
     */
    public void spawnVampire(List<BasicEnemy> spawningEnemies) {
        for (int i = 0; i < buildingEntities.size(); i++) {
            if (buildingEntities.get(i) instanceof VampireCastleBuilding) {
                Vampire vampire = new Vampire(new PathPosition(0, orderedPath));
                vampire.setOrderedPath(orderedPath);
                Boolean vampireSpawned =  vampire.setVampirePosition(countVampires(), countVampireCastles(), buildingEntities);
                if (vampireSpawned) {
                    enemies.add(vampire);
                    spawningEnemies.add(vampire);
                }
            }
        }
        return;
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     * 
     * @return list of the enemies to be displayed on screen
     */
    public List<BasicEnemy> possiblySpawnEnemies() {
        // TODO = expand this very basic version
        List<BasicEnemy> spawningEnemies = new ArrayList<BasicEnemy>();
        spawnSlug(spawningEnemies);
        spawnZombie(spawningEnemies);
        spawnVampire(spawningEnemies);
        return spawningEnemies;
    }

    /**
     * clean enemies
     * 
     * 
     */
    public List<BasicEnemy> cleanEnemies() {
        List<BasicEnemy> deadEnemies = new ArrayList<BasicEnemy>();
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).getHealth().get() <= 0) {
                deadEnemies.add(enemies.get(i));
                enemies.get(i).destroy();
                enemies.remove(enemies.get(i));
            }
        }
        return deadEnemies;
    }

    private boolean checkBuffCampfire() {
        return character.checkBuffCampfire(buildingEntities);
    }

    /**
     * run the expected battles in the world, based on current world state
     * 
     * @return list of enemies which have been killed
     */
    public void runBattles() {
        Random rd = new Random(System.currentTimeMillis());
        character.setBuffCampfire(checkBuffCampfire());
        // TODO = modify this - currently the character automatically wins all battles
        // without any damage!
        // List<BasicEnemy> defeatedEnemies = new ArrayList<BasicEnemy>();
        // for (BasicEnemy e : enemies) {
        // this for loop should avord concurrent modification exception
        // if more enemies are added to enemies[]
        for (int i = 0; i < enemies.size(); i++) {
            BasicEnemy e = enemies.get(i);
            
            // Pythagoras: a^2+b^2 < radius^2 to see if within radius
            // TODO = you should implement different RHS on this inequality, based on
            // influence radii and battle radii
            // if (Math.pow((character.getX() - e.getX()), 2) + Math.pow((character.getY() -
            // e.getY()), 2) < 4) {
            // // fight...
            // defeatedEnemies.add(e);
            // }
            character.runBattle(e, rd, this, buildingEntities);
        }
        character.endBattle();
    }

    private Item convertNametoItem(SimpleIntegerProperty x, SimpleIntegerProperty y, String itemName) {
        switch (itemName) {
            case "Sword":
                return new Sword(x, y, itemName);
            case "Armour":
                return new Armour(x, y, itemName);
            case "Helmet":
                return new Helmet(x, y, itemName);
            case "Stake":
                return new Stake(x, y, itemName);
            case "Shield":
                return new Shield(x, y, itemName);
            case "Staff":
                return new Staff(x, y, itemName);
            case "TheOneRing":
                return new TheOneRing(x, y, itemName);
            case "Potion":
                return new Potion(x, y, itemName);
            default:
                System.out.println("convertNametoItem ERROR: {" + itemName + "}");
                return null;
        }
    }

    private Card convertTypetoCard(SimpleIntegerProperty x, SimpleIntegerProperty y, String type) {
        switch (type) {
            case "Village":
                return new VillageCard(x, y, type);
            case "Tower":
                return new TowerCard(x, y, type);
            case "Barracks":
                return new BarracksCard(x, y, type);
            case "Trap":
                return new TrapCard(x, y, type);
            case "Campfire":
                return new CampfireCard(x, y, type);
            case "VampireCastle":
                return new VampireCastleCard(x, y, type);
            case "ZombiePit":
                return new ZombiePitCard(x, y, type);
            default:
                System.out.println("convertTypetoCard ERROR: {" + type + "}");
                return null;
        }
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public Card loadCard(String cardName) {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            // give some gold/experience rewards for the discarding of the oldest card
            characterGoldGain(20);
            characterExpGain(50);
            removeCard(0);
        }

        SimpleIntegerProperty x = new SimpleIntegerProperty(cardEntities.size());
        SimpleIntegerProperty y = new SimpleIntegerProperty(0);
        Card card = convertTypetoCard(x, y, cardName);
        cardEntities.add(card);
        return card;
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed
     * cards)
     * 
     * @param index the index of the card, from 0 to length-1
     */
    private void removeCard(int index) {
        Card c = cardEntities.get(index);
        int x = c.getX();
        c.destroy();
        cardEntities.remove(index);
        shiftCardsDownFromXCoordinate(x);
    }

    /**
     * spawn a sword in the world and return the sword entity
     * 
     * @return a sword to be spawned in the controller as a JavaFX node
     */
    /*
     * public Sword addUnequippedSword() { // TODO = expand this - we would like to
     * be able to add multiple types of items, // apart from swords Pair<Integer,
     * Integer> firstAvailableSlot = getFirstAvailableSlotForItem(); if
     * (firstAvailableSlot == null) { // eject the oldest unequipped item and
     * replace it... oldest item is that at // beginning of items // TODO = give
     * some cash/experience rewards for the discarding of the oldest // sword
     * removeItemByPositionInUnequippedInventoryItems(0); firstAvailableSlot =
     * getFirstAvailableSlotForItem(); }
     * 
     * // now we insert the new sword, as we know we have at least made a slot //
     * available... Sword sword = new Sword(new
     * SimpleIntegerProperty(firstAvailableSlot.getValue0()), new
     * SimpleIntegerProperty(firstAvailableSlot.getValue1()));
     * unequippedInventoryItems.add(sword); return sword; }
     */

    /**
     * Similar to addUnequippedSword(), item name -> ["Sword", "Armour", "Helmet",
     * "Stake", "Shield", "TheOneRing"]
     * 
     * @param itemName
     * @return
     */
    public Item addUnequipedItem(String itemName) {
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            // Give some cash/experience rewards for the discarding of the oldest item
            characterGoldGain(20);
            characterExpGain(50);
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        SimpleIntegerProperty x = new SimpleIntegerProperty(firstAvailableSlot.getValue0());
        SimpleIntegerProperty y = new SimpleIntegerProperty(firstAvailableSlot.getValue1());
        // now we insert the new item depends on the name, as we know we have at least
        // made a slot available...
        Item item = convertNametoItem(x, y, itemName);
        unequippedInventoryItems.add(item);
        return item;
    }

    /**
     * remove an item by x,y coordinates
     * 
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y) {
        Entity item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryItem(item);
    }

    /**
     * remove an item by x,y coordinates
     * 
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeEquippedInventoryItemByCoordinates(int x, int y) {
        Entity item = getEquippedInventoryItemEntityByCoordinates(x, y);
        removeEquippedInventoryItem(item);
    }

    /**
     * run moves which occur with every tick without needing to spawn anything
     * immediately
     */
    public void runTickMoves() {
        character.moveDownPath();
        character.moveDownPathSoldier();
        moveBasicEnemies();
        moves++;
        updateLoop();
        character.setLoop(loop);
        updateBuildingLoop();
        updateBuildingSkill();
    }

    /**
     * add an item to the equipped inventory
     * 
     * @param item item to be added
     */
    public void addEquippedInventoryItem(Entity item) {
        equippedInventoryItems.add(item);
    }

    /**
     * remove an item from the unequipped inventory
     * 
     * @param item item to be removed
     */
    public void removeUnequippedInventoryItem(Entity item) {
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * remove an item from the equipped inventory
     * 
     * @param item item to be removed
     */
    public void removeEquippedInventoryItem(Entity item) {
        unequipItem(item);
        item.destroy();
        equippedInventoryItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates assumes that no 2
     * unequipped inventory items share x and y coordinates
     * 
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    public Entity getUnequippedInventoryItemEntityByCoordinates(int x, int y) {
        for (Entity e : unequippedInventoryItems) {
            if ((e.getX() == x) && (e.getY() == y)) {
                return e;
            }
        }
        return null;
    }

    /**
     * return an equipped inventory item by x and y coordinates assumes that no 2
     * equipped inventory items share x and y coordinates
     * 
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return equipped inventory item at the input position
     */
    public Entity getEquippedInventoryItemEntityByCoordinates(int x, int y) {
        // Armour (1, 1)
        // Helmet (1, 0)
        // Shield (2, 1)
        // Weapon (0, 1)

        for (Entity item : equippedInventoryItems) {
            if (x == 1 && y == 1 && item instanceof Armour)
                return item;
            if (x == 1 && y == 0 && item instanceof Helmet)
                return item;
            if (x == 2 && y == 1 && item instanceof Shield)
                return item;
            if (x == 0 && y == 1 && (item instanceof Sword || item instanceof Staff || item instanceof Stake))
                return item;
        }
        return null;
    }

    /**
     * remove item at a particular index in the unequipped inventory items list
     * (this is ordered based on age in the starter code)
     * 
     * @param index index from 0 to length-1
     */
    private void removeItemByPositionInUnequippedInventoryItems(int index) {
        Entity item = unequippedInventoryItems.get(index);
        item.destroy();
        unequippedInventoryItems.remove(index);
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the
     * unequipped inventory
     * 
     * @return x,y coordinate pair
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForItem() {
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available
        // slot defined by looking row by row
        for (int y = 0; y < unequippedInventoryHeight; y++) {
            for (int x = 0; x < unequippedInventoryWidth; x++) {
                if (getUnequippedInventoryItemEntityByCoordinates(x, y) == null) {
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * shift card coordinates down starting from x coordinate
     * 
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x) {
        for (Card c : cardEntities) {
            if (c.getX() >= x) {
                c.x().set(c.getX() - 1);
            }
        }
    }

    /**
     * move all enemies
     */
    private void moveBasicEnemies() {
        // TODO = expand to more types of enemy
        for (BasicEnemy e : enemies) {
            e.move(getCharacterPos(), buildingEntities);
        }
    }

    /**
     * count total number of zombie exist
     */
    private int countZombies() {
        int count = 0;
        for (BasicEnemy e : enemies) {
            if (e.getType().get().equals("Zombie"))
                count++;
        }
        return count;
    }

    /**
     * count total number of slug exist
     */
    private int countSlugs() {
        int count = 0;
        for (BasicEnemy e : enemies) {
            if (e.getType().get().equals("Slug"))
                count++;
        }
        return count;
    }

    /**
     * count total number of vampire exist
     */
    private int countVampires() {
        int count = 0;
        for (BasicEnemy e : enemies) {
            if (e.getType().get().equals("Vampire"))
                count++;
        }
        return count;
    }

    /**
     * count total number of zombiepit exist
     */
    private int countZombiePits() {
        int count = 0;
        for (Building building : buildingEntities) {
            if (building.getType().equals("ZombiePit"))
                count++;
        }
        return count;
    }

    /**
     * count total number of vampirecastle exist
     */
    private int countVampireCastles() {
        int count = 0;
        for (Building building : buildingEntities) {
            if (building.getType().equals("VampireCastle"))
                count++;
        }
        return count;
    }

    /**
     * Check if character just pass through barracks
     * @param x
     * @param y
     * @param buildingEntities
     * @return
     */
    private Boolean passByBarracks(int x, int y, List<Building> buildingEntities) {
        List<Pair<Integer, Integer>> barrackPos = new ArrayList<>();
        for (Building building : buildingEntities) {
            if (building.getType().equals("Barracks"))
                barrackPos.add(new Pair<Integer, Integer>(building.getX(), building.getY()));
        }
        return barrackPos.contains(new Pair<Integer, Integer>(x, y));
    }


    /**
     * Convert into building object based on string
     * @param x
     * @param y
     * @param type
     * @return
     */
    private Building convertTypetoBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, String type) {
        int startMoves = moves % orderedPath.size();
        switch (type) {
            case "Village":
                return new VillageBuilding(x, y, type, startMoves);
            case "Tower":
                return new TowerBuilding(x, y, type, startMoves);
            case "Barracks":
                return new BarracksBuilding(x, y, type, startMoves);
            case "Trap":
                return new TrapBuilding(x, y, type, startMoves);
            case "Campfire":
                return new CampfireBuilding(x, y, type, startMoves);
            case "VampireCastle":
                return new VampireCastleBuilding(x, y, type, startMoves);
            case "ZombiePit":
                return new ZombiePitBuilding(x, y, type, startMoves);
            default:
                return null;
        }
    }

    /**
     * Convert card Index into card
     * @param cardNodeX
     * @param cardNodeY
     * @return
     */
    private Card convertCardIndextoCard(int cardNodeX, int cardNodeY) {
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Asset if target location can build building
     * @param cardNodeX
     * @param cardNodeY
     * @param buildingNodeX
     * @param buildingNodeY
     * @return
     */
    public boolean canBuildingPlace(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        // The new building cannot set in the position that already got buildings
        for (Building building : getBuildings()) {
            if (building.positionTaken(buildingNodeX, buildingNodeY))
                return false;
        }

        Card card = convertCardIndextoCard(cardNodeX, cardNodeY);
        Pair<Integer, Integer> buildingIndex = new Pair<Integer, Integer>(buildingNodeX, buildingNodeY);
        Boolean canSetProperty1 = orderedPath.contains(buildingIndex) && card.checkPath();
        Boolean canSetProperty2 = !orderedPath.contains(buildingIndex) && card.checkNonPath();
        return canSetProperty1 || canSetProperty2;
    }

    /**
     * remove a card by its x, y coordinates
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        // start by getting card
        Card card = convertCardIndextoCard(cardNodeX, cardNodeY);

        SimpleIntegerProperty x = new SimpleIntegerProperty(buildingNodeX);
        SimpleIntegerProperty y = new SimpleIntegerProperty(buildingNodeY);

        // now spawn building
        Building newBuilding = convertTypetoBuilding(x, y, card.getType()); // temp Type here
        buildingEntities.add(newBuilding);

        // destroy the card
        card.destroy();
        cardEntities.remove(card);
        shiftCardsDownFromXCoordinate(cardNodeX);

        return newBuilding;
    }

    // add getters for character stats (health, gold, exp, atk, def)
    /**
     * Returns current character health
     * @return
     */
    public SimpleStringProperty getCharacterHealth() {
        return character.healthProperty();
    }

    /**
     * Returns current character gold
     * @return
     */
    public SimpleIntegerProperty getCharacterGold() {
        return character.getGold();
    }

    /**
     * Returns current character exp
     * @return
     */
    public SimpleIntegerProperty getCharacterExp() {
        return character.getExp();
    }

    /**
     * assert if character is at castle
     * @return
     */
    public Boolean isAtHeroCastle() {
        int heroCastleX = getHeroCastlePosition().getValue0();
        int heroCastleY = getHeroCastlePosition().getValue0();
        return (character.getX() == heroCastleX) && (character.getY() == heroCastleY);
    }

    /**
     * Assert if character is dead
     * @return
     */
    public Boolean isCharacterDead() {
        return character.ifDead();
    }

    /**
     * Let character loss i hp
     * @param i
     */
    public void characterHealthLoss(Integer i) {
        character.setHealth(character.getHealth().get() - i);
    }
    
    /**
     * Let character gain i gold
     * @param i
     */
    public void characterGoldGain(Integer i) {
        character.setGold(character.getGold().get() + i);
    }

    /**
     * Let character gain i exp
     * @param i
     */
    public void characterExpGain(Integer i) {
        character.setExp(character.getExp().get() + i);
    }

    /**
     * Attempts to use health potion
     */
    public void useHealthPotion() {
        // if has potion in inventory
        // then remove potion and restore health to maxhealth
        // elso do nothing
        final String POTION = "Potion";
        if (ifHasItem(POTION)) {
            // remove one of the potions in inventory
            System.out.println("Player consumed Potion!!!");
            removeItem(POTION);
            character.setHealth(character.getMaxHealth().get());
        } else {
            System.out.println("No Potion for Player to use......");
        }
    }

    /**
     * Check if player has item in inventory
     * @param name
     * @return
     */
    public Boolean ifHasItem(String name) {
        int i = 0;
        for (i = 0; i < unequippedInventoryItems.size(); i++) {
            Item se = (Item) unequippedInventoryItems.get(i);
            if (se.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * remove item from inventory
     * @param name
     */
    public void removeItem(String name) {
        for (int i = 0; i < unequippedInventoryItems.size(); i++) {
            Item se = (Item) unequippedInventoryItems.get(i);
            if (se.getName().equals(name)) {
                int nodeX = se.getX();
                int nodeY = se.getY();
                removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
                return;
            }
        }
    }

    /**
     * equipHelmet()
     */
    public void equipHelmet() {
        character.equipHelmet();
    }

    /**
     * equipArmour()
     */
    public void equipArmour() {
        character.equipArmour();
    }

    /**
     * unequipHelmet()
     */
    public void unequipHelmet() {
        character.unequipHelmet();
    }

    /**
     * unequipArmour()
     */
    public void unequipArmour() {
        character.unequipArmour();
    }

    /**
     * equipShield
     */
    public void equipShield() {
        character.equipShield();
    }

    /**
     * unequipShield
     */
    public void unequipShield() {
        character.unequipShield();
    }

    /**
     * equipSword
     */
    public void equipSword() {
        character.equipSword();
    }

    /**
     * equipStake
     */
    public void equipStake() {
        character.equipStake();
    }

    /**
     * equipStaff
     */
    public void equipStaff() {
        character.equipStaff();
    }

    /**
     * unequipWeapon
     */
    public void unequipWeapon() {
        character.unequipWeapon();
    }

    /**
     * returns the list of soldiers following character
     */
    public List<Soldier> getSoldiers() {
        return character.getSoldiers();
    }

    /**
     * Check if new soldier can spawn
     */
    public List<Soldier> possiblySpawnSoldiers() {
        Pair<Integer, Integer> charactorPosition = new Pair<Integer, Integer>(character.getX(), character.getY());
        int positionIndex = orderedPath.indexOf(charactorPosition);
        List<Soldier> newSoldiers = new ArrayList<Soldier>();
        // maximum soldier number = 3
        if (character.soldierNumber() < 3 && passByBarracks(character.getX(), character.getY(), buildingEntities)) {
            Soldier s = new Soldier(new PathPosition(
                    (positionIndex - 1 - character.soldierNumber() + orderedPath.size()) % orderedPath.size(),
                    orderedPath));
            soldiers.add(s);
            newSoldiers.add(s);
            character.addSoldier(s);
        }
        return newSoldiers;
    }

    /**
     * When soldier is instantly killed by zombie, spawn zombie
     */
    public void spawnZombieOnSoldier() {
        Pair<Integer, Integer> charactorPosition = new Pair<Integer, Integer>(character.getX(), character.getY());
        int indexInPath = orderedPath.indexOf(charactorPosition);
        BasicEnemy zombie = new Zombie(new PathPosition(indexInPath, orderedPath));
        enemies.add(zombie);
    }

    /**
     * Equip item
     * @param item
     */
    public void equipItem(Entity item) {
        if (item instanceof Armour)
            equipArmour();
        if (item instanceof Helmet)
            equipHelmet();
        if (item instanceof Shield)
            equipShield();
        if (item instanceof Sword)
            equipSword();
        if (item instanceof Staff)
            equipStaff();
        if (item instanceof Stake)
            equipStake();
    }

    /**
     * unequip items
     * @param item
     */
    private void unequipItem(Entity item) {
        if (item instanceof Armour)
            unequipArmour();
        if (item instanceof Helmet)
            unequipHelmet();
        if (item instanceof Shield)
            unequipShield();
        if (item instanceof Sword || item instanceof Staff || item instanceof Stake)
            unequipWeapon();
    }

    /**
     * removes one ring from inventory
     * @return
     */
    public boolean removeOneRing() {
        for (int i = 0; i < unequippedInventoryItems.size(); i++) {
            StaticEntity se = (StaticEntity) unequippedInventoryItems.get(i);
            if (se instanceof TheOneRing) {
                int nodeX = se.getX();
                int nodeY = se.getY();
                removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
                return true;
            }
        }
        return false;
    }

    /**
     * uses one ring upon death
     * @return
     */
    public boolean useOneRing() {
        // if has OneRing in inventory
        // then remove OneRIng and restore health to maxhealth
        // elso do nothing
        if (removeOneRing()) {
            // remove one of the OneRings in inventory
            character.setHealth(character.getMaxHealth().get());
            return true;
        }
        return false;
    }

    /**
     * check if item is corrrect set to slot
     * @param item
     * @param x
     * @param y
     * @return
     */
    public boolean checkEquipSlot(Entity item, int x, int y) {
        // Armour (1, 1)
        // Helmet (1, 0)
        // Shield (2, 1)
        // Weapon (0, 1)
        if (x == 1 && y == 1 && item instanceof Armour) return true;
        if (x == 1 && y == 0 && item instanceof Helmet) return true;
        if (x == 2 && y == 1 && item instanceof Shield) return true;
        if (x == 0 && y == 1 && (item instanceof Sword || item instanceof Staff || item instanceof Stake)) return true;
        return false;
    }

    /**
     * Assert if item can be equiped at slot
     * @param item
     * @param x
     * @param y
     * @param targetGridPane
     * @param equipItemsRecord
     * @return
     */
    public boolean canEquipItem(Entity item, int x, int y, GridPane targetGridPane, HashMap<Entity, ImageView> equipItemsRecord) {
        if (!checkEquipSlot(item, x, y)) return false;
        boolean armour = false;
        boolean helmet = false;
        boolean shield = false;
        boolean weapon = false;
        for (Entity equippedItem : equippedInventoryItems) {
            if (equippedItem instanceof Armour) armour = true;
            if (equippedItem instanceof Helmet) helmet = true;
            if (equippedItem instanceof Shield) shield = true;
            if (equippedItem instanceof Sword || equippedItem instanceof Staff || equippedItem instanceof Stake) weapon = true;
        }
        if (item instanceof Armour && armour) {
            for (Entity equippedItem : equipItemsRecord.keySet()) {
                if (equippedItem instanceof Armour) targetGridPane.getChildren().remove(equipItemsRecord.get(equippedItem));
            }
        }
        if (item instanceof Helmet && helmet) {
            for (Entity equippedItem : equipItemsRecord.keySet()) {
                if (equippedItem instanceof Helmet) targetGridPane.getChildren().remove(equipItemsRecord.get(equippedItem));
            }
        }
        if (item instanceof Shield && shield) {
            for (Entity equippedItem : equipItemsRecord.keySet()) {
                if (equippedItem instanceof Shield) targetGridPane.getChildren().remove(equipItemsRecord.get(equippedItem));
            }
        }
        if ((item instanceof Sword || item instanceof Staff || item instanceof Stake) && weapon) {
            for (Entity equippedItem : equipItemsRecord.keySet()) {
                if (equippedItem instanceof Sword || equippedItem instanceof Staff || equippedItem instanceof Stake) targetGridPane.getChildren().remove(equipItemsRecord.get(equippedItem));
            }
        }
        return true;
    }

    /**
     * let shop appears for an interval of 1, 2, 3, 4.... loops
     * @return
     */
    public Boolean shouldShopAppear(){
        loopSinceLastShop++;
        if (loopSinceLastShop >= loopForShop){
            loopForShop++;
            loopSinceLastShop = 0;
            return true;
        } else {
            return false;
        }
    }
}
