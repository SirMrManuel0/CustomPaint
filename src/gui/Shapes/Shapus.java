package gui.Shapes;

import Sulfur.Entry;

import java.awt.*;

public class Shapus {
    public static int N_CORNER = 0;
    public static int CIRCLE = 1;
    public static int RECTANGLE = 2;
    public static int CIRCLE_WITH_B_STATUS = 3;

    protected Shape S;
    protected Color C;
    protected int fill;
    protected int bStatus = Entry.BELONGING_NONE;
    protected double[] val;
    protected int kind;

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
}
