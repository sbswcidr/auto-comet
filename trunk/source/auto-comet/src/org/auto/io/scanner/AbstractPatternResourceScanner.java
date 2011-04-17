package org.auto.io.scanner;

import java.util.LinkedList;
import java.util.List;

import org.auto.io.Resource;
import org.auto.util.AntPathMatcher;
import org.auto.util.PathMatcher;

/**
 *
 * @author huxh
 * */
public abstract class AbstractPatternResourceScanner implements ResourceScanner {

	private List<ResourceHandler> handlers = new LinkedList<ResourceHandler>();

	private PathMatcher pathMatcher = new AntPathMatcher();

	protected String determineRootDir(String location) {
		int prefixEnd = location.indexOf(":") + 1;
		int rootDirEnd = location.length();
		while (rootDirEnd > prefixEnd
				&& pathMatcher.isPattern(location.substring(prefixEnd,
						rootDirEnd))) {
			rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
		}
		if (rootDirEnd == 0) {
			rootDirEnd = prefixEnd;
		}
		return location.substring(0, rootDirEnd);
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
