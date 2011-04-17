package org.auto.io.scanner;

import org.auto.io.Scanner;

/**
 * @author huxh
 */
public interface FileScanner extends Scanner {

	void addHandler(FileHandler handler) throws FileScannerException;
}
