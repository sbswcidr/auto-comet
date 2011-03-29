package org.auto.io.file;

import org.auto.io.Scanner;

/**
 * @author huxh
 */
public interface FileScanner extends Scanner {

	void addHandler(FileHandler handler);
}
