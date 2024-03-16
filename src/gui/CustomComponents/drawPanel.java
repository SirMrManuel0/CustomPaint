package gui.CustomComponents;

import gui.Shapes.*;
import gui.Shapes.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class drawPanel extends JPanel {
    private ArrayList<Shape> Memory;
    private Constructor[] Constr;

    public drawPanel(){
        Memory = new ArrayList<>();
        Constr = new Constructor[]{

        };
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        Shape S = new nCorners(200,100,300,300, Color.BLACK, 3).getShape();
        g2D.draw(S);
        g2D.fill(S);
        S = new Rectangle(200, 100, 300, 300, Color.BLACK).getShape();
        g2D.draw(S);
        S = new Ellipse(200, 100, 300, 300, Color.BLACK).getShape();
        g2D.draw(S);
    }
}
