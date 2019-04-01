package com.feiyu.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 * 
 * ClassName: ReflectUtils Date:2017年7月17日下午4:31:09
 *
 * @author nanshouxiao
 *
 */
public class ReflectUtils
{

	/**
	 * 获取类里的属性Field，包含父类
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		Class<?> tempClass = clazz;
		Field field = null;
		for (; tempClass != Object.class; tempClass = tempClass.getSuperclass()) {
			try {
				field = tempClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException | SecurityException e) {
			}
		}
		return field;
	}
	/**
	 * 获取类里的所有属性Field，包含父类
	 *
	 * @param clazz
	 * @return
	 */
	public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        Class<?> tempClass = clazz;
		for (; tempClass != Object.class; tempClass = tempClass.getSuperclass()) {
			try {
                list.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			} catch (SecurityException e) {
			    e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 检测某个属性是否存在
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static boolean checkFieldIsExist(Class<?> clazz, String fieldName) {
		if (Map.class.isAssignableFrom(clazz)) {
			return true;
		} else {
			return getField(clazz, fieldName) != null;
		}
	}

	/**
	 * 获取对象里的属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getValue(Object obj, String fieldName) {
		Object result = null;
		try {
			if ((obj instanceof Map)) {
				return ((Map<?, ?>) obj).get(fieldName);
			}
			Field field = getField(obj.getClass(), fieldName);
			if (field != null) {
				field.setAccessible(true);
				result = field.get(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获取对象里的属性值
	 *
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static <E> E getValue(Object obj, String fieldName, Class<E> clazz) {
		E result = null;
		try {
			if ((obj instanceof Map)) {
				return ((Map<?, E>) obj).get(fieldName);
			}
			Field field = getField(obj.getClass(), fieldName);
			if (field != null) {
				field.setAccessible(true);
				result = clazz.cast(field.get(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 通过反射给属性设置值
	 * @param obj
	 * @param fieldName
	 * @param fieldValue
	 */
	public static void setValue(Object obj, String fieldName, Object fieldValue) {
        if (fieldValue == null) {
            return;
        }
        try {
			if (obj instanceof Map) {
				((Map) obj).put(fieldName, fieldValue);
			} else {
				Field field = getField(obj.getClass(), fieldName);
				if (field != null && field.getType().isAssignableFrom(fieldValue.getClass())) {
					field.setAccessible(true);
					field.set(obj, fieldValue);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取对象里的属性值
	 *
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getValueByMethod(Object obj, String fieldName){
		Object result = null;
		String methodName = "get"+ Character.toUpperCase(fieldName.charAt(0))+ fieldName.substring(1);
		try
		{
			Method method = obj.getClass().getDeclaredMethod(methodName);
			result = method.invoke(obj);
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
