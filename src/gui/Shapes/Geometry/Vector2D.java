package gui.Shapes.Geometry;

import java.awt.*;

public class Vector2D{
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double[] toArray(){
        return new double[]{x,y};
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public double getX() {
        return x;
    }
    public double length(){
        return Math.sqrt((x*x) + (y*y));
    }
    public double angle(Vector2D vec){
        return Math.acos(dot(vec)/(length() * vec.length()));
    }
    public double dot(Vector2D vec){
        return x * vec.getX() + y * vec.getY();
    }

    public Vector2D add(Vector2D vec){
        return new Vector2D(x + vec.getX(), y + vec.getY());
    }

    public Vector2D subtract(Vector2D vec){
        return new Vector2D(x - vec.getX(), y - vec.getY());
    }

    public Vector2D scale(double scalar){
        return new Vector2D(x * scalar, y * scalar);
    }

    public cPoint movePoint(cPoint P){
        // SE = E - S
        // SE + S = E
        return new cPoint(x + P.getX(), y + P.getY());
    }

    public Vector2D rotate(double Theta){
        Theta = Theta % 360;
        Theta = Math.toRadians(Theta);
        double _x = (x * Math.cos(Theta)) - (y * Math.sin(Theta));
        double _y = (x * Math.sin(Theta)) + (y * Math.cos(Theta));
        return new Vector2D(_x, _y);
    }

    @Override
    public String toString(){
        return "X: " + x + " Y: " + y + " Length: " + length();
    }
}
