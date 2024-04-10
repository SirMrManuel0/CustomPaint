package gui.Shapes;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Shapus{
    public Rectangle(double x, double y, double width, double height, Color col, int fill){
        val = new double[]{
                x,
                y,
                width,
                height
        };
        kind = Shapus.RECTANGLE;
        C = col;
        S = new Rectangle2D.Double(x, y, width, height);
        this.fill = fill;
    }
}
