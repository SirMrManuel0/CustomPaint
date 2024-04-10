package gui.CustomComponents;

import gui.PaintMain;
import gui.PathRetriever;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HelpFrame extends JFrame {
    private PaintMain parent;
    private ArrayList<Component> Memory;

    public HelpFrame(String title, PaintMain parent){
        super(title);
        this.parent = parent;
        Memory = new ArrayList<>();
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        Point parentLocation = parent.getLocation();
        Dimension parentDimension = parent.getSize();
        switcher();
        int x = (int) (parentLocation.getX() + parentDimension.getWidth() / 2 - getWidth() / 2);
        int y = (int) (parentLocation.getY() + parentDimension.getHeight() / 2 - getHeight() / 2);
        setLocation(x,y);
    }

    private void nEck(){
        setSize(new Dimension(600,900));
        ImageIcon icon = new ImageIcon(PathRetriever.getPath(PathRetriever.N_ECK_EXPLAIN));
        icon = CustomFrame.scaleImageIcon(icon, 150,150);
        JLabel label = new JLabel(icon);
        label.setBounds(0,50,150,150);

        JLabel title = new JLabel("Wie funktioniert 'Viel Eck'?");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        Font f = title.getFont();
        FontMetrics metrics = title.getFontMetrics(f);
        title.setBounds( (int) ((getWidth() - metrics.stringWidth(title.getText())) / 2),
                5, 400, 20);

        JLabel[] lines = new JLabel[]{
                new JLabel("Der makierte Winkel θ (Türkis) wird durch die Formel"),
                new JLabel(CustomFrame.scaleImageIcon(new ImageIcon(PathRetriever.getPath(PathRetriever.THETA_CAL)),150,50)),
                new JLabel("berechnet."),
                new JLabel("Alle anderen Winkel um θ sind gleich groß mit θ,"),
                new JLabel("dadurch kann man auch nur ein gleichseitiges Dreieck"),
                new JLabel("kreieren."),
                new JLabel("Alle Ecken werden auf dem Kreis (Schwarz) sein,"),
                new JLabel("da alle blauen Linien gleich groß sind."),
                new JLabel("Dies wird durch einen Vektor ermöglicht, der als Radius agiert."),
                new JLabel("Für den Vektor wird zu erst der Mittelpunkt errechnet:"),
                new JLabel(CustomFrame.scaleImageIcon(new ImageIcon(PathRetriever.getPath(PathRetriever.CENTER_CAL)), 200,50)),
                new JLabel("Als nächstes berechnet man den Normalvector."),
                new JLabel(CustomFrame.scaleImageIcon(new ImageIcon(PathRetriever.getPath(PathRetriever.NORMAL_VEC_CAL)), 100,50)),
                new JLabel("Nun wird getest, ob man eine gerade Anzahl an Ecken hat, wenn ja wird"),
                new JLabel("die erste VeKtor um einhalb θ nach links rotiert."),
                new JLabel("Einen Vektor in Java zu verschieben erfordert nicht den Winkel, da"),
                new JLabel("Math.sin() und Math.cos() nicht einen Winkel erwarten, sondern"),
                new JLabel("das Bogenmaß, den der WInkel im Einheitskreis hat."),
                new JLabel("Dies rechnet man so um:"),
                new JLabel(CustomFrame.scaleImageIcon(new ImageIcon(PathRetriever.getPath(PathRetriever.RADIAN_CAL)), 150,50)),
                new JLabel("Jetzt kann man den Vektor einfach rotieren:"),
                new JLabel(CustomFrame.scaleImageIcon(new ImageIcon(PathRetriever.getPath(PathRetriever.ROTATE_CAL)), 150,50)),
                new JLabel("Und schon wird ein n-Eck kreiert.")
        };

        int[] imgIndexes = new int[]{
                1,
                10,
                12,
                19,
                21
        };

        for (int i = 0; i < lines.length; i++){
            int x = 160;
            int y = 50 + 25 * i;
            int width = getWidth() - 160;
            int height = 20;
            if (inArray(imgIndexes, i)) height = 50;
            if (y > 160) x = 5;
            if (y > 160) width = getWidth();
            y += 55 * countLowerVals(imgIndexes, i);

            lines[i].setBounds(x,y,width,height);

            add(lines[i]);
        }
        add(title);
        add(label);
    }

    private void brush(){
        setSize(new Dimension(600,700));
        JLabel title = new JLabel("Wie funktioniert 'Pinsel'?");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        Font f = title.getFont();
        FontMetrics metrics = title.getFontMetrics(f);
        title.setBounds( (int) ((getWidth() - metrics.stringWidth(title.getText())) / 2),
                5, 400, 20);
        add(title);

        JLabel[] lines = new JLabel[]{
                new JLabel("Die Grundform des Pinsels ist ein Kreis, d.h. es werden sehr viele Kreise gezeichnet."),
                new JLabel("Dies wird durch einen Timer gemacht, der jede Millisekunde einen Kreis"),
                new JLabel("an der Stelle der Maus zeichnet."),
                new JLabel("Der Timer ist eine eingebaute standard Klasse von Java, die eine Zeit"),
                new JLabel("in Milliseunden und einen ActionListener in ihren Konstruktor einnimmt.")
        };

        int[] imgIndexes = new int[]{
        };

        for (int i = 0; i < lines.length; i++){
            int x = 5;
            int y = 50 + 25 * i;
            int width = getWidth();
            int height = 20;
            if (inArray(imgIndexes, i)) height = 50;
            y += 55 * countLowerVals(imgIndexes, i);

            lines[i].setBounds(x,y,width,height);

            add(lines[i]);
        }
    }

    public void switcher(){
        cleanUp();
        switch (parent.getDropDownIndex()){
            case 0:
                nEck();
                break;
            case 1:
                brush();
                break;
            default:
                dispose();
                break;
        }
    }

    private void cleanUp(){
        setSize(new Dimension(500, 600));
        if (Memory.size() > 0){
            for (Component com : Memory) remove(com);
            Memory = new ArrayList<>();
        }
        repaint();
        revalidate();
    }

    public static boolean inArray(int[] arr, int val){
        for (int j : arr){
            if (j == val) return true;
        }
        return false;
    }
    public static int countLowerVals(int[] arr, int val){
        int count = 0;
        for (int j : arr){
            if (j < val) count++;
            if (j >= val) break;
        }
        return count;
    }
}
