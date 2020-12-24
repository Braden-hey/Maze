package exp3;

import java.awt.Container;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;

import exp3.MazeFrame;

import javax.swing.*;

class Setting implements ActionListener,KeyListener {

    /*int count = 23;*/
    private int widthCount = 23;
    private int heightCount = 23;
    private final int DFSMethod = 1;
    private final int BFSMethod = 2;
    int model = DFSMethod;
    static String[] comboBoxStrArr = new String[]{"DFS生成算法", "BFS生成算法"};

    JFrame frame = new JFrame("设置难度");
    JTabbedPane tabPane = new JTabbedPane();
    Container _ONE = new Container();
    JLabel labelWidth = new JLabel("迷宫宽度");
    JLabel labelHeight = new JLabel("迷宫高度");
    JLabel label2 = new JLabel("生成算法");
    JTextField fieldWidth = new JTextField();
    JTextField fieldHeight = new JTextField();
    final JComboBox<String> comboBox = new JComboBox<String>(comboBoxStrArr);
    JButton button = new JButton("确定");
    //传递mazeFrame
    MazeFrame mazeFrame;

    public Setting (MazeFrame mazeFrame) {
        this.mazeFrame = mazeFrame;
        int labelX = 10, labelY = 10, labelW = 70, fieldX = 80, fieldY = 10, fieldW = 240, height = 30, hSpace = 35;
        int frameW = 350, frameH = 230, btnW = 120;
        double a = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double b = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation(new Point((int) (a / 2) - 150, (int) (b / 2) - 150));// 设定窗口出现位置
        frame.setSize(frameW, frameH);// 设定窗口大小
        frame.setContentPane(tabPane);// 设置布局
        labelWidth.setBounds(labelX, labelY, labelW, height);
        labelHeight.setBounds(labelX, labelY = labelY + hSpace, labelW, height);
        label2.setBounds(labelX, labelY = labelY + hSpace, labelW, height);
        fieldWidth.setBounds(fieldX, fieldY, fieldW, height);
        fieldHeight.setBounds(fieldX, fieldY = fieldY + hSpace, fieldW, height);
        comboBox.setBounds(fieldX, fieldY = fieldY + hSpace, fieldW, height);
        button.setBounds((frameW-btnW)/2, fieldY = fieldY + hSpace, btnW, height);
        button.addActionListener(this);
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String value = comboBox.getSelectedItem().toString();
                    if ("DFS生成算法".equals(value)) {
                        model = DFSMethod;
                    } else {
                        model = BFSMethod;
                    }
                    System.out.println(value);
                }
            }
        });
        _ONE.add(labelWidth);
        _ONE.add(labelHeight);
        _ONE.add(label2);
        _ONE.add(fieldWidth);
        _ONE.add(fieldHeight);
        _ONE.add(comboBox);
        _ONE.add(button);
        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabPane.add("难度设置", _ONE);
        fieldWidth.setText("23");
        fieldHeight.setText("23");
    }

    //监听事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(button)) {
            startPanel();
            mazeFrame.initPanel();
        }
    }


    private void startPanel() {

        String widthValue = fieldWidth.getText();
        String HeightValue = fieldHeight.getText();
        try {
            widthCount = Integer.parseInt(widthValue);
            if (widthCount > 53) {
                JOptionPane.showMessageDialog(comboBox,"宽度过长，已经帮您修改为53","警告",JOptionPane.WARNING_MESSAGE);
                widthCount = 53;
                fieldWidth.setText("53");
            }
            if (widthCount < 7) {
                JOptionPane.showMessageDialog(comboBox,"宽度过短，已经帮您修改为7","警告",JOptionPane.WARNING_MESSAGE);
                widthCount = 7;
                fieldWidth.setText("7");
            }
            if (widthCount % 2 != 1) {
                JOptionPane.showMessageDialog(comboBox,"宽度需要是奇数哦，已经帮您修改数值为" + (widthCount+1),
                        "警告",JOptionPane.WARNING_MESSAGE);
                widthCount += 1;
                fieldWidth.setText(String.valueOf(widthCount));
            }
            System.out.println("宽度"  + widthCount);
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(comboBox,"宽度输入格式不正确！","警告",JOptionPane.WARNING_MESSAGE);
        }
        try {
            heightCount = Integer.parseInt(HeightValue);
            heightCount = (heightCount % 2 == 1) ? heightCount : heightCount + 1;
            if (heightCount > 31) {
                JOptionPane.showMessageDialog(comboBox,"长度过长，已经帮您修改为31","警告",JOptionPane.WARNING_MESSAGE);
                heightCount = 31;
                fieldHeight.setText("31");
            }
            if (heightCount < 7) {
                JOptionPane.showMessageDialog(comboBox,"长度过短，已经帮您修改为7","警告",JOptionPane.WARNING_MESSAGE);
                heightCount = 7;
                fieldHeight.setText("7");
            }
            if (heightCount % 2 != 1) {
                JOptionPane.showMessageDialog(comboBox,"长度需要是奇数哦，已经帮您修改数值为" + (heightCount+1),
                        "警告",JOptionPane.WARNING_MESSAGE);
                heightCount += 1;
                fieldHeight.setText(String.valueOf(heightCount));
            }
            System.out.println("高度" + heightCount);
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(comboBox,"长度输入格式不正确！","警告",JOptionPane.WARNING_MESSAGE);
        }

        //new SelectTableToGenerageDialog();触发下一个弹框
        frame.dispose();//关闭弹框
    }

    public int getWidth() {
        return widthCount;
    }

    public int getHeight() {
        return heightCount;
    }

    public int getModel() {
        return model;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

