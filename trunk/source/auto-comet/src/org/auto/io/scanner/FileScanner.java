package org.auto.io.scanner;

/**
 * @author XiaohangHu
 */
public interface FileScanner {

	void scan();

	void addHandler(FileHandler handler) throws ScannerException;
}
