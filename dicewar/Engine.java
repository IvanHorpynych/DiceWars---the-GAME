package dicewar;

import dicewar.objects.Cell;
import dicewar.objects.Filed;


public class Engine {
    Filed filed;
    private static Engine engine;

    private Engine(Filed filed) {
        this.filed = filed;
    }

    public static Engine getEngine(Filed filed) {
        engine = new Engine(filed);
        return engine;
    }

    public Filed getFiled() {
        return filed;
    }

    public boolean battle(int posCellMy_x, int posCellMy_y, int posCellEnemy_x, int posCellEnemy_y, Filed filed) {
        Cell cellMy = filed.getCell(posCellMy_x, posCellMy_y);
        Cell cellEnemy = filed.getCell(posCellEnemy_x, posCellEnemy_y);
        boolean result = false;
        if (cellMy.getDices() != 1) {
            result = loto(cellMy, cellEnemy);
            if (result) {
                cellEnemy.setBoss(cellMy.getBoss_id());
                cellEnemy.setDices((byte) (cellMy.getDices() - 1));
                cellMy.setDices((byte) 1);
            } else {
                cellMy.setDices((byte) 1);
            }
        }
        return result;
    }

    private boolean loto(Cell cell_1, Cell cell_2) {
        return ((cell_1.drawing() > cell_2.drawing()) ? true : false);
    }
}

