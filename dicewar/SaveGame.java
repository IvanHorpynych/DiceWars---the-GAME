package dicewar;

import dicewar.objects.Filed;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveGame {

    public void toByteArray(Filed filed) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream ous = new ObjectOutputStream(baos);
            ous.writeObject(filed);
            ous.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            Files.write(Paths.get("save.db"), buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Save is complited!!!");
    }

    public Filed getSave() {
        Filed filed = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(Files.readAllBytes(Paths.get("save.db")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            filed = (Filed) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Load is complited!!!");
        return filed;
    }


}
