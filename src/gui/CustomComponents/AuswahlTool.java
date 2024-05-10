package gui.CustomComponents;

import gui.Shapes.*;
import gui.Shapes.Geometry.Vector2D;
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
    private int pastIndex;
    private boolean hasSelected;
    private int selectedIndex;
    private ArrayList<Integer> selectedIndexes;
    private boolean isDragging;
    private cPoint dragStart;
    private Vector2D moveEnd;
    private boolean[] suppress;

    public AuswahlTool(){
        pastIndex = -1;
        selectedIndex = -1;
        hasSelected = false;
        isDragging = false;
        suppress = new boolean[]{ false, false, false, false, false };
        dragStart = new cPoint(0,0);
        moveEnd = new Vector2D(0,0);
        Observer = new ArrayList<>();
        selectedIndexes = new ArrayList<>();
    }

    public void setDragStart(cPoint p) {dragStart = p;}

    public void drag(cPoint newLoc){
        if (!hasSelected) return;
        if (isDragging){
            deleteDragTransp();
        }
        isDragging = true;
        Shapus element = dPanel.getMemory(selectedIndex);
        double val[] = element.getVal();
        Vector2D move = dragStart.vector(newLoc);
        moveEnd = moveEnd.add(move);
        dragStart = newLoc;
        switch (element.getKind()) {
            case Shapus.CIRCLE:
            case Shapus.RECTANGLE:
            case Shapus.N_CORNER:
                cPoint loc = new cPoint(val[0], val[1]);
                double x = moveEnd.movePoint(loc).getX();
                double y = moveEnd.movePoint(loc).getY();
                Color elemCol = element.getColor();
                Color col = new Color(elemCol.getRed(), elemCol.getGreen(), elemCol.getBlue(), 50);
                Shapus dragging = new Ellipse(0,0,0,0, Color.RED, 0);
                if (element.getKind() == Shapus.N_CORNER)
                    dragging = new nCorners(x, y, val[2], val[3], col,element.getFillStatus());
                else if (element.getKind() == Shapus.CIRCLE)
                    dragging = new Ellipse(x, y, val[2], val[3], col, element.getFillStatus());
                else if (element.getKind() == Shapus.RECTANGLE)
                    dragging = new Rectangle(x, y, val[2], val[3], col, element.getFillStatus());
                dPanel.addAt(dragging, selectedIndex+1);
                break;
            case Shapus.CIRCLE_WITH_B_STATUS:
                ArrayList<Integer> intShapes = selectAllBStatus(element, selectedIndex, false);
                ArrayList<Shapus> shapusShapes = selectAllBStatus(element, selectedIndex,true);
                shapusShapes = reverseShapus(shapusShapes);
                int addAt = largest(intShapes) + 1;
                for (Shapus s : shapusShapes){
                    double[] values = s.getVal();
                    cPoint loc_b = new cPoint(values[0], values[1]);
                    double x_b = moveEnd.movePoint(loc_b).getX();
                    double y_b = moveEnd.movePoint(loc_b).getY();
                    Color elemCol_b = s.getColor();
                    Color col_b = new Color(elemCol_b.getRed(), elemCol_b.getGreen(), elemCol_b.getBlue(), 50);
                    int bStatus = s.getbStatus();
                    Shapus shape = new Ellipse(x_b, y_b, values[2], values[3], col_b, element.getFillStatus(), bStatus);
                    dPanel.addAt(shape, addAt);
                }
                break;
            case Shapus.UNSYMMETRICAL_N_CORNER:
                ArrayList<cPoint> Points = element.getPoints();
                Color elemCol_u = element.getColor();
                Color col_u = new Color(elemCol_u.getRed(), elemCol_u.getGreen(), elemCol_u.getBlue(), 50);
                unsymmNCorner unsymm = new unsymmNCorner(col_u, element.getFillStatus());
                for (cPoint P : Points){
                    P = moveEnd.movePoint(P);
                    unsymm.addPoint(P);
                }
                unsymm.wrap();
                dPanel.addAt(unsymm, selectedIndex+1);
                break;
        }
    }

    public void release(cPoint end){
        if (!isDragging) return;
        deleteDragTransp();
        Shapus element = dPanel.getMemory(selectedIndex);
        double val[] = element.getVal();
        switch (element.getKind()) {
            case Shapus.CIRCLE:
            case Shapus.RECTANGLE:
            case Shapus.N_CORNER:
                cPoint loc = new cPoint(val[0], val[1]);
                double x = moveEnd.movePoint(loc).getX();
                double y = moveEnd.movePoint(loc).getY();
                Color elemCol = element.getColor();
                Shapus dragging = new Ellipse(0,0,0,0, Color.RED, 0);
                if (element.getKind() == Shapus.N_CORNER)
                    dragging = new nCorners(x, y, val[2], val[3], elemCol,element.getFillStatus());
                else if (element.getKind() == Shapus.CIRCLE)
                    dragging = new Ellipse(x, y, val[2], val[3], elemCol, element.getFillStatus());
                else if (element.getKind() == Shapus.RECTANGLE)
                    dragging = new Rectangle(x, y, val[2], val[3], elemCol, element.getFillStatus());
                dPanel.deleteMemory(selectedIndex);
                dPanel.addAt(dragging, selectedIndex);
                break;
            case Shapus.CIRCLE_WITH_B_STATUS:
                ArrayList<Integer> intShapes = selectAllBStatus(element, selectedIndex, false);
                ArrayList<Shapus> shapusShapes = selectAllBStatus(element, selectedIndex,true);
                shapusShapes = reverseShapus(shapusShapes);
                int minIndex = smallest(intShapes);
                for (int i : intShapes){
                    dPanel.deleteMemory(minIndex);
                }
                for (Shapus s : shapusShapes){
                    double[] values = s.getVal();
                    cPoint loc_b = new cPoint(values[0], values[1]);
                    double x_b = moveEnd.movePoint(loc_b).getX();
                    double y_b = moveEnd.movePoint(loc_b).getY();
                    Color elemCol_b = s.getColor();
                    int bStatus = s.getbStatus();
                    Shapus shape = new Ellipse(x_b, y_b, values[2], values[3], elemCol_b, element.getFillStatus(), bStatus);
                    dPanel.addAt(shape, minIndex);
                }
                break;
            case Shapus.UNSYMMETRICAL_N_CORNER:
                ArrayList<cPoint> Points = element.getPoints();
                Color elemCol_u = element.getColor();
                unsymmNCorner unsymm = new unsymmNCorner(elemCol_u, element.getFillStatus());
                for (cPoint P : Points){
                    P = moveEnd.movePoint(P);
                    unsymm.addPoint(P);
                }
                unsymm.wrap();
                dPanel.deleteMemory(selectedIndex);
                dPanel.addAt(unsymm, selectedIndex);
                break;
        }
        moveEnd = new Vector2D(0,0);
        dragStart = new cPoint(0,0);
        isDragging = false;
        clicked(end);
    }

    public void clicked(cPoint locationPoint){
        setLocationPoint(locationPoint);
        int index = containTest(LocationPoint);
        if (index == -1) {
            switchAway();
            hasSelected = false;
            selectedIndex = -1;
            selectedIndexes = new ArrayList<>();
            dPanel.clearSelected();
            return;
        }
        suppress = new boolean[]{ true, true, true, true, true };
        hasSelected = true;
        selectedIndex = index;
        ArrayList<Integer> selectedElements = new ArrayList<>();
        Shapus element = dPanel.getMemory(index);
        switch (element.getKind()) {
            case Shapus.UNSYMMETRICAL_N_CORNER:
            case Shapus.CIRCLE:
            case Shapus.RECTANGLE:
            case Shapus.N_CORNER:
                selectedElements.add(index);
                break;
            case Shapus.CIRCLE_WITH_B_STATUS:
                selectedElements = (ArrayList<Integer>) selectAllBStatus(element, index, false);
                break;
        }
        for (ObsAuswahl O : Observer) O.selected(element);
        selectedIndexes = selectedElements;
        createSelectedOutline(selectedElements);
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
            case Shapus.UNSYMMETRICAL_N_CORNER:
                ArrayList<Shapus> list = new ArrayList<>();
                Shapus shape = hoverShapus.increase(HOVER_DIAMETER);
                shape.setColor(UNSELECTED_HOVER);
                shape.setFill(false);
                list.add(shape);
                dPanel.drawHover(list);
                break;
        }
    }

    public void clear(){
        selectedIndexes = new ArrayList<>();
        selectedIndex = -1;
        hasSelected = false;
        dPanel.clearHover();
        dPanel.clearSelected();
    }

    // private helper functions
    private void deleteDragTransp(){
        Shapus element = dPanel.getMemory(selectedIndex);
        if (dPanel.outOfRange(selectedIndex+1)) return;
        switch (element.getKind()) {
            case Shapus.CIRCLE:
            case Shapus.RECTANGLE:
            case Shapus.N_CORNER:
            case Shapus.UNSYMMETRICAL_N_CORNER:
                dPanel.deleteMemory(selectedIndex+1);
                break;
            case Shapus.CIRCLE_WITH_B_STATUS:
                ArrayList<Integer> indexes = selectAllBStatus(element, selectedIndex, false);
                int after = largest(indexes) + 1;
                if (after < 0) throw new IllegalArgumentException("Nothing has benn dragged yet!");
                ArrayList<Integer> transPIndexes = selectAllBStatus(dPanel.getMemory(after), after, false);
                int sm = smallest(transPIndexes);
                for (int i : transPIndexes){
                    dPanel.deleteMemory(sm);
                }
                break;
        }
    }

    private int largest(ArrayList<Integer> list){
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }

        int largest = Integer.MIN_VALUE; // Initialize with the smallest possible value
        int size = list.size();

        for (int i = 0; i < size / 2; i++){
            int j = size - 1 - i;
            int a = list.get(i);
            int b = list.get(j);
            largest = Math.max(largest, Math.max(a, b));
        }

        return largest;
    }


    private int smallest(ArrayList<Integer> list){
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }

        int smallest = Integer.MAX_VALUE;
        int size = list.size();

        for (int i = 0; i < size / 2; i++){
            int j = size - 1 - i;
            int a = list.get(i);
            int b = list.get(j);
            smallest = Math.min(smallest, Math.min(a, b));
        }

        return smallest;
    }

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
                    S.add(new nCorners(shape.getCenter(), diameter, val[3], SELECTED_PICKED, Entry.FILL_FALSE));
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
                case Shapus.UNSYMMETRICAL_N_CORNER:
                    ArrayList<Shapus> list = new ArrayList<>();
                    Shapus shapeunsShape = shape.increase(HOVER_DIAMETER);
                    shapeunsShape.setColor(SELECTED_PICKED);
                    shapeunsShape.setFill(false);
                    list.add(shapeunsShape);
                    dPanel.drawSelected(list);
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
        if (suppress[0]){
            suppress[0] = false;
            return;
        }
    	ArrayList<Shapus> Memo = dPanel.getMemory();
        for (int index : selectedIndexes){
            if (dPanel.getMemory(index).getKind() == Shapus.UNSYMMETRICAL_N_CORNER){
                Memo.get(index).scale(width);
            } else {
                Memo.get(index).setWidth(width);
            }
        }
        dPanel.clear();
        dPanel.updateMemory(Memo);
        dPanel.clearHover();
        createSelectedOutline(selectedIndexes);
    }

    @Override
    public void height(double height) {
    	if (!hasSelected) return;
        if (suppress[1]){
            suppress[1] = false;
            return;
        }
    	ArrayList<Shapus> Memo = dPanel.getMemory();
        for (int index : selectedIndexes){
        	Memo.get(index).setHeight(height);
        }
        dPanel.clear();
        dPanel.updateMemory(Memo);
        createSelectedOutline(selectedIndexes);
    }

    @Override
    public void corners(int corners) {
    	if (!hasSelected) return;
        if (suppress[2]){
            suppress[2] = false;
            return;
        }
    	ArrayList<Shapus> Memo = dPanel.getMemory();
        for (int index : selectedIndexes){
        	Memo.get(index).setCorner(corners);
        }
        dPanel.updateMemory(Memo);
        dPanel.clearHover();
        createSelectedOutline(selectedIndexes);
    }

    @Override
    public void color(Color color) {
        if (!hasSelected) return;
        if (suppress[3]){
            suppress[3] = false;
            return;
        }
        for (int index : selectedIndexes){
            dPanel.getMemory(index).setColor(color);
        }
        dPanel.clearHover();
        createSelectedOutline(selectedIndexes);
    }

    @Override
    public void fill(boolean fill) {
        if (!hasSelected) return;
        if (suppress[4]){
            suppress[4] = false;
            return;
        }
        for (int index : selectedIndexes){
            dPanel.getMemory(index).setFill(fill);
        }
        dPanel.clearHover();
        createSelectedOutline(selectedIndexes);
    }

    @Override
    public void switchAway() {
        if (!hasSelected) return;
        if (dPanel.getMemory(selectedIndex).getKind() != Shapus.UNSYMMETRICAL_N_CORNER) return;
        dPanel.getMemory(selectedIndex).setOriginalPoints(dPanel.getMemory(selectedIndex).getPoints());
    }
}
