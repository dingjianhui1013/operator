/**
 * 2012-5-17 上午5:08:04
 */
package com.itrus.ca.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: bin_liu
 */
public class ReflectHelper<T> {

	/**
	 * 为一个指定属性赋值
	 * 
	 * @param obj
	 * @param paramer
	 * @param value
	 * @return Object
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object setValue(Object obj, String paramer, Object value)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (value == null)
			return obj;
		String methodName = "set" + paramer.substring(0, 1).toUpperCase()
				+ paramer.substring(1);

		Method m = getMethod(obj.getClass(), methodName);

		if (m == null) {
			// 大写首字找不到找全小写
			methodName = "set" + paramer;
			m = getMethod(obj.getClass(), methodName);
			if (m == null)
				return obj;
		}

		// 类型不一样则转换,暂只支持基本类型数据
		value = convObj(value.getClass(), m.getParameterTypes()[0], value);
		m.invoke(obj, value);

		return obj;
	}

	/**
	 * 对象类型转换，暂支持基本类型数据
	 * 
	 * @param input
	 * @param output
	 * @return Object
	 */
	public static Object convObj(Class input, Class output, Object value) {
		String inputValueType = input.getName();
		String outputValueType = output.getName();

		if (inputValueType.equals(outputValueType)) {
			return value;
		}
		// 输出为String
		if (outputValueType.equals(String.class.getName())) {
			if (inputValueType.equals(Integer.class.getName())
					|| inputValueType.equals(Double.class.getName())
					|| inputValueType.equals(Float.class.getName())
					|| inputValueType.equals(Long.class.getName())
					|| inputValueType.equals(Boolean.class.getName())
					|| inputValueType.equals(Character.class.getName())
					|| inputValueType.equals(java.lang.Byte.class.getName())
					|| inputValueType.equals(java.lang.Short.class.getName())
					|| inputValueType.equals(java.lang.StringBuffer.class
							.getName())) {
				return value.toString();
			}
		} else if (outputValueType.equals(Integer.class.getName())) {
			if (inputValueType.equals(String.class.getName())) {
				return new Integer(value.toString());
			} else if (inputValueType.equals(Double.class.getName())) {
				return new Double(value.toString()).intValue();
			} else if (inputValueType.equals(Float.class.getName())) {
				return new Float(value.toString()).intValue();
			} else if (inputValueType.equals(Long.class.getName())) {
				return new Long(value.toString()).intValue();
			} else if (inputValueType.equals(Byte.class.getName())) {
				return new Byte(value.toString()).intValue();
			} else if (inputValueType.equals(Short.class.getName())) {
				return new Short(value.toString()).intValue();
			}

		}

		return null;

	}

	/**
	 * 获取所有get方法
	 * 
	 * @param obj
	 * @return List<Method>
	 */
	public static List<Method> getAllGetMethod(Object obj) {
		Method[] methods = obj.getClass().getMethods();
		List<Method> res = new ArrayList<Method>();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("get")) {
				res.add(methods[i]);
			}
		}
		return res;
	}

	/**
	 * 获取所有set方法
	 * 
	 * @param obj
	 * @return List<Method>
	 */
	public static List<Method> getAllSetMethod(Object obj) {
		Method[] methods = obj.getClass().getMethods();
		List<Method> res = new ArrayList<Method>();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("set")) {
				res.add(methods[i]);
			}
		}
		return res;
	}

	public static Object getValue(Object obj, String paramer)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		String methodName = "get" + paramer;

		Method m = getMethod(obj.getClass(), methodName);
		if (m == null)
			return null;
		return m.invoke(obj, null);
	}

	/**
	 * 得到一个类的指定名称函数
	 * 
	 * @param theClass
	 * @param name
	 * @return Method
	 */
	public static Method getMethod(Class theClass, String name) {
		Method[] methods = theClass.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equalsIgnoreCase(name)) {
				return methods[i];
			}
		}
		return null;
	}

	/**
	 * 指定类的成员函数是否是public
	 * 
	 * @param clazz
	 * @param member
	 * @return boolean
	 */
	public static boolean isPublic(Class clazz, Member member) {
		return Modifier.isPublic(member.getModifiers())
				&& Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * 是否使抽象类
	 * 
	 * @param clazz
	 * @return boolean
	 */
	public static boolean isAbstractClass(Class clazz) {
		int modifier = clazz.getModifiers();
		return Modifier.isAbstract(modifier) || Modifier.isInterface(modifier);
	}

	/**
	 * 得到默认的构造函数
	 * 
	 * @param clazz
	 * @return Constructor
	 * @throws NoSuchMethodException
	 */
	public static Constructor getDefaultConstructor(Class clazz)
			throws NoSuchMethodException {

		if (isAbstractClass(clazz))
			return null;

		Constructor constructor = clazz.getDeclaredConstructor();
		if (!isPublic(clazz, constructor)) {
			constructor.setAccessible(true);
		}
		return constructor;
	}

	public static String[] getAllvarName(Class clazz) {
		List<String> res = new ArrayList<String>();
		Field[] f = clazz.getDeclaredFields();
		for (Field e : f) {
			res.add(e.getName());
		}

		return (String[]) res.toArray(new String[0]);
	}

	/**
	 * 得到一个类里所有的变量
	 * 
	 * @param clazz
	 * @return List<Class>
	 * @throws ClassNotFoundException
	 */
	public static List<Class> getAllvarClass(Class clazz)
			throws ClassNotFoundException {
		Field[] f = clazz.getDeclaredFields();
		List<Class> res = new ArrayList<Class>();
		for (Field e : f) {
			String s = e.getGenericType().toString();
			s = s.substring(s.indexOf(" ")).trim();
			Class cls = Class.forName(s);
			res.add(cls);
		}
		return res;
	}

	public static <T> T getAnnotationByClass(Class objCls, Class varCls,
			Class annotationClass) throws ClassNotFoundException {
		Field[] f = objCls.getDeclaredFields();

		for (Field e : f) {
			String s = e.getGenericType().toString();
			s = s.substring(s.indexOf(" ")).trim();
			Class cls = Class.forName(s);
			if (cls.equals(varCls)) {
				return (T) e.getAnnotation(annotationClass);
			}
		}
		return null;
	}

	public static String makeToString(Object obj)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		StringBuffer sb = new StringBuffer();
		sb.append(obj.getClass().getName()).append("[");

		String[] var = getAllvarName(obj.getClass());
		for (String e : var) {
			sb.append("|").append(e).append(":")
					.append(getValue(obj, e) == null ? "" : getValue(obj, e));
		}

		sb.append("]");
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		// Gateway g = new Gateway();
		// g.setId(157);
		// g.setGatewayApiName("测试API");
		// Field[] f = g.getClass().getDeclaredFields();
		// for(Field e : f){
		// System.out.println(e.getName());
		// }
		//
		// System.out.println(makeToString(g));
	}
}
