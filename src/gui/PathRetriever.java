package gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PathRetriever {
    private static final String path = "paths.properties";

    public static final int N_ECK_EXPLAIN = 0;
    public static final int THETA_CAL = 1;
    public static final int ROTATE_CAL = 2;
    public static final int RADIAN_CAL = 3;
    public static final int NORMAL_VEC_CAL = 4;
    public static final int CENTER_CAL = 5;

    private static final String[] keys = new String[]{
            "nEckExplain",
            "ThetaCal",
            "RotateCal",
            "RadianCal",
            "NormalVecCal",
            "CenterCal"
    };

    public static String getPath(int keyConstant){
        if (keyConstant < 0 || keyConstant >= keys.length) throw new IllegalArgumentException("Invalid key constant");
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String key = keys[keyConstant];
        return properties.getProperty(key);
    }
}
