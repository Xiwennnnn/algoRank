package com.algo.crawler;

import com.algo.util.HttpRequester;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class BaseCrawler {

    protected String get(String url) throws IOException {
        try {
            return HttpRequester.get(url);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Get Request failed: " + url);
        }
    }

    protected String post(String url, String data, Map<String, String> headers) throws IOException {
        try {
            return HttpRequester.post(url, data, headers);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Post Request failed: " + url);
        }
    }

    protected String subStart(String str, String mid) {
        int index = str.indexOf(mid);
        if (index == -1) {
            return null;
        }

        return str.substring(0, index);
    }

    protected String subMid(String str, String start, String end) {
        int startIndex = str.indexOf(start);
        int endIndex = str.indexOf(end, startIndex);

        if (startIndex != -1 && endIndex != -1) {
            return str.substring(startIndex + start.length(), endIndex);
        }

        return null;
    }

    protected String subEnd(String str, String mid) {
        int index = str.lastIndexOf(mid);
        if (index == -1) {
            return null;
        }

        return str.substring(index + mid.length());
    }

    protected Date parseDate(String time, String pattern) {
        String timeZone = TimeZone.getDefault().getID();
        return parseDate(time, pattern, timeZone);
    }

    protected Date parseDate(String time, String pattern, String timeZone) {
        SimpleDateFormat format = new SimpleDateFormat("", Locale.CHINA);
        format.applyPattern(pattern);
        format.setLenient(false);
        format.setTimeZone(TimeZone.getTimeZone(timeZone));

        return format.parse(time, new ParsePosition(0));
    }
}
