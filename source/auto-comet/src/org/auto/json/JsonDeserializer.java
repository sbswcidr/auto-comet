package org.auto.json;

import java.util.Map;

/**
 * JsontDerializer
 *
 * jsongString to Object
 *
 * @author XiaohangHu
 * */
public class JsonDeserializer {

	/**
	 * jsonString to javaBean
	 *
	 * @param jsonString
	 * @param beanClass
	 *
	 * @return javaBean
	 *
	 * */
	public <T> T toJavaBean(String jsonString, Class<T> beanClass) {
		// TODO ...
		return null;
	}

	/**
	 * jsonString to javaBean
	 *
	 * @param jsonString
	 * @param beanClass
	 *
	 * @return javaBean
	 *
	 * */
	public <T> T toJavaBean(String jsonString) {
		if (null == jsonString) {
			return null;
		}
		jsonString = jsonString.trim();
		if ("".equals(jsonString) || JsonProtocol.NULL.equals(jsonString)) {
			return null;
		}
		if (jsonString.startsWith(JsonProtocol.OBJECT_BEGIN)
				&& jsonString.endsWith(JsonProtocol.OBJECT_END)) {
		}
		if (jsonString.startsWith(JsonProtocol.ARRAY_BEGIN)
				&& jsonString.endsWith(JsonProtocol.ARRAY_END)) {
		}
		// TODO ...
		return null;
	}

	/**
	 * @param jsonString
	 *            must starts with "{", and extends with "}"
	 * */
	private JsonObject toJsonObject(String jsonString) {
		JsonObject object = new JsonObject();
		jsonString = jsonString.substring(1, jsonString.length() - 1);
		String[] pairs = jsonString
				.split(JsonProtocol.OBJECT_MEMBERS_SEPARATOR);
		for (String pair : pairs) {
			putPari(pair, object);
		}
		return object;
	}

	/**
	 * @param pair
	 *            must not be bull. eg: "name":"XiaohangHu"
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void putPari(String pair, Object object) {
		pair = pair.trim();
		int separatorInext = -1;
		String name = null;
		if (pair.startsWith(JsonProtocol.STRING_BEGIN)) {
			int endOfNameInext = pair.indexOf(JsonProtocol.STRING_END, 1);
			if (endOfNameInext == -1) {
				throw new IllegalArgumentException("Illegal pair [" + pair
						+ "]. Pair must be a String!");
			}
			name = pair.substring(1, endOfNameInext);
			separatorInext = pair.indexOf(JsonProtocol.PAIR_SEPARATOR,
					endOfNameInext);
		} else {
			separatorInext = pair.indexOf(JsonProtocol.PAIR_SEPARATOR);
			name = pair.substring(0, separatorInext);
			name = name.trim();
		}
		if (separatorInext == -1) {
			throw new IllegalArgumentException("Illegal pair [" + pair
					+ "]. Pair must like [string: value]");
		}
		String valueString = pair.substring(separatorInext + 1, pair.length());

		Object value = getValue(valueString);
		if (object instanceof Map) {// Map or JsonObject
			Map map = (Map) object;
			map.put(name, value);
			return;
		}
	}

	/**
	 * @param jsonString
	 *            must starts with "[", and extends with "]"
	 * */
	private JsonArray toJsonArray(String jsonString) {
		JsonArray object = new JsonArray();
		jsonString = jsonString.substring(1, jsonString.length() - 1);
		String[] elements = jsonString
				.split(JsonProtocol.ARRAY_ELEMENTS_SEPARATOR);
		for (String element : elements) {
			Object value = getArrayValue(element);
			object.add(value);
		}
		return object;
	}

	/**
	 * @param value
	 *            special array value: "" == null
	 *
	 * */
	private Object getArrayValue(String value) {
		value = value.trim();
		if ("".equals(value)) {
			return null;
		}
		return getValue(value);
	}

	private Object getValue(String value, Class type) {
		value = value.trim();
		return null;
	}

	/**
	 * value ::= string | number | object | array | true | false | null
	 *
	 * @param value
	 *            value must not be null
	 * */
	private Object getValue(String value) {

		value = value.trim();

		if (value.startsWith(JsonProtocol.STRING_BEGIN)) {// string
			value = value.substring(1, value.length() - 1);
			return value;
		}
		if (value.startsWith(JsonProtocol.OBJECT_BEGIN)) {// object
			return toJsonObject(value);
		}
		if (value.startsWith(JsonProtocol.ARRAY_BEGIN)) {// array
			return toJsonArray(value);
		}
		if (JsonProtocol.NULL.equals(value)) {// null
			return null;
		}
		if (JsonProtocol.TRUE.equals(value)) {// true
			return Boolean.TRUE;
		}
		if (JsonProtocol.FALSE.equals(value)) {// false
			return Boolean.FALSE;
		}
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Illegal json value [" + value
					+ "]", e);
		}
	}

}
