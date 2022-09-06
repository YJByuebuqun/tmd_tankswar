package com.tmd.util;

import java.awt.*;

/**
 * 该类为所有的常量
 */
public class Constant {
    /**************************游戏窗口相关****************************/
    //主窗口名字
    public static final String GAME_TITLE = "严镓波的坦克大战";
    //主窗口宽高
    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 600;
    //获取电脑屏幕的宽高属性
    public static final int SCREEN_W = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_H = Toolkit.getDefaultToolkit().getScreenSize().height;
    //主窗口坐标,右移一位相当于除以2 -可以用setLocationRelativeTo(null);代替
    public static final int FRAME_X = SCREEN_W - FRAME_WIDTH >> 1;
    public static final int FRAME_Y = SCREEN_H - FRAME_HEIGHT >> 1;

    /**************************游戏菜单相关****************************/
    public static final int STATE_MENU = 0;
    public static final int STATE_HELP = 1;
    public static final int STATE_ABOUT = 2;
    public static final int STATE_RUN = 3;
    public static final int STATE_LOST = 4;
    public static final int STATE_WIN = 5;
    public static final int STATE_CROSS = 6;

    public static final String[] MENUS = {
            "开始游戏",
            "继续游戏",
            "游戏帮助",
            "游戏关于",
            "退出游戏"
    };

    public static final Font GAME_FONT = new Font("仿宋", Font.BOLD, 24);
    public static final Font SMALL_FONT = new Font("仿宋", Font.BOLD, 12);
    public static final Font BIG_FONT = new Font("华文彩云", Font.BOLD, 50);

    public static final int REPAINT_INTERVAL = 30;

    //敌人坦克的数量不能超过10量，太多打不过
    public static final int ENEMY_MAX_COUNT = 10;
    //敌人坦克产生的间隔时间
    public static final int ENEMY_BORN_INTERVAL = 3000;
    //敌人AI切换state状态的时间间隔
    public static final double ENEMY_AI_INTERVAL = 1000;
    //敌人AI切换dir状态的时间间隔
    public static final double ENEMY_DIR_AI_INTERVAL = 1000;
    //敌人发射子弹开火的百分率0.05
    public static final double ENEMY_FIRE_PERCENT = 0.03;

    //游戏结束选项定义
    public static final String OVER_STR0 = "按ESC键退出游戏";
    public static final String OVER_STR1 = "按ENTER键返回主菜单";


}
