package dicewar.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Filed implements Serializable {
    Cell[][] filed;
    ArrayList<Player> players = new ArrayList<>();
    private int filedSize;
    private int realFiledSize;
    private Player currentPlayer;

    public Filed(int size_y, int size_x, ArrayList<Player> players) {
        for (Player player : players) {
            try {
                this.players.add((Player) player.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        filed = new Cell[size_y][size_x];
        filedSize = (size_y * size_x) - (size_y * size_x / 6);
        fillingFiled();
        setCurrentPlayer();
        setRealFiledSize();
    }

    private void fillingFiled() {
        for (int i = 1; i <= filed.length * filed[0].length / 6; i++) {
            filed[(int) (Math.random() * filed.length)][(int) (Math.random() * filed[1].length)] = new Brick();
        }
        byte[] bufDices = randomFillingDices();
        for (Player player : players) {
            for (int p = 1; p <= (filedSize / 2); p++) {
                int i;
                int j;
                while (true) {
                    i = (int) (Math.random() * filed.length);
                    j = (int) (Math.random() * filed[1].length);
                    if (filed[i][j] == null) {
                        filed[i][j] = new Cell(player.getPlayer_id(), bufDices[p - 1]);
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < filed.length; i++) {
            for (int j = 0; j < filed[1].length; j++) {
                if (filed[i][j] == null) {
                    filed[i][j] = new Brick();
                }
            }
        }
    }

    void setRealFiledSize() {
        int buf = 0;
        for (int i = 0; i < filed.length; i++) {
            for (int j = 0; j < filed[1].length; j++) {
                if (!filed[i][j].getClass().getSimpleName().equals("Brick")) {
                    buf++;
                }
            }
        }
        this.realFiledSize = buf;
    }

    private byte[] randomFillingDices() {
        byte[] buf = new byte[filedSize / 2];
        int requiedSumm = (filedSize * 3) / 2;
        int actualSumm = 0;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (Math.random() * 8 + 1);
            actualSumm += buf[i];
        }
        for (int i = 0; i < actualSumm - requiedSumm; i++) {
            int j = 0;
            while (true) {
                j = (int) (Math.random() * buf.length);
                if (buf[j] != 1) {
                    buf[j] -= 1;
                    break;
                }
            }
        }
        return buf;
    }

    int BonusDice(Player player) {
        ArrayList<ArrayList<Cell>> connectionList = new ArrayList();
        int boss_id = player.getPlayer_id();
        for (int i = 0; i < filed.length; i++) {
            for (int j = 0; j < filed[1].length; j++) {
                if (filed[i][j].getBoss_id() == boss_id) {
                    if (filed[i][j].getConnection() == -1) {
                        if (i != 0 && filed[i - 1][j].getBoss_id() == boss_id) {
                            filed[i][j].setConnection(filed[i - 1][j].getConnection());
                            connectionList.get(filed[i - 1][j].getConnection()).add(filed[i][j]);
                        } else {
                            connectionList.add(new ArrayList<Cell>());
                            filed[i][j].setConnection(connectionList.size() - 1);
                            connectionList.get(filed[i][j].getConnection()).add(filed[i][j]);
                        }
                    }
                    if (i != 0 && filed[i - 1][j].getBoss_id() == boss_id && filed[i - 1][j].getConnection() != filed[i][j].getConnection()) {
                        int buf = filed[i][j].getConnection();
                        for (Cell cell : connectionList.get(filed[i][j].getConnection())) {
                            cell.setConnection(filed[i - 1][j].getConnection());
                        }
                        connectionList.get(filed[i - 1][j].getConnection()).addAll(connectionList.get(buf));
                    }
                    if (j != filed[1].length - 1) {
                        if (filed[i][j + 1].getBoss_id() == boss_id) {
                            filed[i][j + 1].setConnection(filed[i][j].getConnection());
                            connectionList.get(filed[i][j].getConnection()).add(filed[i][j + 1]);
                        }
                    }

                }
            }
        }
        int maxValue = 0;
        for (ArrayList<Cell> cellList : connectionList) {
            if (cellList.size() > maxValue) {
                maxValue = cellList.size();
            }
            for (Cell cell : cellList) {
                cell.setConnection(-1);
            }
        }

        return maxValue;
    }

    public boolean fillingBonusDices() {
        int dices = BonusDice(currentPlayer);
        int i;
        int j;
        int numberCell = 0;
        int sunDices = 0;
        for (i = 0; i < filed.length; i++) {
            for (j = 0; j < filed[1].length; j++) {
                if (filed[i][j].getBoss_id() == currentPlayer.getPlayer_id()) {
                    numberCell++;
                    sunDices += filed[i][j].getDices();
                }
            }
        }
        int permissiveSumDices = numberCell * 8 - sunDices;
        if (dices > permissiveSumDices) {
            dices = permissiveSumDices;
        }
        for (int p = 0; p < dices; p++) {
            while (true) {
                i = (int) (Math.random() * filed.length);
                j = (int) (Math.random() * filed[1].length);
                if (filed[i][j].getBoss_id() == currentPlayer.getPlayer_id() && filed[i][j].getDices() < 8) {
                    filed[i][j].setDices((byte) (filed[i][j].getDices() + 1));
                    break;
                }
            }
        }
        return (dices == realFiledSize ? true : false);
    }

    public Cell[][] getFiled() {
        return filed;

    }

    public Cell getCell(int x, int y) {
        return filed[x][y];
    }


    public void setCurrentPlayer() {
        if (currentPlayer == null) {
            currentPlayer = players.get(0);
        } else {
            int posPlayer = players.indexOf(currentPlayer);

            if (players.size() - 1 == posPlayer) {
                currentPlayer = players.get(0);
            } else {
                currentPlayer = players.get(posPlayer + 1);
            }
        }

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getRealFiledSize() {
        return realFiledSize;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    //    public static void main(String[] args) {
//        Player[] players = {new Player("Ivan", (byte) 0), new Player("John,", (byte) 2)};
//        Filed filed = new Filed(5, 10, players);
//        for (int i = 0; i < filed.getFiled().length; i++) {
//            for (int j = 0; j < filed.getFiled()[1].length; j++) {
//                System.out.printf("%4d", filed.getFiled()[i][j].getDices());
//            }
//            System.out.println();
//        }
//        filed.fillingBonusDices();
//        System.out.println("");
//        for (int i = 0; i < filed.getFiled().length; i++) {
//            for (int j = 0; j < filed.getFiled()[1].length; j++) {
//                System.out.printf("%4d", filed.getFiled()[i][j].getDices());
//            }
//            System.out.println();
//        }
//        System.out.println(filed.getCurrentPlayer());
//        filed.setCurrentPlayer();
//        System.out.println(filed.getCurrentPlayer());
//        filed.setCurrentPlayer();
//        System.out.println(filed.getCurrentPlayer());
//    }


}






