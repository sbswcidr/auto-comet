package org.auto.comet.example.chat.web.view;

import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: xiaohanghu
 * Date: 11-7-23
 * Time: ����6:24
 * To change this template use File | Settings | File Templates.
 */
public class JsonModelAndView extends ModelAndView {

    public static final String DEFAULT_VIEW_NAME = "jsonView";

    public static final String JSON_DATA_ATTRIBUTE_NAME = "_jsonData";

    {
        super.setViewName(DEFAULT_VIEW_NAME);
    }

    public JsonModelAndView() {
        super();
    }

    public JsonModelAndView(Object jsonData) {
        if (jsonData != null) {
            getModelMap().addAttribute(JSON_DATA_ATTRIBUTE_NAME, jsonData);
        }
    }

}
