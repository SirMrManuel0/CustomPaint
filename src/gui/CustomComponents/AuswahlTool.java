package gui.CustomComponents;

import gui.Shapes.*;
import gui.Shapes.Geometry.cPoint;
import Sulfur.Entry;
import gui.Shapes.Rectangle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AuswahlTool implements ObsDPanel, ActionListener, ObsPaintMain {
    private final double HOVER_DIAMETER = 50;
    private final Color UNSELECTED_HOVER = new Color(92,92,92);
    private final Color SELECTED_PICKED = new Color(200,0,0);

    private cPoint LocationPoint;
    private drawPanel dPanel;
    private ArrayList<ObsAuswahl> Observer;
    private int pastIndex = -1;
    private boolean hasSelected = false;
    private int selectedIndex = -1;
    private ArrayList<Integer> selectedIndexes;

    public AuswahlTool(){
        Observer = new ArrayList<>();
        selectedIndexes = new ArrayList<>();
    }

    public void clicked(cPoint locationPoint){
        setLocationPoint(locationPoint);
        int index = containTest(LocationPoint);
        if (index == -1) {
            hasSelected = false;
            selectedIndex = -1;
            dPanel.clearSelected();
            return;
        }
        hasSelected = true;
        selectedIndex = index;
        ArrayList<Integer> selectedElements = new ArrayList<>();
        Shapus element = dPanel.getMemory(index);
        switch (element.getKind()){
            case Shapus.CIRCLE:
            case Shapus.RECTANGLE:
            case Shapus.N_CORNER:
                selectedElements.add(index);
                break;
            case Shapus.CIRCLE_WITH_B_STATUS:
                selectedElements = (ArrayList<Integer>) selectAllBStatus(element, index, false);
                break;
        }
        createSelectedOutline(selectedElements);
        for (ObsAuswahl O : Observer) O.selected(element);
        selectedIndexes = selectedElements;


    }

    public void hover(cPoint hoverPoint){
        if (dPanel.getMemory().isEmpty()) return;
        int index = containTest(hoverPoint);
        if (pastIndex == -1) pastIndex = index;
        if (index == -1 && !hasSelected) {
            dPanel.clearHover();
            return;
        }
        if (index == -1) {
            dPanel.clearHover();
            return;
        }
        if (index == selectedIndex) return;

        if (index != pastIndex) {
            dPanel.clearHover();
            pastIndex = index;
        }
        Shapus hoverShapus = dPanel.getMemory(index);
        switch (hoverShapus.getKind()){
            case Shapus.N_CORNER:
                double[] val = hoverShapus.getVal();
                double x = val[0];
                x -= HOVER_DIAMETER / 2;
                double y = val[1];
                y -= HOVER_DIAMETER / 2;
                double diameter = val[2] + HOVER_DIAMETER;
                ArrayList<Shapus> S = new ArrayList<>();
                S.add(new nCorners(x, y, diameter, val[3], UNSELECTED_HOVER, Entry.FILL_FALSE));
                dPanel.drawHover(S);
                break;
            case Shapus.RECTANGLE:
            case Shapus.CIRCLE:
                double[] val_circle = hoverShapus.getVal();
                double x_circle = val_circle[0];
                x_circle -= HOVER_DIAMETER / 2;
                double y_circle = val_circle[1];
                y_circle -= HOVER_DIAMETER / 2;
                double width_circle = val_circle[2] + HOVER_DIAMETER;
                double height_circle = val_circle[3] + HOVER_DIAMETER;
                ArrayList<Shapus> S_circle = new ArrayList<>();
                if (hoverShapus.getKind() == Shapus.CIRCLE)
                    S_circle.add(new Ellipse(x_circle, y_circle, width_circle, height_circle, UNSELECTED_HOVER, Entry.FILL_FALSE));
                if (hoverShapus.getKind() == Shapus.RECTANGLE)
                    S_circle.add(new Rectangle(x_circle, y_circle, width_circle, height_circle, UNSELECTED_HOVER, Entry.FILL_FALSE));
                dPanel.drawHover(S_circle);
                break;
            case Shapus.CIRCLE_WITH_B_STATUS:
                dPanel.drawHover(CircleWithBStatus(hoverShapus, index));
                break;
        }
    }

    // private helper functions
    private int containTest(cPoint p){
        ArrayList<Shapus> Memory = dPanel.getMemory();

        for (int i = Memory.size() - 1; i >= 0; i--){
            if (Memory.get(i).contains(p)){
                return i;
            }
        }
        return -1;
    }

    private ArrayList<Shapus> CircleWithBStatus(Shapus hoverOn, int index){
        ArrayList<Shapus> allConnected = (ArrayList<Shapus>) selectAllBStatus(hoverOn, index, true);

        ArrayList<Shapus> correctShapus = new ArrayList<>();
        for (Shapus S : allConnected){
            int bStatus = S.getbStatus();
            double[] val = S.getVal();
            double x = val[0] - HOVER_DIAMETER / 2;
            double y = val[1] - HOVER_DIAMETER / 2;
            double width = val[2] + HOVER_DIAMETER;
            double height = val[3] + HOVER_DIAMETER;
            correctShapus.add(new Ellipse(x, y, width, height, UNSELECTED_HOVER, Entry.FILL_FALSE, bStatus));
        }
        ShapArea area = new ShapArea(correctShapus.get(0));
        for (int i = 1; i < correctShapus.size(); i++) area.add(correctShapus.get(i));
        ArrayList<Shapus> returnee = new ArrayList<>();
        returnee.add(area);

        return returnee;
    }

    private boolean ArrayListContainsInt(ArrayList<Integer> list, int integer){
        for (int i : list){
            if (i == integer) return true;
        }
        return false;
    }

    private ArrayList<Shapus> reverseShapus(ArrayList<Shapus> list){
        ArrayList<Shapus> reversed = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) reversed.add(list.get(i));
        return reversed;
    }

    private ArrayList<Integer> reverseInteger(ArrayList<Integer> list){
        ArrayList<Integer> reversed = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) reversed.add(list.get(i));
        return reversed;
    }

    private ArrayList selectAllBStatus(Shapus element, int index, boolean type){
        // type == true -> ArrayList<Shapus> objects
        // type == false -> ArrayList<Integer> indexes
        ArrayList<Shapus> Memory = dPanel.getMemory();
        ArrayList<Shapus> allConnected = new ArrayList<>();
        ArrayList<Integer> allConnectedIndex = new ArrayList<>();

        if (element.getbStatus() == Entry.BELONGING_END){
            allConnected.add(element);
            allConnectedIndex.add(index);
            for (int i = index - 1; i >= 0; i--){
                Shapus current = Memory.get(i);
                allConnected.add(current);
                allConnectedIndex.add(i);
                if (current.getbStatus() == Entry.BELONGING_START) break;
            }
        }
        else if (element.getbStatus() == Entry.BELONGING_MIDDLE || element.getbStatus() == Entry.BELONGING_START){
            boolean collect = false;
            for (int i = Memory.size() - 1; i >= 0; i--){
                Shapus current = Memory.get(i);
                if (current.getbStatus() == Entry.BELONGING_NONE) continue;
                if (current.getbStatus() == Entry.BELONGING_END) collect = true;
                if (collect) {
                    allConnected.add(current);
                    allConnectedIndex.add(i);
                    if (current.getbStatus() == Entry.BELONGING_START) {
                        if (ArrayListContainsInt(allConnectedIndex, index)) break;
                        else {
                            allConnectedIndex = new ArrayList<>();
                            allConnected = new ArrayList<>();
                            collect = false;
                        }
                    }
                }
            }
        }

        if (allConnected.isEmpty() || allConnectedIndex.isEmpty()) return new ArrayList<>();

        if (type) return reverseShapus(allConnected);
        if (!type) return reverseInteger(allConnectedIndex);
        return new ArrayList<>();
    }

    private void createSelectedOutline(ArrayList<Integer> elem){
        ArrayList<Shapus> Memory = dPanel.getMemory();
        if (elem.size() == 1){
            Shapus shape = Memory.get(elem.get(0));
            switch (shape.getKind()){
                case Shapus.N_CORNER:
                    double[] val = shape.getVal();
                    double x = val[0];
                    x -= HOVER_DIAMETER / 2;
                    double y = val[1];
                    y -= HOVER_DIAMETER / 2;
                    double diameter = val[2] + HOVER_DIAMETER;
                    ArrayList<Shapus> S = new ArrayList<>();
                    S.add(new nCorners(x, y, diameter, val[3], SELECTED_PICKED, Entry.FILL_FALSE));
                    dPanel.drawSelected(S);
                    break;
                case Shapus.RECTANGLE:
                case Shapus.CIRCLE:
                    double[] val_circle = shape.getVal();
                    double x_circle = val_circle[0];
                    x_circle -= HOVER_DIAMETER / 2;
                    double y_circle = val_circle[1];
                    y_circle -= HOVER_DIAMETER / 2;
                    double width_circle = val_circle[2] + HOVER_DIAMETER;
                    double height_circle = val_circle[3] + HOVER_DIAMETER;
                    ArrayList<Shapus> S_circle = new ArrayList<>();
                    if (shape.getKind() == Shapus.CIRCLE)
                        S_circle.add(new Ellipse(x_circle, y_circle, width_circle, height_circle, SELECTED_PICKED, Entry.FILL_FALSE));
                    if (shape.getKind() == Shapus.RECTANGLE)
                        S_circle.add(new Rectangle(x_circle, y_circle, width_circle, height_circle, SELECTED_PICKED, Entry.FILL_FALSE));
                    dPanel.drawSelected(S_circle);
                    break;
            }
        }
        else if (elem.size() > 1) {
            ArrayList<Shapus> allConnected = new ArrayList<>();
            for (int i : elem) allConnected.add(Memory.get(i));

            ArrayList<Shapus> correctShapus = new ArrayList<>();
            for (Shapus S : allConnected){
                int bStatus = S.getbStatus();
                double[] val = S.getVal();
                double x = val[0] - HOVER_DIAMETER / 2;
                double y = val[1] - HOVER_DIAMETER / 2;
                double width = val[2] + HOVER_DIAMETER;
                double height = val[3] + HOVER_DIAMETER;
                correctShapus.add(new Ellipse(x, y, width, height, SELECTED_PICKED, Entry.FILL_FALSE, bStatus));
            }
            ShapArea area = new ShapArea(correctShapus.get(0));
            for (int i = 1; i < correctShapus.size(); i++) area.add(correctShapus.get(i));
            ArrayList<Shapus> outline = new ArrayList<>();
            outline.add(area);
            dPanel.drawSelected(outline);
        }
    }

    // small public functions
    public void setDrawPanel(drawPanel dPanel) {this.dPanel = dPanel;}
    public void setLocationPoint(cPoint locationPoint) { LocationPoint = locationPoint; }
    public cPoint getLocationPoint(){ return LocationPoint; }
    public boolean isHasSelected(){ return hasSelected; }
    public void  addObs(ObsAuswahl observ) { Observer.add(observ); }

    @Override
    public void undo() {
        if (hasSelected && selectedIndex > dPanel.getMemory().size() - 1){
            dPanel.clearSelected();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Shapus element = dPanel.getMemory(selectedIndex);
        if (element.getKind() != Shapus.CIRCLE_WITH_B_STATUS) {
            dPanel.deleteMemory(selectedIndex);
        }
        else {
            ArrayList<Integer> indexes = selectAllBStatus(element, selectedIndex, false);
            for (int i = 1; i <= indexes.size(); i++) dPanel.deleteMemory(indexes.get(0));
        }
        for (ObsAuswahl O : Observer) O.deleted();
        dPanel.clearSelected();
        hasSelected = false;
        selectedIndex = -1;
        selectedIndexes = new ArrayList<>();
    }

    @Override
    public void width(double width) {
        if (!hasSelected) return;
        for (int index : selectedIndexes){
            dPanel.getMemory(index).setWidth(width);
        }
        dPanel.clearHover();
        createSelectedOutline(selectedIndexes);
    }

    @Override
    public void height(double height) {
        if (!hasSelected) return;
        for (int index : selectedIndexes){
            dPanel.getMemory(index).setHeight(height);
        }
        dPanel.clearHover();
        createSelectedOutline(selectedIndexes);
    }

    @Override
    public void corners(int corners) {
        if (!hasSelected) return;
        for (int index : selectedIndexes){
            dPanel.getMemory(index).setHeight(corners);
        }
        dPanel.clearHover();
        createSelectedOutline(selectedIndexes);
    }

    @Override
    public void color(Color color) {
        if (!hasSelected) return;
        for (int index : selectedIndexes){
            dPanel.getMemory(index).setColor(color);
        }
        dPanel.clearHover();
        createSelectedOutline(selectedIndexes);
    }

    @Override
    public void fill(boolean fill) {
        if (!hasSelected) return;
        for (int index : selectedIndexes){
            dPanel.getMemory(index).setFill(fill);
        }
        dPanel.clearHover();
        createSelectedOutline(selectedIndexes);
    }
}
