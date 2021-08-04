package unsw.loopmania;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * the main application run main method from this class
 */
public class LoopManiaApplication extends Application {
    // TODO = possibly add other menus?

    /**
     * the controller for the game. Stored as a field so can terminate it when click
     * exit button
     */
    private LoopManiaWorldController mainController;
    private int startMode = 0; // 0 for standard, 1 for restart, 2 for load

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        // set title on top of window bar
        primaryStage.setTitle("Loop Mania");

        // prevent human player resizing game window (since otherwise would see white
        // space)
        // alternatively, you could allow rescaling of the game (you'd have to program
        // resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main game
        LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader(
                "world_with_twists_and_turns.json");
        mainController = loopManiaLoader.loadController();

        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
        gameLoader.setController(mainController);
        Parent gameRoot = gameLoader.load();

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController();
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        Parent mainMenuRoot = menuLoader.load();

        // create new scene with the main menu (so we start with the main menu)
        Scene scene = new Scene(mainMenuRoot);

        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main
        // menu
        mainController.setMainMenuSwitcher(() -> {
            switchToRoot(scene, mainMenuRoot, primaryStage);
        });
        mainMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
            mainController.bindTextFields();
        });
        if (startMode == 1) {
            mainMenuController.switchToGame();
        } else if (startMode == 2) {
            mainController.loadTimer();
            mainController.bindTextFields();
            mainMenuController.switchToGame();
            mainController.pause();
        }
        // deploy the main onto the stage
        gameRoot.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
        resetGameListener(mainMenuController, primaryStage);
        loadGameListener(mainMenuController, primaryStage);
        continueGameListener(mainController, mainMenuController);
    }

    private void continueGameListener(LoopManiaWorldController mainController, MainMenuController mainMenuController) {

        mainController.gameOver.addListener(observable -> {
            if (mainController.gameOver.get()) {
                mainController.stopMusic();
                mainMenuController.disableGameOver();
            }
        });
    }

    private void loadGameListener(MainMenuController mainMenuController, Stage primaryStage) {
        mainMenuController.load.addListener(observable -> {
            if (mainMenuController.getLoad()) {
                mainController.stopMusic();
                System.out.println("Load Game");
                try {
                    startMode = 2;
                    mainController.terminate();
                    start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void resetGameListener(MainMenuController mainMenuController, Stage primaryStage) {
        mainMenuController.reStart.addListener(observable -> {
            if (mainMenuController.getReStart()) {
                mainController.stopMusic();
                try {
                    startMode = 1;
                    mainController.terminate();
                    start(primaryStage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Error in reset game progress");
                }
            }
        });
    }

    @Override
    public void stop() {
        // wrap up activities when exit program
        mainController.terminate();
    }

    /**
     * switch to a different Root
     */
    private void switchToRoot(Scene scene, Parent root, Stage stage) {
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}