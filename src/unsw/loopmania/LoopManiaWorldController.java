package unsw.loopmania;

import unsw.loopmania.enemies.*;
import unsw.loopmania.goal.AndGoal;
import unsw.loopmania.goal.Goal;
import unsw.loopmania.goal.OrGoal;
import unsw.loopmania.items.*;
import unsw.loopmania.allies.*;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.HeroCastleBuilding;

import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.*;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;
import org.javatuples.Pair;
import org.junit.platform.commons.function.Try;

import java.awt.Color;
import javafx.application.Application;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Paint;
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
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import javafx.geometry.Pos;

import javafx.scene.control.TextField;

import java.util.EnumMap;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.lang.Math;

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

    @FXML
    private MediaPlayer mediaPlayer;

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

    private boolean useLifefromAd;
    /**
     * runs the periodic game logic - second-by-second moving of character through
     * maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

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
    public SimpleBooleanProperty gameOver = new SimpleBooleanProperty();

    private HashMap<Entity, ImageView> equipItemsRecord = new HashMap<Entity, ImageView>();

    private final int TOTALITEM = 11;
    private final int MAXITEMPERROW = 8;

    /**
     * @param world           world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be
     *                        loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        useLifefromAd = false;
        potionNum = 999;
        defenseNum = 999;
        entityImages = new ArrayList<>(initialEntities);
        gameOver.set(false);

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
        Tooltip.install(view, new Tooltip(heroBuilding.getTooltip()));
        addEntity(heroBuilding, view);
        squares.getChildren().add(view);
    }

    public void playMusic(String musicName) {
        Media sound = new Media(new File(musicName).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.0);
        mediaPlayer.play();
    }

    public void stopMusic() {
        if (mediaPlayer != null)
            mediaPlayer.pause();
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
            if (world.getMode() == -1) {
                modeSelectionSwitchToMainMenu();
                playMusic("music/play.mp3");
            }
            world.runTickMoves();
            world.runBattles();
            world.updateGoal();
            if (world.goalAchieved()) {
                world.gameOver();
                gameOver.set(true);
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
            // load bosses
            List<BossEnemy> newBosses = world.possiblySpawnBosses();
            for (BossEnemy newBoss : newBosses) {
                loadBosses(newBoss);
            }
            printThreadingNotes("HANDLED TIMER");
            loopNumber.setText("Loop: " + (int) world.getCurrentLoop().get());
            if (world.isCharacterDead()) {
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
     * create and run the timer
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void loadTimer() throws ClassNotFoundException, IOException {
        // TODO = handle more aspects of the behaviour required by the specification
        System.out.println("Continue timer");
        String wd = System.getProperty("user.dir");
        JFileChooser fc = new JFileChooser(wd);
        fc.setDialogType((int) JFileChooser.OPEN_DIALOG);
        int rc = fc.showDialog(null, "Select Data File to Load");

        if (rc == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            this.world = (LoopManiaWorld) input.readObject();
            System.out.println(world.getCurrentLoop());
            input.close();
        }
        pause();
        // startTimer();
    }

    // Method that returns String through a Object as a parameter i.e to be
    // converted

    public String generateJSONStringFromObject(LoopManiaWorld world) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        String jsonString = mapper.writeValueAsString(world);

        return jsonString;
    }

    // Method that returns Object through a String as a parameter

    public LoopManiaWorld generateObjectFromJSONString(String jsonString) {
        Gson gson = new Gson();
        LoopManiaWorld world = gson.fromJson(jsonString, LoopManiaWorld.class);
        return world;
    }

    public void saveGame() throws FileNotFoundException, IOException {
        // TODO = handle more aspects of the behaviour required by the specification
        pause();
        // ObjectOutputStream outputObject = new ObjectOutputStream(new
        // FileOutputStream("save/world"));
        // outputObject.writeObject(world);
        String wd = System.getProperty("user.dir");
        JFileChooser fc = new JFileChooser(wd);

        fc.setDialogType((int) JFileChooser.SAVE_DIALOG);
        int rc = fc.showDialog(null, "Select Data File");

        if (rc == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            ObjectOutputStream output = null;

            output = new ObjectOutputStream(new FileOutputStream(file));
            output.writeObject(world);
            output.close();
        }
    }

    /**
     * pause the execution of the game loop the human player can still drag and drop
     * items during the game pause
     */
    public void pause() {
        isPaused = true;
        System.out.println("pausing");
        if (timeline != null) {
            timeline.stop();
        }
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

    /**
     * Load item into character's inventory, and for image display
     * 
     * @param itemName
     */
    private void loadItem(String itemName) {
        Item item = world.addUnequipedItem(itemName);
        onLoad(item);
    }

    /**
     * Load soldier for image display
     * 
     * @param s
     */
    private void loadSoldier(Soldier s) {
        onLoad(s);
    };

    /**
     * Load enemy for image display
     * 
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
     * Load Boss enemy for image display
     * 
     * @param s
     */
    private void loadBosses(BossEnemy be) {
        String enemyName = be.getType().get();
        switch (enemyName) {
            case "Doggie":
                onLoad((Doggie) be);
                break;
            case "ElonMask":
                onLoad((ElonMask) be);
                break;
            default:
                System.out.println("Should not see this LMWC: {" + enemyName + "}");
                break;
        }
    }

    /**
     * Assert if item should be dropped, by comparing a random double (0~1) to drop
     * rate
     * 
     * @param value
     * @return
     */
    private boolean isDropped(double value) {
        double tar = rd.nextDouble();
        return value > tar;
    }

    private void triggerDropDoggie(double percentage) {
        if (isDropped(percentage)) {
            loadItem("DoggieCoin");
        }
    }

    /**
     * Process item drop, at most one item at a time
     * 
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
     * 
     * @param percentage
     */
    private void triggerDropPotion(double percentage) {
        if (isDropped(percentage)) {
            loadItem("Potion");
        }
    }

    /**
     * Process TheOneRing drop
     * 
     * @param percentage
     */
    private void triggerDropRareItem(double percentage) {
        if (isDropped(percentage)) {
            int tar = rd.nextInt(3);
            switch (tar) {
                case 0:
                    loadItem("TheOneRing");
                    break;
                case 1:
                    loadItem("Rapier");
                    break;
                case 2:
                    loadItem("TreeStump");
                    break;
            }
        }
    }

    /**
     * Process card drop, at most one item at a time
     * 
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
     * 
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
     * 
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
        triggerDropItem(0.10); // from 0.5 to 0.10
        triggerDropPotion(0.05); // from 0.1 to 0.05
        triggerDropRareItem(0.002); // now has a pool of all rare items

        if (world.doDoggieCoinSpawn()) {
            triggerDropDoggie(0.05);
        }

        // gold & exp rewards
        triggerGainGold(10, 0.2);
        triggerGainExp(5, 1);
        updateCharacterHP();

        // card rewards
        triggerDropCard(0.08); // 0.08 actual, 0.5 for test

        if (enemy instanceof BossEnemy) {
            BossEnemy be = (BossEnemy) enemy;
            be.triggerDefeatFlag(world);
        }
    }

    /**
     * Character has increased maxHealth with increasing EXP
     */
    public void updateCharacterHP() {
        world.getCharacter().setMaxHealth(Math.min(world.getCharacterExp().get() + 1000, 9999));
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
        Tooltip.install(view, new Tooltip(Card.getTooltip()));
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
        Tooltip.install(view, new Tooltip(item.getTooltip()));
        unequippedInventory.getChildren().add(view);
        System.out.println("Get new Item " + item.getName() + " in Inventory");
    }

    /**
     * load a enemy into the GUI
     * 
     * @param enemy
     */
    private void onLoad(BasicEnemy enemy) {
        ImageView view = new ImageView(enemy.getImage());
        addEntity(enemy, view);
        Tooltip.install(view, new Tooltip(enemy.getTooltip()));
        squares.getChildren().add(view);
    }

    private void onLoad(Soldier s) {
        ImageView view = new ImageView(s.getImage());
        addEntity(s, view);
        Tooltip.install(view, new Tooltip(s.getTooltip()));
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
        Tooltip.install(view, new Tooltip(building.getTooltip()));
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
                                    SimpleBooleanProperty hidden = new SimpleBooleanProperty(false);
                                    if (!canEquipItem(item, x, y, targetGridPane, hidden))
                                        // TODO added event handler for those not dragging to correct location
                                        return;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    unequippedInventory.getChildren().remove(currentlyDraggedImage);
                                    addDragEventHandlers(image, DRAGGABLE_TYPE.ITEM, equippedItems,
                                            unequippedInventory);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    if (hidden.get()) {
                                        RareItem rt = (RareItem) item;
                                        equipItemsRecord.put(rt.getHidden(), image);
                                        world.equipItem(rt.getHidden());
                                        world.addEquippedInventoryItem(rt.getHidden());
                                    } else {
                                        equipItemsRecord.put(item, image);
                                        world.equipItem(item);
                                        world.addEquippedInventoryItem(item);
                                    }
                                } else {
                                    // Destroy the item
                                    equipItemsRecord
                                            .remove(world.getEquippedInventoryItemEntityByCoordinates(nodeX, nodeX));
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

    protected boolean canEquipItem(Entity item, int x, int y, GridPane targetGridPane, SimpleBooleanProperty hidden) {
        return world.canEquipItem(item, x, y, targetGridPane, equipItemsRecord, hidden);
    }

    /**
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
            case D:
                world.characterHealthLoss(9999);
                break;
            case S:
                world.showBossState();
                break;
            // case R: // restart game
            // LoopManiaApplication a = new LoopManiaApplication();
            // Stage s = new Stage();
            // try {
            // a.start(s);
            // } catch (IOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // break;
            case P: // generate potion for player (test usage)
                System.out.println("Lot of items & cards generated for Admin......");
                loadItem("Potion");
                loadItem("Helmet");
                loadItem("Armour");
                loadItem("Shield");
                loadItem("Sword");
                loadItem("Stake");
                loadItem("Staff");
                loadItem("TheOneRing");
                loadItem("Rapier");
                loadItem("TreeStump");
                loadItem("DoggieCoin");

                loadCard("Tower");
                loadCard("Barracks");
                loadCard("Campfire");
                loadCard("Trap");
                loadCard("VampireCastle");
                loadCard("ZombiePit");
                loadCard("Village");

                updateCharacterHP();
                break;
            // case L:
            // System.out.println("++++++++++++++++++++++++++++++++++");
            // System.out.println("Showing equiped items:");
            // for (int i = 0; i < 4; i++) {
            // for (int j = 0; i < 4; i++) {
            // Boolean b = world.getEquippedInventoryItemEntityByCoordinates(i, j) != null;
            // System.out.print(" " + b + " ");
            // }
            // System.out.println("");
            // }
            // System.out.println("++++++++++++++++++++++++++++++++++");
            // break;
            default:
                break;
        }
    }

    /**
     * set switcher to mainmenu
     * 
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
        Button backGameButton = new Button("Back to Game Menu");
        backGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                startTimer();
                stage.close();
            }
        });
        Pane canvas = new Pane();
        canvas.setPrefSize(640, 360);
        backGameButton.relocate(230, 300);
        backGameButton.setFont(new Font("Comic Sans MS", 14));
        backGameButton.setOpacity(0.65);
        drawGoalTree(260, 60 - 15, 260, 60, world.getGoal(), canvas);
        canvas.getChildren().addAll(backGameButton);
        Scene scene = new Scene(canvas, 640, 360);
        scene.getStylesheets()
                .addAll(this.getClass().getResource("background_images/goal_menu_background.css").toExternalForm());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    private void drawGoalTree(int x1, int y1, int x, int y, Goal node, Pane drawField) {
        Line line = new Line(x1 + 45, y1 + 30, x + 45, y + 15);
        line.setStyle("-fx-stroke-width: 4px; -fx-stroke: #994c00;");
        drawField.getChildren().add(line);
        Rectangle rectangle;
        if (node.isSatisfied()) {
            rectangle = new Rectangle(90, 30, Paint.valueOf("99ff99"));
        } else {
            rectangle = new Rectangle(90, 30, Paint.valueOf("e0e0e0"));
        }
        rectangle.opacityProperty().set(0.65);
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        drawField.getChildren().add(rectangle);
        Label txt = new Label();
        if (node instanceof AndGoal) {
            txt.setText("AND");
        } else if (node instanceof OrGoal) {
            txt.setText("OR");
        } else {
            txt.setText(node.toString());
        }
        txt.setFont(new Font("Comic Sans MS", 14));
        txt.setStyle("-fx-text-fill: #cc6600;");
        txt.relocate(x, y);
        txt.setPrefSize(90, 30);
        txt.setAlignment(Pos.CENTER);
        drawField.getChildren().add(txt);
        if (node instanceof AndGoal) {
            try {
                drawGoalTree(x, y, x - 60, y + 50, (Goal) ((AndGoal) node).subgoals.get(0), drawField);
            } catch (IndexOutOfBoundsException e) {
                // skip
            }
        }
        if (node instanceof OrGoal) {
            try {
                drawGoalTree(x, y, x - 60, y + 50, (Goal) ((OrGoal) node).subgoals.get(0), drawField);
            } catch (IndexOutOfBoundsException e) {
                // skip
            }
        }
        if (node instanceof AndGoal) {
            try {
                drawGoalTree(x, y, x + 60, y + 50, (Goal) ((AndGoal) node).subgoals.get(1), drawField);
            } catch (IndexOutOfBoundsException e) {
                // skip
            }
        }
        if (node instanceof OrGoal) {
            try {
                drawGoalTree(x, y, x + 60, y + 50, (Goal) ((OrGoal) node).subgoals.get(1), drawField);
            } catch (IndexOutOfBoundsException e) {
                // skip
            }
        }
    }

    private Stage watchAdvertisement() throws Exception {
        String url = new File("./src/images/ad.mp4").toURI().toURL().toString();
        Media media = new Media(url);
        MediaPlayer mplayer = new MediaPlayer(media);
        MediaView mView = new MediaView(mplayer);
        System.out.println(media.getSource());

        Pane pane = new Pane();
        pane.getChildren().add(mView);
        mView.fitHeightProperty().bind(pane.heightProperty());
        mView.fitWidthProperty().bind(pane.widthProperty());

        Scene scene = new Scene(pane, 640, 360);
        Stage stage = new Stage();
        stage.setTitle("Game ended");
        stage.setTitle("MediaDemo");
        stage.setScene(scene);
        mplayer.play();
        stage.setOnCloseRequest((WindowEvent event2) -> {
            mplayer.stop();
        });
        return stage;
    }

    private void loseSwitch() {
        // TODO = reset LoopManiaWorld
        final int advertismentDuration = 10;
        pause();
        Stage stage = new Stage();
        stage.setTitle("Game ended");
        Label winLabel;
        stopMusic();
        winLabel = new Label("You lose!");
        playMusic("music/lose.mp3");
        Button restartGameButton = new Button("Back to Main Menu");
        Button advertiseButton = new Button("Watch Advertisement for 20s");
        Button useOneRingButton = new Button("Use One Ring");
        if (world.hasOneRing()) {
            restartGameButton.disableProperty().set(true);
            restartGameButton.setText("Cannot Die Yet!");
        } else {
            useOneRingButton.disableProperty().set(true);
            useOneRingButton.setText("Don't have the One Ring");
        }
        if (useLifefromAd) {
            advertiseButton.disableProperty().set(true);
            advertiseButton.setText("Advertisement has been watched");
        }
        restartGameButton.setFont(new Font("Comic Sans MS", 14));
        restartGameButton.setOpacity(0.65);
        advertiseButton.setFont(new Font("Comic Sans MS", 14));
        advertiseButton.setOpacity(0.65);
        useOneRingButton.setFont(new Font("Comic Sans MS", 14));
        useOneRingButton.setOpacity(0.65);

        useOneRingButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                world.useOneRing();
                stage.close();
                startTimer();
            }
        });

        advertiseButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                stage.close();
                try {
                    Stage adStage = watchAdvertisement();
                    Long startTime = System.currentTimeMillis();
                    adStage.setOnCloseRequest((WindowEvent event1) -> {
                        Long endTime = System.currentTimeMillis();
                        if (endTime - startTime >= advertismentDuration * 1000) {
                            System.out.println("Watch the advertisment successfully");
                            world.getCharacter().setHealth(world.getCharacter().getMaxHealth().get());
                            useLifefromAd = true;
                            startTimer();
                            adStage.close();
                        } else {
                            System.out.println("Fail to watch the advertisment");
                            stage.show();
                        }
                    });
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(advertismentDuration), e -> {
                        if (adStage.isShowing()) {
                            System.out.println("Watch the advertisment successfully");
                            world.getCharacter().setHealth(world.getCharacter().getMaxHealth().get());
                            useLifefromAd = true;
                            startTimer();
                            adStage.close();
                        }
                    }));
                    timeline.play();

                    adStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        restartGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                stage.close();
                try {
                    gameOver.set(true);
                    switchToMainMenu();
                    mediaPlayer.pause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Pane canvas = new Pane();
        canvas.setPrefSize(300, 500);
        winLabel.setFont(new Font("Comic Sans MS", 18));
        winLabel.setStyle("-fx-text-fill: #cc6600;");
        winLabel.relocate(100, 250);
        winLabel.setPrefSize(100, 30);
        winLabel.setAlignment(Pos.CENTER);
        restartGameButton.relocate(83, 400);
        useOneRingButton.relocate(65, 350);
        advertiseButton.relocate(47, 300);
        canvas.getChildren().addAll(winLabel, restartGameButton, useOneRingButton, advertiseButton);
        Scene scene = new Scene(canvas, 300, 500);
        scene.getStylesheets().addAll(
                this.getClass().getResource("background_images/game_mode_menu_background.css").toExternalForm());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    private void winSwitch() {
        pause();
        gameOver.set(true);
        Stage stage = new Stage();
        stage.setTitle("Game ended");
        Label winLabel;
        stopMusic();
        winLabel = new Label("You win!");
        playMusic("music/win.mp3");
        Button restartGameButton = new Button("Back to Main Menu");
        restartGameButton.setFont(new Font("Comic Sans MS", 14));
        restartGameButton.setOpacity(0.65);
        restartGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                stage.close();
                try {
                    switchToMainMenu();
                    mediaPlayer.pause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Pane canvas = new Pane();
        canvas.setPrefSize(300, 185);
        winLabel.setFont(new Font("Comic Sans MS", 18));
        winLabel.setStyle("-fx-text-fill: #cc6600;");
        winLabel.relocate(85, 210);
        winLabel.setPrefSize(100, 30);
        winLabel.setAlignment(Pos.CENTER);
        restartGameButton.relocate(65, 250);
        canvas.getChildren().addAll(winLabel, restartGameButton);
        Scene scene = new Scene(canvas, 280, 300);
        scene.getStylesheets().addAll(
                this.getClass().getResource("background_images/game_mode_menu_background.css").toExternalForm());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * this method is triggered when hero died
     */
    private void deadSwitchToMainMenu(boolean win) {
        if (win) {
            winSwitch();
        } else {
            loseSwitch();
        }
    }

    /**
     * process item purchase from shop
     * 
     * @param item
     */
    private void buyItem(ShopItem item) {
        int gold = world.getCharacter().getGold().get();
        if (world.isSurvivalMode() && item.getName().equals("Potion")) {
            this.potionNum = 0;
            System.out.println("This shop has 0 Potion left.");
        }
        if (world.isBerserkerMode() && (item.getName().equals("Helmet") || item.getName().equals("Shield")
                || item.getName().equals("Armour"))) {
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
     * 
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
     * 
     * @param type
     * @param money
     * @param isBuy
     * @return
     */
    private ShopItem ShoppingItem(int type, int money, boolean isBuy) {
        final int RINGMINPRICE = 250;
        final int ITEMMINPRICE = 6;
        Item item;
        int price = (int) (ITEMMINPRICE * (8.7 + rd.nextDouble()));
        int priceRare = Math.max(RINGMINPRICE, (int) (money * 0.6 + rd.nextDouble() * money * 0.2));
        int priceDoggie = rd.nextInt(400);

        switch (type) {
            case 0:
                item = new Armour("Armour");
                break;
            case 1:
                item = new Helmet("Helmet");
                break;
            case 2:
                item = new Potion("Potion");
                break;
            case 3:
                item = new Shield("Shield");
                break;
            case 4:
                item = new Stake("Stake");
                break;
            case 5:
                item = new Sword("Sword");
                break;
            case 6:
                item = new Staff("Staff");
                break;
            case 7:
                item = new Rapier("Rapier", rd.nextInt(2));
                price = priceRare;
                break;
            case 8:
                item = new TreeStump("TreeStump", rd.nextInt(2));
                price = priceRare;
                break;
            case 9:
                item = new DoggieCoin("DoggieCoin");
                if (world.doDoggieCoinPriceDrop()) {
                    price = (int) (priceDoggie * 0.3);
                } else {
                    price = priceDoggie;
                }
                break;
            case 10:
                item = new TheOneRing("TheOneRing", rd.nextInt(2));
                price = priceRare;
                break;
            default:
                item = new Item("");
                System.out.println("ShoppingItem ERROR");
        }
        if (!isBuy) {
            price *= 0.4;
        }
        return new ShopItem(item.getName(), price, item.getIcon());
    }

    /**
     * Make buttons available / unavailable to be pressed
     * 
     * @param buy
     * @param sell
     */
    private void updateButtonState(ShopItem[] buy, ShopItem[] sell) {
        int gold = world.getCharacter().getGold().get();
        for (int i = 0; i < TOTALITEM; i++) {
            Boolean canBuy = gold < buy[i].getPrice();
            Boolean canSell = !world.ifHasItem(sell[i].getName());
            if (world.isSurvivalMode()) {
                // only can buy one potion if survival mode
                buy[i].getButton().disableProperty().set(canBuy);
                if (i == 2 && this.potionNum == 0) {
                    buy[i].getButton().disableProperty().set(true);
                }
            } else if (world.isBerserkerMode()) {
                buy[i].getButton().disableProperty().set(canBuy);
                // only can buy one piece of defensive gear if berserker mode
                if ((i == 0 || i == 1 || i == 3) && this.defenseNum == 0) {
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
    private void modeSelectionSwitchToMainMenu() {
        pause();
        Pane canvas = new Pane();
        Stage stage = new Stage();
        stage.setTitle("GameMode Selection");
        Label label = new Label("GameMode Selection");
        label.setFont(new Font("Comic Sans MS", 18));
        label.setStyle("-fx-text-fill: #ffbf00;");
        label.relocate(20, 50);
        label.setAlignment(Pos.CENTER);
        label.setPrefSize(180, 30);
        canvas.getChildren().add(label);
        Button buttonStandard = new Button("Standard Mode");
        buttonStandard.setFont(new Font("Comic Sans MS", 14));
        buttonStandard.setOpacity(0.65);
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
        buttonStandard.relocate(35, 100);
        buttonStandard.setPrefSize(150, 30);
        buttonStandard.alignmentProperty().set(Pos.CENTER);
        canvas.getChildren().add(buttonStandard);
        Button buttonBerserker = new Button("Berserker Mode");
        buttonBerserker.setFont(new Font("Comic Sans MS", 14));
        buttonBerserker.setOpacity(0.65);
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
        buttonBerserker.relocate(35, 150);
        buttonBerserker.setPrefSize(150, 30);
        buttonBerserker.alignmentProperty().set(Pos.CENTER);
        canvas.getChildren().add(buttonBerserker);
        Button buttonSurvival = new Button("Survival Mode");
        buttonSurvival.setFont(new Font("Comic Sans MS", 14));
        buttonSurvival.setOpacity(0.65);
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
        buttonSurvival.relocate(35, 200);
        buttonSurvival.setPrefSize(150, 30);
        buttonSurvival.alignmentProperty().set(Pos.CENTER);
        canvas.getChildren().add(buttonSurvival);
        // comfusion mode
        Button buttonConfusion = new Button("Confusion Mode");
        buttonConfusion.setFont(new Font("Comic Sans MS", 14));
        buttonConfusion.setOpacity(0.65);
        buttonConfusion.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                world.setGameModeConfusion();
                modeLabel.setText("Confusion Mode");
                stage.close();
                startTimer();
            }
        });
        buttonConfusion.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                label.setText("Don't trust your eyes");
            }
        });
        buttonConfusion.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                label.setText("GameMode Selection");
            }
        });
        buttonConfusion.relocate(35, 250);
        buttonConfusion.setPrefSize(150, 30);
        buttonConfusion.alignmentProperty().set(Pos.CENTER);
        canvas.getChildren().add(buttonConfusion);

        canvas.setPrefSize(220, 350);
        Scene scene = new Scene(canvas, 220, 350);
        try {
            scene.getStylesheets().addAll(
                    this.getClass().getResource("background_images/game_mode_menu_background.css").toExternalForm());
        } catch (Exception e) {
        }
        stage.setScene(scene);
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
        ShopItem[] buyItems = new ShopItem[TOTALITEM];
        ShopItem[] sellItems = new ShopItem[TOTALITEM];
        for (int i = 0; i < TOTALITEM; i++) {
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

            newButton.setFont(new Font("Comic Sans MS", 14));
            if (i == TOTALITEM - 2) {
                newButton.setOpacity(0);
            }
            buyItems[i] = item;

            ImageView icon = new ImageView(buyItems[i].getImage());
            icon.setFitWidth(32);
            icon.setFitHeight(32);

            if (i < MAXITEMPERROW) {
                newButton.relocate(40 + i * 120, 80);
                icon.relocate(70 + i * 120, 120);
            } else {
                newButton.relocate(40 + (i - MAXITEMPERROW) * 150, 240);
                icon.relocate(100 + (i - MAXITEMPERROW) * 150, 280);
            }

            buyItems[i].setButton(newButton);

            canvas.getChildren().addAll(newButton, icon);

        }

        for (int i = 0; i < TOTALITEM; i++) {
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

            newButton.setFont(new Font("Comic Sans MS", 14));

            if (i < MAXITEMPERROW) {
                newButton.relocate(40 + i * 120, 160);
            } else {
                newButton.relocate(40 + (i - MAXITEMPERROW) * 150, 320);
            }

            sellItems[i] = item;
            sellItems[i].setButton(newButton);

            canvas.getChildren().addAll(newButton);
        }
        Button tempButton;
        for (int i = 0; i < TOTALITEM; i++) {
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

        canvas.setPrefSize(1100, 400);
        winLabel.setFont(new Font("Comic Sans MS", 18));
        winLabel.setStyle("-fx-text-fill: #ffbf00;");
        winLabel.relocate(480, 30);
        returnGameButton.setFont(new Font("Comic Sans MS", 14));
        returnGameButton.setOpacity(0.65);
        returnGameButton.relocate(700, 280);
        canvas.getChildren().addAll(winLabel, returnGameButton);

        Scene scene = new Scene(canvas, 1100, 400);
        scene.getStylesheets()
                .addAll(this.getClass().getResource("background_images/shop_menu_background.css").toExternalForm());
        stage.setScene(scene);
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
