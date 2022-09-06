package com.tmd.map;

import com.tmd.game.Bullet;
import com.tmd.util.BulletsPool;
import com.tmd.util.Constant;
import com.tmd.util.MyUtil;

import java.awt.*;
import java.util.List;

/**
 * 地图元素块-墙
 */
public class MapTile {
    //砖块类型
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOUSE = 1;
    public static final int TYPE_COVER = 2;
    public static final int TYPE_HARD = 3;

    //设置图片长和宽
    private static int tileW = 40;
    private static int tileH = 40;
    public static final int radius = tileW / 2;
    //定义砖块是否可见
    private boolean visible = true;
    private int type = TYPE_NORMAL;

    private static Image[] tileImg;

    static {
        tileImg = new Image[4];
        tileImg[TYPE_NORMAL] = MyUtil.createImage("res/tile.png");
        tileImg[TYPE_HOUSE] = MyUtil.createImage("res/house.png");
        tileImg[TYPE_COVER] = MyUtil.createImage("res/cover.png");
        tileImg[TYPE_HARD] = MyUtil.createImage("res/hard.png");
    }

    //砖块坐标
    private int x, y;

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MapTile() {
    }

    /**
     * 绘制砖块
     *
     * @param g
     */
    public void draw(Graphics g) {
        if (!visible) return;
        g.drawImage(tileImg[type], x, y, null);

    }


    /**
     * 1个子弹与墙壁的碰撞是否有
     *
     * @return
     */
    public boolean isCollideBullet(List<Bullet> bullets) {
        if (!visible) return false;
        // for (Bullet bullet : bullets) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            boolean collide = MyUtil.isCollide(x + radius, y + radius, radius, bulletX, bulletY);
            if (collide) {
                //子弹销毁
                bullet.setVisible(false);
                BulletsPool.theReturn(bullet);
                return true;
            }
        }
        return false;
    }

    public boolean isHouse() {
        return type == TYPE_HOUSE;
    }

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

    public static int getTileW() {
        return tileW;
    }

    public static void setTileW(int tileW) {
        MapTile.tileW = tileW;
    }

    public static int getTileH() {
        return tileH;
    }

    public static void setTileH(int tileH) {
        MapTile.tileH = tileH;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
