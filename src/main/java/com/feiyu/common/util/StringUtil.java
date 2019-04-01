package com.feiyu.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @Author feiyu127@gmail.com
 * @Date 2017-09-30 10:42
 */
public class StringUtil {

    /**
     * 截取字符串到固定长度，长度过长后缀添加fill
     *
     * @param str    要截取的字符串
     * @param length 保留的长度
     * @param fill   后缀
     * @return
     */
    public static String substringToLength(String str, int length, String fill) {
        if (str == null || str.length() <= length) {
            return str;
        }
        if (fill == null) {
            fill = "...";
        }
        return str.substring(0, length - fill.length()) + fill;
    }

    /**
     * 截取字符串到固定长度，如果过长截取后后缀补...
     *
     * @param str    要截取的字符串
     * @param length 保留的长度
     * @return
     */
    public static String substringToLength(String str, int length) {
        return substringToLength(str, length, null);
    }

    /**
     * 阿拉伯数字转中文，只支持int型数字
     *
     * @param number
     * @return
     */
    public static String numberToChinese(int number) {
        String[] numbers = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] units = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十"};
        String sign = number < 0 ? "负" : "";
        if (number < 0) {
            number = -number;
        }
        StringBuilder result = new StringBuilder(sign);
        String string = String.valueOf(number);
        int n = string.length();
        char[] numberCharArray = string.toCharArray();
        for (int i = 0; i < n; i++) {
            int digNum = n - i; // 位数
            int num = numberCharArray[i] - '0';
            if (num != 0) {
                result.append(numbers[num]).append(units[digNum - 1]);
                continue;
            }

            if (result.toString().endsWith(numbers[0])) {
                // 如果是单位所在的位数，则去除上一个0，加上单位
                if (digNum % 4 == 1) {
                    result.deleteCharAt(result.length() - 1);
                    result.append(units[digNum - 1]);
                }
            } else {
                result.append(numbers[0]);
            }
        }
        return result.toString();
    }

    /**
     * 将集合连接
     * @param collection
     * @param prefix
     * @param suffix
     * @param separator
     * @return
     */
    public static String join(Collection collection, String prefix, String suffix, String separator) {
        if (collection == null) {
            return null;
        }
        if (collection.isEmpty()) {
            return "";
        }
        StringBuilder sbr = new StringBuilder();
        for (Object o : collection) {
            if (o == null) {
                break;
            }
            sbr.append(separator).append(prefix).append(o.toString()).append(suffix);
        }
        return sbr.substring(separator.length());
    }

    /**
     * 判断以 separator 分隔的字符串 strs 是否完全包含 item
     * @param strs
     * @param separator
     * @param item
     * @return
     */
    public static boolean completelyContains(String strs, String separator, String item) {
        if (strs == null || item == null || separator == null) {
            return false;
        }
        long containNum = Arrays.stream(strs.split(separator)).filter(str -> Objects.equals(str, item)).count();
        return containNum > 0;
    }

    /**
     * 判断以 "," 分隔的字符串 strs 是否完全包含 item
     * @param strs
     * @param item
     * @return
     */
    public static boolean completelyContains(String strs, String item) {
        return completelyContains(strs, ",", item);
    }

    /**
     *
     * 用0格式化数字
     *
     * @param prefix 前缀，可不加
     * @param number 需要格式化的数字
     * @param count 格式化的位数
     * @return
     */
    public static String formatNumberWithZero(String prefix, int number, int count)
    {
        String format = "%0" + count + "d";
        return (prefix != null ? prefix : "") + String.format(format, number);
    }

    public static void main(String[] args) {
    }

}
