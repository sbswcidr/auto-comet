package org.auto.io.scanner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.auto.util.AntPathMatcher;
import org.auto.util.PathMatcher;

/**
 * 文件扫描器
 *
 * @author huxh
 * */
public class PatternFileScanner implements FileScanner {

	private File rootDir;

	private List<FileHandler> fileHandlers = new LinkedList<FileHandler>();

	private PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 默认扫描路径下的所有文件
	 * */
	private String pattern = "**/*";

	private String fullPattern;

	/**
	 * @param rootDir
	 *            要扫描的目录或者文件
	 * */
	public PatternFileScanner(File rootDir) {
		this.rootDir = rootDir;
		this.fullPattern = this.getFullPattern();
	}

	private String getFullPattern() {

		if (rootDir.isDirectory()) {
			String fullPattern = getReallPath(rootDir);
			if (!pattern.startsWith("/")) {
				fullPattern += "/";
			}
			fullPattern = fullPattern + this.getReallPath(pattern);
			return fullPattern;
		}

		return this.getReallPath(pattern);
	}

	/**
	 * 扫描一个目录
	 * */
	protected void retrieveMatchingFiles() {
		if (rootDir.isDirectory()) {
			doRetrieveMatchingFiles(this.rootDir);
		} else {
			this.handle(this.rootDir);
		}
	}

	/**
	 * 查找所有匹配的文件
	 * */
	protected void doRetrieveMatchingFiles(File dir) {
		File[] dirContents = dir.listFiles();
		if (dirContents == null) {
			throw new RuntimeException(
					"Could not retrieve contents of directory ["
							+ dir.getAbsolutePath() + "]");
		}
		for (int i = 0; i < dirContents.length; i++) {
			File contentFile = dirContents[i];
			String currPath = getReallPath(contentFile);
			if (contentFile.isDirectory() && matchStart(currPath + "/")) {
				doRetrieveMatchingFiles(contentFile);
			} else {
				if (matchPattern(currPath)) {
					this.handle(contentFile);
				}
			}
		}
	}

	protected boolean matchStart(String path) {
		if (null == fullPattern)
			return true;
		return getPathMatcher().matchStart(this.fullPattern, path);
	}

	/**
	 * 匹配路径
	 * */
	protected boolean matchPattern(String path) {
		if (null == fullPattern)
			return true;
		return getPathMatcher().match(this.fullPattern, path);
	}

	/**
	 * 屏蔽系统差异，获得"/"分割的路径。
	 * */
	protected String getReallPath(File file) {
		return getReallPath(file.getAbsolutePath());
	}

	/**
	 * 屏蔽系统差异，获得"/"分割的路径。
	 * */
	protected String getReallPath(String path) {
		return path.replace(File.separator, "/");
	}

	public void scan() {
		this.retrieveMatchingFiles();
	}

	protected void handle(File file) {
		List<FileHandler> fileHandlers = this.getHandlers();
		for (FileHandler fileHandler : fileHandlers) {
			fileHandler.handle(file);
		}
	}

	public String getPattern() {
		return pattern;
	}

	/**
	 * 设置匹配模式
	 * */
	public void setPattern(String pattern) {
		this.pattern = pattern;
		this.fullPattern = this.getFullPattern();
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

}
