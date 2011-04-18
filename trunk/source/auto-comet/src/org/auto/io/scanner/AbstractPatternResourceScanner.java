package org.auto.io.scanner;

import java.util.LinkedList;
import java.util.List;

import org.auto.io.Resource;
import org.auto.io.ResourcePathUtils;
import org.auto.util.AntPathMatcher;
import org.auto.util.PathMatcher;

/**
 *
 * @author XiaohangHu
 * */
public abstract class AbstractPatternResourceScanner implements ResourceScanner {

	private List<ResourceHandler> handlers = new LinkedList<ResourceHandler>();

	private PathMatcher pathMatcher = new AntPathMatcher();

	protected String determineRootDir(String locationPattern) {
		return ResourcePathUtils.getRootDir(locationPattern, pathMatcher);
	}

	protected void handleResource(Resource resource) {
		for (ResourceHandler handler : getHandlers()) {
			handler.handle(resource);
		}
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	@Override
	public void addHandler(ResourceHandler handler) {
		this.handlers.add(handler);
	}

	public List<ResourceHandler> getHandlers() {
		return handlers;
	}

}
