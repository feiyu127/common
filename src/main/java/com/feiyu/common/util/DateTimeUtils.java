package com.feiyu.common.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;

/**
 * 作者：feiyu127
 * 创建时间： 2018/08/30
 * email：feiyu127@gmail.com
 */
public class DateTimeUtils
{
    /**
     * 获取当前毫秒数
     * @return
     */
    public static long getCurrentMilliSeconds(){
        return System.currentTimeMillis();
    }

    /**************** JDK8 提供的方法 *************************/

    /**
     * 获取 yyyy-MM-dd HH:mm:ss 格式的当前时间
     * @return
     */
    public static String getNowDateTime() {
        return formatDateTime(Instant.now());
    }

    /**
     * 获取 yyyy-MM-dd 格式的当前日期
     * @return
     */
    public static String getNowDate() {
        return formatDate(Instant.now());
    }

    /**
     * 获取 指定 格式的当前时间
     *
     * @param formatPattern 格式
     * @return
     */
    public static String getNow(String formatPattern) {
        return formatDate(Instant.now(), formatPattern);
    }
    /**
     * 格式化毫秒数的时间格式 yyyy-MM-dd
     * @param milliseconds
     * @return
     */
    public static String formatMilliDate(Long milliseconds) {
        return formatMilli(milliseconds, "yyyy-MM-dd");
    }
    /**
     * 格式化毫秒数的时间格式 yyyy-MM-dd HH:mm:ss
     * @param milliseconds
     * @return
     */
    public static String formatMilliDateTime(Long milliseconds) {
        return formatMilli(milliseconds, "yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 格式化毫秒数的时间格式
     * @param milliseconds
     * @param pattern
     * @return
     */
    public static String formatMilli(Long milliseconds, String pattern) {
        if (milliseconds == null) {
            return null;
        }
        return formatDate(Instant.ofEpochMilli(milliseconds), pattern);
    }

    /**
     * 格式化成时间格式 yyyy-MM-dd HH:mm:ss
     * @param temporalAccessor
     * @return
     */
    public static String formatDateTime(TemporalAccessor temporalAccessor) {
        return formatDate(temporalAccessor, "yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 格式化成日期格式
     * @param temporalAccessor
     * @return
     */
    public static String formatDate(TemporalAccessor temporalAccessor) {
        return formatDate(temporalAccessor, "yyyy-MM-dd");
    }
    /**
     * 格式化 jdk8 提供的时间类格式
     * @param temporalAccessor
     * @param pattern
     * @return
     */
    public static String formatDate(TemporalAccessor temporalAccessor, String pattern) {
        if (temporalAccessor == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(temporalAccessor);
    }

    /**
     * 获取当前年
     * @return
     */
    public static int getLocalYear() {
        return LocalDate.now().getYear();
    }

    /**
     * 获取当前月
     * @return
     */
    public static int getLocalMonth() {
        return LocalDate.now().getMonthValue();
    }

    /**
     * 获取当前日
     * @return
     */
    public static int getLocalDay() {
        return LocalDate.now().getDayOfMonth();
    }

    /**
     * 获取当前秒
     * @return
     */
    public static int getSecond(){
        return LocalTime.now().getSecond();
    }

    public static void main(String[] args) {
    }
}
