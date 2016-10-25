package dicewar.objects;

import java.io.Serializable;

public class Player implements Cloneable, Serializable {
    private byte player_id;
    private String name = "Unknown";

    public Player(String name, byte player_id) {
        this.name = name;
        this.player_id = player_id;
    }

    public String getName() {
        return name;
    }

    public byte getPlayer_id() {
        return player_id;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


