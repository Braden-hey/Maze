package exp3;

public class MyQueue {
    int x;
    int y;
    MyQueue next;

    public MyQueue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public MyQueue getNext() {
        return next;
    }
}
