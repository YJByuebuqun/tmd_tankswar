package com.tmd.game;

import com.tmd.tank.MyTank;
import com.tmd.tank.Tank;
import com.tmd.util.Constant;
import com.tmd.util.MyUtil;

import java.awt.*;

/**
 * 子弹类
 */
public class Bullet {
    //定义炮弹的默认速度是坦克的2倍
    private static final int DEFAULT_SPEED = Tank.DEFAULT_SPEED * 2;
    //定义坦克炮弹的大小
    private static final int RADIUS = 4;

    //定义炮弹的坐标
    private int x, y;
    //定义炮弹速度变量
    private int speed = DEFAULT_SPEED;
    //定义炮弹的发射方向
    private int dir;
    //定义炮弹的伤害
    private int atk;
    //定义炮弹颜色
    private Color color;
    //定义子弹是否可见
    private boolean visible;

    /**
     * 子弹构造方法
     */
    public Bullet(int x, int y, int dir, int atk, Color color) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.atk = atk;
        this.color = color;
    }

    //为对象池创建无参构造
    public Bullet() {
    }

    /**
     * 炮弹的绘制的方法draw 要用画笔
     */
    public void draw(Graphics g) {
        //如果子弹不可见，就不调用draw方法;
        if (!visible) return;
        logic();
        g.setColor(color);
        //绘制炮弹身体圆
        g.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);

    }

    /**
     * 坦克的处理逻辑
     */
    private void logic() {
        move();

    }

    //炮弹的移动逻辑
    private void move() {
        switch (dir) {
            case Tank.DIR_UP:
                y = y - speed;
                if (y < 0) {
                    visible = false;
                }
                break;
            case Tank.DIR_DOWN:
                if (y > Constant.FRAME_HEIGHT) {//如果子弹坐标不在屏幕高度以内，就不再绘制draw，
                    visible = false;
                }
                y = y + speed;
                break;
            case Tank.DIR_LEFT:
                x = x - speed;
                if (x < 0) {
                    visible = false;
                }
                break;
            case Tank.DIR_RIGHT:
                x = x + speed;
                if (x > Constant.FRAME_WIDTH) {
                    visible = false;
                }
                break;

        }
    }


    /**
     * 改变变量
     *
     * @return
     */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
