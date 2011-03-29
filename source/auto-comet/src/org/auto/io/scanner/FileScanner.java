package org.auto.io.scanner;

import org.auto.io.handler.FileHandler;

/**
 * @author huxh
 */
public interface FileScanner extends Scanner {

	void addProcessor(FileHandler processor);
}
