package Sulfur;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SulfurManager {
    public static final int VOID_ERROR = -1;
    public static final int VOID_SUCCESS = 0;
    private File file;
    public SulfurManager(File file){
        this.file = file;
    }
    public SulfurManager(String path){
        this.file = new File(path);
    }

    private static String tillChar(String str, String chr){
        return str.substring(0,str.indexOf(chr));
    }
    private static String fromChar(String str, String chr){
        return str.substring(str.indexOf(chr)+1);
    }

    public String read(){
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String add;
            while ((add = reader.readLine()) != null){
                text.append(add+"\n");
            }

            return text.toString();

        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String[] readLines(){
        String text = read();
        String[] lines = text.split("\n");
        return lines;
    }

    public Entry[] getData(){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String version = reader.readLine();
            if (version == null) return null;
            if (version.equals("v1")){
                return v1getData();
            }

        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private Entry[] v1getData(){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            int skip = 1;
            String loop;
            ArrayList<Entry> list = new ArrayList<>();

            while ((loop = reader.readLine()) != null){
                if (skip <= 2) {
                    skip++;
                    continue;
                }

                int id = Integer.parseInt(tillChar(loop, ":"));
                loop = fromChar(loop, "|");
                int belongingStatus = Integer.parseInt(tillChar(loop, "|"));
                loop = fromChar(loop, "[");
                int fill = Integer.parseInt(tillChar(loop, "]"));
                loop = fromChar(loop, ":");
                int kind = Integer.parseInt(tillChar(loop, ":"));
                loop = fromChar(loop, ":");
                ArrayList<Double> data = new ArrayList<>();
                for (String split : loop.split(",")){
                    data.add(Double.parseDouble(split));
                }
                double[] arr = new double[data.size()];
                for (int i = 0; i < arr.length; i++){
                    arr[i] = data.get(i);
                }

                list.add(new Entry(id, kind,  arr, belongingStatus, fill));

            }
            Entry[] arr = new Entry[list.size()];

            for (int i = 0; i < arr.length; i++) arr[i] = list.get(i);

            return arr;

        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public Entry v1getData(int id){
        Entry[] data = v1getData();
        if (data == null || id >= data.length || id < 0) return null;
        return data[id];
    }

    public static Entry v1translateData(String data){
        int id = Integer.parseInt(tillChar(data, ":"));
        data = fromChar(data, "|");
        int belongingStatus = Integer.parseInt(tillChar(data, "|"));
        data = fromChar(data, "[");
        int fill = Integer.parseInt(tillChar(data, "]"));
        data = fromChar(data, ":");
        int kind = Integer.parseInt(tillChar(data, ":"));
        data = fromChar(data, ":");
        ArrayList<Double> values = new ArrayList<>();
        for (String split : data.split(",")){
            values.add(Double.parseDouble(split));
        }
        double[] arr = new double[values.size()];
        for (int i = 0; i < arr.length; i++){
            arr[i] = values.get(i);
        }
        return new Entry(id, kind, arr, belongingStatus, fill);
    }


    public int v1addData(int kind, double[] data, int fill, int belongingStatus){
        String[] old = readLines();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for(String str : old){
                writer.write(str);
                writer.newLine();
            }
            int id;
            if (!old[old.length-1].contains(":")){
                id = 0;
            } else {
                id = Integer.parseInt(tillChar(old[old.length-1], ":"))+1;
            }

            if (belongingStatus != Entry.BELONGING_END && belongingStatus != Entry.BELONGING_MIDDLE
                    && belongingStatus != Entry.BELONGING_START && belongingStatus != Entry.BELONGING_NONE)
                belongingStatus = Entry.BELONGING_NONE;

            if (fill != Entry.FILL_FALSE && fill != Entry.FILL_TRUE) fill = Entry.FILL_FALSE;


            writer.write(id+":"+"|"+belongingStatus+"|"+":"+"["+fill+"]"+":"+kind+":");

            for (int i = 0; i < data.length; i++){
                if (i == data.length-1){
                    writer.write(String.valueOf(data[i]));
                    continue;
                }
                writer.write(String.valueOf(data[i])+",");
            }
            writer.flush();
            return VOID_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return VOID_ERROR;
    }

    public Dimension v1getResolution(){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String res = reader.readLine();
            res = reader.readLine();

            int width = Integer.parseInt(tillChar(res, "x"));
            int height = Integer.parseInt(fromChar(res, "x"));

            return new Dimension(width, height);

        } catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

    public int v1removeData(int id){
        String[] old = readLines();
        Entry  entry = v1getData(id);
        if (entry == null) return VOID_ERROR;
        ArrayList<String> updated = new ArrayList<>();
        int belongingStatus = entry.getBelongingStatus();
        boolean reverse = belongingStatus != Entry.BELONGING_NONE;
        boolean deleteBelonging = reverse && belongingStatus != Entry.BELONGING_END;
        for (String line : old){
            if (!line.contains(":")){
                updated.add(line);
                continue;
            }
            int curr_id = Integer.parseInt(tillChar(line, ":"));
            if (curr_id == id) continue;
            if (curr_id < id) updated.add(line);
            if (curr_id > id){
                if (deleteBelonging){
                    deleteBelonging = v1getData(curr_id).getBelongingStatus() != Entry.BELONGING_END;
                    continue;
                }
                line = (curr_id - 1) + ":" + fromChar(line, ":");
                updated.add(line);

            }
        }

        if (reverse){
            boolean foundEnd = false;
            for (int i = updated.size() - 1 ; i > 1 ; i --){
                int curr_id = Integer.parseInt(tillChar(updated.get(i), ":"));
                Entry data = v1translateData(updated.get(i));
                int curr_belongingStatus = data.getBelongingStatus();
                if (curr_belongingStatus == Entry.BELONGING_END) foundEnd = true;
                if (foundEnd){
                    if (curr_belongingStatus == Entry.BELONGING_START) foundEnd = false;

                } else {
                    if (curr_belongingStatus == Entry.BELONGING_MIDDLE) updated.remove(i);
                    if (curr_belongingStatus == Entry.BELONGING_START) updated.remove(i);
                }
            }
            for (int i = 2; i < updated.size(); i ++){
                String line = updated.get(i);
                line = fromChar(line, ":");
                line = (i - 2) + ":" + line;
                updated.remove(i);
                updated.add(i, line);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for(String str : updated){
                writer.write(str);
                writer.newLine();
            }
            writer.flush();
            return VOID_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return VOID_ERROR;
    }
}

/*
*
* Sulfur.s layout:
* 1:v1
* 2:widthxheight
* id:|belongingStatus|:[fill]:kind:data
* 4:|-1|:[0]:6:54.5,1.2,45.5
*
*
*
*
* */