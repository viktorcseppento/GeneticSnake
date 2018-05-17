package gameLogic;

import java.io.*;
import java.util.prefs.Preferences;

public class GameConfig {
    private String mapName;
    private Item[][] map = new Item[25][25];

    public GameConfig() {
        Preferences pref = Preferences.userNodeForPackage(this.getClass());
        String mapName = pref.get("mapName", "default.txt");
        Reader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("maps/" + mapName))));
        } catch (IOException e) {
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("maps/default.txt"))));
                mapName = "default.txt";
            } catch (IOException e2) {
                e.printStackTrace();
                return;
            }
        }

        try {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    int ch = reader.read();
                    if (ch == '1')
                        map[i][j] = Item.WALL;
                    else if (ch == '0')
                        map[i][j] = Item.NOTHING;
                    else {
                        throw new IOException("Bad file format");
                    }
                }
                if (reader.skip(2) != 2)
                    throw new IOException("Bad file format");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    public Item[][] getMap() {
        return map;
    }
}
