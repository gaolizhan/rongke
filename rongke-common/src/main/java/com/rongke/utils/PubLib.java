package com.rongke.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PubLib {

	//日期的正则表达式：匹配 2004-04
	public static String DATE_PATTERN_MONTH = "^[0-9]{4}-(0[1-9]|1[0-2])$";

	//日期的正则表达式：匹配 2004-04-30 | 2004-02-29
	public static String DATE_PATTERN = "^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$";

	/**
	 * 根据生日(Date)类型获取年龄
	 * @param birthDate 生日
	 * @return
	 */
	public static Integer getAge(Date birthDate) {
		if (birthDate == null) {
			return null;
		}
		int age;
		Date now = new Date();
		SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new SimpleDateFormat("MM");

		String birth_year = format_y.format(birthDate);
		String this_year = format_y.format(now);

		String birth_month = format_M.format(birthDate);
		String this_month = format_M.format(now);

		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);

		// 如果未到出生月份，则age - 1
		if (this_month.compareTo(birth_month) < 0) age -= 1;
		if (age < 0) age = 0;

		return age;
	}

	/**
	 * 判断字符串的编码格式
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}

	/**
	 * 修改日期的时间
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date replaceTime(Date date, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		return cal.getTime();
	}

	/**
	 * 修改日期的某一部分
	 * @param date
	 * @param field Calendar.Type的一个值
	 * @param value
	 * @return
	 */
	public static Date dateReplace(Date date, int field, int value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(field, value);
		return cal.getTime();
	}

	/**
	 * 创建该类的实例
	 * @param cls
	 * @return
	 */
	public static Object createObjByClass(Class cls, Object...objects) {
		try {
			if (objects==null || objects.length==0) {
				return cls.newInstance();
			}
			Class[] paramTypes = new Class[objects.length];
			for (int i=0; i<objects.length; i++) {
				paramTypes[i] = objects[i].getClass();
			}
			java.lang.reflect.Constructor constructor=cls.getConstructor(paramTypes);
			return constructor.newInstance(objects);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据Object重新创建一个object
	 * @param obj
	 * @return
	 */
	public static Object recreateObject(Object obj) {
		try {
			return obj.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * list转化为“,”隔开的STRING
	 * @param list
	 * @return
	 */
	public static String listToStr(List list) {
		if (list == null || list.size()==0) return "";

		String s = list.toString();
		return s.replace("[", "").replace("]", "");
	}

	/**
	 * 以“,”隔开的str转化为int类型的List
	 * @param str
	 * @return
	 */
	public static List<Integer> strToIntList(String str) {
		if (str == null) return null;

		List<Integer> list = new ArrayList<Integer>();
		for (String s: str.split(",")) {
			list.add(StrToInt(s));
		}
		return list;
	}

	/**
	 * 反映射构建类
	 * @param clsName
	 * @param parameterTypes
	 * @param parameters
	 * @return
	 */
	public static Object constructorClass(String clsName
			, Class[] parameterTypes, Object[] parameters) {
		try {
			Class c=Class.forName(clsName);
			if (parameterTypes != null) {
				java.lang.reflect.Constructor constructor=c.getConstructor(parameterTypes);
				return constructor.newInstance(parameters);
			} else {
				return c.newInstance();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检测obj中的类型，自动四舍五入double和float类型
	 * @param obj
	 * @param i
	 */
	public static void roundCls(Object obj, int i) {
		for (Field field: obj.getClass().getDeclaredFields()) {
			if(double.class.equals(field.getType())
					|| Double.class.equals(field.getType())
					|| float.class.equals(field.getType())
					|| Float.class.equals(field.getType())) {
				field.setAccessible(true);
				try {
					field.set(obj, round(field.getDouble(obj), i));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除文件
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return true;
		} else {
			try {
				file.delete();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	//字符串转化为日期
	public static Date strToDate(String s) {
		return strToDate(s, "yyyy-MM-dd");
	}

	// 字符串转化为日期
	public static Date strToDate(String s, String dateFormat) {
		DateFormat dd = new SimpleDateFormat(dateFormat);
		try {
			if(isNumeric(s)&&s.length()==8){
				s=s.substring(0, 4)+"-"+s.substring(4,6)+"-"+s.substring(6,8);
			}
			return dd.parse(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 当年的第一天
	 * @param date
	 * @return
	 */
	public static Date getYearFirst(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	//当月的第一天
	public static Date getMonthFirst(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 当年的最后一天
	 * @param date
	 * @return
	 */
	public static Date getYearLast(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DATE, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	//当月的最后一天
	public static Date getMonthLast(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.roll(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	//当前时间所在月份的天数
	public static int getMonthDay(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.roll(Calendar.DATE, -1);
		return cal.get(Calendar.DATE);
	}

	//当前时间获取上个月的时间
	public static Date getSameMonthDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}
	//当前时间获取上年的时间
	public static Date getSameYearDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}

	/**
	 * 获取两个时间之间间隔月份
	 * 小的时间在前，大的在后
	 * 不足月，返回1
	 * @param smallDate
	 * @param bigDate
	 * @return
	 */
	public static int dateDiffMonth(Date smallDate, Date bigDate) {
		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();
		startCal.setTime(smallDate);
		endCal.setTime(bigDate);
		int m = 0;

		while (!startCal.after(endCal)) {
			startCal.add(Calendar.MONTH, 1);
			m++;
		}

		return m;
	}

	/**
	 * 增加时间
	 * @param date
	 * @param calendarType 如： Calendar.MONTH
	 * @param i
	 * @return
	 */
	public static Date dateAdd(Date date, int calendarType, int i) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(calendarType, i);
		return cal.getTime();
	}

	/**
	 * 获取两个时间之间的间隔天数
	 * 不足一天也返回1
	 * @param date1  小的日期
	 * @param date2  大的日期
	 * @return
	 */
	public static int dateDiffDay(Date date1, Date date2) {
		if (date1.getTime() > date2.getTime()) {
			return -1;
		}
		double f = (date2.getTime()-date1.getTime())/(3600*24*1000);
		return (int) Math.floor(f) + 1;
	}

	//从对象中获取指定字段的值
	public static Object getObjFieldValue(Object obj, String field){
		try {
			Field f = getObjField(obj.getClass(), field);
			f.setAccessible(true);
			return f.get(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	//设置对象中字段的值
	public static void setObjFieldValue(Object obj, String field, Object value) {
		try {
			Field f = getObjField(obj.getClass(), field);
			f.setAccessible(true);
			f.set(obj, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	//将String转为integer
	public static int StrToInt(String str){
		int i;
		try {
			i = Integer.parseInt(str.trim());
		} catch (NumberFormatException e) {
			i = 0;
		}
		return i;
	}

	//将String转为float
	public static double StrToFloat(String str){
		Double i;
		try {
			i = Double.parseDouble(str.trim());
		} catch (NumberFormatException e) {
			i = (double) 0;
		}
		return i;
	}

	//将obj转化成Str
	public static String ObjToStr(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	//将obj转化成Float
	public static Double ObjToFloat(Object obj) {
		return StrToFloat(ObjToStr(obj));
	}

	//根据定义的样式,将date转化为Str
	public static String DateToStr(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * 指定当前日期往前推的某个星期
	 * @param date 指定的时间
	 * @param i 星期几,星期日为1
	 */
	public static Date dateLastWeek(Date date, int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int iday = 7 + cal.get(Calendar.DAY_OF_WEEK) - i;
		return dateAdd(date, Calendar.DAY_OF_MONTH, -iday);
	}

	//将date转为星期
	public static String DateToWeak(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch (cal.get(Calendar.DAY_OF_WEEK)-1) {
			case 0:
				return "星期日";
			case 1:
				return "星期一";
			case 2:
				return "星期二";
			case 3:
				return "星期三";
			case 4:
				return "星期四";
			case 5:
				return "星期五";
			case 6:
				return "星期六";
			default:
				return "星期日";
		}
	}

	//四舍五入，保留多少位小数
	public static double round(double d, int i) {
		return PubLib.StrToFloat(String.format("%."+i+"f", d));
	}

	//四舍五入，保留多少位小数
	public static float round(float d, int i) {
		return (float) PubLib.StrToFloat(String.format("%."+i+"f", d));
	}

	public static double floatToDouble(float f) {
		return Double.parseDouble(String.valueOf(f));
	}


	//Str首字母大写
	public static String fCharToUpper(String str){
		char[] array = str.toCharArray();
		array[0] -= 32;
		return String.valueOf(array);
	}

	//反映射Object中的field
	//如果找不到，则查询父类中的字段
	public static Field getObjField(Class cls,String f) {
		try {
			return cls.getDeclaredField(f);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		if (cls!=Object.class){
			return getObjField(cls.getSuperclass(), f);
		} else {
			return null;
		}
	}

	//Map中key对应Object字段值转换为Array
	public static Object[] getItemByObj(Map<String, ?> map, Object obj) {
		Object[] cells = new Object[map.size()];
		int i = 0;
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			cells[i] = PubLib.getObjFieldValue(obj, entry.getKey());
			i++;
		}
		return cells;
	}

	//通过链表得到组装的数据集
	public static Object[] getItemByObj(Collection<?> containerPropertyIds, Object obj) {
		Object[] cells = new Object[containerPropertyIds.size()];
		Iterator it = containerPropertyIds.iterator();
		for (int i=0; i < containerPropertyIds.size(); i++) {
			cells[i] = PubLib.getObjFieldValue(obj,it.next().toString());
		}
		return cells;
	}

	//根据field的类型判断返回不同的CLASS
	public static Class<?> getClsByField(Field f){
		if (String.class.equals(f.getType())) {
			return String.class;
		} else if (int.class.equals(f.getType()) || Integer.class.equals(f.getType())) {
			return Integer.class;
		} else if (double.class.equals(f.getType()) || Double.class.equals(f.getType())) {
			return Double.class;
		} else if (boolean.class.equals(f.getType()) || Boolean.class.equals(f.getType())) {
			return Boolean.class;
		} else if (Date.class.equals(f.getType())) {
			return Date.class;
		} else {
			return Object.class;
		}
	}

	public static int getDateField(Date workDay, int calendar) {
		Calendar cal =  Calendar.getInstance();
		cal.setTime(workDay);
		return cal.get(calendar);
	}

	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

	/**
	 * 去掉日期中的时间
	 * @param date Date类型日期
	 * @return
	 */
	public static Date dateRemoveTime(Date date) {
		return PubLib.strToDate(PubLib.DateToStr(date, "yyyy-MM-dd"));
	}

	/**
	 * 获取附件的保存路径（部署目录+/ATTACHMENT)
	 * @param request
	 * @return
	 */
	public static String getFileDir(HttpServletRequest request){
		String path = request.getSession().getServletContext().getRealPath("/");
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		return path+"/ATTACHMENT";
	}

	private static final char UNDERLINE = '_';

	/**
	 * 如：
	 * userName --> user_name
	 * @param param
	 * @return
	 */
	public static String camelToUnderline(String param){
		if (param==null||"".equals(param.trim())){
			return "";
		}
		int len=param.length();
		StringBuilder sb=new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c=param.charAt(i);
			if (Character.isUpperCase(c)){
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 如：
	 * user_name --> userName
	 * user_name_ --> userName
	 * @param param
	 * @return
	 */
	public static String underlineToCamel(String param){
		if (param==null||"".equals(param.trim())){
			return "";
		}
		int len=param.length();
		StringBuilder sb=new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c=param.charAt(i);
			if (c==UNDERLINE){
				if (++i<len){
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 如：
	 * user_name --> userName
	 * @param param
	 * @return
	 */
	public static String underlineToCamel2(String param){
		if (param==null||"".equals(param.trim())){
			return "";
		}
		StringBuilder sb = new StringBuilder(param);
		Matcher mc = Pattern.compile("_").matcher(param);
		int i=0;
		while (mc.find()){
			int position=mc.end()-(i++);
			sb.replace(position-1,position+1,sb.substring(position,position+1).toUpperCase());
		}
		return sb.toString();
	}

	private static void convert(Object json, int type) {
		if (json instanceof JSONArray) {
			JSONArray arr = (JSONArray) json;
			for (Object obj : arr) {
				convert(obj, type);
			}
		} else if (json instanceof JSONObject) {
			JSONObject jo = (JSONObject) json;
			Set<String> keys = jo.keySet();
			String[] array = keys.toArray(new String[keys.size()]);
			for (String key : array) {
				Object value = jo.get(key);
				String[] key_strs = key.split("_");
				if (key_strs.length > 1) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < key_strs.length; i++) {
						String ks = key_strs[i];
						if (!"".equals(ks)) {
							if (i == 0) {
								sb.append(ks);
							} else {
								int c = ks.charAt(0);
								if (c >= 97 && c <= 122) {
									int v = c - 32;
									sb.append((char) v);
									if (ks.length() > 1) {
										sb.append(ks.substring(1));
									}
								} else {
									sb.append(ks);
								}
							}
						}
					}
					jo.remove(key);
					jo.put(sb.toString(), value);
				}
				convert(value, type);
			}
		}
	}

	/**
	 * 下划线转驼峰形式
	 * @param o
	 * @return
	 */
	public static Object convert(Object o) {
		String json = JSONObject.toJSONString(o);
		Object obj = JSON.parse(json);
		convert(obj,1);
		return obj;
	}

	/**
	 * 实体复制
	 * @param source 实体
	 * @return <D>
	 */
	public static <D> D copyObject(Object source, Class<D> destinationType) {
		if (null == source) {
			return null;
		}
		ModelMapper model = new ModelMapper();
		return model.map(source, destinationType);
	}

	/**
	 * 实体转JSONObject(忽略指定和添加指定属性)
	 * @param source 实体
	 * @param arr 需要忽略的属性
	 * @param map 需要添加的属性和属性值
	 * @return 返回JSONObject
	 */
	public static JSONObject copyToJSONObject(Object source, String[] arr, Map<String, Object> map) {
		if (source == null) {
			return null;
		}
		String json = JSONObject.toJSONString(source, SerializerFeature.WriteMapNullValue);
		JSONObject jsonObject = JSON.parseObject(json);

		//设置忽略的属性值为null
		if (null != arr) {
			for (String str : arr) {
				//jsonObject.put(str, null);
				jsonObject.remove(str);
			}
		}

		//设置需要添加的属性和属性值
		if (map != null) {
			Set<String> strings = map.keySet();
			for (String string : strings) {
				jsonObject.put(string, map.get(string));
			}
		}

		return jsonObject;
	}

	/**
	 * 实体转JSONObject(忽略指定和添加指定属性) *** 格式化时间
	 * @param source 实体
	 * @param arr 需要忽略的属性
	 * @param map 需要添加的属性和属性值
	 * @return 返回JSONObject
	 */
	public static JSONObject copyToJSONObjectUseDateFormat(Object source, String[] arr, Map<String, Object> map) {
		if (source == null) {
			return null;
		}
		String json = JSONObject.toJSONString(source, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
		JSONObject jsonObject = JSON.parseObject(json);

		//设置忽略的属性值为null
		if (null != arr) {
			for (String str : arr) {
				//jsonObject.put(str, null);
				jsonObject.remove(str);
			}
		}

		//设置需要添加的属性和属性值
		if (map != null) {
			Set<String> strings = map.keySet();
			for (String string : strings) {
				jsonObject.put(string, map.get(string));
			}
		}

		return jsonObject;
	}

	/**
	 * 实体转JSONObject(忽略指定属性)
	 * @param source 实体
	 * @param arr 需要忽略的属性
	 * @return 返回JSONObject
	 */
	public static JSONObject copyToJSONObject(Object source, String[] arr) {
		if (null == source) {
			return null;
		}
		return copyToJSONObject(source, arr, null);
	}

	/**
	 * 实体转JSONObject(忽略指定属性) *** 格式化时间
	 * @param source 实体
	 * @param arr 需要忽略的属性
	 * @return 返回JSONObject
	 */
	public static JSONObject copyToJSONObjectUseDateFormat(Object source, String[] arr) {
		if (null == source) {
			return null;
		}
		return copyToJSONObjectUseDateFormat(source, arr, null);
	}

	/**
	 * 实体转JSONObject(返回指定属性)
	 * @param source 实体
	 * @param arr 需要返回的属性
	 * @return 返回JSONObject
	 */
	public static JSONObject copyToJSONObjectAppoint(Object source, String[] arr) {
		if (arr.length == 0) {
			return null;
		}
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(arr);
		String json = JSON.toJSONString(source, filter);
		return JSON.parseObject(json);
	}

	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	/**
	 * 判断字符串是否是浮点数
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}

	/**
	 * 过滤表情符号字符
	 * @param source -
	 * @return -
	 */
	public static String filterEmoji(String source) {
		if(source != null)
		{
			Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
			Matcher emojiMatcher = emoji.matcher(source);
			if ( emojiMatcher.find()) {
				source = emojiMatcher.replaceAll("*");
//				try {
//					source = URLEncoder.encode(String.valueOf(emojiMatcher), "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
				return source ;
			}
			return source;
		}
		return source;
	}


	/**
	 * 编码格式
	 */
	private static final String ENCODING = "UTF-8";

	/**
	 * 将字符串中的emoji表情转换成可以在utf-8字符集数据库中保存的格式（表情占4个字节，需要utf8mb4字符集）
	 *
	 * @param str 待转换字符串
	 * @return 转换后字符串
	 */
	public static String emojiConvert(String str) {

		if (str == null) return "";

		String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";

		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			try {
				matcher.appendReplacement(sb, "[[" + URLEncoder.encode(matcher.group(1), ENCODING) + "]]");
			} catch (UnsupportedEncodingException e) {
				return str;
			}
		}
		matcher.appendTail(sb);

		return sb.toString();
	}

	/**
	 * 还原utf8数据库中保存的含转换后emoji表情的字符串
	 *
	 * @param str 转换后的字符串
	 * @return 转换前的字符串
	 */
	public static String emojiRecovery(String str) {

		String patternString = "\\[\\[(.*?)\\]\\]";

		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(str);

		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			try {
				matcher.appendReplacement(sb, URLDecoder.decode(matcher.group(1), ENCODING));
			} catch (UnsupportedEncodingException e) {
				return str;
			}
		}
		matcher.appendTail(sb);

		return sb.toString();
	}

	/**
	 * 获取一定长度的随机字符串(纯数字)
	 * @param length 指定字符串长度
	 * @return 一定长度的字符串
	 */
	public static String getRandomStringByLength(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}


}
