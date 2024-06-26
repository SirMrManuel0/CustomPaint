package gui.CustomComponents;

import Sulfur.Entry;
import Sulfur.SulfurManager;
import gui.Shapes.*;
import gui.Shapes.Geometry.cPoint;
import gui.Shapes.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class drawPanel extends JPanel {
    private ArrayList<Shapus> Memory;
    private ArrayList<Shapus> hoverShapus;
    private ArrayList<Shapus> selectedShapus;
    private ArrayList<Shapus> UnsymmPoints;
    private ArrayList<ObsDPanel> Observer;
    private boolean isDrawingUnsymm;
    private unsymmNCorner Unsymm;

    public drawPanel(){
        Memory = new ArrayList<>();
        hoverShapus = new ArrayList<>();
        selectedShapus = new ArrayList<>();
        Observer = new ArrayList<>();
        UnsymmPoints = new ArrayList<>();
        isDrawingUnsymm = false;
    }

    public void drawRectangle(double x, double y, double width, double height, int fill, Color color){
        if (fill != Entry.FILL_TRUE && fill != Entry.FILL_FALSE) fill = Entry.FILL_FALSE;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (width <= 0) width = 5;
        if (height <= 0) height = 5;
        Memory.add(new Rectangle(x, y, width, height, color, fill));
        repaint();
    }

    public void drawNCorners(double x, double y, double diameter, int corners, int fill, Color color){
        if (corners < 2) corners = 2;
        if (fill != Entry.FILL_TRUE && fill != Entry.FILL_FALSE) fill = Entry.FILL_FALSE;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (diameter <= 0) diameter = 5;
        Memory.add(new nCorners(x, y, diameter, corners, color, fill));
        repaint();
    }

    public void drawCircle(double x, double y, double width, double height, int fill, Color color){
        if (fill != Entry.FILL_TRUE && fill != Entry.FILL_FALSE) fill = Entry.FILL_FALSE;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (width <= 0) width = 5;
        if (height <= 0) height = 5;
        Memory.add(new Ellipse(x, y, width, height, color, fill));
        repaint();
    }

    public void drawCircle(double x, double y, double width, double height, int fill, Color color, int bStatus){
        if (fill != Entry.FILL_TRUE && fill != Entry.FILL_FALSE) fill = Entry.FILL_FALSE;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (width <= 0) width = 5;
        if (height <= 0) height = 5;
        Ellipse E = new Ellipse(x, y, width, height, color, fill, bStatus);
        Memory.add(E);
        if (bStatus != Entry.BELONGING_START){
            for (int i = Memory.size() - 1; i >= 0; i--){
                if (Memory.get(i).getbStatus() == Entry.BELONGING_START){
                    Memory.get(i).addEllipse(E);
                    break;
                }
            }
        }
        repaint();
    }

    public void drawUnsymm(cPoint P, Color col, int fill){
        if (!isDrawingUnsymm) {
            Unsymm = new unsymmNCorner(col, fill);
            UnsymmPoints = new ArrayList<>();
            isDrawingUnsymm = true;
        }
        drawUnsymm(P);
    }

    public void drawUnsymm(cPoint P){
        cPoint Start = Unsymm.getPoint(0);
        if (Start != null && Start.vector(P).length() <= 10 && Unsymm.amountPoints() >= 3) {
            Unsymm.wrap();
            Memory.add(Unsymm);
            UnsymmPoints = new ArrayList<>();
            isDrawingUnsymm = false;
            repaint();
            return;
        }
        Color col = Unsymm.getColor();
        col = new Color(col.getRed(), col.getGreen(), col.getBlue(), 50);
        if (Unsymm.amountPoints() == 0)
            col = new Color(86, 56, 130);
        Shapus Rotating = new Ellipse(P.getX() - 10, P.getY() - 10, 20, 20, col, Entry.FILL_TRUE);
        UnsymmPoints.add(Rotating);
        Unsymm.addPoint(P);
        repaint();
    }

    public void undo(){
        if (Memory.isEmpty()) return;
        Shapus S = Memory.remove(Memory.size()-1);
        while (S.getbStatus() != Entry.BELONGING_NONE) {
            if (Memory.isEmpty()) break;
            S = Memory.remove(Memory.size()-1);
            if (S.getbStatus() == Entry.BELONGING_START) break;
        }
        for (ObsDPanel O : Observer) O.undo();
        repaint();
    }

    public void clear(){
        Memory = new ArrayList<>();
        hoverShapus = new ArrayList<>();
        selectedShapus = new ArrayList<>();
        repaint();
    }

    public void save(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Sulfur Files", "s");
        fileChooser.setFileFilter(filter);

        int choice = fileChooser.showSaveDialog(this);
        if (choice != JFileChooser.APPROVE_OPTION) return;

        File selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.getName().endsWith(".s")) {
            selectedFile = new File(selectedFile.getAbsolutePath() + ".s");
        }

        SulfurManager manager = new SulfurManager(selectedFile);
        manager.v1blankFile(getSize());

        for (Shapus S : Memory){
            manager.v1addData(S.getKind(), S.getVal(), S.getColor(), S.getFillStatus(), S.getbStatus());
        }
    }

    public void load(){
        Memory = new ArrayList<>();

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Sulfur Files", "s");
        fileChooser.setFileFilter(filter);

        int choice = fileChooser.showOpenDialog(this);
        if (choice != JFileChooser.APPROVE_OPTION) return;

        File selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.getName().endsWith(".s")) {
            selectedFile = new File(selectedFile.getAbsolutePath() + ".s");
        }

        SulfurManager manager = new SulfurManager(selectedFile);
        Entry[] data = manager.getData();
        for (Entry E : data){
            int kind = E.getKey();
            double[] val = E.getValues();
            if (kind == Shapus.N_CORNER) Memory.add(new nCorners(val[0], val[1], val[2], val[3], E.getColor(), E.getFill()));
            if (kind == Shapus.CIRCLE) Memory.add(new Ellipse(val[0], val[1], val[2], val[3], E.getColor(), E.getFill()));
            if (kind == Shapus.RECTANGLE) Memory.add(new Rectangle(val[0], val[1], val[2], val[3], E.getColor(), E.getFill()));
            if (kind == Shapus.CIRCLE_WITH_B_STATUS) Memory.add(new Ellipse(val[0], val[1], val[2], val[3], E.getColor(), E.getFill(), E.getBelongingStatus()));
            if (kind == Shapus.UNSYMMETRICAL_N_CORNER){
                unsymmNCorner U = new unsymmNCorner(E.getColor(), E.getFill());
                if (val.length % 2 != 0) throw new IllegalStateException("The File has been corrupted! Data is missing!");
                for (int i = 0; i < val.length; i+=2){
                    U.addPoint(new cPoint(val[i], val[i+1]));
                }
                U.wrap();
                Memory.add(U);
            }
        }
        repaint();
    }

    public void export(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png");
        fileChooser.setFileFilter(filter);

        int choice = fileChooser.showSaveDialog(this);
        if (choice != JFileChooser.APPROVE_OPTION) return;

        File selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.getName().endsWith(".png")) {
            selectedFile = new File(selectedFile.getAbsolutePath() + ".png");
        }


        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        for (Shapus S : Memory){
            g2d.setColor(S.getColor());
            g2d.draw(S.getShape());
            if (S.getFill()) g2d.fill(S.getShape());
        }

        try {
            ImageIO.write(image, "png", selectedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAt(Shapus shape, int index){ Memory.add(index, shape); }
    public boolean outOfRange(int index){ return index >= Memory.size(); }

    public ArrayList<Shapus> getMemory(){ return Memory; }
    public Shapus getMemory(int index) { return Memory.get(index); }
    public void updateMemory(ArrayList<Shapus> Memo) {
    	Memory = Memo; 
    	repaint();
    }
    public void drawHover(ArrayList<Shapus> s) { hoverShapus = s; repaint();}
    public void drawSelected(ArrayList<Shapus> s) { selectedShapus = s; repaint();}
    public void clearHover() { hoverShapus = new ArrayList<>(); repaint();}
    public void clearSelected() { selectedShapus = new ArrayList<>(); repaint();}
    public void addObs(ObsDPanel observ){ Observer.add(observ); }
    public void deleteMemory(int index) { Memory.remove(index); repaint(); }
    public boolean isDrawingUnsymm() { return isDrawingUnsymm; }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        for (Shapus S : Memory){
            g2D.setColor(S.getColor());
            g2D.draw(S.getShape());
            if (S.getFill()) g2D.fill(S.getShape());
        }
        for (Shapus S : hoverShapus){
            g2D.setColor(S.getColor());
            g2D.setStroke(new BasicStroke(5));
            g2D.draw(S.getShape());
        }

        for (Shapus S : selectedShapus){
            g2D.setColor(S.getColor());
            g2D.setStroke(new BasicStroke(5));
            g2D.draw(S.getShape());
        }

        for (Shapus S : UnsymmPoints){
            g2D.setColor(S.getColor());
            g2D.draw(S.getShape());
            if (S.getFill()) g2D.fill(S.getShape());
        }
    }
}
