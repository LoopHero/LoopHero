package unsw.loopmania;

import java.io.IOException;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    @FXML
    private Button restartGameButton;
    @FXML
    private Button startGameButton;

    @FXML
    private Label mainMenu;

    // @FXML
    // private Button startGameButton1; // Berserker Mode

    // @FXML
    // private Button startGameButton2; // Survival Mode

    public void setGameSwitcher(MenuSwitcher gameSwitcher) {
        this.gameSwitcher = gameSwitcher;
        this.reStart.set(false);
        restartGameButton.setDisable(true);
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    public void switchToGame() throws IOException {
        gameSwitcher.switchMenu();
        // disable selection of other modes, unless game process reset
        // startGameButton1.setDisable(true);
        // startGameButton2.setDisable(true);
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
    private void reStartGame() throws IOException {
        this.reStart.set(true);
        // startGameButton.setDisable(false);
        // startGameButton1.setDisable(false);
        // startGameButton2.setDisable(false);
    }

    /**
     * return if game is successfully retarted
     * @return
     */
    public Boolean getReStart() {
        return this.reStart.get();
    }

    // methods added by Haoran for mode switching and information display

    /**
     * Set mainMenu back to "Main Menu"
     */
    @FXML
    public void undisplay(){
        mainMenu.setText("Main Menu");
    }

    /**
     * Set mainMenu to Standard mode information
     */
    @FXML
    public void display(){
        mainMenu.setText("Polished Looping Experience");
    }

    // /**
    //  * Set mainMenu to Berserker mode information
    //  */
    // @FXML
    // public void displayBerserker(){
    //     mainMenu.setText("Not a fan of much defence");
    // }

    // /**
    //  * Set mainMenu to Survival mode information
    //  */
    // @FXML
    // public void displaySurvival(){
    //     mainMenu.setText("Mean shop, one potion one time");
    // }

    // /**
    //  * facilitates switching to main game (Berserker) upon button click
    //  * 
    //  * @throws IOException
    //  */
    // @FXML
    // public void switchToGameBerserker() throws IOException {
    //     gameSwitcher.switchMenu();
    //     // disable selection of other modes, unless game process reset
    //     startGameButton.setDisable(true);
    //     startGameButton2.setDisable(true);
    //     restartGameButton.setDisable(false);
    //     restartGameButton.textProperty().set("Reset Game Progress");
    //     startGameButton1.textProperty().set("Continue");
    // }

    // /**
    //  * facilitates switching to main game (Survival) upon button click
    //  * 
    //  * @throws IOException
    //  */
    // @FXML
    // public void switchToGameSurvival() throws IOException {
    //     gameSwitcher.switchMenu();
    //     // disable selection of other modes, unless game process reset
    //     startGameButton.setDisable(true);
    //     startGameButton1.setDisable(true);
    //     restartGameButton.setDisable(false);
    //     restartGameButton.textProperty().set("Reset Game Progress");
    //     startGameButton2.textProperty().set("Continue");
    // }
}
