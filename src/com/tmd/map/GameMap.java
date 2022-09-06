package com.tmd.map;

import com.tmd.game.GameFrame;
import com.tmd.game.LevelInfo;
import com.tmd.tank.Tank;
import com.tmd.util.Constant;
import com.tmd.util.MapTilePool;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 游戏地图类
 */
public class GameMap {
    //地图x坐标
    public static final int MAP_X = Tank.RADIUS * 3;
    //地图y坐标
    public static final int MAP_Y = Tank.RADIUS * 3 + GameFrame.titleBarH;
    //地图宽
    public static final int MAP_WIDTH = Constant.FRAME_WIDTH - Tank.RADIUS * 6;
    //地图高
    public static final int MAP_HEIGHT = Constant.FRAME_HEIGHT - Tank.RADIUS * 8 - GameFrame.titleBarH;
    //创建大本营对象
    private TankHouse house;

    //创建砖块类对象容器集合
    private List<MapTile> tiles = new ArrayList<MapTile>();

    public GameMap() {

    }

    /**
     * 关卡
     *
     * @param level 第几关
     */
    public void initMap(int level) {
        tiles.clear();
        //关卡解析导入关卡
        try {
            loadLevel(level);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建大本营
        house = new TankHouse();
        addHouse();
    }

    /**
     * 加载关卡信息
     *
     * @param level
     */
    private void loadLevel(int level) throws IOException {
        //获得关卡信息类唯一实例对象
        LevelInfo levelInof = LevelInfo.getInstance();
        //设置关卡
        levelInof.setLevel(level);

        //创建Properties集合，load方法将FileInputStream读取的文件键值对加入到集合中
        Properties prop = new Properties();
        prop.load(new FileInputStream("level/lv_" + level));

        //从文本中提取键对应的值，因为prop.getProperty("enemyCount")是一个String，所以要转成int
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        //设置敌人数量
        levelInof.setEnemyCount(enemyCount);

        //读取敌人类型,获得一个int数组
        String[] enemyType = prop.getProperty("enemyType").split(",");
        int[] type = new int[enemyType.length];
        for (int i = 0; i < enemyType.length; i++) {
            type[i] = Integer.parseInt(enemyType[i]);
        }
        //设置敌人类型
        levelInof.setEnemyType(type);
        //设置关卡难度
        levelInof.setLevelType(Integer.parseInt(prop.getProperty("levelType") == null ? "1" : prop.getProperty("levelType")));

        //提取用什么方法创建地图，用行方法还是列方法
        String methodName = prop.getProperty("method");
        //上步该方法调用几次
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));
        //将地图样式从文本中读取到String数组
        String[] params = new String[invokeCount];
        for (int i = 1; i <= invokeCount; i++) {
            params[i - 1] = prop.getProperty("param" + i);
        }

        //使用读取到的参数，调用对应方法
        invokeMethod(methodName, params);


    }

    private void invokeMethod(String methodName, String[] params) {
        //遍历文本读取的砖块排列方式params，并遍历
        //for (String param : params) {
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            //使用字符串split方法用分隔符逗号分开
            String[] split = param.split(",");
            //把遍历出来的字符串，转成int类型并放入数组保存
            int[] arr = new int[split.length];
            int a;
            for (a = 0; a < split.length - 1; a++) {
                arr[a] = Integer.parseInt(split[a]);
            }
            int dis = (int) (Double.parseDouble(split[a]) * MapTile.getTileW());
            switch (methodName) {
                case "addRow":
                    addRow(MAP_X + MapTile.getTileW() * arr[0],
                            (MAP_Y + 20) + (MapTile.getTileH() + 5) * arr[1],
                            MAP_X + MAP_WIDTH - MapTile.getTileW() * arr[2], arr[3],
                            dis);
                    break;
                case "addCol":
                    addCol(MAP_X + (MapTile.getTileW() + 5) * arr[0],
                            (MAP_Y + 20) + MapTile.getTileH() * arr[1],
                            MAP_Y + MAP_HEIGHT - MapTile.getTileH() * arr[2], arr[3],
                            dis);
                    break;
                case "addRect":
                    addRect(MAP_X + MapTile.getTileW() * arr[0],
                            (MAP_Y + 20) + MapTile.getTileH() * arr[1],
                            MAP_X + MAP_WIDTH - MapTile.getTileW() * arr[2],
                            MAP_Y + MAP_HEIGHT - MapTile.getTileH() * arr[3], arr[4],
                            dis);
                    break;
            }
        }

    }

    //把老巢砖块集合添加入地图砖块集合
    private void addHouse() {
        tiles.addAll(house.getTiles());
    }

    private boolean isCollide(List<MapTile> tiles, int x, int y) {
        for (MapTile tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            if (Math.abs(tileX - x) < MapTile.getTileW() && Math.abs(tileY - y) < MapTile.getTileH()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 只绘制没有遮挡效果的砖块
     *
     * @param g
     */
    public void drawBk(Graphics g) {
        for (MapTile tile : tiles) {
            if (tile.getType() != MapTile.TYPE_COVER) {
                tile.draw(g);
            }
        }
        //house.draw(g);
    }

    /**
     * 只绘制没有遮挡效果的砖块
     *
     * @param g
     */
    public void drawCover(Graphics g) {
        for (MapTile tile : tiles) {
            if (tile.getType() == MapTile.TYPE_COVER) {
                tile.draw(g);
            }
        }
        //house.draw(g);
    }

    /**
     * 降所有不可见的地图快从集合消除
     *
     * @return
     */
    public void clearDestoryTile() {
        for (int i = 0; i < tiles.size(); i++) {
            if (!tiles.get(i).isVisible()) {
                tiles.remove(i);
            }
        }
    }

    /**
     * 往地图块容器中添加一行指定类型的砖块
     *
     * @param startX 砖块起始X轴坐标
     * @param startY 砖块起始Y轴坐标
     * @param endX   砖块结束X轴坐标
     * @param type   砖块的类型
     * @param DIS    砖块的间隔:砖块中心点的间隔，如果中心点之间的间隔是块的宽度，那就说明没有间隔，否则就说明存在间隔
     */
    public void addRow(int startX, int startY, int endX, int type, final int DIS) {
        //获取一行砖块的数量
        int count = (endX - startX) / (MapTile.getTileW() + DIS);
        //遍历砖块的数量，从对象池获得count数量的砖块，并赋值坐标,并设置砖块类型
        for (int i = 0; i < count; i++) {
            MapTile mapTile = MapTilePool.get();
            mapTile.setX(startX + i * (MapTile.getTileW() + DIS));
            mapTile.setY(startY);
            mapTile.setType(type);
            tiles.add(mapTile);
        }


    }

    /**
     * 往地图块容器中添加一列指定类型的砖块
     *
     * @param startX 砖块起始X轴坐标
     * @param startY 砖块起始Y轴坐标
     * @param endY   砖块结束Y轴坐标
     * @param type   砖块的类型
     * @param DIS    砖块的间隔:砖块中心点的间隔，如果中心点之间的间隔是块的宽度，那就说明没有间隔，否则就说明存在间隔
     */
    public void addCol(int startX, int startY, int endY, int type, final int DIS) {
        //获取一列砖块的数量
        int count = (endY - startY) / (MapTile.getTileH() + DIS);
        //遍历砖块的数量，从对象池获得count数量的砖块，并赋值坐标,并设置砖块类型
        for (int i = 0; i < count; i++) {
            MapTile mapTile = MapTilePool.get();
            mapTile.setX(startX);
            mapTile.setY(startY + i * (MapTile.getTileH() + DIS));
            mapTile.setType(type);
            tiles.add(mapTile);
        }


    }

    /**
     * 往地图块容器中添加n行n列指定类型的砖块
     *
     * @param startX 砖块起始X轴坐标
     * @param startY 砖块起始Y轴坐标
     * @param endX   砖块结束X轴坐标
     * @param endY   砖块结束Y轴坐标
     * @param type   砖块的类型
     * @param DIS    砖块的间隔:砖块中心点的间隔，如果中心点之间的间隔是块的宽度，那就说明没有间隔，否则就说明存在间隔
     */

    public void addRect(int startX, int startY, int endX, int endY, int type, final int DIS) {
        int rows = (endY - startY) / (MapTile.getTileH() + DIS);
        for (int i = 0; i < rows; i++) {
            addRow(startX, startY + i * (MapTile.getTileH() + DIS), endX, type, DIS);
        }
    }


    public List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }
}
