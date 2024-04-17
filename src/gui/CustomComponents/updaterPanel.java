package gui.CustomComponents;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class updaterPanel extends JPanel {
    private ArrayList<ObsPaintMain> Observer;
    public updaterPanel(){Observer = new ArrayList<>();}
    public void update(Color color){
        setBackground(color);
        for (ObsPaintMain O : Observer) O.color(color);
    }
    public void addObs(ObsPaintMain observ) { Observer.add(observ); }
}
