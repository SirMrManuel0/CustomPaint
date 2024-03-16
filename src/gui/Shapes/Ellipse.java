package gui.Shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ellipse extends Shapus{
    public Ellipse(double x, double y, double width, double height, Color col){
        C = col;
        S = new Ellipse2D.Double(x, y, width, height);
    }
}
