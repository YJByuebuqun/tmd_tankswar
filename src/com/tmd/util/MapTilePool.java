package com.tmd.util;

import com.tmd.game.Bullet;
import com.tmd.map.MapTile;

import java.util.ArrayList;
import java.util.List;

public class MapTilePool {
    //用于保存所有的炮弹的容器
    private static final int DEFAULT_POOL_SIZE = 100;
    //在类加载的时候创建200个炮弹对象添加入容器中
    private static List<MapTile> pool = new ArrayList<MapTile>();

    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new MapTile());
        }
    }

    /**
     *
     */
    public static MapTile get() {
        MapTile tile = null;
        //如果池塘空了，我就新建一个子弹
        if (pool.size() ==0) {
            tile = new MapTile();
        } else {//每次都拿走池塘第一个子弹对象
            tile = pool.remove(0);
        }
        return tile;
    }

    /**
     *
     */
    public static void theReturn(MapTile tile) {
        if (pool.size() == 50) {
            return;
        }
        pool.add(tile);


    }

}
