package com.tmd.tank;

import com.tmd.game.GameFrame;
import com.tmd.game.LevelInfo;
import com.tmd.util.Constant;
import com.tmd.util.MyUtil;
import com.tmd.util.EnemyTanksPool;

import java.awt.*;

public class EnemyTank extends Tank {
    public static final int TYPE_GREEN = 0;
    public static final int TYPE_YELLOW = 1;
    private int type = TYPE_GREEN;
    //敌人坦克的图片数组
    private static Image[] greenImg;
    private static Image[] yellowImg;
    //记录敌人AI时间
    private long aiTime;

    /**
     * 静态代码块中对他进行初始化
     */
    static {
        greenImg = new Image[4];
        greenImg[0] = MyUtil.createImage("res/ul.png");
        greenImg[1] = MyUtil.createImage("res/dl.png");
        greenImg[2] = MyUtil.createImage("res/ll.png");
        greenImg[3] = MyUtil.createImage("res/rl.png");

        yellowImg = new Image[4];
        yellowImg[0] = MyUtil.createImage("res/u.png");
        yellowImg[1] = MyUtil.createImage("res/d.png");
        yellowImg[2] = MyUtil.createImage("res/l.png");
        yellowImg[3] = MyUtil.createImage("res/r.png");

    }

    private EnemyTank(int x, int y, int dir) {
        super(x, y, dir);
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0, 2);
    }

    public EnemyTank() {
        type = MyUtil.getRandomNumber(0, 2);
        aiTime = System.currentTimeMillis();
    }

    /**
     * 用于创建一个敌人的坦克
     *
     * @return enemy
     */
    public static Tank createEnemy() {
        //生成一个随机数，通过随机数控制x坐标的位置
        int x = MyUtil.getRandomNumber(0, 2) == 0 ? RADIUS : Constant.FRAME_WIDTH - RADIUS;
        int y = GameFrame.titleBarH + RADIUS;
        int dir = DIR_DOWN;
        //从坦克对象池中获取对象
        EnemyTank enemy = (EnemyTank) EnemyTanksPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setName("");

        enemy.setEnemy(true);
        enemy.setState(STATE_MOVE);
        //根据游戏设定的LevelType游戏难度，来增加敌人坦克的血量，让游戏更难
        int maxHp = Tank.DEFAULT_HP * LevelInfo.getInstance().getLevelType();
        enemy.setHp(maxHp);
        enemy.setMaxHP(maxHp);

        //通过关卡信息中的敌人类型。来设置当前出生敌人的类型
        int enemyType = LevelInfo.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);
        return enemy;
    }

    @Override
    public void drawImgTank(Graphics g) {
        ai();
        if (type == TYPE_GREEN) {
            g.drawImage(greenImg[getDir()], getX() - RADIUS, getY() - RADIUS, null);
        } else if (type == TYPE_YELLOW) {
            g.drawImage(yellowImg[getDir()], getX() - RADIUS, getY() - RADIUS, null);
        }

    }

    /**
     * 敌人的AI功能
     */
    private void ai() {
        if (System.currentTimeMillis() - aiTime > Constant.ENEMY_AI_INTERVAL) {
            //间隔1秒随机一个状态
            setState(MyUtil.getRandomNumber(0, 2) == 0 ? STATE_STAND : STATE_MOVE);
            //间隔1秒随机一个方向
            setDir(MyUtil.getRandomNumber(DIR_UP, DIR_RIGHT + 1));
            //将现在的时间付给aiTime重新开始计时
            aiTime = System.currentTimeMillis();

        }
        //如果概率小于0.05就开火
        if (Math.random() < Constant.ENEMY_FIRE_PERCENT) {
            fire();
        }

    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
