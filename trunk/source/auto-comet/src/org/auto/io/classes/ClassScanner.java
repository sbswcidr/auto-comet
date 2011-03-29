package org.auto.io.classes;

import org.auto.io.Scanner;

/**
 * @author huxh
 * */
public interface ClassScanner extends Scanner {

	String DEFAULT_CLASS_RESOURCE_PATTERN = "**/*.class";

	/**
	 * 添加处理器
	 * */
	void addHandler(ClassHandler classHandler);

}
