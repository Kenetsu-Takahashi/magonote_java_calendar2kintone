package com.magonote.kintone;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helper class
 */
public class Helper {
    public static ZonedDateTime getZonedDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, formatter);
        return  zonedDateTime;
    }
}
