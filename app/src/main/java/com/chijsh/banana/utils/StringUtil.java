package com.chijsh.banana.utils;

import com.chijsh.banana.model.PicUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chijsh on 11/11/14.
 */
public class StringUtil {

    /** Splits a String based on a single character, which is usually faster than regex-based String.split(). */
    public static String[] fastSplit(String string, char delimiter) {
        List<String> list = new ArrayList<String>();
        int size = string.length();
        int start = 0;
        for (int i = 0; i < size; i++) {
            if (string.charAt(i) == delimiter) {
                if (start < i) {
                    list.add(string.substring(start, i));
                } else {
                    list.add("");
                }
                start = i + 1;
            } else if (i == size - 1) {
                list.add(string.substring(start, size));
            }
        }
        String[] elements = new String[list.size()];
        list.toArray(elements);
        return elements;
    }

    public static String urlsToString(List<PicUrl> urls) {
        String s = "";
        for (int i = 0; i < urls.size(); ++i) {
            s += urls.get(i).toString();
            if(i != urls.size() - 1) {
                s += ",";
            }
        }
        return s;
    }
}
