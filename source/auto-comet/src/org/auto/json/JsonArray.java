package org.auto.json;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author XiaohangHu
 * */
public class JsonArray extends ArrayList<Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("[");

		Iterator<Object> iterator = this.iterator();
		if (iterator.hasNext()) {
			appendValue(buffer, iterator.next());
		}
		while (iterator.hasNext()) {
			buffer.append(",");
			appendValue(buffer, iterator.next());
		}
		buffer.append("]");
		return buffer.toString();
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
