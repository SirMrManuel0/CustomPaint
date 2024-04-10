package gui.Shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ellipse extends Shapus{
    public Ellipse(double x, double y, double width, double height, Color col, int fill){
        val = new double[]{
                x,
                y,
                width,
                height
        };
        kind = Shapus.CIRCLE;
        C = col;
        S = new Ellipse2D.Double(x, y, width, height);
        this.fill = fill;
    }
    public Ellipse(double x, double y, double width, double height, Color col, int fill, int bStatus){
        val = new double[]{
          x,
          y,
          width,
          height
        };
        kind = Shapus.CIRCLE_WITH_B_STATUS;
        C = col;
        S = new Ellipse2D.Double(x, y, width, height);
        this.fill = fill;
        this.bStatus = bStatus;
    }
}
