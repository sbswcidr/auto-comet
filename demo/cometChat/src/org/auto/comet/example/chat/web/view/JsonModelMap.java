package org.auto.comet.example.chat.web.view;

import java.util.Map;

import org.springframework.ui.ModelMap;

/**
 * User: xiaohanghu Date: 11-7-23 To change this template use File | Settings |
 * File Templates.
 */
public class JsonModelMap extends ModelMap {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2376338755708554083L;

	/**
	 * Copy all attributes in the supplied <code>Map</code> into this
	 * <code>Map</code>, with existing objects of the same name taking
	 * precedence (i.e. not getting replaced).
	 */
	public ModelMap mergeAttributes(@SuppressWarnings("rawtypes") Map attributes) {
		// ignore
		return this;
	}
}
