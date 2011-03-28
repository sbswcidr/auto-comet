package org.auto.io.scanner;

import org.auto.io.processor.FileProcessor;

/**
 * @author huxh
 */
public interface FileScanner extends Scanner {

	void addProcessor(FileProcessor processor);
}
