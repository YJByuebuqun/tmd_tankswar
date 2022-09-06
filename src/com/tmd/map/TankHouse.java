package com.tmd.map;

import com.tmd.tank.Tank;
import com.tmd.util.Constant;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TankHouse {
    //老巢的x坐标，Y坐标
    public static final int HOUSE_X = (Constant.FRAME_WIDTH - MapTile.getTileW() * 3) / 2;
    public static final int HOUSE_Y = Constant.FRAME_HEIGHT - MapTile.getTileH() * 2;

    //一共六个砖块
    private List<MapTile> tiles = new ArrayList<MapTile>();

    public TankHouse() {
        tiles.add(new MapTile(HOUSE_X, HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X + MapTile.getTileW(), HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X + MapTile.getTileW() * 2, HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X + MapTile.getTileW() * 2, HOUSE_Y + MapTile.getTileH()));
        tiles.add(new MapTile(HOUSE_X, HOUSE_Y + MapTile.getTileH()));
        tiles.add(new MapTile(HOUSE_X + MapTile.getTileW(), HOUSE_Y + MapTile.getTileH()));
        tiles.get(tiles.size() - 1).setType(MapTile.TYPE_HOUSE);
    }

    public void draw(Graphics g) {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }
}
