package gui.Shapes;

import gui.PaintMain;
import gui.Shapes.Geometry.cPoint;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Shapus{
    public Rectangle(double x, double y, double width, double height, Color col, int fill){
        setup(x, y, width, height, col, fill);
    }

    private void setup(double x, double y, double width, double height, Color col, int fill){
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
    	if (width == val[2]) return;
        double x = val[0] - (width - val[2]) / 2;
        setup(x, val[1], width, val[3], this.C, this.fill);
    }

    @Override
    public void setHeight(double height) {
    	if (height == val[3]) return;
        double y = val[1] - (height - val[3]) / 2;
        setup(val[0], y, val[2], height, this.C, this.fill);
    }

    @Override
    public boolean contains(Point mouseP) {
        return contains(new cPoint(mouseP.x, mouseP.y - PaintMain.HEADER_HEIGHT));
    }

    @Override
    public boolean contains(cPoint point) {
        cPoint A = Corners[0];
        cPoint B = Corners[1];
        cPoint C = Corners[2];
        cPoint D = Corners[3];

        double x = point.getX();
        double y = point.getY();

        return !(y > D.getY())
                && !(x > C.getX())
                && !(x < A.getX())
                && !(y < B.getY());
    }
}
