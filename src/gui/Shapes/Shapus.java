package gui.Shapes;

import Sulfur.Entry;
import gui.Shapes.Geometry.cPoint;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class Shapus {
    public static final int N_CORNER = 0;
    public static final int CIRCLE = 1;
    public static final int RECTANGLE = 2;
    public static final int CIRCLE_WITH_B_STATUS = 3;
    public static final int UNSYMMETRICAL_N_CORNER = 4;

    protected Shape S;
    protected Color C;
    protected int fill;
    protected int bStatus = Entry.BELONGING_NONE;
    protected double[] val;
    protected int kind;
    protected cPoint[] Corners;

    public void setWidth(double width){}
    public void setHeight(double height){}
    public void setCorner(int corners) {}
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
    public cPoint getCenter() { return new cPoint(0,0); };
    public Shapus increase(double diameter){ return null; }
    public ArrayList<cPoint> getPoints(){ return null; }
    public void scale(double scale){}
    public GeneralPath getPath(){ return null;}
    public void setOriginalPoints(ArrayList<cPoint> Points){}
    public void addEllipse(Ellipse el){}
    public ShapArea getArea(){ return null; }
}
