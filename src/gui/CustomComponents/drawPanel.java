package gui.CustomComponents;

import Sulfur.Entry;
import gui.Shapes.*;
import gui.Shapes.Rectangle;

import javax.swing.*;
import java.awt.*;
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

    public void drawNCorners(double x, double y, double width, double height, int corners, int fill, Color color){
        if (corners < 2) corners = 2;
        if (fill != Entry.FILL_TRUE && fill != Entry.FILL_FALSE) fill = Entry.FILL_FALSE;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (width <= 0) width = 5;
        if (height <= 0) height = 5;
        Memory.add(new nCorners(x, y, width, height, color, corners, fill));
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
