package dicewar.GUI;

import javafx.scene.Node;
import javafx.scene.control.Button;


public class UserButton extends Button {
    private int x, y;

    public UserButton() {
    }

    public UserButton(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}