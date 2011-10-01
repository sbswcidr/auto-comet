package org.auto.comet.example.chat.web.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * User: xiaohanghu
 * Date: 11-8-1
 */
public class NumberDateFormat extends DateFormat {
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        if (null == date) return null;
        return new StringBuffer(String.valueOf(date.getTime()));
    }

    @Override
    public Date parse(String source, ParsePosition pos) {

        if (null == source) {
            pos.setIndex(1);
            return null;
        }
        source = source.trim();
        if ("".equals(source)) {
            pos.setIndex(1);
            return null;
        }
        long ms = Long.parseLong(source);
        Date data = new Date(ms);
        pos.setIndex(source.length());
        return new Date(ms);
    }
}
