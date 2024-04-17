package gui.Shapes;

import gui.Shapes.Geometry.cPoint;

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
        this.Corners = new cPoint[] {
                new cPoint(x,y),
                new cPoint(x+width,y),
                new cPoint(x+width,y+height),
                new cPoint(x,y+height)
        };
    }

    @Override
    public void setWidth(double width) {
        double x = val[0];
        double y = val[1];
        double height = val[3];
        S = new Rectangle2D.Double(x, y, width, height);
        this.Corners = new cPoint[] {
                new cPoint(x,y),
                new cPoint(x+width,y),
                new cPoint(x+width,y+height),
                new cPoint(x,y+height)
        };
    }

    @Override
    public void setHeight(double height) {
        double x = val[0];
        double y = val[1];
        double width = val[2];
        S = new Rectangle2D.Double(x, y, width, height);
        this.Corners = new cPoint[] {
                new cPoint(x,y),
                new cPoint(x+width,y),
                new cPoint(x+width,y+height),
                new cPoint(x,y+height)
        };
    }

    @Override
    public boolean contains(Point mouseP) {
        return contains(new cPoint(mouseP.x, mouseP.y - 148));
    }

    @Override
    public boolean contains(cPoint point) {
        cPoint A = Corners[0];
        cPoint B = Corners[1];
        cPoint C = Corners[2];
        cPoint D = Corners[3];

        double x = point.getX();
        double y = point.getY();

        if (y > D.getY()) return false;
        if (x > C.getX()) return false;
        if (x < A.getX()) return false;
        if (y < B.getY()) return false;

        return true;
    }
}
