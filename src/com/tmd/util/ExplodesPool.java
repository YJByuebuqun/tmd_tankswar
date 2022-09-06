package com.tmd.util;

import com.tmd.game.Bullet;
import com.tmd.game.Explode;

import java.util.ArrayList;
import java.util.List;

/**
 * 爆炸对象池
 */
public class ExplodesPool {
    //用于保存所有的炮弹的容器
    private static final int DEFAULT_POOL_SIZE = 10;
    //在类加载的时候创建10个爆炸效果对象添加入容器中
    private static List<Explode> pool = new ArrayList<Explode>();

    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Explode());
        }
    }

    /**
     *
     */
    public static Explode get() {
        Explode explode = null;
        //如果池塘空了，我就新建一个爆炸效果
        if (pool.size() == 0) {
            explode = new Explode();
        } else {//每次都拿走池塘第一个子爆炸效果对象
            explode = pool.remove(0);
        }
        return explode;
    }

    /**
     *
     */
    public static void theReturn(Explode explode) {
        if (pool.size() == 10) {
            return;
        }
        pool.add(explode);


    }
}
