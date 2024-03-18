package gui;

import Sulfur.Entry;
import gui.CustomComponents.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.*;

public class PaintMain extends CustomFrame {

    private JPanel header;
    private drawPanel dPanel;
    private JComboBox<String> DropDown;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField cornerField;
    private JCheckBox fillBox;
    private JColorChooser colorChooser;
    private String[] drops;
    private JLabel cornersLabel;
    private JButton helpButton;
    private JButton colorButton;
    private updaterPanel colorShow;
    private HelpFrame helpFrame;

    public PaintMain(){
        super(CustomFrame.PHI_HEIGHT, 1.7 , 2, "CustomPaint", false);
        setSize(new Dimension(1188, 734));
        addMouseListener(new BrushMouseListener());
        setLayout(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents(){
        header = new JPanel();
        dPanel = new drawPanel();
        header.setLayout(null);

        int width = getWidth();
        int height = getHeight();

        int headerHeight = 100;
        int dPanelHeight = height - headerHeight - 5;

        header.setBounds(0,0,width,headerHeight);
        dPanel.setBounds(-5,headerHeight+5,width,dPanelHeight);

        Border emptyBorder = BorderFactory.createEmptyBorder(
                dPanel.getPreferredSize().height,  // top
                0,                                  // left
                0,                                  // bottom
                0                                 // right
        );

        dPanel.setBorder(new CompoundBorder(emptyBorder, BorderFactory.createLineBorder(Color.black, 5)));

        JLabel DropDownLabel = new JLabel("'Pinsel'");
        JLabel widthLabel = new JLabel("Breite");
        JLabel heightLabel = new JLabel("Höhe");
        cornersLabel = new JLabel("Anzahl Ecken");

        drops = new String[]{
            "Viel Eck",
            "Pinsel",
            "Kreis",
            "Rechteck"
        };

        widthField = new JTextField("100");
        heightField = new JTextField("100");
        cornerField = new JTextField("3");
        fillBox = new JCheckBox("Ausfüllen");
        colorChooser = new JColorChooser();
        DropDown = new JComboBox<>(drops);
        helpButton = new JButton("Info");
        colorButton = new JButton("Farbe ändern");
        colorShow = new updaterPanel();

        DropDown.addActionListener(new DropDownActionListener());
        helpButton.addActionListener(new HelpActionListener());
        colorButton.addActionListener(new ColorActionListener());

        DropDown.setBounds(10,20,100,30);
        widthField.setBounds(120, 20, 100, 30);
        heightField.setBounds(120 * 2, 20, 100, 30);
        cornerField.setBounds(120 * 3, 20, 100, 30);
        fillBox.setBounds(120 * 4, 20, 100, 30);
        colorButton.setBounds(120 * 5, 20, 150, 30);
        colorShow.setBounds(120 * 5, 60, 150, 30);
        helpButton.setBounds(120 * 9, 20, 80, 30);

        Font f = DropDownLabel.getFont();
        FontMetrics Metrics = DropDownLabel.getFontMetrics(f);

        colorShow.update(colorChooser.getColor());

        int x;
        int textWidth = Metrics.stringWidth(DropDownLabel.getText());
        DropDownLabel.setBounds(60 - textWidth / 2, 60, 100, 30);

        textWidth = Metrics.stringWidth(widthLabel.getText());
        x = (120 * 1) + 50 - textWidth / 2;
        widthLabel.setBounds(x, 60, 100, 30);

        textWidth = Metrics.stringWidth(heightLabel.getText());
        x = (120 * 2) + 50 - textWidth / 2;
        heightLabel.setBounds(x, 60, 100, 30);

        textWidth = Metrics.stringWidth(cornersLabel.getText());
        x = (120 * 3) + 50 - textWidth / 2;
        cornersLabel.setBounds(x, 60, 100, 30);

        header.add(DropDown);
        header.add(widthField);
        header.add(heightField);
        header.add(cornerField);
        header.add(fillBox);
        header.add(colorButton);
        header.add(colorShow);
        header.add(helpButton);

        header.add(DropDownLabel);
        header.add(widthLabel);
        header.add(heightLabel);
        header.add(cornersLabel);


        add(header);
        add(dPanel);
    }

    public int getDropDownIndex(){return DropDown.getSelectedIndex();}

    public static double safeParse(String toParse, int normal){
        try{
            return Double.parseDouble(toParse.replace(",","."));
        } catch (NumberFormatException ex) {
            return normal;
        }
    }
    public static int safeParseInt(String toParse, int normal){
        try{
            return Integer.parseInt(toParse.replace(",","."));
        } catch (NumberFormatException ex) {
            return normal;
        }
    }

    class DropDownActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            int index = source.getSelectedIndex();
            if (index <= 1) helpFrame.switcher();
            if (index != 0){
                cornerField.setEditable(false);
                cornersLabel.setForeground(Color.gray);
            } else {
                cornerField.setEditable(true);
                cornersLabel.setForeground(Color.black);
            }

        }
    }

    class BrushMouseListener extends MouseAdapter{
        Timer Brush;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getY() < 148) return;
            int x = e.getX();
            int y = e.getY() - 148;
            double width = safeParse(widthField.getText(), 100);
            double height = safeParse(heightField.getText(), 100);
            int corners = safeParseInt(cornerField.getText(), 3);
            int fill = Entry.FILL_FALSE;
            Color color = Color.black;
            if (colorChooser.getColor() != null) color = colorChooser.getColor();

            x = (int) (x - width / 2);
            y = (int) (y - height / 2);

            if (fillBox.isSelected()) fill = Entry.FILL_TRUE;
            switch (DropDown.getSelectedIndex()){
                case 0:
                    dPanel.drawNCorners(x, y, width, height, corners, fill, color);
                    break;
                case 1:
                    Brush = new Timer(1, new TimerActionListener(width, height, fill, color));
                    dPanel.drawCircle(x,y,width,height,fill,color,Entry.BELONGING_START);
                    Brush.start();
                    break;
                case 2:
                    dPanel.drawCircle(x,y,width,height,fill,color);
                    break;
                case 3:
                    dPanel.drawRectangle(x,y,width,height,fill,color);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int y = e.getY() - 148;
            if (y < 0) y = 148;
            int x = e.getX();
            double width = safeParse(widthField.getText(), 100);
            double height = safeParse(heightField.getText(), 100);
            int fill = Entry.FILL_FALSE;
            Color color = Color.black;
            if (colorChooser.getColor() != null) color = colorChooser.getColor();

            x = (int) (x - width / 2);
            y = (int) (y - height / 2);


            if (DropDown.getSelectedIndex() == 1) {
                dPanel.drawCircle(x, y, width, height, fill, color, Entry.BELONGING_END);
                if (Brush != null) Brush.stop();
            }
        }
    }

    class TimerActionListener implements ActionListener{

        private double width;
        private double height;
        private int fill;
        private Color color;

        TimerActionListener(double width, double height, int fill, Color color){
            this.color = color;
            this.fill = fill;
            this.width = width;
            this.height = height;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mouseLocation, PaintMain.this);
            int x = (int) mouseLocation.getX();
            int y = (int) mouseLocation.getY();
            y -= 148;
            if (y < 0) y = 148;
            x = (int) (x - width / 2);
            y = (int) (y - height / 2);

            dPanel.drawCircle(x,y,width,height,fill,color,Entry.BELONGING_MIDDLE);


        }
    }

    class ColorActionListener extends WindowAdapter implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame colorFrame = new JFrame("Farbe ändern");
            colorButton.setEnabled(false);
            colorFrame.addWindowListener(this);
            colorFrame.setResizable(false);
            colorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            colorFrame.add(colorChooser);
            colorFrame.pack();

            int x = (int) (PaintMain.this.getX() + PaintMain.this.getWidth() / 2 - colorFrame.getWidth() / 2);
            int y = (int) (PaintMain.this.getY() + PaintMain.this.getHeight() / 2 - colorFrame.getHeight() / 2);
            colorFrame.setLocation(x,y);

            colorFrame.setVisible(true);
            colorFrame.requestFocus();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            colorButton.setEnabled(true);
            colorShow.update(colorChooser.getColor());
        }
    }

    class HelpActionListener extends WindowAdapter implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getDropDownIndex() <= 1) helpFrame = new HelpFrame("Info", PaintMain.this);
        }
    }
}
