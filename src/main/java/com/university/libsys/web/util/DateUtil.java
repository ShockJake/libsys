package com.university.libsys.web.util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private final static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public static String format(@NotNull Long timestamp) {
        return formatter.format(new Date(timestamp));
    }
}
