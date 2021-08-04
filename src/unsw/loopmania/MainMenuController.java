package unsw.loopmania;

import java.io.IOException;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 * controller for the main menu. TODO = you could extend this, for example with
 * a settings menu, or a menu to load particular maps.
 */
public class MainMenuController {
    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;
    public SimpleBooleanProperty reStart = new SimpleBooleanProperty();
    public SimpleBooleanProperty load = new SimpleBooleanProperty();
    private String musicFile = "music/main.mp3";
    @FXML
    private Button restartGameButton;
    @FXML
    private Button startGameButton;
    @FXML
    private Button loadGameButton;
    @FXML
    private Label mainMenu;

    @FXML
    private MediaPlayer mediaPlayer;

    public MainMenuController() {
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }

    public void setGameSwitcher(MenuSwitcher gameSwitcher) {
        this.gameSwitcher = gameSwitcher;
        this.reStart.set(false);
        this.load.set(false);
        restartGameButton.setDisable(true);
        File saveDir = new File("save/");
        if (saveDir.list().length > 0)
            loadGameButton.setDisable(false);
        else
            loadGameButton.setDisable(true);
    }

    public void disableGameOver() {
        startGameButton.textProperty().set("Game Over");
        startGameButton.setDisable(true);
    }

    public void turnOffMusic() {
        mediaPlayer.pause();
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    public void switchToGame() throws IOException {
        turnOffMusic();
        gameSwitcher.switchMenu();
        restartGameButton.setDisable(false);
        restartGameButton.textProperty().set("Start New Game");
        startGameButton.textProperty().set("Continue");
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    public void loadToGame() throws IOException {
        this.load.set(true);
        turnOffMusic();
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    private void reStartGame() throws IOException {
        turnOffMusic();
        this.reStart.set(true);
    }

    /**
     * return if game is successfully restarted
     * 
     * @return
     */
    public Boolean getReStart() {
        return this.reStart.get();
    }

    /**
     * return if game is successfully started
     * 
     * @return
     */
    public Boolean getLoad() {
        return this.load.get();
    }

    // methods added by Haoran for mode switching and information display

    /**
     * Set mainMenu back to "Main Menu"
     */
    @FXML
    public void undisplay() {
        mainMenu.setText("Main Menu");
    }

    /**
     * Set mainMenu to Standard mode information
     */
    @FXML
    public void display() {
        mainMenu.setText("Polished Looping Experience");
    }
}
