package org.auto.io.scanner;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.auto.io.FileResource;
import org.auto.io.JarEntryResource;
import org.auto.io.Resource;
import org.auto.io.ResourceUtils;
import org.auto.util.AntPathMatcher;
import org.auto.util.PathMatcher;
import org.springframework.util.ClassUtils;

/**
 * 文件扫描器
 *
 * @author huxh
 * */
public class ClassPathResourceScanner implements ResourceScanner {

	private String location;

	private String rootDirPath;

	private String subPattern;

	private PathMatcher pathMatcher = new AntPathMatcher();

	private List<ResourceHandler> handlers = new LinkedList<ResourceHandler>();

	public ClassPathResourceScanner(String location) {
		this.location = location;
		rootDirPath = determineRootDir(location);
		subPattern = location.substring(rootDirPath.length());
	}

	public String getLocation() {
		return location;
	}

	protected String determineRootDir(String location) {
		int prefixEnd = location.indexOf(":") + 1;
		int rootDirEnd = location.length();
		while (rootDirEnd > prefixEnd
				&& pathMatcher.isPattern(location.substring(prefixEnd,
						rootDirEnd))) {
			rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
		}
		if (rootDirEnd == 0) {
			rootDirEnd = prefixEnd;
		}
		return location.substring(0, rootDirEnd);
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
					throw new ScannerException("IOException ["
							+ url.getPath() + "]!", e);
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
					handle(resource);
				}
			}
		}
	}

	private ClassLoader getClassLoader() {
		return ClassUtils.getDefaultClassLoader();
	}

	private Enumeration<URL> gerClassPathResources(String location) {
		Enumeration<URL> resourceUrls = null;
		try {
			resourceUrls = getClassLoader().getResources(location);
		} catch (IOException e1) {
			throw new ScannerException("IOException get resources ["
					+ location + "] from classLoader!", e1);
		}
		return resourceUrls;
	}

	protected void handleResource(Resource resource) {
		for (ResourceHandler handler : handlers) {
			handler.handle(resource);
		}
	}

	protected void handle(Resource resource) {
		handleResource(resource);
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	@Override
	public void addHandler(ResourceHandler handler) {
		this.handlers.add(handler);
	}

}
