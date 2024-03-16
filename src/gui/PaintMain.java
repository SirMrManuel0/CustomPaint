package gui;

import gui.CustomComponents.*;

public class PaintMain extends CustomFrame {

    private drawPanel dPanel;

    public PaintMain(){
        super(CustomFrame.PHI_HEIGHT, 1.7 , 2, "CustomPaint", true);
        initComponents();
        setVisible(true);
    }

    private void initComponents(){
        dPanel = new drawPanel();
        dPanel.setSize(getSize());
        add(dPanel);
    }
}
