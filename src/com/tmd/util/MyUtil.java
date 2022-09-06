package com.tmd.util;

import java.awt.*;

/**
 * 工具类
 */
public class MyUtil {
    private MyUtil() {
    }

    /**
     * @param min 区间最小随机数，包含
     * @param max 区间最大随机数，不包含
     * @return 随机数
     */

    public static final int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    /**
     * @return 得到随机的颜色
     */

    public static final Color getRandomColor() {
        int red = getRandomNumber(0, 256);
        int green = getRandomNumber(0, 256);
        int blue = getRandomNumber(0, 256);
        Color c = new Color(red, green, blue);
        return c;
    }

    /**
     * 判断一个点是否在一个正方形的内部
     *
     * @param rectX  正方形中心点的X轴坐标；
     * @param rectY  正方形中心点的Y轴坐标；
     * @param radius 正方形的边长的一半；
     * @param pointX 点的X轴坐标；
     * @param pointY 点的Y轴坐标；
     * @return 如果点在正方形内返回true，反之false；
     */

    public static final boolean isCollide(int rectX, int rectY, int radius, int pointX, int pointY) {
        boolean flag = false;
        //求正方形X轴坐标与点X轴坐标的距离
        int disX = Math.abs(rectX - pointX);
        //求正方形Y轴坐标与点Y轴坐标的距离
        int disY = Math.abs(rectY - pointY);
        //如果 正方形X轴坐标与点X轴坐标的距离 和 正方形Y轴坐标与点Y轴坐标的距离同时满足 小于radius,才是在正方形内
        if (disX-1 < radius && disY-1 < radius) {
            flag = true;
        }
        return flag;
    }

    /**
     * @param path 图片路径
     * @return
     */
    public static final Image createImage(String path) {
        return Toolkit.getDefaultToolkit().createImage(path);
    }

    //名词中选一个
    private static final String[] NAMES = {
            "行人", "乐园", "花草", "人才", "左手", "目的", "课文", "优点", "年代", "灰尘",
            "沙子", "小说", "儿女", "难题", "明星", "本子", "彩色", "水珠", "路灯", "把握",
            "房屋", "心愿", "左边", "新闻", "早点",
            "市场", "雨点", "细雨", "书房", "毛巾", "画家", "元旦", "绿豆", "本领", "起点",
            "青菜", "土豆", "总结", "礼貌", "右边",
            "老虎", "老鼠", "猴子", "树懒", " 斑马", "小狗", "狐狸", "狗熊", "黑熊",
            "大象", "豹子", " 麝牛", "狮子", "熊猫", "疣猪", "羚羊", "驯鹿", "考拉",
            "犀牛", "猞猁", "猩猩", "海牛", "水獭", "海豚", "海象", "刺猬", "袋鼠",
            "犰狳", "河马", "海豹", "海狮", "蝙蝠", "白虎", "狸猫", "水牛", "山羊",
            "绵羊", "牦牛", "猿猴", "松鼠", "野猪", "豪猪", "麋鹿", "花豹", "野狼",
            "灰狼", "蜂猴", "熊猴", "叶猴", "紫貂", "貂熊", "熊狸", "云豹", "雪豹",
            "黑麂", "野马", "鼷鹿", "坡鹿", "豚鹿", "野牛", "藏羚", "河狸", "驼鹿",
            "黄羊", "鬣羚", "斑羚", "岩羊", "盘羊", "雪兔"
    };
    //形容词中选一个
    private static final String[] MODIFIY = {
            "可爱", "傻傻", "萌萌", "羞羞", "笨笨", "呆呆", "美丽", "聪明", "伶俐", "狡猾",
            "胖乎乎", "粉嫩嫩", "白胖胖", "漂亮", "可爱", "聪明", "懂事", "乖巧", "淘气",
            "淘气", "顽劣", "调皮", "顽皮", "天真", "可爱", "无邪", "单纯", "纯洁", "无暇",
            "纯真", "稚气", "温润", "好奇"
    };

    /**
     * 得到一个随机的名字（形容词+名词）
     *
     * @return
     */
    public static final String getRandomName() {
        return MODIFIY[getRandomNumber(0, MODIFIY.length)] + "的" +
                NAMES[getRandomNumber(0, NAMES.length)];
    }
}
