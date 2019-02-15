package main.java.FEM.model;


public class Node {

    private double x;
    private double y;
    private double t;
    private boolean boarderCondition =false;

    public double getX() {
        return x;
    }
    void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    void setY(double y) {
        this.y = y;
    }
    public double getT() {
        return t;
    }
    public void setT(double t) {
        this.t = t;
    }
    public boolean isBoarderCondition() {
        return boarderCondition;
    }
    void setBoarderCondition() {
        this.boarderCondition = true;
    }
}
