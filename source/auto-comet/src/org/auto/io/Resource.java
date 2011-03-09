package org.auto.io;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author XiaohangHu
 */
public interface Resource {

	InputStream getInputStream() throws IOException;

	/**
	 * Return a description for this resource, to be used for error output when
	 * working with the resource.
	 * <p>
	 * Implementations are also encouraged to return this value from their
	 * <code>toString</code> method.
	 *
	 * @see java.lang.Object#toString()
	 */
	String getDescription();
}
