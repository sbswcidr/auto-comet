package org.auto.comet.web;

import java.util.Collection;

import org.auto.comet.config.CometConfigMetadata;
import org.auto.comet.config.CometMetadata;
import org.auto.comet.web.controller.ObjectFactory;
import org.auto.comet.web.controller.SocketController;

/**
 * @author XiaohangHu
 * */
public class UrlHandlerMappingBuilder {

	private ObjectFactory objectFactory;

	public UrlHandlerMappingBuilder(ObjectFactory objectFactory) {
		if (null == objectFactory) {
			throw new IllegalArgumentException(
					"objectFactory must not be null!");
		}
		this.objectFactory = objectFactory;

	}

	public UrlHandlerMapping buildHandlerMapping(CometConfigMetadata cometConfig) {
		UrlHandlerMapping handlerMapping = new UrlHandlerMapping();
		Collection<CometMetadata> cometMetadatas = cometConfig
				.getCometMetadatas();
		addMapping(cometMetadatas, handlerMapping);
		return handlerMapping;
	}

	public void addMapping(Collection<CometMetadata> cometMetadatas,
			UrlHandlerMapping handlerMapping) {
		for (CometMetadata cometMetadata : cometMetadatas) {
			addMapping(cometMetadata, handlerMapping);
		}
	}

	public void addMapping(CometMetadata cometMetadata,
			UrlHandlerMapping handlerMapping) {
		String uri = cometMetadata.getRequest();
		String controllerName = cometMetadata.getController();
		Object obj = objectFactory.getObject(controllerName);
		if (obj instanceof SocketController) {
			handlerMapping.putHandler(uri, (SocketController) obj);
		} else {
			throw new IllegalStateException("controller must implements ["
					+ SocketController.class.getName() + "]");
		}
	}
}
