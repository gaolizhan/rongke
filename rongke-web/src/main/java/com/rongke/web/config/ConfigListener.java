package com.rongke.web.config;


import com.rongke.enums.SourceType;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;


public class ConfigListener implements ServletContextAware {

    @Override
    public void setServletContext(ServletContext servletContext) {
        Map<String, String> pair = new HashMap<>();
        pair.put("E10ADC3949BA59ABBE56E057F20F883E","E10ADC3949BA59ABBE56E057F20F883E");
        if (pair != null) SourceType.KEY_PAIR.putAll(pair);
    }
}