package org.auto.io.scanner;

/**
 *
 * @author XiaohangHu
 * */
public interface ResourceScanner {

	void scan();

	void addHandler(ResourceHandler handler);

}
