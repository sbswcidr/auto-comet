package org.auto.web.resource;

import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import org.auto.io.ClassPathResource;
import org.auto.io.DefaultResourceLoader;
import org.auto.io.FileResource;
import org.auto.io.Resource;
import org.auto.io.ResourceUtils;
import org.auto.io.scanner.ClassPathResourceScanner;
import org.auto.io.scanner.FileSystemResourceScanner;
import org.auto.io.scanner.ResourcePatternScannerManager;

/**
 *
 * @author XiaohangHu
 */
public class WebServerResourceLoader extends DefaultResourceLoader {

	private ServletContext servletContext;

	public WebServerResourceLoader(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	private Resource getServletContextResource(String location) {
		return new ServletContextResource(servletContext, location);
	}

	public Resource getResource(String location) {
		if (location.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
			return getClassPathResource(location);
		} else {
			try {
				return getUrlResource(location);
			} catch (MalformedURLException ex) {
				return getServletContextResource(location);
			}
		}

	}

	public Resource[] getResources(String protocolLocationPattern) {
		ResourcePatternScannerManager scannerManager = new ResourcePatternScannerManager();
		scannerManager.addScanner(ClassPathResource.RESOURCE_PROTOCOL_NAME,
				new ClassPathResourceScanner());
		scannerManager.addScanner(FileResource.RESOURCE_PROTOCOL_NAME,
				new FileSystemResourceScanner());
		scannerManager.addScanner(
				ServletContextResource.RESOURCE_PROTOCOL_NAME,
				new ServletContextResourceScanner(servletContext));
		// TODO..
		return null;
	}

}
