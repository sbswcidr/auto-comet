package org.auto.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author XiaohangHu
 * */
public class JsonObject extends HashMap<Object, Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = -1493269365377697547L;

	/**
	 * 转化为json格式字符串
	 * */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("{");
		Set<java.util.Map.Entry<Object, Object>> entrySet = this.entrySet();
		Iterator<java.util.Map.Entry<Object, Object>> iterator = entrySet
				.iterator();
		if (iterator.hasNext()) {
			appendEntrySet(buffer, iterator.next());
		}
		while (iterator.hasNext()) {
			buffer.append(",");
			appendEntrySet(buffer, iterator.next());
		}
		buffer.append("}");
		return buffer.toString();
	}

	private void appendEntrySet(StringBuffer buffer,
			java.util.Map.Entry<Object, Object> entry) {
		Object key = entry.getKey();
		Object value = entry.getValue();
		buffer.append("\"");
		buffer.append(key);
		buffer.append("\"");
		buffer.append(":");
		appendValue(buffer,value);
	}

	private void appendValue(StringBuffer buffer, Object value) {
		if (value instanceof String) {
			buffer.append("\"");
			buffer.append(value);
			buffer.append("\"");
		} else {
			buffer.append(value);
		}
	}

}
