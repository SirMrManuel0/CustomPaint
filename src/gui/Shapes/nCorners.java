package gui.Shapes;

import gui.Shapes.Geometry.*;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class nCorners extends Shapus{
    public nCorners(double x, double y, double width, double height, Color col, double c){
        int corners = (int) c;
        corners = corners % 360;
        C = col;
        cPoint Center = new cPoint(x+(width/2), y+(height/2));
        Vector2D Normal = new Vector2D(0,  -height/2);
        GeneralPath path = new GeneralPath();
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
        cPoint test = Normal.movePoint(Center);
        //S = new Ellipse(test.getX()-5, test.getY()-5, 10,10,Color.BLACK).getShape();
    }
}
