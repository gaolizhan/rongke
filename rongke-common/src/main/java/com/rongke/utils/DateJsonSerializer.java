package com.rongke.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/25.
 */
public class DateJsonSerializer extends JsonSerializer<Date> {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期时间 Date转String
     *
     * @param date
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     */
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(format.format(date));
    }
}
