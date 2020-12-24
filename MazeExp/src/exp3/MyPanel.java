package exp3;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;

public class MyPanel extends JPanel implements ActionListener, KeyListener {

    //地图宽度和高度
    private int width;
    private int height;
    private int model;
    private final int DFSMethod = 1;
    private final int BFSMethod = 2;
    //地图种类定义
    private final int WALL = 0;
    private final int EMPTY = 1;
    private final int SELF = 2;
    private final int END = 3;
    private final int TESTFLAG = 4;
    private final int WALKED = 5;
    private final int ANS = 6;
    //地图具体参数定义
    private int side = 30;
    private int[][] map;
    private int mapWidth;//地图的列数
    private int mapHeight;//地图的行数
    private String direction = "R";
    private boolean isSucceed = false;
    //为A*算法准备
    private int[][] mapForComputer;
    //图片定义
    private ImageIcon wall;
    private ImageIcon floor;
    private ImageIcon frog;
    private ImageIcon tadpoleRight;
    private ImageIcon tadpoleLeft;
    private ImageIcon tadpoleUp;
    private ImageIcon tadpoleDown;
    //与前一个框建立连接
    private JFrame Jf;
    //电脑自动开关按钮
    private boolean buttonOpen = false;
    private JButton ansButton;
    //判断是否开始
    private boolean go = false;
    //方向数组，便于后面加减,分别为上下左右
    int[][] dir = {
            {-1,0},
            {1,0},
            {0,-1},
            {0,1}
    };
    //音乐
    Clip bgm;

    class LookWay extends Thread {
        @Override
        public void run() {

            while (!isSucceed && buttonOpen) {
                //todo
                int[][] xy = getSELFxy();
                int selfX = xy[0][0];
                int selfY = xy[0][1];
                moveDirect(selfX, selfY);
                //System.out.println(selfX + " " + selfY);
                //printMap();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void moveDirect(int x, int y) {
            for (int i = 0; i < 4; i++) {
                if (map[x + dir[i][0]][y + dir[i][1]] == ANS
                        || map[x + dir[i][0]][y + dir[i][1]] == END) {
                    if (i == 0) {
                        goUp();
                        System.out.println(i);
                        return;
                    } else if (i == 1) {
                        goDown();
                        System.out.println(i);
                        return;
                    } else if (i == 2) {
                        goLeft();
                        System.out.println(i);
                        return;
                    } else if (i == 3) {

                        goRight();
                        System.out.println(i);
                        return;
                    }
                }
            }
        }
    }
    MyPanel(int widthCount, int heightCount, int model, JFrame newJFrame) {
        //初始化图片
        loadImages();
        //初始化音乐
        loadBGM();
        //初始化长和宽
        this.width = widthCount * side;
        this.height = heightCount * side;
        this.model = model;
        this.map = new CreateMap(widthCount, heightCount, model).getMap();
        this.mapForComputer = map;
        this.mapWidth = map[0].length;
        this.mapHeight = map.length;
        this.Jf = newJFrame;
        //添加键盘事件监听
        this.setFocusable(true);
        this.addKeyListener(this);
        //初始化答案
        initAns();
        //添加按钮
        initButton();
        this.add(ansButton);
        //开启音乐
        playBGM();
    }

    void initButton() {
        ansButton = new JButton("开启自动寻路");
        ansButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (go && !buttonOpen) {
                    JOptionPane.showMessageDialog(null,
                            "您已经开始，所以无法再帮您寻找通路了",
                            "警告",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                buttonOpen = !buttonOpen;
                if (!buttonOpen) {
                    ansButton.setText("开启自动寻路");
                } else {
                    ansButton.setText("关闭自动寻路");
                    go = true;
                }
                map = mapForComputer;
                Thread thread = new Thread(new LookWay());
                thread.start();
            }
        });
        ansButton.setVisible(true);
        ansButton.setFocusable(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (map[i][j] == WALL) {
                    wall.paintIcon(null,g,j * side,i * side);
                } else if (map[i][j] == SELF) {
                    if (direction == "R") {
                        tadpoleRight.paintIcon(null,g,j * side,i * side);
                    } else if (direction == "L") {
                        tadpoleLeft.paintIcon(null,g,j * side,i * side);
                    } else if (direction == "U") {
                        tadpoleUp.paintIcon(null,g,j * side,i * side);
                    } else {
                        tadpoleDown.paintIcon(null,g,j * side,i * side);
                    }
                } else if (map[i][j] == END) {
                    frog.paintIcon(null,g,j * side,i * side);
                } else if (map[i][j] == WALKED) {
                    floor.paintIcon(null, g, j * side, i * side);
                }
            }
        }
    }
    //暂时没啥用，将来可以用来补充功能
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("----actionPerformed---");
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();//获取键盘背后对应的数字
        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
            goRight();
        } else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            goLeft();
        } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
            goUp();
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
            goDown();
        }
    }

    private void goDown() {
        go = true;
        direction = "D";
        if (!isSucceed) {
            moveDown();
        }
        repaint();
    }

    private void goUp() {
        go = true;
        direction = "U";
        if (!isSucceed) {
            moveUp();
        }
        repaint();
    }

    private void goLeft() {
        go = true;
        direction = "L";
        if (!isSucceed) {
            moveLeft();
        }
        repaint();
    }

    private void goRight() {
        go = true;
        direction = "R";
        if (!isSucceed) {
            moveRight();
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void initAns() {
        mapForComputer = new int[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                mapForComputer[i][j] = map[i][j];
            }
        }
        SolveMap solveMap = new SolveMap(mapForComputer);
        mapForComputer = solveMap.getMap();
    }

    private void moveRight() {
        //这里的x和y，代表的是map中的坐标位置
        int[][] selfPos = getSELFxy();
        int selfX = selfPos[0][0];
        int selfY = selfPos[0][1];
        //System.out.println(inArea(selfX + 1,selfY));
        //判断是否到达了终点
        if (inArea(selfX,selfY+1) && map[selfX][selfY+1] == END) {
            map[selfX][selfY+1] = SELF;
            map[selfX][selfY] = EMPTY;
            isSucceed = true;
            congratulate();
            return;
        }
        if (inArea(selfX ,selfY + 1) &&
                (map[selfX][selfY+1] == EMPTY
                        || map[selfX][selfY+1] == TESTFLAG
                        || map[selfX][selfY+1] == WALKED
                        || map[selfX][selfY+1] == ANS)) {
            map[selfX][selfY+1] = SELF;
            map[selfX][selfY] = WALKED;
        }
        //printMap();
    }

    private void moveLeft() {
        int[][] selfPos = getSELFxy();
        int selfX = selfPos[0][0];
        int selfY = selfPos[0][1];
        //System.out.println(inArea(selfX - 1,selfY));
        //判断是否到达了终点
        if (inArea(selfX,selfY-1) && map[selfX][selfY-1] == END) {
            map[selfX][selfY-1] = SELF;
            map[selfX][selfY] = EMPTY;
            isSucceed = true;
            congratulate();
            return;
        }
        if (inArea(selfX,selfY-1) &&
                (map[selfX][selfY-1] == EMPTY
                        || map[selfX][selfY-1] == TESTFLAG
                        || map[selfX][selfY-1] == WALKED
                        || map[selfX][selfY-1] == ANS)) {
            map[selfX][selfY-1] = SELF;
            map[selfX][selfY] = WALKED;
        }
        //printMap();
    }
    private void moveUp() {
        int[][] selfPos = getSELFxy();
        int selfX = selfPos[0][0];
        int selfY = selfPos[0][1];
        //System.out.println(inArea(selfX,selfY - 1));
        //判断是否到达了终点
        if (inArea(selfX-1,selfY) && map[selfX-1][selfY] == END) {
            map[selfX-1][selfY] = SELF;
            map[selfX][selfY] = EMPTY;
            isSucceed = true;
            congratulate();
            return;
        }
        if (inArea(selfX-1,selfY) &&
                (map[selfX-1][selfY] == EMPTY
                        || map[selfX-1][selfY] == TESTFLAG
                        || map[selfX-1][selfY] == WALKED
                        || map[selfX-1][selfY] == ANS)) {
            map[selfX-1][selfY] = SELF;
            map[selfX][selfY] = WALKED;
        }
        //printMap();
    }
    private void moveDown() {
        int[][] selfPos = getSELFxy();
        int selfX = selfPos[0][0];
        int selfY = selfPos[0][1];
        //System.out.println(inArea(selfX+1,selfY));
        //判断是否到达了终点
        if (inArea(selfX+1,selfY) && map[selfX+1][selfY] == END) {
            map[selfX+1][selfY] = SELF;
            map[selfX][selfY] = EMPTY;
            isSucceed = true;
            congratulate();
            return;
        }
        if (inArea(selfX+1,selfY) &&
                (map[selfX+1][selfY] == EMPTY
                        || map[selfX+1][selfY] == TESTFLAG
                        || map[selfX+1][selfY] == WALKED
                        || map[selfX+1][selfY] == ANS)) {
            map[selfX+1][selfY] = SELF;
            map[selfX][selfY] = WALKED;
        }
        //printMap();
    }


    private void loadImages() {

        wall = new ImageIcon("src\\images\\wall.jpg");
        frog = new ImageIcon("src\\images\\frog.png");
        tadpoleRight = new ImageIcon("src\\images\\tadpoleRight.jpg");
        tadpoleLeft = new ImageIcon("src\\images\\tadpoleLeft.jpg");
        tadpoleUp = new ImageIcon("src\\images\\tadpoleUp.jpg");
        tadpoleDown = new ImageIcon("src\\images\\tadpoleDown.jpg");
        floor = new ImageIcon("src\\images\\floor.jpg");

    }

    private void congratulate() {

        stopBGM();
        int num = JOptionPane.showConfirmDialog(this,
                "恭喜您，让小蝌蚪找到了妈妈！您还需要再来一局吗？",
                "恭喜",
                JOptionPane.YES_NO_OPTION);
        if (num == JOptionPane.YES_OPTION) {
            MazeFrame frame = new MazeFrame();
            frame.init();
            Jf.setVisible(false);
        } else if (num == JOptionPane.NO_OPTION) {
            System.exit(0);
        }

    }

    private void loadBGM() {
        // 定义字节输入流变量
        InputStream is;
        // 该类为inputstream的直接子类，用于读取音频
        AudioInputStream ais;
        // 该类提供对一系列浮点值的控制，此处定义用来控制音频音量大小
        FloatControl gainControl;

        try {

            bgm = AudioSystem.getClip();
            // 通过类加载器找到字节流
            is = this.getClass().getClassLoader().
                    getResourceAsStream("music\\morning.wav");
            ais = AudioSystem.getAudioInputStream(is);// 转换成音频字节流
            bgm.open(ais);
            gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(1.0f);// 控制调整bgm的音量大小

        } catch (LineUnavailableException
                | IOException
                | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private void playBGM() {
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void stopBGM() {
        bgm.stop();
    }

    private int[][] getSELFxy() {
        int[][] temp = new int[1][2];
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (map[i][j] == SELF) {
                    temp[0][0] = i;
                    temp[0][1] = j;
                    return temp;
                }
            }
        }
        return temp;
    }

    //是否在地图内
    private boolean inArea(int x,int y)
    {
        if(x > 0 && x < mapHeight-1 && y > 0 && y < mapWidth-1)
            return true;
        return false;
    }
    //test
    private void printMap() {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
    //test
    private void getENDxy() {
        int[][] temp = new int[1][2];
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (map[i][j] == END) {
                    temp[0][0] = i;
                    temp[0][1] = j;
                    System.out.println("x = " + i + "y = " + j);
                }
            }
        }
    }
}

