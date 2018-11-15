package com.rongke.utils;


import java.util.Date;

public abstract class DateRule {
    int s;
    int e;

    public DateRule(int start, int end) {
        this.s = start;
        this.e = end;
    }

    /**
     * 验证是否是否在这区间
     *
     * @param d
     * @return
     */
    public boolean between(Date d) {
        if (s == -1 && e == -1)
            return true;

        int n = getFormat(d);

        if (s == -1) {
            return n <= e;
        }
        if (e == -1) {
            return n >= s;
        }

        return n >= s && n <= e;
    }

    public abstract int getFormat(Date d);
}
