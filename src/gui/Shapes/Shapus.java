package gui.Shapes;

import Sulfur.Entry;
import gui.Shapes.Geometry.cPoint;

import java.awt.*;

public class Shapus {
    public static final int N_CORNER = 0;
    public static final int CIRCLE = 1;
    public static final int RECTANGLE = 2;
    public static final int CIRCLE_WITH_B_STATUS = 3;

    protected Shape S;
    protected Color C;
    protected int fill;
    protected int bStatus = Entry.BELONGING_NONE;
    protected double[] val;
    protected int kind;
    protected cPoint[] Corners;

    public void setWidth(double width){}
    public void setHeight(double height){}
    public void setFill(boolean fill){
        if (fill) this.fill = Entry.FILL_TRUE;
        if (!fill) this.fill = Entry.FILL_FALSE;
    }
    public void setColor(Color c) {
        C = c;
    }
    public Color getColor() {
        return C;
    }
    public boolean getFill(){return fill == Entry.FILL_TRUE;}
    public Shape getShape() {
        return S;
    }
    public int getbStatus(){return bStatus;}
    public int getFillStatus(){return fill;}
    public double[] getVal(){return val;}
    public int getKind(){return kind;}
    public boolean contains(Point mouseP) {return false;}
    public boolean contains(cPoint point) {return false;}
}
