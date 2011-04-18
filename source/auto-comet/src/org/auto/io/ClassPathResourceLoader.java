package org.auto.io;

import org.auto.io.scanner.ClassPathResourceScanner;
import org.auto.io.scanner.ResourcePatternScanner;

public class ClassPathResourceLoader implements ResourceLoader {

	protected Resource getClassPathResource(String location) {
		String classPathLocation = location
				.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length());
		return new ClassPathResource(classPathLocation);
	}

	public Resource getResource(String location) {
		return getClassPathResource(location);
	}

	@Override
	public Resource[] getResources(String locationPattern) {
		ResourcePatternScanner resourceScanner = new ClassPathResourceScanner();
		return null;
	}

}
