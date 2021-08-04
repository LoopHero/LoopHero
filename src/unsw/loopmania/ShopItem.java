package unsw.loopmania;

import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class ShopItem {
    private String name;
    private int price;
    private Image icon;
    private Button button;

    public ShopItem(String name, int price, Image icon) {
        this.name = name;
        this.price = price;
        this.icon = icon;
    }

    /**
     * Bind button for ShopItem
     * 
     * @param button
     */
    public void setButton(Button button) {
        this.button = button;
    }

    /**
     * returns binded button
     * 
     * @return
     */
    public Button getButton() {
        return this.button;
    }

    /**
     * returns price of ShopItem
     * 
     * @return
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * returns image object of ShopItem
     * 
     * @return
     */
    public Image getImage() {
        return this.icon;
    }

    /**
     * returns name of ShopItem
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }
}
