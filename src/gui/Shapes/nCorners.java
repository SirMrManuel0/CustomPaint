package gui.Shapes;

import gui.Shapes.Geometry.*;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class nCorners extends Shapus{
    private GeneralPath path;
    private cPoint Center;

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
        Center = new cPoint(x+(diameter/2), y+(diameter/2));
        calcPath(diameter, corners);
        this.fill = fill;
        val = new double[]{
                x,
                y,
                diameter,
                c
        };

    }

    public nCorners(cPoint Center, double diameter, double c, Color col, int fill){
        val = new double[]{
                0,
                0,
                diameter,
                c
        };
        C = col;
        kind = Shapus.N_CORNER;
        int corners = (int) c;
        this.Center = Center;
        calcPath(diameter, corners);
        this.fill = fill;

    }


    private void calcPath(double diameter, int corners){
        corners = corners % 360;
        Vector2D Normal = new Vector2D(0,  -diameter/2);
        path = new GeneralPath();
        cPoint Start = Normal.movePoint(Center);
        double Theta = (double) 360 / corners;
        if (corners % 2 == 0) Start = Normal.rotate(Theta/2).movePoint(Center);
        Vector2D StartVec = Center.vector(Start);
        path.moveTo(Start.getX(), Start.getY());
        for (int i = 1; i <= corners; i++){
            cPoint P = StartVec.rotate(Theta*i).movePoint(Center);
            path.lineTo(P.getX(), P.getY());
        }
        path.closePath();
        this.S = path;        
        val = new double[]{
                val[0],
                val[1],
                diameter,
                corners
        };
    }
    
    @Override
    public cPoint getCenter() {
    	return Center;
    }
    
    @Override
    public void setWidth(double width){
    	if(width == val[2]) return;
        calcPath(width, (int) val[3]);
    }

    @Override
    public void setCorner(int corners){
        calcPath(val[2], corners);
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
