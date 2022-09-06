package com.tmd.tank;

import com.tmd.game.Bullet;
import com.tmd.game.Explode;
import com.tmd.game.GameFrame;
import com.tmd.map.MapTile;
import com.tmd.util.*;

import java.awt.*;
import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 坦克类
 */
public abstract class Tank {


    /**
     * 坦克各大属性
     */
    //定义坦克行走的四个方向
    public static final int DIR_UP = 0;//向上
    public static final int DIR_DOWN = 1;//向下
    public static final int DIR_LEFT = 2;//向左
    public static final int DIR_RIGHT = 3;//向右
    //定义坦克大小直径
    public static final int RADIUS = 20;
    //默认速度 每帧1ms
    public static final int DEFAULT_SPEED = 4;
    //坦克状态
    public static final int STATE_STAND = 0;//停止状态
    public static final int STATE_MOVE = 1;//行走状态
    public static final int STATE_DIE = 2;//死亡状态
    //坦克默认生命值
    public static final int DEFAULT_HP = 100;
    public int maxHP = DEFAULT_HP;
    //定义坦克坐标
    private int x, y;
    //定义坦克血量
    private int hp = DEFAULT_HP;
    //定义坦克名字
    private String name;
    //定义坦克攻击力,伤害控制在50-100；
    private int atk;
    public static final int ATK_MAX = 25;
    public static final int ATK_MIN = 20;
    //定义坦克速度
    private int speed = DEFAULT_SPEED;
    //定义坦克行走方向
    public int dir;
    private int state = STATE_STAND;
    //坦克颜色
    private Color color;
    //定义坦克是否为敌机标识，默认为false
    private boolean isEnemy = false;

    private BloodBar Bar = new BloodBar();


    /**
     * 创建坦克构造方法
     */
    //炮弹
    private List<Bullet> bullets = new ArrayList<Bullet>();
    //创建一个容器用来装爆炸效果
    private List<Explode> explodes = new ArrayList<Explode>();

    public Tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        initTank();
        //加在这里是生成坦克的时候就固定了坦克的伤害，不是每一发子弹的伤害都不同
        //atk=MyUtil.getRandomNumber(ATK_MIN,ATK_MAX);
    }

    public Tank() {
        initTank();
    }

    private void initTank() {
        color = MyUtil.getRandomColor();
        name = MyUtil.getRandomName();
    }


    /**
     * 坦克类的绘制方法
     */
    public void draw(Graphics g) {
        //因为draw是每一帧调用的，我的logic也需要每一帧都调用
        logic();
        //坦克类绘制方法
        //drawTank(g);
        drawImgTank(g);
        //绘制炮弹方法
        drawBullets(g);
        //绘制坦克名字；
        drawName(g);
        Bar.draw(g);

    }

    /**
     * 坦克名字的绘制方法
     */
    private void drawName(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(Constant.SMALL_FONT);
        g.drawString(name, x - RADIUS, y - 35);
    }

    /**
     * 使用图片的方式去绘制坦克
     */
    public abstract void drawImgTank(Graphics g);

    /**
     * 坦克的绘制的方法draw 要用画笔:使用系统的方式去绘制坦克
     */
//    private void drawTank(Graphics g) {
//        g.setColor(color);
//        //绘制坦克身体圆
//        g.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
//        //绘制坦克炮口
//        int endX = x;
//        int endY = y;
//        switch (dir) {
//            case DIR_UP:
//                endY = y - RADIUS * 2;
//                g.drawLine(x - 1, y, endX - 1, endY);
//                g.drawLine(x + 1, y, endX + 1, endY);
//                break;
//            case DIR_DOWN:
//                endY = y + RADIUS * 2;
//                g.drawLine(x - 1, y, endX - 1, endY);
//                g.drawLine(x + 1, y, endX + 1, endY);
//                break;
//            case DIR_LEFT:
//                endX = x - RADIUS * 2;
//                g.drawLine(x, y - 1, endX, endY - 1);
//                g.drawLine(x, y + 1, endX, endY + 1);
//                break;
//            case DIR_RIGHT:
//                endX = x + RADIUS * 2;
//                g.drawLine(x, y - 1, endX, endY - 1);
//                g.drawLine(x, y + 1, endX, endY + 1);
//                break;
//        }
//        g.drawLine(x, y, endX, endY);
//
//    }

    /**
     * 坦克的处理逻辑
     */
    private void logic() {
        switch (state) {
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                break;
            case STATE_DIE:
                break;
        }

    }

    private int oldX = -1, oldY = -1;

    //坦克move移动逻辑
    private void move() {
        oldX = x;
        oldY = y;
        switch (dir) {
            case DIR_UP:
                y = y - speed;
                if (y < RADIUS + GameFrame.titleBarH) {
                    y = RADIUS + GameFrame.titleBarH;
                }
                break;
            case DIR_DOWN:
                y = y + speed;
                if (y > Constant.FRAME_HEIGHT - RADIUS) {
                    y = Constant.FRAME_HEIGHT - RADIUS;
                }
                break;
            case DIR_LEFT:
                x = x - speed;
                if (x < RADIUS) {
                    x = RADIUS;
                }
                break;
            case DIR_RIGHT:
                x = x + speed;
                if (x > Constant.FRAME_WIDTH - RADIUS) {
                    x = Constant.FRAME_WIDTH - RADIUS;
                }
                break;
        }
    }

    /**
     * 坦克发射炮弹的功能,控制坦克发射炮弹间隔时间
     */
    //上一次开火时间
    private long fireTime;
    public static final int FIRE_INTERVAL = 200;

    public void fire() {
        if (System.currentTimeMillis() - fireTime >= FIRE_INTERVAL) {
            int bulletX = x;
            int bulletY = y;
            switch (dir) {
                case DIR_UP:
                    bulletY = y - RADIUS;
                    break;
                case DIR_DOWN:
                    bulletY = y + RADIUS;
                    break;
                case DIR_LEFT:
                    bulletX = x - RADIUS;
                    break;
                case DIR_RIGHT:
                    bulletX = x + RADIUS;
                    break;
            }
            //从对象池BulletsPool中获取子弹
            Bullet bullet = BulletsPool.get();
            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setDir(dir);
            //每一个子弹的伤害都是随机的
            bullet.setAtk(MyUtil.getRandomNumber(ATK_MIN, ATK_MAX));
            bullet.setColor(color);
            bullet.setVisible(true);
            bullets.add(bullet);
            fireTime = System.currentTimeMillis();
        }

    }

    /**
     * 将所有子弹绘制出来
     */
    private void drawBullets(Graphics g) {
        for (Bullet bullte01 : bullets) {
            bullte01.draw(g);
        }
        //是遍历所有的子弹，将不可见的子弹移除，并还原到对象池
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isVisible()) {
                Bullet remove = bullets.remove(i);
                BulletsPool.theReturn(remove);
                i--;
            }
        }
        //System.out.println(bullets.size());

    }

    /**
     * 游戏结束返回所有子弹,并清空子弹集合
     */
    public void bulletsReturn() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            BulletsPool.theReturn(bullet);
        }
        bullets.clear();
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            ExplodesPool.theReturn(explode);
        }
        explodes.clear();
    }

    /**
     * 坦克和子弹的碰撞方法
     */
    public void collideBulltes(List<Bullet> bullets) {
        //遍历所有的子弹，依此和当前的坦克进行碰撞检测
        //for (Bullet bullet : bullets) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            //如果if中返回true就说明碰上了
            if (MyUtil.isCollide(x, y, RADIUS, bullet.getX(), bullet.getY())) {
                //碰到了，我就吧子弹Visible属性甚至成false，通过调用方法让子弹消失
                bullet.setVisible(false);
                //坦克受到伤害
                hurt(bullet);
                //添加爆炸效果的对象放入集合储存
                addExpole(x, y + RADIUS);
            }
        }
    }

    /**
     * 添加爆炸效果
     */
    private void addExpole(int x, int y) {
        //添加爆炸效果的对象放入集合储存
        Explode explode = ExplodesPool.get();
        explode.setX(x);
        explode.setY(y);
        explode.setVisible(true);
        explode.setIndex(0);
        explodes.add(explode);
    }

    /**
     * 坦克炮弹的伤害
     */
    private void hurt(Bullet bullet) {
        int atk = bullet.getAtk();
        System.out.println("atk=" + atk);
        hp = hp - atk;
        if (hp < 0) {
            hp = 0;
            die();
        }
    }

    //坦克死亡的方法：
    private void die() {
        //敌机如果死了就换回对象池
        if (isEnemy) {
            GameFrame.killEnemyCount++;
            EnemyTanksPool.theReturn(this);
            //判断本关是否结束
            if (GameFrame.isCrossLevel()) {
                if (GameFrame.isLastLevel()) {
                    //如果通过本关且通过的关卡数等于游戏关卡数，通关
                    GameFrame.setGameState(Constant.STATE_WIN);

                } else {
                    //TODO 进入下一关
                    GameFrame.startCrossLevel();
                    //GameFrame.nextLevel();
                }
            }
        } else {
            //我方坦克被消灭了，就gameover
            MusicUtil.playStart2();
            delaySecondsToOver(1000);

        }


    }

    //如果hp血量小于0，表示坦克已经死了
    public boolean isDie() {
        return hp <= 0;
    }


    /**
     * 绘制爆炸效果：通过集合遍历绘制每一个对象的爆炸效果
     *
     * @param g
     */
    public void drawExplodes(Graphics g) {
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            explode.draw(g);
        }
//        for (Explode explode : explodes) {
//            explode.draw(g);
//        }
        //是遍历所有的爆炸效果，将不可见的爆炸效果，并还原到对象池
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            if (!explode.isVisible()) {
                Explode remove = explodes.remove(i);
                ExplodesPool.theReturn(remove);
                i--;
            }
            //System.out.println(explodes.size());
        }

    }


    //内部类：来表示坦克的血条
    class BloodBar {
        private static final int BAR_LENGTH = 70;
        private static final int BAR_HEIGHT = 5;

        private void draw(Graphics g) {
            //绘制血条底色
            g.setColor(Color.lightGray);
            g.fillRect(x - RADIUS, y - RADIUS - 10, BAR_LENGTH, BAR_HEIGHT);
            //绘制红色的当前血量
            g.setColor(Color.red);
            g.fillRect(x - RADIUS, y - RADIUS - 10, hp * BAR_LENGTH / maxHP, BAR_HEIGHT);
            //绘制血条边框
            g.setColor(Color.white);
            g.drawRect(x - RADIUS, y - RADIUS - 10, BAR_LENGTH, BAR_HEIGHT);
        }

    }

    /**
     * 坦克子弹与所有地图块的碰撞
     */
    public void bulletCollideMapTile(List<MapTile> tiles) {
        //使用增强for循环，底层使用迭代器实现，删除增加元素存在安全隐患
        //for (MapTile tile : tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (tile.getType() != MapTile.TYPE_COVER) {
                if (tile.isCollideBullet(bullets)) {
                    //添加爆炸效果的对象放入集合储存
                    addExpole(tile.getX() + MapTile.radius, tile.getY() + MapTile.radius * 2);
                    //如果不是硬砖块的话，设置砖块消失，并归还对象池
                    if (tile.getType() != MapTile.TYPE_HARD) {
                        tile.setVisible(false);
                        MapTilePool.theReturn(tile);
                    }
                    //如果老巢被击败后，一秒切换到游戏结束画面
                    if (tile.isHouse()) {
                        delaySecondsToOver(2000);

                    }

                }
            }
        }

    }

    /**
     * 被击败后延迟进入游戏over状态
     *
     * @param millisSecond
     */
    private void delaySecondsToOver(int millisSecond) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millisSecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameFrame.setGameState(Constant.STATE_LOST);
            }
        }.start();

    }

    /**
     * 1个地图块与坦克的碰撞
     */
    public boolean isCollideMapTile(List<MapTile> tiles) {
        //点-1 顺时针算点坐标
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            //for (MapTile tile : tiles) {
            if (!tile.isVisible() || tile.getType() != MapTile.TYPE_COVER) {
                int tileX = tile.getX();
                int tileY = tile.getY();
                boolean collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
                //如果碰上了就直接返回，否则继续判断下一个点
                if (collide) {
                    return true;
                }
                //点-2
                tileX = tile.getX() + MapTile.radius;
                tileY = tile.getY();
                collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
                //如果碰上了就直接返回，否则继续判断下一个点
                if (collide) {
                    return true;
                }
                //点-3
                tileX = tile.getX() + MapTile.radius * 2;
                tileY = tile.getY();
                collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
                //如果碰上了就直接返回，否则继续判断下一个点
                if (collide) {
                    return true;
                }
                //点-4
                tileX = tile.getX() + MapTile.radius * 2;
                tileY = tile.getY() + MapTile.radius;
                collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
                //如果碰上了就直接返回，否则继续判断下一个点
                if (collide) {
                    return true;
                }
                //点-5
                tileX = tile.getX() + MapTile.radius * 2;
                tileY = tile.getY() + MapTile.radius * 2;
                collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
                //如果碰上了就直接返回，否则继续判断下一个点
                if (collide) {
                    return true;
                }
                //点-6
                tileX = tile.getX() + MapTile.radius;
                tileY = tile.getY() + MapTile.radius * 2;
                collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
                //如果碰上了就直接返回，否则继续判断下一个点
                if (collide) {
                    return true;
                }
                //点-7
                tileX = tile.getX();
                tileY = tile.getY() + MapTile.radius * 2;
                collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
                //如果碰上了就直接返回，否则继续判断下一个点
                if (collide) {
                    return true;
                }
                //点-8
                tileX = tile.getX();
                tileY = tile.getY() + MapTile.radius;
                collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
                //如果碰上了就直接返回，否则继续判断下一个点
                if (collide) {
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * 碰到砖块后坦克回退的方法
     */
    public void back() {
        x = oldX;
        y = oldY;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
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

    public int getState(int stateMove) {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public List<Explode> getExplodes() {
        return explodes;
    }

    public void setExplodes(List<Explode> explodes) {
        this.explodes = explodes;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
