package com.chijsh.banana;

/**
 * Created by chijsh on 10/28/14.
 */
public class Utility {

    public static String[] strToArray(String source) {
        return source.split(",");
    }

    public static String arrayToStr(String[] list) {
        int size = list.length;
        String str = "";
        for (int i = 0; i < size; ++i) {
            str += ",";
            str += list[i];
        }
        return str;
    }
}
