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
			buffer.append(iterator.next());
		}
		while (iterator.hasNext()) {
			buffer.append(",");
			buffer.append(iterator.next());
		}
		buffer.append("]");
		return buffer.toString();
	}

}
