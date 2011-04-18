package org.auto.io;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.auto.util.PathMatcher;

/**
 * @author XiaohangHu
 * */
public abstract class ResourcePathUtils {

	/**
	 * 屏蔽系统差异，获得"/"分割的路径。
	 * */
	public static String getReallPath(File file) {
		return getReallPath(file.getAbsolutePath());
	}

	/**
	 * 屏蔽系统差异，获得"/"分割的路径。
	 * */
	public static String getReallPath(String path) {
		return path.replace(File.separator, "/");
	}

	/**
	 * 获得一个匹配模式的根路径
	 * */
	public static String getRootDir(String locationPattern,
			PathMatcher pathMatcher) {
		int prefixEnd = locationPattern.indexOf(":") + 1;
		int rootDirEnd = locationPattern.length();
		while (rootDirEnd > prefixEnd
				&& pathMatcher.isPattern(locationPattern.substring(prefixEnd,
						rootDirEnd))) {
			rootDirEnd = locationPattern.lastIndexOf('/', rootDirEnd - 2) + 1;
		}
		if (rootDirEnd == 0) {
			rootDirEnd = prefixEnd;
		}
		return locationPattern.substring(0, rootDirEnd);
	}

	/**
	 * Return whether the given resource location is a URL: either a special
	 * "classpath" pseudo URL or a standard URL.
	 *
	 * @param resourceLocation
	 *            the location String to check
	 * @return whether the location qualifies as a URL
	 * @see #CLASSPATH_URL_PREFIX
	 * @see java.net.URL
	 */
	public static boolean isUrl(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		if (resourceLocation.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
			return true;
		}
		try {
			new URL(resourceLocation);
			return true;
		} catch (MalformedURLException ex) {
			return false;
		}
	}

	/**
	 * 获得协议名称
	 *
	 * */
	public static String getProtocol(String locationPattern) {
		int prefixEnd = locationPattern.indexOf(":");
		if (prefixEnd < 1) {
			return null;
		}
		return locationPattern.substring(0, prefixEnd);
	}

}
