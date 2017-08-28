package org.tmall.utils;

import java.sql.Timestamp;
import java.util.Date;

/**
 *  日期工具类，用于java.util.Date类
 *  与java.sql.Timestamp类互相转换
 */
public class DateUtil {

    // Date 转 Timestamp
    public static Timestamp d2t(Date date){
        if (null == date)
            return null;
        return new Timestamp(date.getTime());
    }

    // Ti么stamp 转 Date
    public static Date t2d(Timestamp timestamp){
        if (timestamp == null)
            return null;
        return new Date(timestamp.getTime());
    }
}
