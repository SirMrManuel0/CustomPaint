package gui.Shapes;

import gui.Shapes.Geometry.cPoint;

import java.awt.geom.Area;

public class ShapArea extends Shapus{
    private Area area;
    public ShapArea(Shapus S){
        area = new Area(S.getShape());
        this.S = area;
        this.C = S.getColor();
        this.Corners = new cPoint[]{};
        this.fill = S.getFillStatus();
        this.val = new  double[]{};
        this.kind = -1;
    }
    public void add(Shapus shapus){
        area.add(new Area(shapus.getShape()));
        this.S = area;
    }
}
