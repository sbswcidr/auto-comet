package org.auto.comet.example.chat.web.view;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xiaohanghu
 * Date: 11-8-1
 * <p/>
 */
public class NumberDateSerializer implements com.google.gson.JsonSerializer<Date> {
    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTime());
    }
}
