package org.auto.comet.example.chat.web.view;

import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 */
public class JsonSerializer implements ResourceSerializer {

    public String serialization(Serializable resource) {
        if (null == resource)
            return null;
        JSONObject obj = JSONObject.fromObject(resource);
        return obj.toString();
    }

}
