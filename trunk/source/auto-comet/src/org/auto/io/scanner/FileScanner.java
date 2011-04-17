package org.auto.io.scanner;

/**
 * @author huxh
 */
public interface FileScanner {

	void scan();

	void addHandler(FileHandler handler) throws FileScannerException;
}
