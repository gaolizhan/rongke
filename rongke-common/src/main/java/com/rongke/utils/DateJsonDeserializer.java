package com.rongke.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/25.
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat format3 = new SimpleDateFormat("HH:mm:ss");

    /**
     * 日期时间 String转Date
     *
     * @param jsonParser
     * @param deserializationContext
     * @return
     * @throws IOException
     */
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        try {
            if (text.contains("-") && !text.contains(":")) {
                return format2.parse(text);
            }
            if (text.contains(":") && !text.contains("-")) {
                return format3.parse(text);
            }
            return format.parse(text);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
