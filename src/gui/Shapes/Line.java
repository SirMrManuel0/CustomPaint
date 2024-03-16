package gui.Shapes;

import java.awt.*;
import java.awt.geom.Line2D;

public class Line extends Shapus{
    public Line(double x1, double y1, double x2, double y2, Color col){
        C = col;
        S = new Line2D.Double(x1, y1, x2, y2);
    }
}
