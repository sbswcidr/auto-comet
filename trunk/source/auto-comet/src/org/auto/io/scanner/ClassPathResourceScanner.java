package org.auto.io.scanner;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.auto.io.FileResource;
import org.auto.io.JarEntryResource;
import org.auto.io.Resource;
import org.auto.io.ResourceUtils;
import org.springframework.util.ClassUtils;

/**
 * 资源扫描器
 *
 * @author huxh
 * */
public class ClassPathResourceScanner extends AbstractPatternResourceScanner {

	private String locationPattern;

	private String rootDirPath;

	private String subPattern;

	public ClassPathResourceScanner(String locationPattern) {
		this.locationPattern = locationPattern;
		rootDirPath = determineRootDir(locationPattern);
		subPattern = locationPattern.substring(rootDirPath.length());
	}

	public String getLocationPattern() {
		return locationPattern;
	}

	/**
	 * 扫描ClassPath
	 * */
	public void scan() {
		Enumeration<URL> urlEnumeration = gerClassPathResources(rootDirPath);
		while (urlEnumeration.hasMoreElements()) {
			URL url = (URL) urlEnumeration.nextElement();
			if (ResourceUtils.isJarURL(url)) {
				try {
					scanJarUrl(url);
				} catch (IOException e) {
					throw new ScannerException("IOException [" + url.getPath()
							+ "]!", e);
				}
			} else {
				try {
					File file = new File(url.toURI());
					PatternFileScanner fileScanner = new PatternFileScanner(
							file);
					fileScanner.setPattern(subPattern);
					fileScanner.addHandler(new FileHandler() {
						@Override
						public void handle(File file) {
							Resource resource = new FileResource(file);
							handleResource(resource);
						}
					});
					fileScanner.scan();
				} catch (URISyntaxException e) {
					throw new ScannerException("URISyntaxException [" + url
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
					jarFile = ResourceUtils.getJarFile(jarFileUrl);
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

	private void scanJarFile(JarFile jarFile, String rootEntryPath) {

		if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
			// Root entry path must end with slash to allow for proper matching.
			// The Sun JRE does not return a slash here, but BEA JRockit does.
			rootEntryPath = rootEntryPath + "/";
		}

		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryPath = entry.getName();
			if (entryPath.startsWith(rootEntryPath)) {
				String relativePath = entryPath.substring(rootEntryPath
						.length());
				if (getPathMatcher().match(subPattern, relativePath)) {
					Resource resource = new JarEntryResource(jarFile, entry);
					handleResource(resource);
				}
			}
		}
	}

	private Enumeration<URL> gerClassPathResources(String location) {
		Enumeration<URL> resourceUrls = null;
		try {
			resourceUrls = getClassLoader().getResources(location);
		} catch (IOException e1) {
			throw new ScannerException("IOException get resources [" + location
					+ "] from classLoader!", e1);
		}
		return resourceUrls;
	}

	private ClassLoader getClassLoader() {
		return ClassUtils.getDefaultClassLoader();
	}
}
