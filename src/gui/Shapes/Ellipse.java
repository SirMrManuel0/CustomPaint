package gui.Shapes;

import gui.PaintMain;
import gui.Shapes.Geometry.cPoint;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ellipse extends Shapus{
    public Ellipse(double x, double y, double width, double height, Color col, int fill){
        this.kind = Shapus.CIRCLE;
        setup(x, y, width, height, col, fill);
    }
    public Ellipse(double x, double y, double width, double height, Color col, int fill, int bStatus){
        this.kind = Shapus.CIRCLE_WITH_B_STATUS;
        this.bStatus = bStatus;
        setup(x, y, width, height, col, fill);
    }

    private void setup(double x, double y, double width, double height, Color col, int fill){
        val = new double[]{
                x,
                y,
                width,
                height
        };
        this.C = col;
        this.S = new Ellipse2D.Double(x, y, width, height);
        this.fill = fill;
        this.Corners = new cPoint[] {
                new cPoint(x+(width/2), y+(height/2)),
                new cPoint(x+width,y+(height/2)),
                new cPoint(x,y+(height/2))
        };
    }

    @Override
    public void setWidth(double width){
    	if (width == val[2]) return;
        double x = val[0] - (width - val[2]) / 2;
        setup(x, val[1], width, val[3], this.C, this.fill);
    }

    @Override
    public void setHeight(double height){
    	if (height == val[3]) return;
        double y = val[1] - (height - val[3]) / 2;
        setup(val[0], y, val[2], height, this.C, this.fill);
    }

    @Override
    public boolean contains(Point mouseP) {
        return contains(new cPoint(mouseP.x, mouseP.y - PaintMain.HEADER_HEIGHT));
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
        return answer <= 1;
    }
}
