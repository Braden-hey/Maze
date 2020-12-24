package exp3;

import java.util.Random;

class CreateMap {
    private final int WALL = 0;
    private final int EMPTY = 1;
    private final int SELF = 2;
    private final int END = 3;
    private final int TESTFLAG = 4;
    private int width = 23;
    private int height = 23;
    private  int model;
    private final int DFSMethod = 1;
    private final int BFSMethod = 2;
    int[][] map;
    Random rand = new Random();
    MyQueue front;//记录队头
    MyQueue rear;//记录队尾
    int len = 0;//记录队列的长度
    //方向数组，便于后面加减,分别为上下左右
    int[][] dir = {
        {-1,0},
        {1,0},
        {0,-1},
        {0,1}
    };
    CreateMap(int width, int height, int model) {
        this.width = width;
        this.height = height;
        this.model = model;
        init(width, height, model);
    }

    void init(int width, int height, int model){
        map = new int[height][width];
        //初始化为墙和空白
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i % 2 != 0 && j % 2 != 0) {
                    map[i][j] = EMPTY;
                } else {
                    map[i][j] = WALL;
                }
            }
        }
        //初始化玩家位置
        int myX = 1;
        int myY = 1;
        map[myX][myY] = SELF;
        if (model == DFSMethod) {
            DFSCreate(myX, myY);
        } else {
            BFSCreate(myX, myY);
        }
        //设置此处为终点
        map[height-2][width-2] = END;
        //printMap(map);
    }

    private void DFSCreate(int x, int y){
        int i;
        while (isHaveNeighbor(x,y)) {
            i = rand.nextInt(4);
            if (inArea(x + 2 * dir[i][0], y + 2 * dir[i][1])
                    && map[x + 2 * dir[i][0]][y + 2 * dir[i][1]] == EMPTY) {
                map[x + dir[i][0]][y + dir[i][1]] = TESTFLAG;
                map[x + 2 * dir[i][0]][y + 2 * dir[i][1]] = TESTFLAG;
                DFSCreate(x + 2 * dir[i][0], y + 2 * dir[i][1]);
            }
        }
    }

    private void BFSCreate (int x, int y) {
        int i;
        MyQueue p;
        while (isHaveNeighbor(x,y)) {
            i = rand.nextInt(4);
            if (inArea(x + 2 * dir[i][0],y + 2 * dir[i][1])
                    && (map[x + 2 * dir[i][0]][y + 2 * dir[i][1]] == EMPTY)) {
                map[x + 2 * dir[i][0]][y + 2 * dir[i][1]] = TESTFLAG;
                map[x + dir[i][0]][y + dir[i][1]] = TESTFLAG;
                if (rand.nextInt(2) == 1) {
                    insertFront(new MyQueue(x + 2 * dir[i][0],y + 2 * dir[i][1]));
                } else {
                    insertRear(new MyQueue(x + 2 * dir[i][0],y + 2 * dir[i][1]));
                }
            }
        }
        if (len == 0) {
            return;
        }
        if (rand.nextInt(2) == 1) {
            p = popFront();
        } else {
            p = popRear();
        }
        BFSCreate(p.getX(),p.getY());
    }

    private void printMap(int[][] map) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j] == TESTFLAG) {
                    System.out.print(" " + " ");
                } else {
                    System.out.print(map[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    //是否在地图内
    private boolean inArea(int x,int y)
    {
        if(x > 0 && x < height-1 && y > 0 && y < width-1)
            return true;
        return false;
    }
    //判断结点周围是否有邻居
    private boolean isHaveNeighbor(int x, int y) {
        for (int i = 0; i < 4; i++) {
            if (inArea(x + 2 * dir[i][0],y + 2 * dir[i][1])
                    && (map[x + 2 * dir[i][0]][y + 2 * dir[i][1]] == EMPTY)) {
                return true;
            }
        }
        return false;
    }
    private void insertFront (MyQueue queue) {
        MyQueue p;
        if (len == 0) {
            front = queue;
            rear = queue;
        } else {
            p = front;
            front = queue;
            front.next = p;
        }
        len ++;
    }

    private void insertRear (MyQueue queue) {
        MyQueue p;
        if (len == 0) {
            front = queue;
            rear = queue;
        } else {
            p = rear;
            rear = queue;
            p.next = rear;
        }
        len ++;
    }
    private MyQueue popFront () {
        MyQueue p;
        p = front;
        front = front.next;
        len --;
        return p;
    }

    private MyQueue popRear () {
        MyQueue p = front;
        if (len == 1) {
            len --;
            return rear;
        }
        while (p.next != rear) {
            p = p.next;
        }
        rear = p;
        len --;
        return rear.next;
    }

    public int[][] getMap() {
        return map;
    }


}


