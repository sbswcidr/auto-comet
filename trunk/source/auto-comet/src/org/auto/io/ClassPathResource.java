package org.auto.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.auto.util.ClassUtils;

/**
 *
 * @author XiaohangHu
 */
public class ClassPathResource implements Resource {

	private String path;

	private ClassLoader classLoader;

	public ClassPathResource(String path) {
		this.path = path;
		this.classLoader = ClassUtils.getDefaultClassLoader();
	}

	public ClassPathResource(String path, ClassLoader classLoader) {
		this.path = path;
		if (null == classLoader) {
			this.classLoader = ClassUtils.getDefaultClassLoader();
		} else {
			this.classLoader = classLoader;
		}
	}

	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	public InputStream getInputStream() throws IOException {
		InputStream is = null;
		is = this.getClassLoader().getResourceAsStream(this.path);
		if (is == null) {
			throw new FileNotFoundException(getDescription()
					+ " cannot be opened because it does not exist");
		}
		return is;
	}

	/**
	 * This implementation returns a description that includes the class path
	 * location.
	 *
	 * 描述
	 */
	public String getDescription() {
		return "class path resource [" + this.path + "]";
	}

}