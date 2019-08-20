package com.cn.jm.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Predicate;

import com.jfinal.kit.StrKit;

/**
 * mysql语句工具包
 * 1.0.0 新增基础sql语句拼接
 * 1.0.1 修改搜索语句bug,新增jdk8的校验条件再加入判断
 * 1.0.2 去除jfinal的SqlPara类,在传入值时进行注入处理
 * 
 * @author Administrator
 *
 */
public class SqlUtil {
	public static final String CHECK_WHERE_AND = ":(W|w)(H|h)(E|e)(R|r)(E|e)\\s{1,}(AND|OR|and|or)\\s{1,}";
	public static final String CHECK_WHERE = ":(W|w)(H|h)(E|e)(R|r)(E|e)";
	public static final String REMOVE_MULTIPLE_SPACES = " {1,}";
	public static final String _WHERE = ":WHERE";
	public static final String WHERE = " WHERE ";
	public static final String BLANK_SPACE = " ";
	public static final String LIKE = " LIKE ";
	public static final String SELECT = " SELECT ";
	public static final String FROM = " FROM ";
	public static final String LIMIT = " LIMIT ";
	public static final String ALL = " * ";
	public static final String DESC = " DESC ";
	public static final String ASC = " ASC ";
	public static final String DELETE = " DELETE ";
	public static final String IN = " IN ";
	public static final String NOT_IN = " NOT IN ";
	/**order by 放在 group BY 后面*/
	public static final String ORDER_BY = "ORDER BY ";
	/**group by 放在 where 判断后*/
	public static final String GROUP_BY = "GROUP BY ";
	/** 左括号*/
	public static final char LEFT_PARENTHESES = '(';
	/** 右括号*/
	public static final char RIGHT_PARENTHESES = ')';
	
	/**
	 * 判断值是否为空,不为空则防止sql注入拼接将值拼接上
	 * @param sql
	 * @param key 字段 需要自己加入判断符号 例如 AND 或者 OR
	 * @param leftSign 左符号,例如%
	 * @param value 值
	 * @param rightSign 右符号,例如 %
	 */
	public static void addLike(StringBuilder sql, String key, String leftSign, String value, String rightSign) {
		if (StrKit.notBlank(value)) {
			sql.append(key).append(LIKE).append(replaceStr(leftSign+value+rightSign));
		}
	}

	/**
	 * @param sql 
	 * @param appendSql 需要加入的sql语句
	 */
	public static void addSql(StringBuilder sql, String appendSql) {
		if (StrKit.notBlank(appendSql)) {
			sql.append(appendSql);
		}
	}
	
	/**
	 * @param sql 
	 * @param appendSql 需要加入拼接字段的sql语句
	 * @param judgeValue 需要判断的值,根据值的判断进行拼接语句
	 */
	public static void addSql(StringBuilder sql, String appendSql, String injectionValue) {
		if (StrKit.notBlank(injectionValue)) {
			sql.append(appendSql).append(injectionValue);
		}
	}
	
	/**
	 * 
	 * @param sql 
	 * @param appendSql 需要加入拼接字段的sql语句
	 * @param judgeValue 需要判断的值,根据值的判断进行拼接语句
	 */
	public static void addSql(StringBuilder sql,String appendSql,Object injectionValue) {
		if (null != injectionValue) {
			sql.append(appendSql).append(injectionValue);
		}
	}
	/**
	 * 
	 * @param sql 
	 * @param appendSql 需要加入拼接字段的sql语句
	 * @param judgeValue 需要判断的值,根据值的判断进行拼接语句
	 */
	public static void addOrderBySql(StringBuilder sql,String injectionValue,String sort) {
		if (StrKit.notBlank(injectionValue)) {
			sql.append(ORDER_BY).append(injectionValue).append(sort);
		}
	}

	/**
	 * @param sql
	 * @param key 字段 需要自己加入判断符号
	 * @param value 值
	 */
	public static void addWhere(StringBuilder sql, String key, String value) {
		if (StrKit.notBlank(value)) {
			sql.append(BLANK_SPACE).append(key).append(replaceStr(value)).append(BLANK_SPACE);
		}
	}

	/**
	 * @param sql
	 * @param key 字段 
	 * @param sign 判断符号
	 * @param value 值
	 */
	public static void addWhere(StringBuilder sql, String key,String sign, String value) {
		if (StrKit.notBlank(value)) {
			sql.append(BLANK_SPACE).append(key).append(sign).append(replaceStr(value)).append(BLANK_SPACE);
		}
	}

	/**
	 * @param sql
	 * @param key 字段 需要自己加入判断符号
	 * @param value 值
	 */
	public static void addWhere(StringBuilder sql, String key, Object value) {
		if (value!=null) {
			sql.append(BLANK_SPACE).append(key).append(replaceObj(value)).append(BLANK_SPACE);
		}
	}

	/**
	 * 当value值不为空,且不等于传入的值时加入判断条件
	 * @param sql
	 * @param key 字段 需要自己加入判断符号
	 * @param value
	 * @param noThisValue
	 */
	public static void addWhere(StringBuilder sql, String key, Integer value, Integer noThisValue) {
		if(!noThisValue.equals(value)) {
			addWhere(sql, key, value);
		}
	}
	/**
	 * 当value值不为空,且不等于传入的值时加入判断条件
	 * @param sql
	 * @param key 字段 需要自己加入判断符号
	 * @param value
	 * @param predicate 需要执行的判断语句
	 */
	public static <T> void addWhere(StringBuilder sql, String key,T value,Predicate<T> predicate) {
		if (predicate.test(value)) {
			sql.append(BLANK_SPACE).append(key).append(replaceObj(value)).append(BLANK_SPACE);
		}
	}

	public static void addNull(StringBuilder sql, String key) {
		sql.append(key).append(" IS NULL ");
	}

	public static void addNotNull(StringBuilder sql, String key) {
		sql.append(key).append(" IS NOT NULL ");
	}

	public static void addWhereParamTime(StringBuilder sql, String theTime, String timeName) {
		if (StrKit.notBlank(theTime)) {
			String theTimes[] = theTime.split(" - ");// 将时间进行拆分
			String startTime = theTimes[0];// 0为开始时间
			String endTime = theTimes[1];// 1为结束时间
			if (startTime != null && endTime != null) {
				addWhere(sql, timeName, " >= ", startTime);
				addWhere(sql, timeName, " <= ", endTime);
			}
		}
	}

	public static void addBetweenTime(StringBuilder sql, String startTime, String endTime, String timeName) {
		if (StrKit.notBlank(startTime) && StrKit.notBlank(endTime)) {
			addWhere(sql, timeName, " >= ", startTime);
			addWhere(sql, timeName, " <= ", endTime);
		}
	}

	/**
	 * 如果传入的ids不为空则拼接sql的in 语句 
	 * @param sql 
	 * @param key 字段名称,需加入拼接符 例如 AND id,OR id
	 * @param inIds 对应字段in的值,不对in去重
	 */
	public static <E> void putIn(StringBuilder sql, String key, Object[] inIds){
		if(inIds != null && inIds.length != 0) {
			sql.append(key).append(IN).append(changeArray(inIds));
		}
	}
	
	/**
	 * 如果传入的ids不为空则拼接sql的 not in 语句 
	 * @param sql 
	 * @param key 字段名称,需加入拼接符 例如 AND id,OR id
	 * @param notInIds 对应字段in的值,不对in去重
	 */
	public static <E> void putNotIn(StringBuilder sql, String key, Object[] notInIds){
		if(notInIds != null && notInIds.length != 0) {
			sql.append(key).append(NOT_IN).append(changeArray(notInIds));
		}
	}

	/**
	 * 如果传入的ids不为空则拼接sql的in 语句 
	 * @param sql 
	 * @param key 字段名称,需加入拼接符 例如 AND id,OR id
	 * @param inIds 对应字段in的值,对值去除
	 */
	public static <E> void putInNotRepeat(StringBuilder sql, String key, Object[] inIds){
		if(inIds != null && inIds.length != 0) {
			sql.append(key).append(IN).append(changeArray(inIds));
		}
	}
	
	/**
	 * 如果传入的ids不为空则拼接sql的 not in 语句 
	 * @param sql 
	 * @param key 字段名称,需加入拼接符 例如 AND id,OR id
	 * @param notInIds 对应字段in的值,对值去除
	 */
	public static <E> void putNotInNotRepeat(StringBuilder sql, String key, Object[] notInIds){
		if(notInIds != null && notInIds.length != 0) {
			sql.append(key).append(NOT_IN).append(changeArray(notInIds));
		}
	}
	
	/**
	 * 将数组[1,1,2,3]转换成(1,1,2,3)的样式,不去重复
	 * 
	 * @param <E>
	 * @param ids
	 * @return
	 */
	public static <E> String changeArray(E[] ids) {
		StringBuilder str = new StringBuilder(Arrays.toString(ids));
		str.setCharAt(0, LEFT_PARENTHESES);
		str.setCharAt(str.length()-1, RIGHT_PARENTHESES);
		return str.toString();
	}

	/**
	 * 将数组[1,1,2,3]转换成(1,2,3)的样式,去重复
	 * 
	 * @param <E>
	 * @param ids
	 * @return
	 */
	public static <E> String changeArrayNotRepeat(E[] ids) {
		StringBuilder str = new StringBuilder(new HashSet<E>(Arrays.asList(ids)).toString());
		str.setCharAt(0, LEFT_PARENTHESES);
		str.setCharAt(str.length()-1, RIGHT_PARENTHESES);
		return str.toString();
	}
	
	/**
	 * 帮sql 自动加上WHERE关键字,并且将第一个AND去除 只能更换包含AND的判断
	 * @param sql
	 * @return
	 */
	public static void changeWhere(StringBuilder sql) {
		if(sql.indexOf(_WHERE) != -1) {
			String strSql = sql.toString();
			sql.replace(0,sql.length(),strSql.replaceAll(CHECK_WHERE_AND, WHERE).replaceAll(CHECK_WHERE,BLANK_SPACE).replaceAll(REMOVE_MULTIPLE_SPACES, BLANK_SPACE));
		}
	}
	
	/**
	 * 返回传入的StringBuilder中sql的去除多空格sql
	 * @param sql
	 * @return
	 */
	public static String getSql(StringBuilder sql) {
		return sql.toString().replaceAll(REMOVE_MULTIPLE_SPACES, BLANK_SPACE);
	}
	
	/**
	 * 将搜索语句需要转换中英文的字段统一别名
	 * @param select SELECT a,b,c
	 * @param language 中文:zn_CH 英文:en_US 
	 * @param flieds 需要转换的字段,英文字段命名必须为    en+中文字段 (中文字段首字母大写)
	 */
	public static void getLanguageField(StringBuilder select, String language, String... flieds) {
		String pre = "";
		for (String flied : flieds) {
			String newflied = flied;
			if ("en_US".equals(language)) {
				pre = "en";
			}

			int i = flied.indexOf(".");

			newflied = pre + flied.substring(i + 1, i + 2).toUpperCase() + flied.substring(i + 2);
			if (i != -1) {
				newflied = flied.substring(0, i + 1) + newflied;
				flied = flied.substring(i + 1);
			}
			select.append(",").append(newflied).append(" as `").append(flied).append("` ");
		}
	}
	
	/**正则表达式**/
    private static String reg = "'";
    public static String replaceStr(String str){
		return reg + str.replace(reg,"") + reg;
    }
    public static Object replaceObj(Object obj) {
    	if(obj instanceof String) {
    		return replaceStr(obj.toString());
    	}
    	return obj;
    }
}
