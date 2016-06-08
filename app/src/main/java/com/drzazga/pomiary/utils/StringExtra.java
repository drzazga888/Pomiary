package com.drzazga.pomiary.utils;

public class StringExtra {

    public static String simplify(String str) {
        String trimmed = str.trim();
        if (!trimmed.isEmpty())
            return trimmed;
        return null;
    }
}
