package dicewar.objects;

import java.io.Serializable;

public class Cell implements Serializable {
    private byte boss_id = -1;
    private byte dices = 0;
    private int connection = -1;

    Cell() {
    }

    Cell(byte boss_id, byte dices) {
        this.dices = dices;
        this.boss_id = boss_id;
    }

    public void setBoss(byte boss_id) {
        this.boss_id = boss_id;
    }

    public void setDices(byte dices) {
        this.dices = dices;
    }

    public void setConnection(int connection) {
        this.connection = connection;
    }

    public int getConnection() {
        return connection;
    }

    public byte getBoss_id() {
        return boss_id;
    }

    public byte getDices() {
        return dices;
    }

    public int drawing() {
        int sum = 0;
        for (int i = 0; i < dices; i++) {
            sum += (int) (Math.random() * 6 + 1);
        }
        return sum;
    }

}
