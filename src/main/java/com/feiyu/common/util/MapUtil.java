package com.feiyu.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nanshouxiao on 2017/9/5 9:53.
 */
public class MapUtil {

    /**
     * 从map里获取某个key的值，如果为null，则将key的值设为value并返回
     *
     * @param map 需要获取的对象
     * @param key 要获取的key
     * @param value 如果获取不到设置并返回的默认值
     * @return
     */
    public static <K, V> V getOrDefaultAndPutIfNull(Map<K, V> map, K key, V value) {
        V v = map.get(key);
        if (v == null) {
            map.put(key, value);
            return value;
        }
        return v;
    }

    /**
     * 从map里获取某个key的值，如果为null，则将key的值设为value并返回
     *
     * @param map 需要获取的对象
     * @param key 要获取的key
     * @param value 如果获取不到返回的默认值
     * @return
     */
    public static <K, V> V getOrDefaultIfNull(Map<K, V> map, K key, V value) {
        V v = map.get(key);
        if (v == null) {
            return value;
        }
        return v;
    }

    /**
     * 将 集合 中的两个字段组装成map
     *
     * @param collection 需要转的 集合
     * @param keyName map的key在 集合 中对象的键名称
     * @param valueName map的value在 集合 中对象的键名称
     * @param keyType key的类型
     * @param valueType value的类型
     * @return Map
     */
    public static <K, V> Map<K, V> convertListToMap(Collection<?> collection, String keyName, String valueName,
                                                    Class<K> keyType, Class<V> valueType) {

        if (keyName == null || valueName == null || keyType == null || valueType == null) {
            return null;
        }
        Map<K, V> resultMap = new HashMap<>();
        if (collection != null && collection.size() > 0) {
            for (Object object : collection) {
                K key = keyType.cast(ReflectUtils.getValue(object, keyName));
                V value = valueType.cast(ReflectUtils.getValue(object, valueName));
                resultMap.put(key, value);
            }
        }
        return resultMap;
    }

    /**
     * map的key value进行转换，如果存在重复则会直接替换掉
     * @param map 原始map
     * @param <K> key的类型
     * @param <V> value的类型
     * @return
     */
    public static <K, V> Map<V, K> exchangeKeyAndValue(Map<K, V> map) {
        Map<V, K> resultMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            resultMap.put(entry.getValue(), entry.getKey());
        }
        return resultMap;
    }
}
