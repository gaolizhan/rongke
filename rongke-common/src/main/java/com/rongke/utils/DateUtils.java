package com.rongke.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期帮助类
 *
 * @author rplees
 * @date 2010-10-18
 */
public class DateUtils {

    public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String DEFAULT_DATE_FORMAT = YYYY_MM_DD_HH_MM_SS;
    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    public final static String YYYYMMDD = "yyyyMMdd";

    /**
     * 获取当年第一天
     */
    public static Date getYearFirstDay() {
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(year + "-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当前时间格式化
     *
     * @return YYYY MM DD
     */
    public static String nowDateSimple() {
        return dateSimple(new Date());
    }

    /**
     * 当前时间格式化
     *
     * @return YYYY MM DD
     */
    public static String dateSimple(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
        return simpleDateFormat.format(date);
    }

    /**
     * 将string 格式字符串转换成long型
     */
    public static Long verifyDateformString(String s) throws ParseException {
        return getDateFormat(YYYY_MM_DD_HH_MM_SS).parse(s).getTime();
    }

    public static Date getTodayStartDate() {
        return getDayStartDate(new Date());
    }

    /**
     * 转时间
     *
     * @param f    格式化
     * @param date 时间
     * @return date类型
     */
    public static Date parse(String f, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(f);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getDayStartDate(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        return currentDate.getTime();
    }

    public static Date getDayEndDate(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);

        currentDate.set(Calendar.HOUR_OF_DAY, 24);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        return currentDate.getTime();
    }

    public static Date getTodayEndDate() {
        return getDayEndDate(new Date());
    }

    public static Date getWeekStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    public static Date getWeekEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeekStartDate());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    public static Date getMonthStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getMonthEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    /**
     * 验证是不是有效的日期
     *
     * @param hrs
     * @return
     */
    public static boolean verifyDateRule(DayRule dr, DateRule... hrs) {
        DayRule[] drs = new DayRule[1];
        drs[0] = dr;

        return verifyDateRule(drs, hrs);
    }

    /**
     * 验证是不是有效的日期
     *
     * @return
     */
    public static boolean verifyDateRule(DayRule dr) {
        DayRule[] drs = new DayRule[1];
        drs[0] = dr;
        return verifyDateRule(drs);
    }

    /**
     * 验证是不是有效的日期
     *
     * @param dys
     * @param hrs
     * @return
     */
    public static boolean verifyDateRule(DayRule[] dys, DateRule... hrs) {
        Date d = new Date();

        boolean f = false;
        if (dys != null && dys.length > 0) {
            for (DayRule dr : dys) {
                if (dr.between(d))
                    f = true;
                if (f)
                    break;
            }
        } else {
            f = true;
        }

        // 天数验证不通过，就没必要验证小时了
        if (!f)
            return f;

        if (hrs == null || hrs.length < 1)
            return true;
        for (DateRule hr : hrs) {
            if (hr.between(d))
                return true;
        }

        return false;
    }

    /**
     * 在现在时间的基础上增加 days 后的日期
     *
     * @param days
     * @return
     */
    public static Date dayAdd(int days) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, day + days);
        return calendar.getTime();
    }

    /**
     * 在指定时间的基础上增加 hour 后的日期
     *
     * @param hour
     * @return
     */
    public static Date hourAdd(int hour, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, hours + hour);
        return calendar.getTime();
    }

    /**
     * 在现在时间的基础上增加 days 后的日期
     */
    public static Date hourAdd(int hour) {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, hours + hour);
        return calendar.getTime();
    }

    /**
     * 在指定时间的基础上增加 days 后的日期
     *
     * @param days
     * @return
     */
    public static Date dayAdd(int days, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, day + days);
        return calendar.getTime();
    }


    /**
     * 在指定时间的基础上增加 days 后的日期
     *
     * @param days
     * @return
     */
    public static String dayAdd(int days, String date) {
        try {
            return getDateFormat().format(DateUtils.dayAdd(days, parse(date)));
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse(String date) {
        try {
            return getDateFormat().parse(date);
        } catch (Exception e) {
            try {
                return getDateFormat(YYYY_MM_DD_HH_MM_SS).parse(date);
            } catch (Exception e1) {
                try {
                    return getDateFormat(YYYY_MM_DD_HH_MM).parse(date);
                } catch (ParseException e2) {
                    try {
                        return getDateFormat(YYYYMMDDHHMMSS).parse(date);
                    } catch (Exception e3) {
                        try {
                            return getDateFormat(YYYY_MM_DD).parse(date);
                        } catch (Exception e4) {
                            try {
                                return getDateFormat("HH:mm:ss").parse(date);
                            } catch (Exception e5) {
                                return null;
                            }
                        }
                    }
                }
            }
        }
    }

    public static int getNowDayOfWeek() {
        return getDayOfWeek(new Date());
    }

    /**
     * 描述
     *
     * @param dt
     * @return 0 - 礼拜7
     */
    public static int getDayOfWeek(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return w;
    }

    // 月数操作
    public static Date monthAdd(int months) {
        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, m + months);
        return calendar.getTime();
    }

    // 月数操作
    public static Date monthAdd(int months, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int m = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, m + months);
        return calendar.getTime();
    }

    public static Date minAdd(int mins) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(12);
        calendar.set(12, day + mins);
        return calendar.getTime();
    }

    public static Date minAdd(int hour, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(12);
        calendar.set(12, hours + hour);
        return calendar.getTime();
    }

    /**
     * 根据DateFormat 格式化现在时间
     *
     * @param df
     * @return 如:2012-12-21
     */
    public static String nowDate(DateFormat df) {
        if (null == df) {
            df = getDateFormat();
        }
        return df.format(new Date());
    }

    /**
     * 默认的格式化时间
     *
     * @return 2012-12-21 12:31:20
     */
    public static String nowDate() {
        return getDateFormat().format(new Date());
    }

    /**
     * 默认的格式化时间
     *
     * @return 2012-12-21 12:31:20
     */
    public static String nowDate(String format) {
        return getDateFormat(format).format(new Date());
    }

    public static DateFormat getDateFormat() {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    }

    public static DateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    public static String format(String format, Date date) {
        return getDateFormat(format).format(date);
    }

    /**
     * 是否为同一个月份
     *
     * @param d1
     * @param d2
     * @return Boolean.True 同一个
     */
    public static boolean isSameMonth(Date d1, Date d2) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(d1);
        int i1 = instance.get(Calendar.MONTH);

        instance.setTime(d2);
        int i2 = instance.get(Calendar.MONTH);

        return i1 == i2;
    }

    /**
     * 是否为同一个天数
     *
     * @param d1
     * @param d2
     * @return Boolean.True 同一个
     */
    public static boolean isSameDay(Date d1, Date d2) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(d1);
        int i1 = instance.get(Calendar.DAY_OF_YEAR);

        instance.setTime(d2);
        int i2 = instance.get(Calendar.DAY_OF_YEAR);

        return i1 == i2;
    }

    /**
     * 格式化时间 YYYY_MM_DD_HH_MM_SS 2015-11-24 17:12:00 YYYY_MM_DD_HH_MM 2015-11-24
     * 17:12 data 2015-11-24 17:12
     *
     * @return 2015-11-24 17:12:00格式的时间
     */
    public static String timeDate(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty())
            ;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 格式化时间为周 星期几
     *
     * @param l l 时间戳 return 周几
     */
    public static String getWeekOfDate(Long l) {
        Date dt = new Date(l);
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    // 获得当天0点时间
    public static Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天24点时间
    public static Date getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得本周一0点时间
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    // 获得本周日24点时间
    public static Date getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    // 获得本月第一天0点时间
    public static Date getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    // 获得本月最后一天24点时间
    public static Date getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    /**
     * System.out.println("当前时间："+ new Date().toLocaleString());
     * System.out.println("当天0点时间："+ getTimesmorning().toLocaleString());
     * System.out.println("当天24点时间："+ getTimesnight().toLocaleString());
     * System.out.println("本周周一0点时间："+ getTimesWeekmorning().toLocaleString());
     * System.out.println("本周周日24点时间："+ getTimesWeeknight().toLocaleString());
     * System.out.println("本月初0点时间："+ getTimesMonthmorning().toLocaleString());
     * System.out.println("本月未24点时间："+ getTimesMonthnight().toLocaleString());
     */
    /****
     * @param date1 <String>
     * @param date2 <String>
     * @return int
     * @throws ParseException
     */
    public static int getMonthSpace(Date date1, Date date2) throws ParseException {
        int result = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        result = c2.get(Calendar.MONDAY) - c1.get(Calendar.MONTH);
        return result == 0 ? 1 : Math.abs(result);
    }

    /**
     * 获取传入日期年的第几个季度
     *
     * @param date 日期
     * @return 此年份地几个季度
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    /**
     * 获取时间段之间的 季度最后一天(最后一天为结束时间)
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 之间的 季度最后一天
     */
    public static Date[] getQuarterLastDateLast(Date startDate, Date endDate) {
        Date[] quarterLastDate = getQuarterLastDate(startDate, endDate);
        quarterLastDate[quarterLastDate.length - 1] = endDate;
        return quarterLastDate;
    }

    /**
     * 获取时间段之间的 季度最后一天
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 之间的 季度最后一天
     */
    public static Date[] getQuarterLastDate(Date startDate, Date endDate) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startDate);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endDate);

        YearMonth[] yearMonths = twoDateBetweenYearAndMonth(startDate, endDate);

        if (yearMonths.length == 1) {
            return getQuarterLastDateByOneYear(startDate, endDate);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
            List<Date> dateList = new ArrayList<>();
            for (int i = 0; i < yearMonths.length; i++) {
                YearMonth yearMonth = yearMonths[i];
                Integer[] months = yearMonth.getMonths();

                try {
                    Date[] quarterLastDateByOneYear = getQuarterLastDateByOneYear(
                            sdf.parse(yearMonth.getYear() + monthParse(months[0]) + "01"),
                            sdf.parse(yearMonth.getYear() + monthParse(months[months.length - 1]) + "01"));
                    dateList.addAll(Arrays.asList(quarterLastDateByOneYear));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            Date[] dates = new Date[dateList.size()];
            return dateList.toArray(dates);
        }
    }

    /**
     * 计算量时间之间的年份和月份
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 之间的年份和月份
     */
    public static YearMonth[] twoDateBetweenYearAndMonth(Date startDate, Date endDate) {
        String startTime = format(YYYY_MM_DD, startDate);
        String endTime = format(YYYY_MM_DD, endDate);

        String[] arg1 = startTime.split("-");
        String[] arg2 = endTime.split("-");
        int year1 = Integer.valueOf(arg1[0]);
        int year2 = Integer.valueOf(arg2[0]);
        int month1 = Integer.valueOf(arg1[1]);
        int month2 = Integer.valueOf(arg2[1]);

        int index = 0;

        YearMonth[] yearMonths = new YearMonth[(year2 - year1) + 1];
        if (year1 == year2) {
            YearMonth yearMonth = new YearMonth(year1);
            Integer[] months = new Integer[(month2 - month1) + 1];
            for (int j = month1; j <= month2; j++) {
                months[index] = j;
                index++;
            }
            yearMonth.setMonths(months);
            yearMonths[0] = yearMonth;
            return yearMonths;
        }

        for (int i = year1; i <= year2; i++) {
            int monthCount = 12;
            int monthStart = 1;
            if (i == year1) {
                monthStart = month1;
                monthCount = 12 - monthStart + 1;
            } else if (i == year2) {
                monthCount = month2;
            }
            YearMonth yearMonth = new YearMonth(year1);
            Integer[] months = new Integer[monthCount];
            for (int j = 0; j < monthCount; j++) {
                int temp = monthStart + j;
                months[j] = temp;
            }
            yearMonth.setMonths(months);
            yearMonths[index] = yearMonth;
            index++;
        }
        return yearMonths;
    }

    /**
     * 获取当年的季度结束时间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 结束时间
     */
    public static Date[] getQuarterLastDateByOneYear(Date startDate, Date endDate) {
        YearMonth[] yearAndMonth = twoDateBetweenYearAndMonth(startDate, endDate);
        Integer[] months = yearAndMonth[0].getMonths();
        int startMonth = months[0], endMonth = months[months.length - 1];
        startMonth = getQuarterStartMonth(startMonth);
        endMonth = getQuarterEndMonth(endMonth);
        Date[] dates = new Date[(endMonth - startMonth + 1) / 3];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        int index = 0;
        for (int i = endMonth; i > startMonth; i = i - 3) {
            try {
                dates[index] = getCurrentQuarterEndTime(sdf.parse(yearAndMonth[0].getYear() + monthParse(i) + "01"));
                index++;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return dates;
    }

    /**
     * 获取一个月份的季度开始月份
     *
     * @param month 月份
     * @return 开始月份
     */
    public static Integer getQuarterStartMonth(Integer month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentQuarterStartTime;
        try {
            currentQuarterStartTime = getCurrentQuarterStartTime(sdf.parse("2000-" + monthParse(month) + "-" + "01"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(currentQuarterStartTime);
        return calendarStart.get(Calendar.MONTH);
    }

    /**
     * 获取一个月份的季度结束月份
     *
     * @param month 月份
     * @return 结束月份
     */
    public static Integer getQuarterEndMonth(Integer month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentQuarterStartTime;
        try {
            currentQuarterStartTime = getCurrentQuarterEndTime(sdf.parse("2000-" + monthParse(month) + "-" + "01"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(currentQuarterStartTime);
        return calendarStart.get(Calendar.MONTH);
    }

    /**
     * 月份整形转字符串(补0)
     *
     * @param month 月份
     * @return 两位
     */
    public static String monthParse(Integer month) {
        if (month <= 9) {
            return "0" + month;
        }
        return month.toString();
    }

    /**
     * 月和年
     */
    public static class YearMonth {
        public YearMonth(Integer year) {
            super();
            this.year = year;
        }

        /**
         * 年份
         */
        private Integer year;
        /**
         * 月份
         */
        private Integer[] months;

        public Integer getYear() {
            return year;
        }

        public YearMonth(Integer year, Integer[] months) {
            super();
            this.year = year;
            this.months = months;
        }

        public YearMonth(Integer[] months) {
            super();
            this.months = months;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Integer[] getMonths() {
            return months;
        }

        public void setMonths(Integer[] months) {
            this.months = months;
        }
    }

    /**
     * 获取每隔半年的最后一天(最后一天为结束时间)
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 最后一天
     */
    public static Date[] getHalfYearLastDayTimeLast(Date startDate, Date endDate) {
        Date[] halfYearLastDayTime = getHalfYearLastDayTime(startDate, endDate);
        halfYearLastDayTime[halfYearLastDayTime.length - 1] = endDate;
        return halfYearLastDayTime;
    }

    /**
     * 获取每隔半年的最后一天
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 最后一天
     */
    public static Date[] getHalfYearLastDayTime(Date startDate, Date endDate) {
        YearMonth[] yearAndMonth = twoDateBetweenYearAndMonth(startDate, endDate);
        if (yearAndMonth.length == 1) {
            return getHalfYearLastDayTimeOneYear(startDate, endDate);
        } else {
            List<Date> dataList = new ArrayList<>();
            for (YearMonth yearMonth : yearAndMonth) {
                Integer[] months = yearMonth.getMonths();
                Date[] halfYearLastDayTimeOneYear = getHalfYearLastDayTimeOneYear(
                        parse(YYYY_MM_DD, yearMonth.getYear() + monthParse(months[0]) + "01"),
                        parse(YYYY_MM_DD, yearMonth.getYear() + monthParse(months[months.length - 1]) + "01"));
                dataList.addAll(Arrays.asList(halfYearLastDayTimeOneYear));
            }
            Date[] dates = new Date[dataList.size()];
            return dataList.toArray(dates);
        }
    }

    /**
     * 获取每隔半年的最后一天（当年的不跨年）
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 最后一天
     */
    public static Date[] getHalfYearLastDayTimeOneYear(Date startDate, Date endDate) {
        YearMonth[] yearAndMonth = twoDateBetweenYearAndMonth(startDate, endDate);
        YearMonth yearMonth = yearAndMonth[0];
        Integer[] months = yearMonth.getMonths();
        int startMonth = months[0], endMonth = months[months.length - 1];
        Date[] dates = null;
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        if (startMonth <= 6 && endMonth <= 6) {
            dates = new Date[1];
            try {
                dates[0] = getMouthLastDay(sdf.parse(calendar.get(Calendar.YEAR) + "05" + "01"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else if (startMonth > 6) {
            dates = new Date[1];
            try {
                dates[0] = getMouthLastDay(sdf.parse(calendar.get(Calendar.YEAR) + "12" + "01"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else if (startMonth <= 6 && endMonth > 6) {
            dates = new Date[2];
            try {
                dates[0] = getMouthLastDay(sdf.parse(calendar.get(Calendar.YEAR) + "06" + "01"));
                dates[1] = getMouthLastDay(sdf.parse(calendar.get(Calendar.YEAR) + "12" + "01"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return dates;
    }

    /**
     * 当月第一天
     *
     * @return 第一天
     */
    public static Date geMouthtFirstDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 23:23:59");
        df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        try {
            return df.parse(str.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当月最后一天
     *
     * @return 最后一天
     */
    public static Date getMouthLastDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.add(Calendar.MONTH, 1); // 加一个月
        gcLast.set(Calendar.DATE, 1); // 设置为该月第一天
        gcLast.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 23:23:59");
        df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        try {
            return df.parse(str.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取年份最后一天(最后一年为结束时间)
     *
     * @param startDate 开始年份
     * @param endDate   结束年份
     * @return 最后一天
     */
    public static Date[] getYaerLastTimeLast(Date startDate, Date endDate) {
        Date[] yaerLastTime = getYaerLastTime(startDate, endDate);
        yaerLastTime[yaerLastTime.length - 1] = endDate;
        return yaerLastTime;
    }

    /**
     * 获取年份最后一天
     *
     * @param startDate 开始年份
     * @param endDate   结束年份
     * @return 最后一天
     */
    public static Date[] getYaerLastTime(Date startDate, Date endDate) {
        YearMonth[] yearAndMonth = twoDateBetweenYearAndMonth(startDate, endDate);
        if (yearAndMonth.length == 1) {
            return new Date[]{getYearLast(yearAndMonth[0].getYear())};
        } else {
            Date[] dates = new Date[yearAndMonth.length];
            for (int i = 0; i < yearAndMonth.length; i++) {
                YearMonth yearMonth = yearAndMonth[i];
                dates[i] = getYearLast(yearMonth.getYear());
            }
            return dates;
        }
    }
    // ======new

    /**
     * 获取 当前年、半年、季度、月、日、小时 开始结束时间
     */
    private final static SimpleDateFormat shortSdf;
    private final static SimpleDateFormat longHourSdf;
    private final static SimpleDateFormat longSdf;

    static {
        shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
        longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public Date getCurrentWeekDayStartTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return c.getTime();
    }

    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public Date getCurrentWeekDayEndTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return c.getTime();
    }

    /**
     * 获得本天的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public Date getCurrentDayStartTime() {
        Date now = new Date();
        try {
            now = shortSdf.parse(shortSdf.format(now));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获得本天的结束时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public Date getCurrentDayEndTime() {
        Date now = new Date();
        try {
            now = longSdf.parse(shortSdf.format(now) + " 23:59:59");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获得本小时的开始时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public Date getCurrentHourStartTime() {
        Date now = new Date();
        try {
            now = longHourSdf.parse(longHourSdf.format(now));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获得本小时的结束时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public Date getCurrentHourEndTime() {
        Date now = new Date();
        try {
            now = longSdf.parse(longHourSdf.format(now) + ":59:59");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获得本月的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public Date getCurrentMonthStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 当前月的结束时间，即2012-01-31 23:59:59
     *
     * @return
     */
    public Date getCurrentMonthEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获取当前年的开始时间
     *
     * @return 指定年限年的开始时间
     */
    public static Date getCurrentYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        Date now = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获取当前年的开始时间
     *
     * @return 当前年的开始时间
     */
    public static Date getCurrentYearStartTime() {
        return getCurrentYearStartTime(null);
    }

    /**
     * 当前年的结束时间，即2012-12-31 23:59:59
     *
     * @return
     */
    public Date getCurrentYearEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获取指定日期的季度第一天
     *
     * @param date 指定日期
     * @return 指定日期季度第一天
     */
    public static Date getCurrentQuarterStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获取当前季度的开始时间(第一天)
     *
     * @return 当前季度的开始时间
     */
    public static Date getCurrentQuarterStartTime() {
        return getCurrentQuarterStartTime(null);
    }

    /**
     * 获取指定日期的季度的结束时间(最后一天)
     *
     * @return 指定日期的季度的结束时间(最后一天)
     */
    public static Date getCurrentQuarterEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                // c.set(Calendar.MONTH,;
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获取当前季度的结束时间(最后一天)
     *
     * @return 当前季度的结束时间(最后一天)
     */
    public static Date getCurrentQuarterEndTime() {
        return getCurrentQuarterEndTime(null);
    }

    /**
     * 获取指定日期前/后半年的开始时间
     *
     * @param date 指定日期
     * @return 前/后半年的开始时间
     */
    public static Date getHalfYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 0);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 6);
            }
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;

    }

    /**
     * 获取前/后半年的开始时间
     *
     * @return 前/后半年的开始时间
     */
    public static Date getHalfYearStartTime() {
        return getHalfYearStartTime(null);
    }

    /**
     * 获取指定年限前/后半年的结束时间
     *
     * @param date 指定时间
     *             * @return 前/后半年的结束时间
     */
    public static Date getHalfYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return now;
    }

    /**
     * 获取<em>今年</em> 前/后半年的结束时间
     *
     * @return 前/后半年的结束时间
     */
    public static Date getHalfYearEndTime() {
        return getHalfYearEndTime(null);
    }

    /**
     * 获取两日期之间相差天数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 天数
     */
    public static long twoDateBetweenDays(Date startDate, Date endDate) {
        return (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date 最后一天日期
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        currYearLast = setTime(currYearLast, 23, 59, 59);
        return currYearLast;
    }

    /**
     * 设置时分秒
     *
     * @param date 时间
     * @param shi  时
     * @param fen  分
     * @param miao 秒
     * @return 设置好时间
     */
    public static Date setTime(Date date, int shi, int fen, int miao) {
        String ymd = format(YYYY_MM_DD, date);
        ymd += " " + shi + ":" + fen + ":" + miao;
        return parse(YYYY_MM_DD_HH_MM_SS, ymd);
    }

    /**
     * 获取某年的天数
     *
     * @param year 年数
     * @return 次年天数
     */
    public static int getDaysOfYear(Integer year) {
        Calendar cal = Calendar.getInstance();
        if (year != null) {
            cal.set(Calendar.YEAR, year);
        }

        return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取今年的天数
     *
     * @return 次年天数
     */
    public static int getDaysOfYear() {
        return getDaysOfYear(null);
    }


    public static void main(String[] args) {
        Date date = DateUtils.dayAdd(1, new Date());
        System.out.println(date);
    }

    /**
     * 保留日期部分(时分秒设置为0)
     *
     * @param date 日期时间
     * @return yyyy-mm-dd 0:0;0
     */
    public static Date retainDate(Date date) {
        date = dateInit(date);
        Calendar cal = getCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    /**
     * 保留日期部分
     *
     * @param date 日期时间
     * @return yyyy-mm-dd
     */
    public static Date YYMMDDDate(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date);
        Date YYMMDDDate =  sdf.parse(s);
        return  YYMMDDDate ;
    }

    /**
     * 保留日期部分
     *
     * @param date 日期时间
     * @return yyyy-mm-dd 年月日
     */
    public static String YYMMDDDateChinese(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String s = sdf.format(date);
        return  s ;
    }

    /**
     * 日期设置成当天最后一秒部分(时分秒设置为0)
     *
     * @param date 日期时间
     * @return yyyy-mm-dd 23：59：59
     */
    public static Date dateSetLastSecond(Date date) {
        date = dateInit(date);
        Calendar cal = getCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        return cal.getTime();
    }

    /**
     * {@link} dateSetLastSecond
     */
    public static Date dateSetLastSecond() {
        return dateSetLastSecond(null);
    }

    /**
     * {@link} retainDate
     */
    public static Date retainDate() {
        return retainDate(null);
    }

    private static final Calendar calendar = Calendar.getInstance();

    public static Calendar getCalendar() {
        return calendar;
    }

    private static Date dateInit(Date date) {
        if (date == null) date = new Date();
        return date;
    }
}