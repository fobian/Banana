package com.chijsh.banana;

import java.util.List;

/**
 * Created by chijsh on 10/28/14.
 */
public class Utility {

    public static String[] strToArray(String source) {
        return source.split(",");
    }

    public static String arrayToStr(List<String> list) {
        int size = list.size();
        String str = "";
        for (int i = 0; i < size; ++i) {
            str += ",";
            str += list.get(i);
        }
        return str;
    }
}
