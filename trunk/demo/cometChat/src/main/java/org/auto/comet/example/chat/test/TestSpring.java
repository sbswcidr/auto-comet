package org.auto.comet.example.chat.test;

import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author XiaohangHu
 * */
public class TestSpring {

	private Resource[] mappingLocations;

	public void setMappingLocations(Resource[] mappingLocations) {
		System.out.println("###############");
		this.mappingLocations = mappingLocations;
	}

}
