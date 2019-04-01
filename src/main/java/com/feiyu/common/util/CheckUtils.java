package com.feiyu.common.util;

import java.util.Collection;

/**
 * 验证工具类
 *
 * @author feiyu127@gmail.com
 * @date 2018-09-14 14:14
 */
public class CheckUtils {
    /**
     * 建议集合是否为null或空
     *
     * @param collection 集合
     * @return
     */
    public static final boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 建议集合是否不为null或空
     *
     * @param collection 集合
     * @return
     */
    public static final boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断对象是否为空，如果是字符串的话，空字符串也算空
     *
     * @param str 判断的对象
     * @return
     */
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * 判断对象是否不为空或字符串
     *
     * @param str 判断的对象
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }
}
