package gui.CustomComponents;

import Sulfur.Entry;
import Sulfur.SulfurManager;
import gui.Shapes.*;
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

    public drawPanel(){
        Memory = new ArrayList<>();
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
        Memory.add(new Ellipse(x, y, width, height, color, fill, bStatus));
        repaint();
    }

    public void undo(){
        if (Memory.size() == 0) return;
        Shapus S = Memory.remove(Memory.size()-1);
        while (S.getbStatus() != Entry.BELONGING_NONE) {
            if (Memory.size() == 0) break;
            S = Memory.remove(Memory.size()-1);
            if (S.getbStatus() == Entry.BELONGING_START) break;
        }
        repaint();
    }

    public void clear(){
        Memory = new ArrayList<>();
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

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        for (Shapus S : Memory){
            g2D.setColor(S.getColor());
            g2D.draw(S.getShape());
            if (S.getFill()) g2D.fill(S.getShape());
        }
    }
}
