package org.auto.io.scanner;

import org.auto.io.processor.ClassProcessor;

/**
 * @author huxh
 * */
public interface ClassScanner extends Scanner {

	String DEFAULT_CLASS_RESOURCE_PATTERN = "**/*.class";

	/**
	 * 添加处理器
	 * */
	void addProcessor(ClassProcessor classProcessor);

}
