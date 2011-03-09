package org.auto.comet.web;

/**
 * @author XiaohangHu
 * */
public interface RequestParameter {

	/**
	 * 获取参数
	 * */
	String getParameter(String name);

	/**
	 * 获取参数
	 * */
	String[] getParameterValues(String name);

}
