package gui.Shapes.Geometry;

import java.awt.*;

public class cPoint {
    private double x;
    private double y;

    public cPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2D vector(cPoint T){
        // PT = T - P
        double _x = T.getX() - x;
        double _y = T.getY() - y;
        return new Vector2D(_x, _y);
    }

    public double[] toArray(){
        return new double[]{x,y};
    }
    public Point toPoint(){
        Point p = new Point();
        p.setLocation(x, y);
        return p;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public cPoint translate(Dimension size){
        return new cPoint(x, size.getHeight() - y);
    }
    @Override
    public String toString(){
        return "X: " + x + " Y: " + y;
    }
}
