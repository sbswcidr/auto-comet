package org.auto.comet.example.chat.test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.auto.json.JsonArray;
import org.auto.json.JsonDeserializer;
import org.auto.json.JsonObject;
import org.auto.json.JsonSerializer;

public class Test {

	/**
	 * @param args
	 * @throws NoSuchMethodException
	 */
	public static void main(String[] args) throws Exception {


		// getsdfdsf(p, "persons");
		// testJavaBean();
		testJson();
	}

	private static Class getsdfdsf(Object bean, String propertyName) {
		PropertyDescriptor ptor;
		try {
			ptor = PropertyUtils.getPropertyDescriptor(bean, propertyName);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		Method method = ptor.getWriteMethod();
		Type[] gptypes = method.getGenericParameterTypes();
		if (null != gptypes && gptypes.length == 1) {
			Type type = method.getGenericParameterTypes()[0];// 方法的第一个参数
			if (type instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Type[] types = parameterizedType.getActualTypeArguments();
				System.out.println("  TypeArgument: ");
				for (Type t : types) {
					System.out.println("   " + t);
				}

			}

		}
		return null;
	}

	public static void testJavaBean() throws NoSuchMethodException,
			IllegalAccessException, Exception {

		Object po = new Person();

		System.out.println(JSONObject.fromObject(po));

		Class poClass = po.getClass();

		// Introspector相当beans这个架构的一个入口。类似于Hibernate的SessionFactory//
		// 通过bean的类型获得bean的描述—》获得属性描述的集合
		BeanInfo bi;
		System.out.println(PropertyUtils.describe(po));
		try {
			bi = Introspector.getBeanInfo(poClass);
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();

			for (int i = 0; i < pds.length; i++) {
				PropertyDescriptor pd = pds[i];
				Method method = pd.getReadMethod();
				try {

					System.out.println(pd.getName());
					System.out.println(method.invoke(po));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void testJson() {
		JsonSerializer jsonSerializer = new JsonSerializer();

		JsonArray array = new JsonArray();
		Person person = new Person();
		person.setAge(23);
		Person person1 = new Person();
		person1.setName("haha");
		person.setPerson(person1);

		array.add(person);
		array.add(4);
		array.add("this is a String");
		array.add('c');
		array.add(true);
		array.add(null);
		JsonObject jsonObject = new JsonObject();
		jsonObject.put("name", "hehe");
		jsonObject.put("sdf", new JsonObject());
		jsonObject.put("khiyiu", null);
		array.add(jsonObject);
		array.add(new JsonObject());

		String jsonString = jsonSerializer.toJsonString(person);

		JsonDeserializer deserializer = new JsonDeserializer();
		Person dperson = (Person) deserializer.toObject(jsonString,
				Person.class);

		System.out.println(jsonString);
		System.out.println(jsonSerializer.toJsonString(dperson));
	}
}
