package exp3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;


public class MazeFrame extends JFrame {

    private final int DFSMethod = 1;
    private final int BFSMethod = 2;
    private int widthCount = 23;
    private int heightCount = 23;
    private int model = 1;
    private JMenuBar jMenuBar;
    private JMenu choose;
    private JMenuItem start;
    private JMenuItem setDiff;
    private MazeFrame mazeFrame = this;
    Setting setting;

    void initMenu () {

        jMenuBar = new JMenuBar();
        choose = new JMenu("选项");
        start = new JMenuItem("开始");
        setDiff = new JMenuItem("设置难度");
        choose.add(start);
        choose.add(setDiff);
        jMenuBar.add(choose);
        this.setJMenuBar(jMenuBar);

        setDiff.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setting = new Setting(mazeFrame);
            }
        });
        start.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initPanel();
            }
        });
    }
    void initPanel () {
        JFrame newJFrame = new JFrame();
        int side = 30;
        int width;
        int height;
        //任务栏高度
        double task = 30;
        double a = Toolkit.getDefaultToolkit().getScreenSize().getWidth() - task;
        double b = Toolkit.getDefaultToolkit().getScreenSize().getHeight() - task;
        //添加画布
        if (setting == null) {
            //这里的6弥补一点宽度
            width = widthCount * side + 6;
            //这里创建的窗口高度需要加1,也需要留一点位置给按钮
            height = heightCount * side + side * 1;
            newJFrame.add(new MyPanel(widthCount, heightCount, DFSMethod, newJFrame));
            newJFrame.setBounds((int)(a/2) - width/2,(int)(b/2) - height/2,
                    width,
                    height);
        } else {
            width = setting.getWidth() * side + 6;
            height = setting.getHeight() * side + side * 1;
            newJFrame.add(new MyPanel(setting.getWidth(), setting.getHeight(), setting.getModel(), newJFrame));
            newJFrame.setBounds((int)(a/2) - width/2,(int)(b/2) - height/2,
                    width,
                    height);
        }
        this.setVisible(false);
        newJFrame.setVisible(true);
        newJFrame.setResizable(false);
        newJFrame.setTitle("小蝌蚪找妈妈迷宫");
        ImageIcon img = new ImageIcon("src\\images\\mazeIcon.jpg");
        newJFrame.setIconImage(img.getImage());
        newJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void initBG () {
        ImageIcon img = new ImageIcon("src\\images\\bg.jpg");
        JLabel imgLabel = new JLabel(img);
        this.add(imgLabel);
    }

    protected void init() {
        //设置菜单
        initMenu();
        //设置初始大小
        this.setBounds(700,200,512,512);
        this.setResizable(false);
        //初始化背景
        initBG();
        //点击关闭能真正关闭
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置标题和icon
        this.setTitle("小蝌蚪找妈妈迷宫");
        ImageIcon img = new ImageIcon("src\\images\\mazeIcon.jpg");
        this.setIconImage(img.getImage());
        this.setVisible(true);
    }

    public static void main(String[] args) {
        MazeFrame frame = new MazeFrame();
        frame.init();
    }

}
