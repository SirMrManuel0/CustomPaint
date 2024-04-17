package gui.Shapes;

import gui.Shapes.Geometry.cPoint;

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
        this.Corners = new cPoint[] {
                new cPoint(x+(width/2), y+(height/2)),
                new cPoint(x+width,y+(height/2)),
                new cPoint(x,y+(height/2))
        };
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
        this.Corners = new cPoint[] {
                new cPoint(x+(width/2), y+(height/2)),
                new cPoint(x+width,y+(height/2)),
                new cPoint(x,y+(height/2))
        };
    }

    @Override
    public void setWidth(double width){
        double x = val[0];
        double y = val[1];
        double height = val[3];
        S = new Ellipse2D.Double(x, y, width, height);
        this.Corners = new cPoint[] {
                new cPoint(x+(width/2), y+(height/2)),
                new cPoint(x+width,y+(height/2)),
                new cPoint(x,y+(height/2))
        };
    }

    @Override
    public void setHeight(double height){
        double x = val[0];
        double y = val[1];
        double width = val[2];
        S = new Ellipse2D.Double(x, y, width, height);
        this.Corners = new cPoint[] {
                new cPoint(x+(width/2), y+(height/2)),
                new cPoint(x+width,y+(height/2)),
                new cPoint(x,y+(height/2))
        };
    }

    @Override
    public boolean contains(Point mouseP) {
        return contains(new cPoint(mouseP.x, mouseP.y - 148));
    }

    @Override
    public boolean contains(cPoint point){
        double x = point.getX();
        double y = point.getY();
        double semiMajor = val[2] / 2;
        double semiMinor = val[3] / 2;
        double h = Corners[0].getX();
        double k = Corners[0].getY();
        double answer = ((x - h) * (x - h))/(semiMajor * semiMajor)
                + ((y - k) * (y - k))/(semiMinor * semiMinor);
        if (answer <= 1) return true;
        return false;
    }
}
