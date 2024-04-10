package Sulfur;

import java.awt.*;

public class Entry {

    public static int BELONGING_START = 0;
    public static int BELONGING_MIDDLE = 1;
    public static int BELONGING_END = 2;
    public static int BELONGING_NONE = -1;
    public static int FILL_TRUE = 1;
    public static int FILL_FALSE = 0;

    private Color color;
    private int id;
    private int key;
    private double[] values;
    private int belongingStatus;
    private int fill;

    public Entry(int id, int key, double[] values, Color c, int belongingStatus, int fill){
        if (belongingStatus > 2 || belongingStatus < -1){
            belongingStatus = -1;
        }
        if (fill < 0 || fill > 1){
            fill = 0;
        }
        this.color = c;
        this.belongingStatus = belongingStatus;
        this.fill = fill;
        this.id = id;
        this.key = key;
        this.values = values;
        this.color = c;
    }

    public int getFill(){return fill;}
    public int getBelongingStatus(){return belongingStatus;}
    public int getKey(){return key;}
    public double[] getValues(){return values;}
    public int getId(){return id;}
    public Color getColor(){return color;}
}
