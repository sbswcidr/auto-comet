package org.auto.comet.example.chat.test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GenericTest {
	public List<String> list = new LinkedList<String>();

	public static void main(String[] args) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		ParameterizedType pt = (ParameterizedType) Person.class.getField(
				"persons").getGenericType();
		// System.out.println(pt.getActualTypeArguments().length);
		// System.out.println(pt.getActualTypeArguments()[0]);
		Class clazz;
		Pojo pojo = new Pojo();
		pojo.setName("1");
		String name;
		int j = 0;
		long s = System.currentTimeMillis();
		for (int i = 0; i < 1000000000; i++) {
			// j++;
			//name = pojo.getName();
		}
	
		
		//System.out.println(j);

		// 通过Field获取
		Field field[] = pojo.getClass().getDeclaredFields();
		Field f=field[0];
		for (int i = 0; i < 1000000; i++) {
			name = (String)f.get(pojo);
		}
		long e = System.currentTimeMillis();
		System.out.println(e - s);
		/*
		 * Map<String, String> pojo1 = new HashMap<String, String>();
		 * pojo1.put("name", "1"); long s1 = System.currentTimeMillis(); for
		 * (int i = 0; i < 1000000000; i++) { name = pojo1.get("name"); } long
		 * e1 = System.currentTimeMillis(); System.out.println(e1 - s1);
		 */
	}

}

class Pojo {
	String age = "age";
	String age1 = "age1";
	String age2 = "age2";
	String age3 = "age3";
	String age4 = "age4";
	String age5 = "age5";
	String age6 = "age6";
	String age7 = "age7";
	String age8 = "age8";
	String age9 = "age9";
	String age0 = "age0";
	String name = "name";
	String name1 = "name1";
	String name2 = "name2";
	String name3 = "name3";
	String name4 = "name4";
	String name5 = "name5";
	int i = 0;

	public String getName() {

		i++;
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}