package org.auto.comet.web;

import java.util.Map;

/**
 * @author XiaohangHu
 * */
public class DefaultRequestParameter implements RequestParameter {
	Map<String, String[]> parameterMap;

	DefaultRequestParameter(Map<String, String[]> parameterMap) {
		this.parameterMap = parameterMap;
	}

	@Override
	public String getParameter(String name) {
		String[] values = (String[]) parameterMap.get(name);
		if (values != null && values.length > 0) {
			return values[0];
		}
		return null;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = (String[]) parameterMap.get(name);
		if (values != null) {
			return values;
		}
		return null;
	}

}
