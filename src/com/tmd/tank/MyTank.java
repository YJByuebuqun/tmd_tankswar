package com.tmd.tank;

import com.tmd.util.MyUtil;

import java.awt.*;

public class MyTank extends Tank {
    /**
     * 我方坦克的图片数组
     */
    private static Image[] tankImg;

    static {
        tankImg = new Image[4];
        tankImg[0] = MyUtil.createImage("res/tank1_0.png");
        tankImg[1] = MyUtil.createImage("res/tank1_1.png");
        tankImg[2] = MyUtil.createImage("res/tank1_2.png");
        tankImg[3] = MyUtil.createImage("res/tank1_3.png");
    }

    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
    }

    @Override
    public void drawImgTank(Graphics g) {
        g.drawImage(tankImg[getDir()], getX() - RADIUS, getY() - RADIUS, null);
    }
}
