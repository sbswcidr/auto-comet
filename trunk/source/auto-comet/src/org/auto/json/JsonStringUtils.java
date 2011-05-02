package org.auto.json;

/**
 *
 * @author XiaohangHu
 * */
class JsonStringUtils {

	static String removeBeginAndEnd(String value) {
		return value.substring(1, value.length() - 1);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	static boolean isStringValue(String value) {
		return value.startsWith(JsonProtocol.STRING_BEGIN);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	static boolean isObjectValue(String value) {
		return value.startsWith(JsonProtocol.OBJECT_BEGIN);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	static boolean isCharValue(String value) {
		return value.startsWith(JsonProtocol.CHAR_BEGIN);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	static boolean isArrayValue(String value) {
		return value.startsWith(JsonProtocol.ARRAY_BEGIN);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	static boolean isNullValue(String value) {
		return JsonProtocol.NULL.equals(value);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	static boolean isTrueValue(String value) {
		return JsonProtocol.TRUE.equals(value);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	static boolean isFalseValue(String value) {
		return JsonProtocol.FALSE.equals(value);
	}

	// suppress default constructor for noninstantiability
	private JsonStringUtils() {
		throw new AssertionError();
	}

}
