package org.auto.io.scanner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.auto.io.ResourcePathUtils;
import org.auto.util.AntPathMatcher;
import org.auto.util.PathMatcher;

/**
 * 文件扫描器
 *
 * @author XiaohangHu
 * */
public class DefaultFilePatternScanner implements FilePatternScanner {

	private List<FileHandler> fileHandlers = new LinkedList<FileHandler>();

	private PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 默认扫描路径下的所有文件
	 * */
	private static final String DEFAULT_PATTERN = "**/*";

	protected String determineRootDir(String locationPattern) {
		return ResourcePathUtils.getRootDir(locationPattern, pathMatcher);
	}

	private String getFullPattern(File rootDir, String pattern) {

		if (rootDir.isDirectory()) {
			String fullPattern = ResourcePathUtils.getReallPath(rootDir);
			if (!pattern.startsWith("/")) {
				fullPattern += "/";
			}
			fullPattern = fullPattern + ResourcePathUtils.getReallPath(pattern);
			return fullPattern;
		}

		return ResourcePathUtils.getReallPath(pattern);
	}

	/**
	 * 扫描一个目录
	 * */
	protected void retrieveMatchingFiles(File rootDir, String fullPattern) {
		if (rootDir.isDirectory()) {
			doRetrieveMatchingFiles(rootDir, fullPattern);
		} else {
			this.handle(rootDir);
		}
	}

	/**
	 * 查找所有匹配的文件
	 * */
	protected void doRetrieveMatchingFiles(File dir, String fullPattern) {
		File[] dirContents = dir.listFiles();
		if (dirContents == null) {
			throw new RuntimeException(
					"Could not retrieve contents of directory ["
							+ dir.getAbsolutePath() + "]");
		}
		for (int i = 0; i < dirContents.length; i++) {
			File contentFile = dirContents[i];
			String currPath = ResourcePathUtils.getReallPath(contentFile);
			if (contentFile.isDirectory()
					&& matchStart(currPath + "/", fullPattern)) {
				doRetrieveMatchingFiles(contentFile, fullPattern);
			} else {
				if (matchPattern(currPath, fullPattern)) {
					this.handle(contentFile);
				}
			}
		}
	}

	protected boolean matchStart(String path, String fullPattern) {
		if (null == fullPattern)
			return true;
		return getPathMatcher().matchStart(fullPattern, path);
	}

	/**
	 * 匹配路径
	 * */
	protected boolean matchPattern(String path, String fullPattern) {
		if (null == fullPattern)
			return true;
		return getPathMatcher().match(fullPattern, path);
	}

	protected void handle(File file) {
		List<FileHandler> fileHandlers = this.getHandlers();
		for (FileHandler fileHandler : fileHandlers) {
			fileHandler.handle(file);
		}
	}

	public List<FileHandler> getHandlers() {
		return fileHandlers;
	}

	public void addHandler(FileHandler handler) {
		this.fileHandlers.add(handler);
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	@Override
	public void scan(File rootDir) {
		String fullPattern = this.getFullPattern(rootDir, DEFAULT_PATTERN);
		retrieveMatchingFiles(rootDir, fullPattern);
	}

	@Override
	public void scan(File rootDir, String pattern) {
		String fullPattern = this.getFullPattern(rootDir, pattern);
		retrieveMatchingFiles(rootDir, fullPattern);
	}

	@Override
	public void scan(String locationPattern) {
		locationPattern = ResourcePathUtils.getReallPath(locationPattern);
		String rootDirPath = determineRootDir(locationPattern);
		File rootDir = new File(rootDirPath);
		retrieveMatchingFiles(rootDir, locationPattern);
	}

}
