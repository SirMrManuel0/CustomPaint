package gui.Shapes;

import Sulfur.Entry;

import java.awt.*;

public class Shapus {
    protected Shape S;
    protected Color C;
    protected int fill;
    protected int bStatus = Entry.BELONGING_NONE;

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
}
