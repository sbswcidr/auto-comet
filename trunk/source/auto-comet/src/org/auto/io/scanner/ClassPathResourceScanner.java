package org.auto.io.scanner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.auto.io.ResourceUtils;
import org.auto.io.scanner.classFile.ClassFileScanner;
import org.auto.io.scanner.classFile.ClassHandler;
import org.auto.io.scanner.classFile.ClassJarScanner;
import org.auto.util.AntPathMatcher;
import org.auto.util.PathMatcher;
import org.springframework.util.ClassUtils;

/**
 * 文件扫描器
 *
 * @author huxh
 * */
public class ClassPathResourceScanner extends PatternFileScanner {

	public ClassPathResourceScanner(File rootDir) {
		super(rootDir);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 扫描ClassPath
	 * */
	public void scanClassPath(String location) {

		Enumeration<URL> urlEnumeration = gerClassPathResources(location);
		while (urlEnumeration.hasMoreElements()) {
			URL url = (URL) urlEnumeration.nextElement();
			if (ResourceUtils.isJarURL(url)) {
				try {
					scanJarUrl(url);
				} catch (IOException e) {
					throw new FileScannerException("IOException ["
							+ url.getPath() + "]!", e);
				}
			} else {
				try {
					File file = new File(url.toURI());
					handle(file);
				} catch (URISyntaxException e) {
					throw new FileScannerException("URISyntaxException [" + url
							+ "]", e);
				}
			}
		}

	}

	protected void scanJarUrl(URL url) throws IOException {
		URLConnection con = url.openConnection();
		JarFile jarFile = null;
		String rootEntryPath = "";
		if (con instanceof JarURLConnection) {
			JarURLConnection jarCon = (JarURLConnection) con;
			jarCon.setUseCaches(false);
			jarFile = jarCon.getJarFile();
			JarEntry jarEntry = jarCon.getJarEntry();
			if (jarEntry != null)
				rootEntryPath = jarEntry.getName();
			scanJarFile(jarFile, rootEntryPath);
		} else {
			try {
				String urlFilePath = url.getPath();
				int separatorIndex = urlFilePath
						.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
				if (separatorIndex != -1) {
					String jarFileUrl = urlFilePath
							.substring(0, separatorIndex);
					rootEntryPath = urlFilePath.substring(separatorIndex
							+ ResourceUtils.JAR_URL_SEPARATOR.length());
					jarFile = getJarFile(jarFileUrl);
				} else {
					jarFile = new JarFile(urlFilePath);
				}
				scanJarFile(jarFile, rootEntryPath);
			} finally {
				if (null != jarFile)
					jarFile.close();
			}
		}

	}

	private void scanJarFile(JarFile jarFile, String rootEntryPath) throws IOException {
		 Enumeration<JarEntry> entries = jarFile.entries();
	        while (entries.hasMoreElements()) {
	            JarEntry entry = entries.nextElement();
	            String entryPath = entry.getName();
	            if (matchPattern(entryPath)) {
	                InputStream inputStream = jarFile.getInputStream(entry);
	                try {
	                } finally {
	                    try {
	                        if (inputStream != null) {
	                            inputStream.close();
	                        }
	                    } catch (IOException ioe) {
	                        // ignore
	                    }
	                }
	            }
	        }
	}

	protected JarFile getJarFile(String jarFileUrl) throws IOException {
		return ResourceUtils.getJarFile(jarFileUrl);
	}

	private ClassLoader getClassLoader() {
		return ClassUtils.getDefaultClassLoader();
	}

	private Enumeration<URL> gerClassPathResources(String location) {
		Enumeration<URL> resourceUrls = null;
		try {
			resourceUrls = getClassLoader().getResources(location);
		} catch (IOException e1) {
			throw new FileScannerException("IOException get resources ["
					+ location + "] from classLoader!", e1);
		}
		return resourceUrls;
	}

}
