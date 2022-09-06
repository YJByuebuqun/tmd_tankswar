package com.tmd.util;

import com.tmd.game.Explode;
import com.tmd.tank.EnemyTank;
import com.tmd.tank.Tank;

import java.util.ArrayList;
import java.util.List;

/**
 * 敌人坦克对象池
 */

public class EnemyTanksPool {
    //用于保存所有的坦克的容器
    private static final int DEFAULT_POOL_SIZE = 10;
    //在类加载的时候创建20坦克对象添加入容器中
    private static List<Tank> pool = new ArrayList<Tank>();

    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new EnemyTank());
        }
    }

    /**
     *
     */
    public static Tank get() {
        Tank tank = null;
        //如果池塘空了，我就新建一个坦克对象
        if (pool.size() == 0) {
            tank = new EnemyTank();
        } else {//每次都拿走池塘第一个坦克对象
            tank = pool.remove(0);
        }
        return tank;
    }

    /**
     *
     */
    public static void theReturn(Tank tank) {
        if (pool.size() == 10) {
            return;
        }
        pool.add(tank);


    }
}
