package org.auto.io.scanner;

import java.io.File;

/**
 * @author XiaohangHu
 */
public interface FilePatternScanner {

	/**
	 * 扫描一个目录下的所有文件
	 *
	 * @param rootDir要扫描的根目录
	 *
	 * */
	void scan(File rootDir);

	/**
	 * 扫描一个目录下的所有文件
	 *
	 * @param rootDir要扫描的根目录
	 *
	 * @param pattern匹配模式
	 *
	 * */
	void scan(File rootDir, String pattern);

	/**
	 * 扫描指定路径匹配模式下的所有文件
	 *
	 * @param locationPattern路径匹配模式
	 *
	 * */
	void scan(String locationPattern);

	/**
	 * 添加处理器
	 * */
	void addHandler(FileHandler handler) throws ScannerException;
}
