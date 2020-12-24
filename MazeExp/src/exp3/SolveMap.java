package exp3;

import java.util.ArrayList;

public class SolveMap {
    private int[][] map;
    //x代表行长，y代表列长
    private int x;
    private int y;
    private int EndX;
    private int EndY;
    private int selfX = 1;
    private int selfY = 1;
    ArrayList<Message> openList = new ArrayList<>();
    ArrayList<Message> closedList = new ArrayList<>();
    //方向数组，便于后面加减,分别为上下左右
    int[][] dir = {
            {-1,0},
            {1,0},
            {0,-1},
            {0,1}
    };
    //地图种类定义
    private final int WALL = 0;
    private final int EMPTY = 1;
    private final int SELF = 2;
    private final int END = 3;
    private final int TESTFLAG = 4;
    private final int WALKED = 5;
    private final int ans = 6;
    //是否成功
    private boolean isSucceed = false;
    //生成需要的共享参数
    Message tempMes;//获取选取点
    //初始化与起点距离
    private int len = 0;

    SolveMap(int[][] map) {
        this.map = map;
        this.x = map.length;
        this.y = map[0].length;
        this.EndX = x - 2;
        this.EndY = y - 2;
        int tempG = len;
        int tempH = EndX + EndY - selfX - selfY;
        int tempF = tempG + tempH;
        Message tempPre = null;
        tempMes = new Message(tempF, tempG, tempH, selfX, selfY, tempPre);
        openList.add(tempMes);
        len ++;
        printMap();
        System.out.println();
        solveMethod();
        //设置初始结点，因为在回溯时将它变为ans了
        map[1][1] = SELF;
        printMap();

    }

    private void solveMethod() {

        while (!isSucceed) {
            checkAndStore();
            storeToOpenList(tempMes.getPosX(),tempMes.getPosY());
        }
        Message endMes = getEndMes();
        reback(endMes);
    }

    private void reback(Message endMes) {
        Message p = endMes;
        p = p.pre;
        while (p != null) {
            int posX = p.getPosX();
            int posY = p.getPosY();
            map[posX][posY] = ans;
            p = p.pre;
        }
    }

    private Message getEndMes() {
        for (int i = closedList.size() - 1; i >= 0; i--) {
            Message mes = closedList.get(i);
            if (mes.getPosX() == EndX && mes.getPosY() == EndY) {
                return mes;
            }
        }
        return null;
    }

    private void checkAndStore() {
        Message chooseMes = null;
        if (openList == null || openList.size() == 0) {
            return;
        }
        for (int i = 0; i < openList.size(); i++) {
            Message mes = openList.get(i);
            if (mes.getPosX() == EndX && mes.getPosY() == EndY) {
                chooseMes = mes;
                closedList.add(chooseMes);
                openList.remove(chooseMes);
                isSucceed = true;
                return;
            }
            //设置起始chooseMes
            if (chooseMes == null) {
                chooseMes = mes;
                continue;
            }
            //如果当前结点比chooseMes更优
            if (mes.getF() < chooseMes.getF()) {
                chooseMes = mes;
                continue;
            }
            if (mes.getF() == chooseMes.getF()) {
                if (mes.getH() < chooseMes.getH()) {
                    chooseMes = mes;
                }
            }
        }
        closedList.add(chooseMes);
        openList.remove(chooseMes);
        tempMes = chooseMes;
        len++;
    }

    private void storeToOpenList(int x, int y) {
        for (int i = 0; i < 4; i++) {
            int tempX = tempMes.getPosX();
            int tempY = tempMes.getPosY();
            int tempLen = len;
            if ((map[x + dir[i][0]][y + dir[i][1]] == EMPTY)
            || (map[x + dir[i][0]][y + dir[i][1]] == TESTFLAG)
            || (map[x + dir[i][0]][y + dir[i][1]] == END)) {
                tempX += dir[i][0];
                tempY += dir[i][1];
                if (!isHave(tempX, tempY)) {
                    //System.out.println("" + tempX + "   " + tempY);
                    int tempG = tempLen;
                    int tempH = EndX + EndY - tempX - tempY;
                    int tempF = tempG + tempH;
                    Message tempPre = tempMes;
                    Message newMes = new Message(tempF, tempG, tempH, tempX, tempY, tempPre);
                    openList.add(newMes);
                }
            }
        }
    }

    private boolean isHave(int x, int y) {
        //判断openList中是否存在添加的message
        if (openList != null) {
            for (int i = 0; i < openList.size(); i++) {
                Message message = openList.get(i);
                if (message.getPosX() == x && message.getPosY() == y) {
                    return true;
                }
            }
        }
        //判断closedList中是否存在添加的message
        if (closedList != null) {
            for (int i = 0; i < closedList.size(); i++) {
                Message message = closedList.get(i);
                if (message.getPosX() == x && message.getPosY() == y) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] getMap() {
        return map;
    }

    //test
    private void printMap() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

}

class Message {

    //路径增量
    int F;
    //开始点到当前方块的移动量
    int G;
    //当前方块到目标点的估算量（曼哈顿距离）
    int H;

    int posX;
    int posY;
    Message pre;

    public Message(int f, int g, int h, int posX, int posY, Message pre) {
        F = f;
        G = g;
        H = h;
        this.posX = posX;
        this.posY = posY;
        this.pre = pre;
    }

    public int getF() {
        return F;
    }

    public void setF(int f) {
        F = f;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Message getPre() {
        return pre;
    }

    public void setPre(Message pre) {
        this.pre = pre;
    }
}