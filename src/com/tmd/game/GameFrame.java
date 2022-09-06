package com.tmd.game;
/**
 * 游戏窗口
 */

import com.tmd.map.GameMap;
import com.tmd.map.TankHouse;
import com.tmd.tank.EnemyTank;
import com.tmd.tank.MyTank;
import com.tmd.tank.Tank;
import com.tmd.util.Constant;
import com.tmd.util.MusicUtil;
import com.tmd.util.MyUtil;

import static com.tmd.util.Constant.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GameFrame extends Frame implements Runnable {
    //加载游戏结束的图片，由于游戏启动加载会大量消耗资源，导致游戏启动较慢，所以先定义为null，等要用的时候进行加载
    private Image overImg = null;
    //解决闪烁问题-双缓冲步骤一：定义一张与背景大小一致的图片
    private BufferedImage bufImg = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    //游戏状态
    private static int gameState;
    //菜单指向
    private static int menuIndex;
    //标题栏的高度
    public static int titleBarH;
    //定义我方坦克对象
    private static Tank myTank;
    //定义敌人坦克
    public static List<Tank> enemies = new ArrayList<Tank>();
    //定义本关卡产生了多少敌人
    private static int bornEnemyCount;
    //记录本关消灭了多少敌人
    public static int killEnemyCount;
    //定义地图绘制的内容
    private static GameMap gameMap = new GameMap();


    //对窗口进行初始化
    public GameFrame() {
        //窗口属性初始化
        initFrame();
        //窗口事件属性初始化
        initEventListener();
        //对游戏进行初始化
        initGame();
        //启动repaint刷新线程
        new Thread(this).start();
    }

    /**
     * 进入下一关的方法
     */
    public static void nextLevel() {
        startGame(LevelInfo.getInstance().getLevel() + 1);

    }

    /**
     * 过关动画
     */
    public static int flashTime;
    public static final int RECT_WIDTH = 40;
    public static final int RECT_COUNT = FRAME_WIDTH / RECT_WIDTH + 1;
    public static boolean isOpen = false;

    public static void startCrossLevel() {
        gameState = STATE_CROSS;
        flashTime = 0;
        isOpen = false;
    }

    //绘制过关动画
    public void drawCross(Graphics g) {
        gameMap.drawBk(g);
        myTank.draw(g);
        gameMap.drawCover(g);

        g.setColor(Color.BLACK);
        //关闭百叶窗的效果
        if (!isOpen) {
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, FRAME_HEIGHT);
            }
            //所有的叶片都关闭了
            if (flashTime++ - RECT_WIDTH > 5) {
                isOpen = true;
                //初始化下一个地图
                gameMap.initMap(LevelInfo.getInstance().getLevel() + 1);
            }
        } else {
            //开百叶窗的效果
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, FRAME_HEIGHT);
            }
            if (flashTime-- == 0) {
                startGame(LevelInfo.getInstance().getLevel());
            }
        }
    }

    //对游戏进行初始化
    private void initGame() {
        gameState = STATE_MENU;
        menuIndex = STATE_MENU;
    }

    //窗口属性初始化
    private void initFrame() {
        //创建主窗口名字
        setTitle(GAME_TITLE);
        //设置窗口大小
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        //窗口居中显示
        setLocation(FRAME_X, FRAME_Y);

        //设置窗口大小不可变
        setResizable(false);
        //设置窗口可见
        setVisible(true);
        //重绘-会底层自动调用update方法-已用多线程，每隔1毫秒刷新一次repaint代替
        //repaint();
        //求标题栏的高度
        titleBarH = getInsets().top;
    }


    /**
     * 是Frame类的方法﹐继承下来的方法﹐
     * 该方法负责了所有的绘制的内容﹐所有需要在屏幕中显示的
     * 内容﹐都要在该方法内调用·该方法不能主动调用。必须通过调用repaint();去回调该方法
     * I
     *
     * @param g1
     */
    @Override
    public void update(Graphics g1) {
        //解决闪烁问题-双缓冲步骤二：得到图片画笔,并将原先的内容绘制到图片
        Graphics g = bufImg.getGraphics();
        g.setFont(GAME_FONT);
        switch (gameState) {
            case STATE_MENU:
                drawMenu(g);
                break;
            case STATE_HELP:
                drawHelp(g);
                break;
            case STATE_ABOUT:
                drawAbout(g);
                break;
            case STATE_RUN:
                drawRun(g);
                break;
            case STATE_LOST:
                drawLost(g, "失败!!");
                break;
            case STATE_WIN:
                drawWin(g);
                break;
            case STATE_CROSS:
                drawCross(g);
                break;
        }

        //解决闪烁问题-双缓冲步骤三：再将图片绘制到系统
        g1.drawImage(bufImg, 0, 0, null);

    }

    /**
     * 绘制游戏胜利的界面
     *
     * @param g
     */
    private void drawWin(Graphics g) {
        drawLost(g, "通关!!");
        //通关文字
        g.setColor(Color.WHITE);
        g.setFont(BIG_FONT);
        g.drawString("通关!!", FRAME_WIDTH / 2 - 65, 100);

    }

    /**
     * 绘制游戏结束的方法
     *
     * @param g
     */
    private void drawLost(Graphics g, String str) {
        //自己加的
        g.setColor(Color.black);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        if (overImg == null) {
            overImg = MyUtil.createImage("res/over.jpg");
        }
        //得到图片的宽和高
        int ImgW = overImg.getWidth(null);
        int ImgH = overImg.getHeight(null);
        //得到图片居中的坐标
        int ImgX = FRAME_WIDTH - ImgW >> 1;
        int ImgY = FRAME_HEIGHT - ImgH >> 1;
        g.drawImage(overImg, ImgX, ImgY, null);

        ////游戏结束选项定义
        g.setColor(Color.WHITE);
        g.drawString(OVER_STR0, FRAME_X - 350, FRAME_Y + 440);
        g.drawString(OVER_STR1, FRAME_X + 180, FRAME_Y + 440);

        //通关文字
        g.setColor(Color.WHITE);
        g.setFont(BIG_FONT);
        g.drawString(str, FRAME_WIDTH / 2 - 65, 100);


    }

    //游戏运行状态的绘制内容
    private void drawRun(Graphics g) {
        //绘制窗口黑色背景
        g.setColor(Color.black);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        //添加绘制不可遮挡砖块
        gameMap.drawBk(g);
        //g.drawImage(Toolkit.getDefaultToolkit().createImage("res/bk.jpg"),0,0,null);
        //绘制敌人坦克
        drawEnemies(g);
        //绘制我方坦克
        myTank.draw(g);
        //添加绘制可遮挡砖块
        gameMap.drawCover(g);
        //子弹和坦克的碰撞的方法的调用
        bulletCollideTank();
        //爆炸效果
        drawExplodes(g);
        //子弹与所有地图快的碰撞
        bulletCollideMapTile();
        tankCollideMapTile();

    }

    //绘制地图
//    private void drawMap(Graphics g) {
//        //g.setColor(Color.pink);
//        //g.fillRect(GameMap.MAP_X, GameMap.MAP_Y, GameMap.MAP_WIDTH, GameMap.MAP_HEIGHT);
//        gameMap.drawBk(g);
//        gameMap.drawCover(g);
//
//    }

    //绘制所有的敌人的坦克，如果坦克死亡就从容器中移除
    private void drawEnemies(Graphics g) {
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if (enemy.isDie()) {
                Tank remove = enemies.remove(i);
                i--;
                continue;
            }
            enemy.draw(g);
        }
        System.out.println("坦克数量" + enemies.size());


    }
    private Image helpImg;
    private Image aboutImg;
    private void drawAbout(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(aboutImg == null){
            aboutImg = MyUtil.createImage("res/about.png");
        }
        int width = aboutImg.getWidth(null);
        int height = aboutImg.getHeight(null);

        int x = FRAME_WIDTH - width >>1;
        int y = FRAME_HEIGHT - height >> 1;
        g.drawImage(aboutImg,x,y,null);

        g.setColor(Color.white);
        g.drawString("任意键继续",10,FRAME_HEIGHT-10);
    }

    private void drawHelp(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(helpImg == null){
            helpImg = MyUtil.createImage("res/help.png");
        }
        int width = helpImg.getWidth(null);
        int height = helpImg.getHeight(null);

        int x = FRAME_WIDTH - width >>1;
        int y = FRAME_HEIGHT - height >> 1;
        g.drawImage(helpImg,x,y,null);

        g.setColor(Color.WHITE);
        g.drawString("任意键继续",10,FRAME_HEIGHT-10);
    }

    //绘制菜单状态下内容
    //@param g 画笔对象：系统提供的
    private void drawMenu(Graphics g) {
        //绘制窗口黑色背景
        g.setColor(Color.black);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        final int STR_WIDTH = 850;//假设字体长度为76
        int x = (SCREEN_W - STR_WIDTH) / 2;//窗口宽度-字体长度除以2为字体x坐标
        int y = FRAME_Y + 80;//窗口高度除以3为字体y坐标
        final int DIS = 50;//设置字体宽度为30
        g.setColor(Color.white);
        for (int i = 0; i < MENUS.length; i++) {
            if (i == menuIndex) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.white);
            }
            g.drawString(MENUS[i], x, y + 50 * i);
        }
    }


    //菜单监听事件
    private void initEventListener() {
        //窗口监听事件
        addWindowListener(new WindowAdapter() {
            @Override
            //点击关闭按钮，退出系统
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //按键监听事件
        addKeyListener(new KeyAdapter() {
            //点击按键事件-按下按键方法被调用
            @Override
            public void keyPressed(KeyEvent e) {
                //获得被按下的键的值
                int keyCode = e.getKeyCode();
                switch (gameState) {
                    case STATE_MENU:
                        keyPressedEventMenu(keyCode);
                        break;
                    case STATE_HELP:
                        keyPressedEventHelp(keyCode);
                        break;
                    case STATE_ABOUT:
                        keyPressedEventAbout(keyCode);
                        break;
                    case STATE_RUN:
                        keyPressedEventRun(keyCode);
                        break;
                    case STATE_LOST:
                        keyPressedEventLost(keyCode);
                        break;
                    case STATE_WIN:
                        keyPressedEventWin(keyCode);
                        break;

                }

            }

            //松开按键事件-松开按键方法被调用
            @Override
            public void keyReleased(KeyEvent e) {
                //获得被按下的键的值
                int keyCode = e.getKeyCode();
                if (gameState == STATE_RUN) {
                    keyReleasedEventRun(keyCode);
                }
            }

        });


    }


    private void keyPressedEventWin(int keyCode) {
        keyPressedEventLost(keyCode);

    }

    //释放点击按键事件对应方法
    private void keyReleasedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myTank.setState(Tank.STATE_STAND);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myTank.setState(Tank.STATE_STAND);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myTank.setState(Tank.STATE_STAND);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setState(Tank.STATE_STAND);
                break;

        }
    }

    /**
     *
     */

    //点击按键事件对应方法
    private void keyPressedEventLost(int keyCode) {
        switch (keyCode) {
            //如果按了ESC退出游戏
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            //如果按了Enter返回主菜单
            case KeyEvent.VK_ENTER:
                setGameState(STATE_MENU);
                //游戏结束，返回主菜单后，很多游戏的的操作需要关闭
                resetGame();
                break;
        }
    }

    //重置游戏状态
    private void resetGame() {
        killEnemyCount = 0;
        menuIndex = 0;
        //先让自己坦克的子弹回到对象池,让自己坦克的爆炸效果回到对象池
        myTank.bulletsReturn();
        //销毁我方坦克
        myTank = null;
        //销毁敌方坦克,把敌人的子弹都情况，并把敌人也清空，并把敌人的爆炸效果返还
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.bulletsReturn();
        }
        enemies.clear();
        //销毁墙块
        gameMap = null;
    }

    private void keyPressedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myTank.setDir(Tank.DIR_UP);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myTank.setDir(Tank.DIR_DOWN);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myTank.setDir(Tank.DIR_LEFT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setDir(Tank.DIR_RIGHT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            //按下空格键炮弹法发射
            case KeyEvent.VK_J:
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_NUMPAD1:
                myTank.fire();
                break;
        }
    }

    private void keyPressedEventAbout(int keyCode) {
       setGameState(STATE_MENU);
    }

    private void keyPressedEventHelp(int keyCode) {
        setGameState(STATE_MENU);
    }

    private void keyPressedEventMenu(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                menuIndex--;
                if (menuIndex < 0) {
                    menuIndex = MENUS.length - 1;
                }
                //repaint();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                menuIndex++;
                if (menuIndex > MENUS.length - 1) {
                    menuIndex = 0;
                }
                //repaint();
                break;
            //按下enter进入新游戏
            case KeyEvent.VK_ENTER:
                switch (menuIndex) {
                    case 0:
                        startGame(1);
                        break;
                    case 1:
                        //进入游戏
                        break;
                    case 2:
                        setGameState(STATE_HELP);
                        break;
                    case 3:
                        setGameState(STATE_ABOUT);
                        break;
                    case 4:
                        System.exit(0);
                        break;
                }

                break;

        }
    }

    //开始新游戏的方法,并加载level关卡信息
    private static void startGame(int level) {
        enemies.clear();
        if (gameMap == null) {
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        MusicUtil.playStart();
        killEnemyCount = 0;
        bornEnemyCount = 0;
        gameState = STATE_RUN;
        myTank = new MyTank(TankHouse.HOUSE_X - Tank.RADIUS * 2 + 10, TankHouse.HOUSE_Y + Tank.RADIUS * 2 + 10, Tank.DIR_UP);
        //用一个单独的线程生产敌人坦克
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (LevelInfo.getInstance().getEnemyCount() > bornEnemyCount && enemies.size() < ENEMY_MAX_COUNT) {
                        Tank enemy = EnemyTank.createEnemy();
                        enemies.add(enemy);
                        bornEnemyCount++;
                    }
                    try {
                        Thread.sleep(ENEMY_BORN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //如果游戏不在run状态了，我就结束产生敌人
                    if (gameState != STATE_RUN) {
                        break;
                    }
                }
            }
        }.start();
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //敌人的坦克的子弹与我方坦克的碰撞
    //我方的坦克的子弹与敌人坦克的碰撞
    private void bulletCollideTank() {
        //敌人的坦克的子弹与我方坦克的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.collideBulltes(myTank.getBullets());
        }

        //我方的坦克的子弹与敌人坦克的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            myTank.collideBulltes(enemy.getBullets());
        }
    }

    //自己的坦克的子弹和地图块的碰撞
    //敌人的坦克的子弹和地图块的碰撞
    private void bulletCollideMapTile() {
        //自己的坦克的子弹和地图块的碰撞
        myTank.bulletCollideMapTile(gameMap.getTiles());
        //敌人的坦克的子弹和地图块的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.bulletCollideMapTile(gameMap.getTiles());
        }
        //如果砖块不可见就从容器中删除-清理所有的被销毁的地图快
        gameMap.clearDestoryTile();

    }

    //自己的坦克和地图块的碰撞
    //敌人的坦克和地图块的碰撞
    private void tankCollideMapTile() {
        if (myTank.isCollideMapTile(gameMap.getTiles())) {
            myTank.back();
        }
        for (Tank enemy : enemies) {
            if (enemy.isCollideMapTile(gameMap.getTiles())) {
                enemy.back();
            }
        }
    }


    //绘制我方和敌人的爆炸效果
    private void drawExplodes(Graphics g) {
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.drawExplodes(g);
        }
        myTank.drawExplodes(g);
    }

    /**
     * 是否是最后一关
     */
    public static Boolean isLastLevel() {
        int currLevel = LevelInfo.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
        //条件一：当前关卡和总关卡一致
        return currLevel == levelCount;

    }

    /**
     * 判断本关是否通过
     *
     * @return
     */
    public static Boolean isCrossLevel() {
        //死掉的敌人总数是否等于产生敌人的总数
        return killEnemyCount == LevelInfo.getInstance().getEnemyCount();
    }

    public static int getGameState(int stateOver) {
        return gameState;
    }

    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }
}
