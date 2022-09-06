package com.tmd.game;

import com.tmd.util.MyUtil;

import java.awt.*;

/**
 * 用来控制爆炸效果的类
 */
public class Explode {
    //定义爆炸效果一共4帧
    public static final int EXPLODE_FRAME_COUNT = 12;

    private static Image[] Img;

    static {
        Img = new Image[EXPLODE_FRAME_COUNT/3];
        Img[0] = MyUtil.createImage("res/boom_0.png");
        Img[1] = MyUtil.createImage("res/boom_1.png");
        Img[2] = MyUtil.createImage("res/boom_2.png");
        Img[3] = MyUtil.createImage("res/boom_3.png");
    }

    //爆炸效果的坐标
    private int x, y;
    //当前播放的帧的下标[0-3]
    private int index;
    //定义爆炸效果图片是否可见
    private boolean visible = true;
    private int explodeWidth;
    private int explodeHeight;

    /**
     * 构造方法
     * @param
     */
    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Explode() {
    }

    /**
     * 爆炸效果的绘画
     * @param g
     */
    public void draw(Graphics g) {
        //对爆炸效果图片的宽高的效果的确定
        //if(explodeHeight<=0){
            explodeWidth=Img[0].getWidth(null)/2;
            explodeHeight=Img[0].getHeight(null);
        //}
        //设置效果：visible如果为false我就不可见，直接返回不会绘制
        if (!visible) {
            return;
        } else {
            g.drawImage(Img[index/3], x-explodeWidth, y-explodeHeight, null);
        }
        index++;
        //如果我图片4帧播放完了，我就设置为不可见，不可见就会直接返回不会绘制图片；
        if (index >= EXPLODE_FRAME_COUNT) {
            visible = false;
        }
    }


    /**
     * 改变变量
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
