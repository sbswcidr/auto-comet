package org.auto.json;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * JsonSerializer
 *
 * object to jsonString
 *
 * @author XiaohangHu
 * */
public class JsonSerializer {

	/**
	 * javaBeans to jsonString
	 *
	 * @param javaBeans
	 *
	 * @return jsonString
	 *
	 * */
	@SuppressWarnings("rawtypes")
	public String toJsonString(Collection javaBeans) {
		if (null == javaBeans) {
			return JsonProtocol.NULL;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(JsonProtocol.ARRAY_BEGIN);
		Iterator iterator = javaBeans.iterator();
		if (iterator.hasNext()) {
			String elementString = toJsonString(iterator.next());
			builder.append(elementString);
		}
		while (iterator.hasNext()) {
			builder.append(JsonProtocol.ARRAY_ELEMENTS_SEPARATOR);
			String elementString = toJsonString(iterator.next());
			builder.append(elementString);
		}

		builder.append(JsonProtocol.ARRAY_END);

		return builder.toString();
	}

	/**
	 * Object to jsonString
	 *
	 * @param object
	 *
	 * @return jsonString
	 *
	 * */
	@SuppressWarnings("rawtypes")
	public String toJsonString(Object object) {
		if (null == object) {
			return JsonProtocol.NULL;
		}
		if (object instanceof Map) {
			return toJsonString((Map) object);
		}
		if (object instanceof String) {
			return toJsonString((String) object);
		}
		if (object instanceof Boolean) {
			return toJsonString((Boolean) object);
		}
		if (object instanceof Number) {
			return toJsonString((Number) object);
		}
		if (object instanceof Character) {
			return toJsonString((Character) object);
		}

		return javaBeanToJsontString(object);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	private String toJsonString(String value) {
		StringBuilder builder = new StringBuilder();
		appendValue(builder, value);
		return builder.toString();
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	private String toJsonString(Boolean value) {
		return value.toString();
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	private String toJsonString(Number value) {
		return value.toString();
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	private String toJsonString(Character value) {
		StringBuilder builder = new StringBuilder();
		appendValue(builder, value);
		return builder.toString();
	}

	/**
	 * @param javaBean
	 *            must not be null
	 * */
	@SuppressWarnings("rawtypes")
	private String javaBeanToJsontString(Object javaBean) {
		try {
			Map map = PropertyUtils.describe(javaBean);
			map.remove("class");
			return toJsonString(map);
		} catch (Exception e) {
			throw new IllegalArgumentException("object ["
					+ javaBean.getClass().getName() + ":" + javaBean
					+ "] can't be described!");
		}
	}

	/**
	 * javaBean to jsonString
	 *
	 * @param map
	 *            map like a javaBean
	 *
	 * @return jsonString
	 *
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String toJsonString(Map map) {
		if (null == map) {
			return JsonProtocol.NULL;
		}
		StringBuilder builder = new StringBuilder(JsonProtocol.OBJECT_BEGIN);
		Set<java.util.Map.Entry> entrySet = map.entrySet();
		Iterator<java.util.Map.Entry> iterator = entrySet.iterator();
		if (iterator.hasNext()) {
			appendEntrySet(builder, iterator.next());
		}
		while (iterator.hasNext()) {
			builder.append(JsonProtocol.OBJECT_MEMBERS_SEPARATOR);
			appendEntrySet(builder, iterator.next());
		}
		builder.append(JsonProtocol.OBJECT_END);
		return builder.toString();
	}

	/**
	 * jsonObject to jsonString
	 *
	 * @param jsonObject
	 *
	 * @return jsonString
	 *
	 * */
	@SuppressWarnings("rawtypes")
	public String toJsonString(JsonObject jsonObject) {
		return toJsonString((Map) jsonObject);
	}

	/**
	 * jsonArray to jsonString
	 *
	 * @param jsonArray
	 *
	 * @return jsonString
	 *
	 * */
	public String toJsonString(JsonArray jsonArray) {
		return toJsonString((Collection<?>) jsonArray);
	}

	@SuppressWarnings("rawtypes")
	private void appendEntrySet(StringBuilder builder, java.util.Map.Entry entry) {
		Object key = entry.getKey();
		appendValue(builder, key);
		builder.append(JsonProtocol.PAIR_SEPARATOR);
		appendValue(builder, entry.getValue());
	}

	private void appendValue(StringBuilder builder, Object value) {
		if (null == value) {
			builder.append(JsonProtocol.NULL);
			return;
		}
		if (value instanceof String) {
			appendValue(builder, (String) value);
			return;
		}
		if (value instanceof Boolean) {
			builder.append(value);
			return;
		}
		if (value instanceof Number) {
			builder.append(value);
			return;
		}
		if (value instanceof Character) {
			appendValue(builder, (Character) value);
			return;
		}
		builder.append(toJsonString(value));
		return;

	}

	/**
	 * @param value
	 *            must not be null
	 * */
	private void appendValue(StringBuilder builder, String value) {
		builder.append(JsonProtocol.STRING_BEGIN);
		builder.append(value);
		builder.append(JsonProtocol.STRING_END);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	private void appendValue(StringBuilder builder, Character value) {
		builder.append(JsonProtocol.CHAR_BEGIN);
		builder.append(value);
		builder.append(JsonProtocol.CHAR_END);
	}

}
