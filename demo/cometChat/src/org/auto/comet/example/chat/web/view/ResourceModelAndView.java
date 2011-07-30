package org.auto.comet.example.chat.web.view;

import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: xiaohanghu
 * Date: 11-7-23
 * To change this template use File | Settings | File Templates.
 */
public class ResourceModelAndView extends ModelAndView {

    public static final String DEFAULT_VIEW_NAME = "resourceView";

    public static final String RESOURCE_DATA_ATTRIBUTE_NAME = "_resourceData";

    {
        super.setViewName(DEFAULT_VIEW_NAME);
    }

    public ResourceModelAndView() {
        super();
    }

    public ResourceModelAndView(Serializable resource) {
        if (resource != null) {
            getModelMap().addAttribute(RESOURCE_DATA_ATTRIBUTE_NAME, resource);
        }
    }

}
