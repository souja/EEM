package com.souja.lib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MDateUtils {


    /**
     * 获取当前日期：yyyy-MM-dd HH:mm
     *
     * @param offset 日期偏移量。正数，日期往后推；反之。
     */
    public static String getStringDate(int offset) {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (offset != 0) {
            calendar.add(Calendar.DATE, offset);// 正数往后推,负数往前移动
            date = calendar.getTime(); // 日期偏移后的结果
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }


    public static long stringToDateLong(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date != null ? date.getTime() : 0;
    }


    /**
     * 计算两个时间戳相差的天数
     */
    public static int calcDate(long stamp1, long stamp2) {
        long t1 = ((stamp1 / 1000) + 28800) / 86400;
        long t2 = ((stamp2 / 1000) + 28800) / 86400;
        return (int) Math.abs(t1 - t2);
    }


    /**
     * 当前日期
     *
     * @return yyyy-MM-dd HH:mm
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(new Date());
    }

    /**
     * 当前日期
     *
     * @return yyyyMMdd
     */
    public static String getCurrentDate1() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(new Date());
    }

    /**
     * 当前日期
     *
     * @return yyyyMMddHHmmss
     */
    public static String getCurrentDate2() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date());
    }

    /**
     * 当前日期
     *
     * @return HH:mm:ss
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Date());
    }


    /**
     * parse String yyyy-MM-dd to Date
     */
    public static Date stringToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String longToStringDate1(long timemiles) {
        if (timemiles <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date(timemiles);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * yyyy-MM
     */
    public static String longToStringDate2(long timemiles) {
        if (timemiles <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date dt = new Date(timemiles);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * MM-dd HH:mm
     */
    public static String longToStringDate4(long timemiles) {
        if (timemiles <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        Date dt = new Date(timemiles);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * MM.dd
     */
    public static String longToStringDate5(long timemiles) {
        if (timemiles <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        Date dt = new Date(timemiles);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * MM-dd
     */
    public static String longToStringDate6(long timemiles) {
        if (timemiles <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Date dt = new Date(timemiles);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * @return yyyy.MM.dd
     */
    public static String longToStringDate7(long timemiles) {
        if (timemiles <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Date dt = new Date(timemiles);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * @return yyyy.MM.dd HH:mm
     */
    public static String longToStringDate8(long timemiles) {
        if (timemiles <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date dt = new Date(timemiles);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * @return yyyy-mm-dd
     */
    public static String dateToString(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = date;
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * @return yyyy-mm-dd HH:mm:ss
     */
    public static String dateToString2(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = date;
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }


}
