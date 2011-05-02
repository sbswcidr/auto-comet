package org.auto.comet.example.chat.test;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

public class GenericTest {
	public List<String> list = new LinkedList<String>();

	public static void main(String[] args) throws SecurityException,
			NoSuchFieldException {
		ParameterizedType pt = (ParameterizedType) Person.class.getField(
				"persons").getGenericType();
		System.out.println(pt.getActualTypeArguments().length);
		System.out.println(pt.getActualTypeArguments()[0]);
	}
}
