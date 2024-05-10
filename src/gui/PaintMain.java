package gui;

import Sulfur.Entry;
import gui.CustomComponents.*;
import gui.Shapes.Geometry.cPoint;
import gui.Shapes.Shapus;
import gui.Shapes.unsymmNCorner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PaintMain extends CustomFrame implements ObsAuswahl {

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
    private JSlider sliderHeight;
    private JSlider sliderWidth;
    private JLabel widthLabel;
    private JLabel heightLabel;
    private JButton saveButton;
    private JButton loadButton;
    private JButton exportButton;
    private AuswahlTool auswahlTool;
    private MovementListener MovListener;
    private JButton deleteButton;
    private ArrayList<ObsPaintMain> Observer;

    public PaintMain(){
        super(CustomFrame.PHI_HEIGHT, 1.7 , 2, "CustomPaint", false);
        setSize(new Dimension(1188, 734));
        addMouseListener(new BrushMouseListener());
        MovListener = new MovementListener();
        addMouseMotionListener(MovListener);
        setLayout(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents(){
        header = new JPanel();
        dPanel = new drawPanel();
        auswahlTool = new AuswahlTool();
        deleteButton = new JButton("Löschen");
        Observer = new ArrayList<>();

        deleteButton.setEnabled(false);
        deleteButton.addActionListener(auswahlTool);
        header.setLayout(null);

        int width = getWidth();
        int height = getHeight();

        int headerHeight = 130;
        int dPanelHeight = height - headerHeight - 5;

        header.setBounds(0,0,width,headerHeight);
        dPanel.setBounds(-5,headerHeight+5,width,dPanelHeight);

        auswahlTool.setDrawPanel(dPanel);

        addObs(auswahlTool);
        auswahlTool.addObs(this);
        dPanel.addObs(auswahlTool);

        Border emptyBorder = BorderFactory.createEmptyBorder(
                dPanel.getPreferredSize().height,  // top
                0,                                  // left
                0,                                  // bottom
                0                                 // right
        );

        dPanel.setBorder(new CompoundBorder(emptyBorder, BorderFactory.createLineBorder(Color.black, 5)));

        JLabel DropDownLabel = new JLabel("'Pinsel'");
        widthLabel = new JLabel("Durchmesser");
        heightLabel = new JLabel("Hoehe");
        cornersLabel = new JLabel("Anzahl Ecken");

        heightLabel.setForeground(Color.gray);

        drops = new String[]{
                "Viel Eck",
                "Pinsel",
                "Kreis",
                "Rechteck",
                "Auswahltool",
                "Viel Eck (unsymmetrisch)"
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
        sliderHeight = new JSlider(JSlider.HORIZONTAL, 5, dPanelHeight, 100);
        sliderWidth = new JSlider(JSlider.HORIZONTAL, 5, getWidth(), 100);
        saveButton = new JButton("Sichern");
        loadButton = new JButton("Laden");
        exportButton = new JButton("Exportieren");
        JButton undoButton = new JButton("Undo");
        JButton clearButton = new JButton("Leeren");

        colorShow.addObs(auswahlTool);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dPanel.save();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dPanel.load();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dPanel.export();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dPanel.clear();
            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dPanel.undo();
            }
        });

        DropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DropDown.getSelectedIndex() == 4) {
                    MovListener.setAuswahlStatus(true);
                } else {
                    MovListener.setAuswahlStatus(false);
                }
            }
        });

        widthField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void changed(){
                for (ObsPaintMain O : Observer) O.width(safeParse(widthField.getText(), 100));
            }
        });

        heightField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void changed(){
                for (ObsPaintMain O : Observer) O.height(safeParse(heightField.getText(), 100));
            }
        });

        cornerField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void changed(){
                for (ObsPaintMain O : Observer) O.corners((int) safeParse(cornerField.getText(), 3));
            }
        });

        fillBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ObsPaintMain O : Observer) O.fill(fillBox.isSelected());
            }
        });

        fillBox.setSelected(true);

        colorChooser.setColor(new Color(26, 112, 143));

        heightField.setEditable(false);
        sliderHeight.setEnabled(false);

        sliderHeight.setMinorTickSpacing(5);
        sliderHeight.setMajorTickSpacing(20);

        sliderHeight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                heightField.setText(String.valueOf(sliderHeight.getValue()));
            }
        });

        sliderWidth.setMinorTickSpacing(5);
        sliderWidth.setMajorTickSpacing(20);

        sliderWidth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                widthField.setText(String.valueOf(sliderWidth.getValue()));
            }
        });

        widthField.addKeyListener(new KeyAdapter() {
            private String old = "100";
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (widthField.getText().equals(old)) return;
                if (widthField.getText().isEmpty() && widthField.hasFocus()) return;
                old = widthField.getText();
                double val = safeParse(widthField.getText(), 100);
                if (val == 100) widthField.setText("100");
                sliderWidth.setValue((int) val);
            }
        });

        heightField.addKeyListener(new KeyAdapter() {
            private String old = "100";
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (heightField.getText().equals(old)) return;
                if (heightField.getText().isEmpty() && heightField.hasFocus()) return;
                old = heightField.getText();
                double val = safeParse(heightField.getText(), 100);
                if (val == 100) heightField.setText("100");
                sliderHeight.setValue((int) val);
            }
        });

        DropDown.addActionListener(new DropDownActionListener());
        helpButton.addActionListener(new HelpActionListener());
        colorButton.addActionListener(new ColorActionListener());

        DropDown.setBounds(10,20,100,30);
        widthField.setBounds(120, 20, 100, 30);
        heightField.setBounds(120 * 2, 20, 100, 30);
        cornerField.setBounds(120 * 3, 20, 100, 30);
        fillBox.setBounds(120 * 4, 20, 100, 30);
        deleteButton.setBounds(120 * 4, 60, 100, 30);
        colorButton.setBounds(120 * 5, 20, 150, 30);
        colorShow.setBounds(120 * 5, 60, 150, 30);
        helpButton.setBounds(120 * 8 + 110, 20, 90, 30);
        undoButton.setBounds(120 * 8 + 110, 60, 90, 30);
        clearButton.setBounds(120 * 8 + 110, 100, 90, 30);
        sliderHeight.setBounds(120*2, 60, 100, 30);
        sliderWidth.setBounds(120, 60, 100, 30);
        saveButton.setBounds(120 * 7, 20, 120, 30);
        loadButton.setBounds(120 * 7, 60, 120, 30);
        exportButton.setBounds(120 * 7, 100, 120, 30);

        Font f = DropDownLabel.getFont();
        FontMetrics Metrics = DropDownLabel.getFontMetrics(f);

        colorShow.update(colorChooser.getColor());

        int x;
        int textWidth = Metrics.stringWidth(DropDownLabel.getText());
        DropDownLabel.setBounds(60 - textWidth / 2, 60, 100, 30);

        textWidth = Metrics.stringWidth(widthLabel.getText());
        x = (120 * 1) + 50 - textWidth / 2;
        widthLabel.setBounds(x, 90, 100, 30);

        textWidth = Metrics.stringWidth(heightLabel.getText());
        x = (120 * 2) + 50 - textWidth / 2;
        heightLabel.setBounds(x, 90, 100, 30);

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
        header.add(sliderHeight);
        header.add(sliderWidth);

        header.add(undoButton);
        header.add(DropDownLabel);
        header.add(widthLabel);
        header.add(heightLabel);
        header.add(cornersLabel);

        header.add(saveButton);
        header.add(loadButton);
        header.add(exportButton);
        header.add(clearButton);
        header.add(deleteButton);

        add(header);
        add(dPanel);
    }

    private void addObs(ObsPaintMain Observ) { Observer.add(Observ); }

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

    @Override
    public void selected(Shapus selectedShapus) {
        int kind = selectedShapus.getKind();
        if (kind == Shapus.UNSYMMETRICAL_N_CORNER) {
            sliderHeight.setEnabled(false);
            heightField.setEditable(false);
            heightLabel.setForeground(Color.gray);

            widthField.setText("0");
            widthLabel.setText("delta Groeße");
            sliderWidth.setMinimum(-100);
            sliderWidth.setValue(0);
            sliderWidth.setMaximum(100);
            FontMetrics Metrics = widthLabel.getFontMetrics(widthLabel.getFont());
            int textWidth = Metrics.stringWidth(widthLabel.getText());
            int x = (120 * 1) + 50 - textWidth / 2;
            widthLabel.setBounds(x, 90, 100, 30);
            deleteButton.setEnabled(true);
            colorChooser.setColor(selectedShapus.getColor());
            colorShow.update(selectedShapus.getColor());
            return;
        }
        if (kind == Shapus.N_CORNER || kind == Shapus.CIRCLE_WITH_B_STATUS)
            helpFrame = new HelpFrame("Info", PaintMain.this);
        if (kind == Shapus.N_CORNER || kind == Shapus.CIRCLE_WITH_B_STATUS)
            helpFrame.switcher();
        if (kind != Shapus.N_CORNER){
            sliderHeight.setEnabled(true);
            cornerField.setEditable(false);
            cornersLabel.setForeground(Color.gray);
            heightField.setEditable(true);
            widthLabel.setText("Breite");
            heightLabel.setForeground(Color.black);

            heightField.setText(String.valueOf(selectedShapus.getVal()[3]).replace(".",","));
            sliderHeight.setValue((int) selectedShapus.getVal()[3]);

            widthField.setText(String.valueOf(selectedShapus.getVal()[2]).replace(".",","));
            sliderWidth.setValue((int) selectedShapus.getVal()[2]);

            colorChooser.setColor(selectedShapus.getColor());
            colorShow.update(selectedShapus.getColor());

            fillBox.setSelected(selectedShapus.getFill());
        } else {
            cornerField.setEditable(true);
            heightField.setEditable(false);
            widthLabel.setText("Durchmesser");
            heightLabel.setForeground(Color.gray);
            cornersLabel.setForeground(Color.black);

            cornerField.setText(String.valueOf((int) selectedShapus.getVal()[3]));

            widthField.setText(String.valueOf(selectedShapus.getVal()[2]).replace(".",","));
            sliderWidth.setValue((int) selectedShapus.getVal()[2]);

            colorChooser.setColor(selectedShapus.getColor());
            colorShow.update(selectedShapus.getColor());

            fillBox.setSelected(selectedShapus.getFill());
        }
        FontMetrics Metrics = widthLabel.getFontMetrics(widthLabel.getFont());
        int textWidth = Metrics.stringWidth(widthLabel.getText());
        int x = (120 * 1) + 50 - textWidth / 2;
        widthLabel.setBounds(x, 90, 100, 30);
        deleteButton.setEnabled(true);
    }

    @Override
    public void deleted() {
        deleteButton.setEnabled(false);
    }

    class DropDownActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            int index = source.getSelectedIndex();
            for (ObsPaintMain Obs : Observer){
                Obs.switchAway();
            }
            sliderWidth.setMinimum(5);
            sliderWidth.setMaximum(getWidth());
            if (index <= 1) helpFrame = new HelpFrame("Info", PaintMain.this);
            if (index <= 1) helpFrame.switcher();
            if (index != 0){
                sliderHeight.setEnabled(true);
                cornerField.setEditable(false);
                cornersLabel.setForeground(Color.gray);
                heightField.setEditable(true);
                widthLabel.setText("Breite");
                heightLabel.setForeground(Color.black);
            } else {
                cornerField.setEditable(true);
                heightField.setEditable(false);
                widthLabel.setText("Durchmesser");
                heightLabel.setForeground(Color.gray);
                cornersLabel.setForeground(Color.black);
            }
            FontMetrics Metrics = widthLabel.getFontMetrics(widthLabel.getFont());
            int textWidth = Metrics.stringWidth(widthLabel.getText());
            int x = (120 * 1) + 50 - textWidth / 2;
            widthLabel.setBounds(x, 90, 100, 30);
            deleteButton.setEnabled(false);
            auswahlTool.clear();
        }
    }

    class BrushMouseListener extends MouseAdapter{

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getY() < 180) return;
            int x = e.getX();
            int y = e.getY() - 165;
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
                    y += height / 2;
                    y -= width / 2;

                    dPanel.drawNCorners(x, y, width, corners, fill, color);
                    break;
                case 1:
                    dPanel.drawCircle(x,y,width,height,fill,color,Entry.BELONGING_START);
                    MovListener.pinsel(width,height,fill,color);
                    MovListener.setPinselStatus(true);
                    break;
                case 2:
                    dPanel.drawCircle(x,y,width,height,fill,color);
                    break;
                case 3:
                    dPanel.drawRectangle(x,y,width,height,fill,color);
                    break;
                case 4:
                    cPoint p = new cPoint(e.getX(),e.getY() - 165);
                    auswahlTool.clicked(p);
                    auswahlTool.setDragStart(p);
                    break;
                case 5:
                    cPoint P = new cPoint(e.getX(), e.getY() - 165);
                    if (!dPanel.isDrawingUnsymm()){
                        dPanel.drawUnsymm(P, color, fill);
                        break;
                    }
                    dPanel.drawUnsymm(P);
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int y = e.getY() - 165;
            if (y < 0) y = 180;
            int x = e.getX();
            double width = safeParse(widthField.getText(), 100);
            double height = safeParse(heightField.getText(), 100);
            int fill = Entry.FILL_FALSE;
            Color color = Color.black;
            if (colorChooser.getColor() != null) color = colorChooser.getColor();
            if (fillBox.isSelected()) fill = Entry.FILL_TRUE;

            x = (int) (x - width / 2);
            y = (int) (y - height / 2);


            if (DropDown.getSelectedIndex() == 1) {
                dPanel.drawCircle(x, y, width, height, fill, color, Entry.BELONGING_END);
                MovListener.setPinselStatus(false);
            }
            if (DropDown.getSelectedIndex() == 4){
                auswahlTool.release(new cPoint(e.getX(), e.getY() - 165));
            }
        }
    }

    class MovementListener extends MouseMotionAdapter{
        private double pinselWidth;
        private double pinselHeight;
        private int pinselFill;
        private Color pinselColor;
        private boolean pinselStatus = false;
        private boolean auswahlStatus = false;

        @Override
        public void mouseDragged(MouseEvent e){
            double x = e.getX();
            double y = e.getY() - 165;

            if (auswahlStatus){
                auswahlTool.drag(new cPoint(x,y));
            }

            if (pinselStatus) {
                x -= pinselWidth / 2;
                y -= pinselHeight / 2;
                dPanel.drawCircle(x,y, pinselWidth, pinselHeight, pinselFill, pinselColor, Entry.BELONGING_MIDDLE);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (auswahlStatus) auswahlTool.hover(new cPoint(e.getX(), e.getY() - 165));
        }

        public void pinsel(double width, double height, int fill, Color color) {
            pinselWidth = width;
            pinselHeight = height;
            pinselFill = fill;
            pinselColor = color;
        }

        public void setPinselStatus(boolean status) { pinselStatus = status; }
        public void setAuswahlStatus(boolean status) { auswahlStatus = status; }
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
            if (helpFrame == null) helpFrame = new HelpFrame("Info", PaintMain.this);
            if (getDropDownIndex() <= 1) helpFrame.setVisible(true);
        }
    }
}
