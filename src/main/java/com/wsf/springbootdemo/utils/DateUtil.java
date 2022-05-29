package com.wsf.springbootdemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wsfstart
 * @create 2022-05-29 22:12
 */
public class DateUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public static Date StringtoDate(String date){
        Date parse=null;
        try {
            parse = format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

}
