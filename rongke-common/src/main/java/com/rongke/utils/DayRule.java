package com.rongke.utils;

import java.util.Date;

public class DayRule extends DateRule {

    /**
     * @param start like 20131213 -1不限制开始
     * @param end   like 20131217 -1不限制结束
     */
    public DayRule(int start, int end) {
        super(start, end);
    }

    @Override
    public int getFormat(Date d) {
        return NumberUtils.parseInt(DateUtils.format(DateUtils.YYYYMMDD, d));
    }
}
