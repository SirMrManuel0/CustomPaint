package gui.Shapes;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Shapus{
    public Rectangle(double x, double y, double width, double height, Color col){
        C = col;
        S = new Rectangle2D.Double(x, y, width, height);
    }
}
