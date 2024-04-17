package gui.Shapes;

import gui.Shapes.Geometry.*;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class nCorners extends Shapus{
    private GeneralPath path;

    public nCorners(double x, double y, double diameter, double c, Color col, int fill){
        val = new double[]{
                x,
                y,
                diameter,
                c
        };
        C = col;
        kind = Shapus.N_CORNER;
        int corners = (int) c;
        calcPath(x, y, diameter, corners);
        this.fill = fill;

    }

    private void calcPath(double x, double y, double diameter, int corners){
        corners = corners % 360;
        cPoint Center = new cPoint(x+(diameter/2), y+(diameter/2));
        Vector2D Normal = new Vector2D(0,  -diameter/2);
        path = new GeneralPath();
        cPoint Start = Normal.movePoint(Center);
        double Theta = (double) 360 / corners;
        if (corners % 2 == 0) Start = Normal.rotate(Theta/2).movePoint(Center);
        Vector2D StartVec = Start.vector(Center);
        path.moveTo(Start.getX(), Start.getY());
        for (int i = 1; i <= corners; i++){
            cPoint P = StartVec.rotate(Theta*i).movePoint(Center);
            path.lineTo(P.getX(), P.getY());
        }
        path.closePath();
        S = path;
        double c = (double) corners;
        val = new double[]{
                x,
                y,
                diameter,
                c
        };
    }

    @Override
    public void setWidth(double width){
        calcPath(val[0], val[1], width, (int) val[3]);
    }

    @Override
    public void setHeight(double height){
        int corner = (int) height;
        calcPath(val[0], val[1], val[2], corner);
    }

    @Override
    public boolean contains(Point mouseP) {
        return contains(new cPoint(mouseP.x, mouseP.y - 148));
    }

    @Override
    public boolean contains(cPoint point) {
        return path.contains(point.toPoint());
    }
}
