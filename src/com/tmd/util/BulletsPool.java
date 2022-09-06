package com.tmd.util;

import com.tmd.game.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * 子弹对象池
 */
public class BulletsPool {
    //用于保存所有的炮弹的容器
    private static final int DEFAULT_POOL_SIZE = 200;
    //在类加载的时候创建200个炮弹对象添加入容器中
    private static List<Bullet> pool = new ArrayList<Bullet>();

    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Bullet());
        }
    }

    /**
     *
     */
    public static Bullet get() {
        Bullet bullet = null;
        //如果池塘空了，我就新建一个子弹
        if (pool.size() == 0) {
            bullet = new Bullet();
        } else {//每次都拿走池塘第一个子弹对象
            bullet = pool.remove(0);
        }
        return bullet;
    }

    /**
     *
     */
    public static void theReturn(Bullet bullet) {
        if (pool.size() == 200) {
            return;
        }
        pool.add(bullet);


    }
}
