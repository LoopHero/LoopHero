package unsw.loopmania;

import unsw.loopmania.enemies.*;
import unsw.loopmania.allies.*;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.HeroCastleBuilding;

import java.util.ArrayList;
import java.util.List;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;
import org.javatuples.Pair;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import javafx.geometry.Pos;

import javafx.scene.control.TextField;

import java.util.EnumMap;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;

import java.util.Random;

/**
 * the draggable types. If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE {
    CARD, ITEM
}

/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application
 * thread:
 * https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 * Note in
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html
 * under heading "Threading", it specifies animation timelines are run in the
 * application thread. This means that the starter code does not need locks
 * (mutexes) for resources shared between the timeline KeyFrame, and all of the
 * event handlers (including between different event handlers). This will make
 * the game easier for you to implement. However, if you add time-consuming
 * processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend: using Task
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by
 * itself or within a Service
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 * Tasks ensure that any changes to public properties, change notifications for
 * errors or cancellation, event handlers, and states occur on the JavaFX
 * Application thread, so is a better alternative to using a basic Java Thread:
 * https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm The Service class
 * is used for executing/reusing tasks. You can run tasks without Service,
 * however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need
 * to implement locks on resources shared with the application thread (i.e.
 * Timeline KeyFrame and drag Event handlers). You can check whether code is
 * running on the JavaFX application thread by running the helper method
 * printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and
 * https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using
 * Platform.runLater
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 * This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    private Integer potionNum;
    private Integer defenseNum;
    /**
     * squares gridpane includes path images, enemies, character, empty grass,
     * buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot
     * stretches over the entire game world, so we can detect dragging of
     * cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    @FXML
    private GridPane stats;

    @FXML
    private TextField healthTxt;
    @FXML
    private TextField goldTxt;
    @FXML
    private TextField expTxt;

    @FXML
    private Label loopNumber;

    @FXML
    private Label modeLabel;

    // all image views including tiles, character, enemies, cards... even though
    // cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here
     * and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through
     * maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    private Image slugImage;
    private Image zombieImage;
    private Image vampireImage;
    private Image swordImage;
    private Image armourImage;
    private Image soldierImage;
    private Image helmetImage;
    private Image shieldImage;
    private Image stakeImage;
    private Image staffImage;
    private Image theOneRingImage;
    private Image potionImage;
    private Random rd;

    /**
     * the image currently being dragged, if there is one, otherwise null. Holding
     * the ImageView being dragged allows us to spawn it again in the drop location
     * if appropriate.
     */
    // TODO = it would be a good idea for you to instead replace this with the
    // building/item which should be dropped
    private ImageView currentlyDraggedImage;

    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged into the boundaries of its appropriate
     * gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged outside of the boundaries of its
     * appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    private HashMap<Entity, ImageView> equipItemsRecord = new HashMap<Entity, ImageView>();

    /**
     * @param world           world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be
     *                        loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        potionNum = 999;
        defenseNum = 999;
        entityImages = new ArrayList<>(initialEntities);
        
        // added image loader
        slugImage = new Image((new File("src/images/slug.png")).toURI().toString());
        soldierImage = new Image((new File("src/images/deep_elf_master_archer.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        armourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        theOneRingImage = new Image((new File("src/images/the_one_ring.png")).toURI().toString());
        vampireImage = new Image((new File("src/images/vampire.png")).toURI().toString());
        zombieImage = new Image((new File("src/images/zombie.png")).toURI().toString());

        // add a randomizer
        rd = new Random(System.currentTimeMillis());

        currentlyDraggedImage = null;
        currentlyDraggedType = null;

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
    }

    @FXML
    public void initialize() {
        // TODO = load more images/entities during initialization

        Image pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (inculding all the
        // twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages) {
            squares.getChildren().add(entity);
        }

        // add the ground underneath the cards
        for (int x = 0; x < world.getWidth(); x++) {
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x = 0; x < LoopManiaWorld.unequippedInventoryWidth; x++) {
            for (int y = 0; y < LoopManiaWorld.unequippedInventoryHeight; y++) {
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);
    }

    /**
     * Initialize positon of HeroCastle
     */
    public void initialHeroCastle() {
        Pair<Integer, Integer> heroCastlePosition = world.getHeroCastlePosition();
        SimpleIntegerProperty x2 = new SimpleIntegerProperty();
        SimpleIntegerProperty y2 = new SimpleIntegerProperty();
        x2.set(heroCastlePosition.getValue0());
        y2.set(heroCastlePosition.getValue1());
        String type = "HeroCastle";
        Building heroBuilding = new HeroCastleBuilding(x2, y2, type, 0);
        ImageView view = new ImageView(heroBuilding.getImage());
        addEntity(heroBuilding, view);
        squares.getChildren().add(view);
    }

    /**
     * create and run the timer
     */
    public void startTimer() {
        // TODO = handle more aspects of the behaviour required by the specification
        System.out.println("starting timer");
        initialHeroCastle();
        isPaused = false;
        // trigger adding code to process main game logic to queue. JavaFX will target
        // framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            if (world.getMode() == -1){
                modeSelectionSwitchToMainMenu();
            }
            world.runTickMoves();
            world.runBattles();
            world.updateGoal();
            if (world.goalAchieved()) {
                world.gameOver();
                deadSwitchToMainMenu(true);
                return;
            }
            List<Soldier> soldiers = world.possiblySpawnSoldiers();
            for (Soldier s : soldiers) {
                loadSoldier(s);
            }
            List<BasicEnemy> deadEnemies = world.cleanEnemies();
            for (BasicEnemy e : deadEnemies) {
                reactToEnemyDefeat(e);
            }
            List<BasicEnemy> newEnemies = world.possiblySpawnEnemies();
            for (BasicEnemy newEnemy : newEnemies) {
                loadEnemies(newEnemy);
            }
            printThreadingNotes("HANDLED TIMER");
            loopNumber.setText("Loop: " + (int) world.getCurrentLoop().get());
            if (world.isCharacterDead()) {
                if (!world.useOneRing())
                    deadSwitchToMainMenu(false);
                    return;
            }
            if (world.isAtHeroCastle() && world.shouldShopAppear()) {
                System.out.println("At Hero Castle. Shopping!");
                shopSwitchToMainMenu();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * pause the execution of the game loop the human player can still drag and drop
     * items during the game pause
     */
    public void pause() {
        isPaused = true;
        System.out.println("pausing");
        timeline.stop();
    }

    public void terminate() {
        pause();
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * 
     * @param entity backend entity to be paired with view
     * @param view   frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load a vampire card from the world, and pair it with an image in the GUI
     */
    private void loadCard(String cardName) {
        // TODO = load more types of card
        Card card = world.loadCard(cardName);
        onLoad(card);
    }

    // /**
    // * load a sword from the world, and pair it with an image in the GUI
    // */
    // private void loadSword(){
    // // TODO = load more types of weapon
    // // start by getting first available coordinates
    // Sword sword = world.addUnequippedSword();
    // onLoad(sword);
    // }
    
    /**
     * Load item into character's inventory, and for image display
     * @param itemName
     */
    private void loadItem(String itemName) {
        Item item = world.addUnequipedItem(itemName);
        onLoad(item);
    }

    /**
     * Load soldier for image display
     * @param s
     */
    private void loadSoldier(Soldier s) {
        onLoad(s);
    };
    
    /**
     * Load enemy for image display
     * @param s
     */
    private void loadEnemies(BasicEnemy enemy) {
        String enemyName = enemy.getType().get();
        switch (enemyName) {
            case "Slug":
                onLoad((Slug) enemy);
                break;
            case "Zombie":
                onLoad((Zombie) enemy);
                break;
            case "Vampire":
                onLoad((Vampire) enemy);
                break;
            default:
                System.out.println("Should not see this LMWC: {" + enemyName + "}");
                break;
        }
    }

    /**
     * Assert if item should be dropped, by comparing a random double (0~1) to drop rate
     * @param value
     * @return
     */
    private boolean isDropped(double value) {
        double tar = rd.nextDouble();
        return value > tar;
    }

    /**
     * Process item drop, at most one item at a time
     * @param percentage
     */
    private void triggerDropItem(double percentage) {
        if (isDropped(percentage)) {
            String name;
            int tar = rd.nextInt(6);
            switch (tar) { // 7 is the total type of building card
                case 0:
                    name = "Sword";
                    break;
                case 1:
                    name = "Stake";
                    break;
                case 2:
                    name = "Staff";
                    break;
                case 3:
                    name = "Shield";
                    break;
                case 4:
                    name = "Helmet";
                    break;
                case 5:
                    name = "Armour";
                    break;
                default:
                    return;
            }
            loadItem(name);
        }
    }

    /**
     * Process Health Potion drop
     * @param percentage
     */
    private void triggerDropPotion(double percentage) {
        if (isDropped(percentage)) {
            loadItem("Potion");
        }
    }

    /**
     * Process TheOneRing drop
     * @param percentage
     */
    private void triggerDropTheOneRing(double percentage) {
        if (isDropped(percentage)) {
            loadItem("TheOneRing");
        }
    }

    /**
     * Process card drop, at most one item at a time
     * @param percentage
     */
    private void triggerDropCard(double percentage) {
        if (isDropped(percentage)) {
            String name;
            int tar = rd.nextInt(7);
            switch (tar) { // 7 is the total type of building card
                case 0:
                    name = "Tower";
                    break;
                case 1:
                    name = "Barracks";
                    break;
                case 2:
                    name = "Campfire";
                    break;
                case 3:
                    name = "Trap";
                    break;
                case 4:
                    name = "VampireCastle";
                    break;
                case 5:
                    name = "ZombiePit";
                    break;
                case 6:
                    name = "Village";
                    break;
                default:
                    return;
            }
            loadCard(name);
        }
    }

    /**
     * Triggers gold gain
     * @param value
     * @param percentage
     */
    public void triggerGainGold(int value, double percentage) {
        if (isDropped(percentage)) {
            world.characterGoldGain(value);
        }
    }

    /**
     * triggers exp gain
     * @param value
     * @param percentage
     */
    public void triggerGainExp(int value, double percentage) {
        if (isDropped(percentage)) {
            world.characterExpGain(value);
        }
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning
     * items/experience/gold
     * 
     * @param enemy defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(BasicEnemy enemy) {
        // react to character defeating an enemy
        // item rewards
        triggerDropItem(0.5);
        triggerDropPotion(0.1);
        triggerDropTheOneRing(0.002);

        // gold & exp rewards
        triggerGainGold(10, 0.2);
        triggerGainExp(5, 1);

        // card rewards
        triggerDropCard(0.4); // 0.08 actual, 0.5 for test
    }

    /**
     * load a vampire castle card into the GUI. Particularly, we must connect to the
     * drag detection event handler, and load the image into the cards GridPane.
     * 
     * @param Card
     */
    private void onLoad(Card Card) {
        ImageView view = new ImageView(Card.getImage());

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(Card, view);
        cards.getChildren().add(view);
        System.out.println("Get new Card " + Card.getType());
    }

    /**
     * load Item into the GUI. Particularly, we must connect to the drag detection
     * event handler, and load the image into the unequippedInventory GridPane.
     * 
     * @param item
     */
    private void onLoad(Item item) {
        ImageView view = new ImageView(item.getIcon());
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(item, view);
        unequippedInventory.getChildren().add(view);
        System.out.println("Get new Item " + item.getName() + " in Inventory");
    }

    /**
     * load a slug into the GUI
     * 
     * @param enemy
     */
    private void onLoad(Slug slug) {
        ImageView view = new ImageView(slugImage);
        addEntity(slug, view);
        squares.getChildren().add(view);
    }

    /**
     * load a soldier into the GUI
     * 
     * @param soldier
     */
    private void onLoad(Soldier soldier) {
        ImageView view = new ImageView(soldierImage);
        addEntity(soldier, view);
        squares.getChildren().add(view);
    }

    /**
     * load a zombie into the GUI
     * 
     * @param enemy
     */
    private void onLoad(Zombie zombie) {
        ImageView view = new ImageView(zombieImage);
        addEntity(zombie, view);
        squares.getChildren().add(view);
    }

    /**
     * load a vampire into the GUI
     * 
     * @param enemy
     */
    private void onLoad(Vampire vampire) {
        ImageView view = new ImageView(vampireImage);
        addEntity(vampire, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(Building building) {
        ImageView view = new ImageView(building.getImage());
        addEntity(building, view);
        squares.getChildren().add(view);
        System.out.println("Set new Building " + building.getType());
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the
     * background, dropping over the background. These are not attached to invidual
     * items such as swords/cards.
     * 
     * @param draggableType  the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to
     *                       (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane,
            GridPane targetGridPane) {
        // TODO = be more selective about where something can be dropped
        // for example, in the specification, villages can only be dropped on path,
        // whilst vampire castles cannot go on the path
        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                // TODO = for being more selective about where something can be dropped,
                // consider applying additional if-statement logic
                /*
                 * you might want to design the application so dropping at an invalid location
                 * drops at the most recent valid location hovered over, or simply allow the
                 * card/item to return to its slot (the latter is easier, as you won't have to
                 * store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType) {
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    // Data dropped
                    // If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node != targetGridPane && db.hasImage()) {

                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        // Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                        switch (draggableType) {
                            case CARD:
                                if (canBuildingPlace(nodeX, nodeY, x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    // TODO = spawn a building here of different types
                                    Building newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                    onLoad(newBuilding);
                                } else {
                                    // draggedEntity.relocateToPoint(new Point2D(event.getSceneX(),
                                    // event.getSceneY()));
                                    // throw new NullPointerException("Can't drop!");
                                    return;
                                }
                                break;
                            case ITEM:
                                if (unequippedInventory.getChildren().contains(currentlyDraggedImage)) {
                                    // Equip the item
                                    Entity item = world.getUnequippedInventoryItemEntityByCoordinates(nodeX, nodeY);
                                    if (!canEquipItem(item, x, y, targetGridPane)) return;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    unequippedInventory.getChildren().remove(currentlyDraggedImage);
                                    addDragEventHandlers(image, DRAGGABLE_TYPE.ITEM, equippedItems,
                                            unequippedInventory);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    equipItemsRecord.put(item, image);
                                    world.equipItem(item);
                                    world.addEquippedInventoryItem(item);
                                } else {
                                    // Destroy the item
                                    equipItemsRecord.remove(world.getEquippedInventoryItemEntityByCoordinates(nodeX, nodeX));
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeEquippedItemByCoordinates(nodeX, nodeX);
                                }
                                break;
                            default:
                                break;
                        }
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;

                        // remove drag event handlers before setting currently dragged image to null
                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a
                // sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read
                // https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }

        });

        // this doesn't fire when we drag over GridPane because in the event handler for
        // dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>() {
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType) {
                    if (event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null) {
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for
        // dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType) {
                    // Data dropped
                    // If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node != anchorPaneRoot && db.hasImage()) {
                        // Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);

                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                // let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    protected boolean canEquipItem(Entity item, int x, int y, GridPane targetGridPane) {
        return world.canEquipItem(item, x, y, targetGridPane, equipItemsRecord);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        return world.convertCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    private boolean canBuildingPlace(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        return world.canBuildingPlace(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove an item from the unequipped inventory by its x and y coordinates in
     * the unequipped inventory gridpane
     * 
     * @param nodeX x coordinate from 0 to unequippedInventoryWidth-1
     * @param nodeY y coordinate from 0 to unequippedInventoryHeight-1
     */
    private void removeItemByCoordinates(int nodeX, int nodeY) {
        world.removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
    }

    /**
     * remove an item from the equipped inventory by its x and y coordinates in the
     * equipped inventory gridpane
     * 
     * @param nodeX x coordinate from 0 to equippedInventoryWidth-1
     * @param nodeY y coordinate from 0 to equippedInventoryHeight-1
     */
    private void removeEquippedItemByCoordinates(int nodeX, int nodeY) {
        world.removeEquippedInventoryItemByCoordinates(nodeX, nodeY);
    }

    /**
     * add drag event handlers to an ImageView
     * 
     * @param view           the view to attach drag event handlers to
     * @param draggableType  the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be
     *                       dragged
     * @param targetGridPane the relevant gridpane to which the entity would be
     *                       dragged to
     */
    private void addDragEventHandlers(ImageView view, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane,
            GridPane targetGridPane) {
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can
                                              // detect it...
                currentlyDraggedType = draggableType;
                // Drag was detected, start drap-and-drop gesture
                // Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);

                // Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);
                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                // switch (draggableType) {
                // case CARD:
                // draggedEntity.setImage(vampireCastleCardImage);
                // break;
                // case ITEM:
                // draggedEntity.setImage(swordImage);
                // break;
                // default:
                // break;
                // }

                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED,
                        anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n : targetGridPane.getChildren()) {
                    // events for entering and exiting are attached to squares children because that
                    // impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = be more selective about whether highlighting changes - if it cannot be
                        // dropped in the location, the location shouldn't be highlighted!
                        public void handle(DragEvent event) {
                            Integer cIndex = GridPane.getColumnIndex(n);
                            Integer rIndex = GridPane.getRowIndex(n);
                            int x = cIndex == null ? 0 : cIndex;
                            int y = rIndex == null ? 0 : rIndex;

                            int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                            int nodeY = GridPane.getRowIndex(currentlyDraggedImage);

                            if (currentlyDraggedType == draggableType) {
                                // The drag-and-drop gesture entered the target
                                // show the user that it is an actual gesture target
                                if (!draggableType.equals(DRAGGABLE_TYPE.ITEM)) {
                                    if (canBuildingPlace(nodeX, nodeY, x, y))
                                        n.setOpacity(0.7);
                                } else if (event.getGestureSource() != n && event.getDragboard().hasImage()) {
                                    n.setOpacity(0.7);
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = since being more selective about whether highlighting changes, you
                        // could program the game so if the new highlight location is invalid the
                        // highlighting doesn't change, or leave this as-is
                        public void handle(DragEvent event) {
                            // if (currentlyDraggedType == draggableType) {
                            n.setOpacity(1);
                            // }

                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }
        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events this is
     * particularly important for slower machines such as over VLAB.
     * 
     * @param draggableType  either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane) {
        // remove event handlers from nodes in children squares, from anchorPaneRoot,
        // and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n : targetGridPane.getChildren()) {
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
            n.setOpacity(1);
        }
    }

    /**
     * handle the pressing of keyboard keys. Specifically, we should pause when
     * pressing SPACE
     * 
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        // TODO = handle additional key presses, e.g. for consuming a health potion
        switch (event.getCode()) {
            case SPACE:
                if (isPaused) {
                    startTimer();
                } else {
                    pause();
                }
                break;
            case H: // try to use health potion
                world.useHealthPotion();
                break;
            case M:
                world.showGameMode();
                break;
            // case R: // restart game
            //     LoopManiaApplication a = new LoopManiaApplication();
            //     Stage s = new Stage();
            //     try {
            //         a.start(s);
            //     } catch (IOException e) {
            //         // TODO Auto-generated catch block
            //         e.printStackTrace();
            //     }
            //     break;
            case P: // generate potion for player (test usage)
                System.out.println("Lot of items & cards generated for Player......");
                loadItem("Potion");
                loadItem("Helmet");
                loadItem("Armour");
                loadItem("Shield");
                loadItem("Sword");
                loadItem("Stake");
                loadItem("Staff");
                loadCard("Tower");
                loadCard("Barracks");
                loadCard("Campfire");
                loadCard("Trap");
                loadCard("VampireCastle");
                loadCard("ZombiePit");
                loadCard("Village");
                break;
            // case L: 
            //     System.out.println("++++++++++++++++++++++++++++++++++");
            //     System.out.println("Showing equiped items:");
            //     for (int i = 0; i < 4; i++) {
            //         for (int j = 0; i < 4; i++) {
            //             Boolean b = world.getEquippedInventoryItemEntityByCoordinates(i, j) != null;
            //             System.out.print(" " + b + " ");
            //         }
            //         System.out.println("");
            //     }
            //     System.out.println("++++++++++++++++++++++++++++++++++");
            //     break;
            default:
                break;
        }
    }

    /**
     * set switcher to mainmenu
     * @param mainMenuSwitcher
     */
    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher) {
        // TODO = possibly set other menu switchers
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * 
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        // TODO = possibly set other menu switchers
        pause();
        mainMenuSwitcher.switchMenu();
    }

    /**
     * this method is triggered when click button to show goals
     * 
     */
    @FXML
    private void showGoal() {
        pause();
        Stage stage = new Stage();
        stage.setTitle("Goal");
        Label winLabel = new Label(world.getGoal().toString());
        Button backGameButton = new Button("Back to Game Menu");
        backGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // startTimer();
                stage.close();
            }
        });
        Pane canvas = new Pane();
        canvas.setPrefSize(300, 120);
        winLabel.relocate(60, 15);
        backGameButton.relocate(80, 70);
        canvas.getChildren().addAll(winLabel, backGameButton);
        stage.setScene(new Scene(canvas, 300, 120));
        stage.sizeToScene();
        stage.show();
    }

    /**
     * this method is triggered when hero died
     */
    private void deadSwitchToMainMenu(boolean win) {
        // TODO = reset LoopManiaWorld
        pause();
        Stage stage = new Stage();
        stage.setTitle("Game ended");
        Label winLabel = new Label("You lose!");
        if (win)
            winLabel = new Label("You win!");
        Button restartGameButton = new Button("Back to Main Menu");
        restartGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                stage.close();
                try {
                    switchToMainMenu();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        Pane canvas = new Pane();
        canvas.setPrefSize(300, 185);
        winLabel.relocate(123, 30);
        restartGameButton.relocate(80, 80);
        canvas.getChildren().addAll(winLabel, restartGameButton);
        stage.setScene(new Scene(canvas, 300, 185));
        stage.sizeToScene();
        stage.show();
    }

    /**
     * process item purchase from shop
     * @param item
     */
    private void buyItem(ShopItem item) {
        int gold = world.getCharacter().getGold().get();
        if (world.isSurvivalMode() && item.getName().equals("Potion")){
            this.potionNum = 0;
            System.out.println("This shop has 0 Potion left.");
        }
        if (world.isBerserkerMode() && 
                (item.getName().equals("Helmet")
                || item.getName().equals("Shield")
                || item.getName().equals("Armour"))){
            this.defenseNum = 0;
            System.out.println("This shop has 0 defensive item left.");
        }
        if (item.getPrice() > gold) {
            System.out.println("You don't have enough money.");
        }
        loadItem(item.getName());
        world.getCharacter().setGold(gold - item.getPrice());
        System.out.println("You have bought Item : " + item.getName() + ".");
    }

    /**
     * process item sell from shop
     * @param item
     */
    private void sellItem(ShopItem item) {
        if (!world.ifHasItem(item.getName())) {
            System.out.println("You don't have Item : " + item.getName());
            return;
        }
        world.removeItem(item.getName());
        int gold = world.getCharacter().getGold().get();
        world.getCharacter().setGold(gold + item.getPrice());
        System.out.println("You have sold Item : " + item.getName());
    }

    /**
     * Decides price of item in shop
     * @param type
     * @param money
     * @param isBuy
     * @return
     */
    private ShopItem ShoppingItem(int type, int money, boolean isBuy) {
        final int RINGMINPRICE = 250;
        final int ITEMMINPRICE = 6;
        String name;
        int price;
        Image icon;
        price = (int) (ITEMMINPRICE * (8.7 + rd.nextDouble()));
        switch (type) {
            case 0:
                name = "Armour";
                icon = armourImage;
                break;
            case 1:
                name = "Helmet";
                icon = helmetImage;
                break;
            case 2:
                name = "Potion";
                icon = potionImage;
                break;
            case 3:
                name = "Shield";
                icon = shieldImage;
                break;
            case 4:
                name = "Stake";
                icon = stakeImage;
                break;
            case 5:
                name = "Sword";
                icon = swordImage;
                break;
            case 6:
                name = "Staff";
                icon = staffImage;
                break;
            default:
                name = "TheOneRing";
                price = Math.max(RINGMINPRICE, (int) (money * 0.8 + rd.nextDouble() * money * 0.2));
                icon = theOneRingImage;
        }
        if (!isBuy) {
            price *= 0.4;
        }
        return new ShopItem(name, price, icon);
    }

    /**
     * Make buttons available / unavailable to be pressed
     * @param buy
     * @param sell
     */
    private void updateButtonState(ShopItem[] buy, ShopItem[] sell) {
        int gold = world.getCharacter().getGold().get();
        for (int i = 0; i < 8; i++) {
            Boolean canBuy = gold < buy[i].getPrice();
            Boolean canSell = !world.ifHasItem(sell[i].getName());
            if (world.isSurvivalMode()){
                // only can buy one potion if survival mode
                buy[i].getButton().disableProperty().set(canBuy);
                if (i == 2 && this.potionNum == 0){
                    buy[i].getButton().disableProperty().set(true);
                }
            } else if (world.isBerserkerMode()) {
                buy[i].getButton().disableProperty().set(canBuy);
                // only can buy one piece of defensive gear if berserker mode
                if ((i == 0 || i == 1 || i == 3) && this.defenseNum == 0){
                    buy[i].getButton().disableProperty().set(true);
                }
            } else {
                buy[i].getButton().disableProperty().set(canBuy);
            }
            sell[i].getButton().disableProperty().set(canSell);
        }
    }

    /**
     * This method is used to trigger mode selection
     */
    private void modeSelectionSwitchToMainMenu(){
        pause();
        Pane canvas = new Pane();
        Stage stage = new Stage();
        stage.setTitle("GameMode Selection");
        Label label = new Label("GameMode Selection");
        label.relocate(20, 50);
        label.setAlignment(Pos.CENTER);
        label.setPrefSize(180, 30);
        canvas.getChildren().add(label);
        Button buttonStandard = new Button("Standard Mode");
        buttonStandard.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                world.setGameModeStandard();
                modeLabel.setText("Standard Mode");
                stage.close();
                startTimer();
            }
        });
        buttonStandard.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                label.setText("Standard Experience");
            }
        });
        buttonStandard.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                label.setText("GameMode Selection");
            }
        });
        buttonStandard.relocate(50, 100);
        buttonStandard.setPrefSize(120, 30);
        buttonStandard.alignmentProperty().set(Pos.CENTER);
        canvas.getChildren().add(buttonStandard);
        Button buttonBerserker = new Button("Berserker Mode");
        buttonBerserker.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                world.setGameModeBerserker();
                modeLabel.setText("Berserker Mode");
                stage.close();
                startTimer();
            }
        });
        buttonBerserker.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                label.setText("Not a fan of Defense");
            }
        });
        buttonBerserker.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                label.setText("GameMode Selection");
            }
        });
        buttonBerserker.relocate(50, 150);
        buttonBerserker.setPrefSize(120, 30);
        buttonBerserker.alignmentProperty().set(Pos.CENTER);
        canvas.getChildren().add(buttonBerserker);
        Button buttonSurvival = new Button("Survival Mode");
        buttonSurvival.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                world.setGameModeSurvival();
                modeLabel.setText("Survival Mode");
                stage.close();
                startTimer();
            }
        });
        buttonSurvival.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                label.setText("Never enough potion");
            }
        });
        buttonSurvival.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                label.setText("GameMode Selection");
            }
        });
        buttonSurvival.relocate(50, 200);
        buttonSurvival.setPrefSize(120, 30);
        buttonSurvival.alignmentProperty().set(Pos.CENTER);
        canvas.getChildren().add(buttonSurvival);
        canvas.setPrefSize(220, 300);
        stage.setScene(new Scene(canvas, 220, 300));
        stage.sizeToScene();
        stage.show();
    }

    /**
     * this method is Shopping
     */
    private void shopSwitchToMainMenu() {
        potionNum = 1;
        defenseNum = 1;
        pause();
        Pane canvas = new Pane();
        Stage stage = new Stage();
        stage.setTitle("Shopping Store");
        Label winLabel = new Label("Shopping Castle!");
        ShopItem[] buyItems = new ShopItem[8];
        ShopItem[] sellItems = new ShopItem[8];
        for (int i = 0; i < 8; i++) {
            int gold = world.getCharacter().getGold().get();
            ShopItem item = ShoppingItem(i, gold, true);
            Button newButton = new Button("Buy" + item.getName() + " " + item.getPrice());
            newButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    buyItem(item);
                }
            });
            if (gold < item.getPrice()) {
                newButton.disableProperty().set(true);
            }
            newButton.relocate(30 + i * 100, 80);
            canvas.getChildren().addAll(newButton);

            buyItems[i] = item;
            buyItems[i].setButton(newButton);
        }

        for (int i = 0; i < 8; i++) {
            int gold = world.getCharacter().getGold().get();
            ShopItem item = ShoppingItem(i, gold, false);
            Button newButton = new Button("Sell" + item.getName() + " " + item.getPrice());

            if (!world.ifHasItem(item.getName())) {
                newButton.disableProperty().set(true);
            }

            newButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    sellItem(item);
                }
            });

            newButton.relocate(30 + i * 100, 150);
            canvas.getChildren().addAll(newButton);

            sellItems[i] = item;
            sellItems[i].setButton(newButton);
        }
        Button tempButton;
        for (int i = 0; i < 8; i++) {
            tempButton = buyItems[i].getButton();
            tempButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    updateButtonState(buyItems, sellItems);
                }
            });
            tempButton = sellItems[i].getButton();
            tempButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    updateButtonState(buyItems, sellItems);
                }
            });
        }

        Button returnGameButton = new Button("Back to Game Menu");
        returnGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                stage.close();
                startTimer();
            }
        });
        canvas.setPrefSize(1000, 400);
        winLabel.relocate(123, 30);
        returnGameButton.relocate(80, 200);
        canvas.getChildren().addAll(winLabel, returnGameButton);
        stage.setScene(new Scene(canvas, 1000, 400));
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Set a node in a GridPane to have its position track the position of an entity
     * in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the model
     * will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we
     * need to track positions of spawned entities such as enemy or items which
     * might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So
     * it is vital this is handled in this Controller class
     * 
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        // TODO = tweak this slightly to remove items from the equipped inventory?
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from
        // equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                .onAttach((o, l) -> o.addListener(xListener)).onDetach((o, l) -> {
                    o.removeListener(xListener);
                    entityImages.remove(node);
                    squares.getChildren().remove(node);
                    cards.getChildren().remove(node);
                    equippedItems.getChildren().remove(node);
                    unequippedInventory.getChildren().remove(node);
                }).buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                .onAttach((o, l) -> o.addListener(yListener)).onDetach((o, l) -> {
                    o.removeListener(yListener);
                    entityImages.remove(node);
                    squares.getChildren().remove(node);
                    cards.getChildren().remove(node);
                    equippedItems.getChildren().remove(node);
                    unequippedInventory.getChildren().remove(node);
                }).buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here,
        // position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is
     * running on the application thread. By running everything on the application
     * thread, you will not need to worry about implementing locks, which is outside
     * the scope of the course. Always writing code running on the application
     * thread will make the project easier, as long as you are not running
     * time-consuming tasks. We recommend only running code on the application
     * thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel) {
        System.out.println("\n###########################################");
        System.out.println("current method = " + currentMethodLabel);
        System.out.println("In application thread? = " + Platform.isFxApplicationThread());
        System.out.println("Current system time = " + java.time.LocalDateTime.now().toString().replace('T', ' '));
    }

    /**
     * Bind textfield in fxml with integerProperty
     */
    public void bindTextFields() {
        // bind character stats with TestFields
        healthTxt.textProperty().bind(world.getCharacterHealth());
        goldTxt.textProperty().bindBidirectional(world.getCharacterGold(), new NumberStringConverter() {
        });
        expTxt.textProperty().bindBidirectional(world.getCharacterExp(), new NumberStringConverter() {
        });
    }
}
