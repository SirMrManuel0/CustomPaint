package gui.Shapes;

import gui.PaintMain;
import gui.Shapes.Geometry.*;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class unsymmNCorner extends Shapus{
    private ArrayList<cPoint> Points;
    private ArrayList<cPoint> OrginalPoints;
    private GeneralPath Path;

    public unsymmNCorner(Color col, int fill){
        Points = new ArrayList<>();
        Path = new GeneralPath();
        this.C = col;
        this.fill = fill;
        this.kind = Shapus.UNSYMMETRICAL_N_CORNER;
    }

    public void addPoint(cPoint add) { Points.add(add); }
    public cPoint getPoint(int index) {
        if (Points.isEmpty()) return null;
        if (index >= Points.size()) return null;
        return Points.get(index);
    }
    public int amountPoints() { return Points.size(); }
    public void wrap(){
        if (Points.size() <= 2){
            throw new IllegalStateException("Add at least three cPoints before wrapping!");
        }
        Path.moveTo(Points.get(0).getX(), Points.get(0).getY());
        for (cPoint p : Points){
            Path.lineTo(p.getX(), p.getY());
        }
        Path.closePath();
        this.S = Path;
        OrginalPoints = Points;
        updateVal();
    }

    private void updateVal(){
        this.val = new double[Points.size()*2];
        int index = 0;
        for (int i = 0; i < Points.size(); i++){
            cPoint P = Points.get(i);
            val[index] = P.getX();
            index++;
            val[index] = P.getY();
            index++;
        }
    }

    @Override
    public void scale(double scale){
        unsymmNCorner Scaled = new unsymmNCorner(this.C, this.fill);

        for (int i = 0; i < OrginalPoints.size(); i++){
            Vector2D left;
            Vector2D right;
            cPoint currentPoint = OrginalPoints.get(i);
            if (i == 0){
                left = currentPoint.vector(OrginalPoints.get(OrginalPoints.size()-1));
                right = currentPoint.vector(OrginalPoints.get(i+1));
            } else if (i == OrginalPoints.size() - 1) {
                left = currentPoint.vector(OrginalPoints.get(i-1));
                right = currentPoint.vector(OrginalPoints.get(0));
            } else {
                left = currentPoint.vector(OrginalPoints.get(i-1));
                right = currentPoint.vector(OrginalPoints.get(i+1));
            }

            Vector2D resVector1;
            Vector2D resVector2;

            resVector1 = left.scale(1 / left.length()).add(right.scale(1 / right.length()));
            resVector2 = resVector1.rotate(180);

            double scalar1 = Math.sqrt(Math.pow(scale, 2) / (Math.pow(resVector1.getX(), 2 ) + Math.pow(resVector1.getY(), 2)));
            double scalar2 = Math.sqrt(Math.pow(scale, 2) / (Math.pow(resVector2.getX(), 2 ) + Math.pow(resVector2.getY(), 2)));

            resVector1 = resVector1.scale(scalar1);
            resVector2 = resVector2.scale(scalar2);

            if (scale < 0){
                Scaled.addPoint(resVector1.movePoint(currentPoint));
                continue;
            }
            if (scale > 0){
                Scaled.addPoint(resVector2.movePoint(currentPoint));
                continue;
            }
            Scaled.addPoint(currentPoint);

        }
        Scaled.wrap();
        this.Path = Scaled.getPath();
        this.S = Scaled.getShape();
        this.Points = Scaled.getPoints();
        updateVal();
    }

    @Override
    public Shapus increase(double diameter){
        unsymmNCorner Increased = new unsymmNCorner(this.C, this.fill);

        for (int i = 0; i < Points.size(); i++){
            Vector2D left;
            Vector2D right;
            cPoint currentPoint = Points.get(i);
            if (i == 0){
                left = currentPoint.vector(Points.get(Points.size()-1));
                right = currentPoint.vector(Points.get(i+1));
            } else if (i == Points.size() - 1) {
                left = currentPoint.vector(Points.get(i-1));
                right = currentPoint.vector(Points.get(0));
            } else {
                left = currentPoint.vector(Points.get(i-1));
                right = currentPoint.vector(Points.get(i+1));
            }

            Vector2D resVector1;
            Vector2D resVector2;

            resVector1 = left.scale(1 / left.length()).add(right.scale(1 / right.length()));
            resVector2 = resVector1.rotate(180);

            double scalar1 = Math.sqrt(Math.pow(diameter, 2) / (Math.pow(resVector1.getX(), 2 ) + Math.pow(resVector1.getY(), 2)));
            double scalar2 = Math.sqrt(Math.pow(diameter, 2) / (Math.pow(resVector2.getX(), 2 ) + Math.pow(resVector2.getY(), 2)));

            resVector1 = resVector1.scale(scalar1);
            resVector2 = resVector2.scale(scalar2);

            boolean resVector1Contain = Path.contains(resVector1.movePoint(currentPoint).toPoint());
            boolean resVector2Contain = Path.contains(resVector2.movePoint(currentPoint).toPoint());

            if (!resVector1Contain && !resVector2Contain){
                for (double j = scalar1; j > .1; j -= .1){
                    if (Path.contains(resVector1.scale(j).movePoint(currentPoint).toPoint())) {
                        resVector1Contain = true;
                        break;
                    }
                }
                for (double j = scalar2; j > .1; j -= .1){
                    if (Path.contains(resVector2.scale(j).movePoint(currentPoint).toPoint())) {
                        resVector2Contain = true;
                        break;
                    }
                }
            }

            if (resVector1Contain && resVector2Contain){
                continue;
            }

            if (resVector1Contain){
                Increased.addPoint(resVector2.movePoint(currentPoint));
                continue;
            }
            if (resVector2Contain){
                Increased.addPoint(resVector1.movePoint(currentPoint));
                continue;
            }
            Increased.addPoint(currentPoint);
        }
        Increased.wrap();
        return Increased;
    }

    @Override
    public boolean contains(Point mouseP) {
        return contains(new cPoint(mouseP.x, mouseP.y - PaintMain.HEADER_HEIGHT));
    }

    @Override
    public boolean contains(cPoint point) {
        return Path.contains(point.toPoint());
    }
    @Override
    public ArrayList<cPoint> getPoints(){ return Points; }

    @Override
    public GeneralPath getPath() {
        return Path;
    }

    @Override
    public void setOriginalPoints(ArrayList<cPoint> Points){
        this.OrginalPoints = Points;
    }
}
