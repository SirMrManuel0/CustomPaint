package gui.Shapes;

public class cPoint {
    private double x;
    private double y;

    public cPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double[] getArray(){
        return new double[]{x,y};
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
}
