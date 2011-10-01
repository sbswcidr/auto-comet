package org.auto.comet.example.chat.web;

import org.auto.comet.example.chat.web.util.NumberDateFormat;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: xiaohanghu Date: 11-8-2 Time: ����10:33 To
 * change this template use File | Settings | File Templates.
 */
public class CustomerBinding implements WebBindingInitializer {
	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		DateFormat dateFormat = new NumberDateFormat();
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
		binder.registerCustomEditor(java.sql.Date.class, new CustomDateEditor(
				dateFormat, false));
	}

}
