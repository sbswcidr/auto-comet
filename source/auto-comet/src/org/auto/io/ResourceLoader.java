package org.auto.io;

import java.io.IOException;

import org.springframework.core.io.Resource;

/**
 *
 * @author XiaohangHu
 */
public interface ResourceLoader {

	/**
	 * @param location
	 *            the resource location
	 * @return a corresponding Resource handle
	 */
	Resource getResource(String location);

	/**
	 * @param locationPattern
	 *            the location pattern to resolve
	 * @return the corresponding Resource objects
	 */
	Resource[] getResources(String locationPattern);

}
