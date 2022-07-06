package com.magonote.kintone;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helper class
 */
public class Helper {
    public static ZonedDateTime getZonedDateTime(String date){
         ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return  zonedDateTime;
    }
}
