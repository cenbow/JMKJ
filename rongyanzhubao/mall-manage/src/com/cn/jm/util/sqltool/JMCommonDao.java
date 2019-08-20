package com.cn.jm.util.sqltool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.cn.jm.core.utils.util.CollectionsUtils;
import com.cn.jm.core.utils.util.MapUtils;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

import cn.hutool.core.lang.Assert;

@SuppressWarnings("unchecked")
public class JMCommonDao {

	public final static JMCommonDao jmd = new JMCommonDao();
	public static TableMapping tableMap = TableMapping.me();

	public static final String WHERE_FLAG = ":where";

	public static final String SELECT_ALL_FIELD = "SELECT * ";

	public static final String GROUP_BY = " GROUP BY ";

	/**
	 * 
	 * @date 2018年11月16日 上午11:13:19
	 * @author lgk
	 * @Description: 查询单条记录
	 * @param clazz
	 * @param whereParam
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> T selectOne(Class<T> clazz, Query query,
			String... selectInner) {

		List<T> modelList = this.selectList(clazz, query, selectInner);

		if (CollectionsUtils.isEmpty(modelList)) {
			return null;
		}
		return modelList.get(0);
	}

	/**
	 * 
	 * @date 2018年11月16日 上午11:13:19
	 * @author lgk
	 * @Description: 缓存查询单挑记录
	 * @param cacheName
	 * @param key
	 * @param clazz
	 * @param whereParam
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> T selectCacheOne(String cacheName, String key,
			Class<T> clazz, Query query, String... selectInner) {

		List<T> modelList = this.selectCacheList(cacheName, key, clazz, query,
				selectInner);

		if (CollectionsUtils.isEmpty(modelList)) {
			return null;
		}
		return modelList.get(0);
	}

	/**
	 * 
	 * @date 2018年11月16日 上午11:11:31
	 * @author lgk
	 * @Description: 查询列表 不需要分页
	 * @param clazz
	 * @param query
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> List<T> selectList(Class<T> clazz, Query query,
			String... selectInner) {
		return selectList(clazz, query, null, null, selectInner);
	}

	/**
	 * 
	 * @date 2018年11月16日 上午11:05:37
	 * @author lgk
	 * @Description: 查询list
	 * @param clazz
	 * @param query
	 * @param page
	 * @param pageSize
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> List<T> selectList(Class<T> clazz, Query query,
			Integer page, Integer pageSize, String... selectInner) {

		TableMapping tableMap = TableMapping.me();
		Table table = tableMap.getTable(clazz);
		List<Object> params = new ArrayList<>();

		String select = this.initializeSelect(selectInner);
		String inner = this.initializeInner(selectInner);

		try {

			T model = clazz.newInstance();

			StringBuilder preSql = new StringBuilder(
					select + " FROM " + table.getName() + " " + inner);

			String sql = prepareParms(preSql, query, params);
			String limit = setLimit(page, pageSize, params);

			List<T> modelList = model.find(sql.toString() + " " + limit,
					params.toArray());

			return modelList;

		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			new SQLException(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @date 2018年11月21日 上午11:54:09
	 * @author lgk
	 * @Description: 不需要分页缓存查询
	 * @param cacheName
	 * @param key
	 * @param clazz
	 * @param query
	 * @param selectInner
	 * @return
	 *
	 */
	public <T extends Model<T>> List<T> selectCacheList(String cacheName,
			String key, Class<T> clazz, Query query, String... selectInner) {
		return selectCacheList(cacheName, key, clazz, query, null, null,
				selectInner);
	}

	/**
	 * 
	 * @date 2018年11月16日 上午11:05:37
	 * @author lgk
	 * @Description: 查询list
	 * @param cacheName
	 * @param key
	 * @param clazz
	 * @param query
	 * @param page
	 * @param pageSize
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> List<T> selectCacheList(String cacheName,
			String key, Class<T> clazz, Query query, Integer page,
			Integer pageSize, String... selectInner) {

		TableMapping tableMap = TableMapping.me();

		Table table = tableMap.getTable(clazz);

		List<Object> params = new ArrayList<>();

		String select = this.initializeSelect(selectInner);

		String inner = this.initializeInner(selectInner);

		try {

			T model = clazz.newInstance();

			StringBuilder preSql = new StringBuilder(
					select + " FROM " + table.getName() + " " + inner);

			String sql = prepareParms(preSql, query, params);

			String limit = setLimit(page, pageSize, params);

			List<T> modelList = model.findByCache(cacheName, key,
					sql.toString() + " " + limit, params.toArray());

			return modelList;

		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			new SQLException(e.getMessage());
		}

		return null;
	}

	/**
	 * 
	 * @date 2018年11月16日 上午9:51:49
	 * @author lgk
	 * @Description: 动态分页
	 * @paramter
	 * @pDescription
	 * @param clazz
	 * @param query
	 *            whereParam.put("and a.id= " id)
	 * @param pageNumber
	 * @param pageSize
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> Page<T> page(Class<T> clazz, Query query,
			int pageNumber, int pageSize, String... selectInner) {

		List<Object> params = new ArrayList<>();

		Table table = tableMap.getTable(clazz);

		String select = this.initializeSelect(selectInner);
		String inner = this.initializeInner(selectInner);

		try {

			T model = clazz.newInstance();
			StringBuilder preSql = new StringBuilder(
					" FROM " + table.getName() + " " + inner);
			String sql = prepareParms(preSql, query, params);
			return model.paginate(pageNumber, pageSize, sql.contains(GROUP_BY),
					select, sql, params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			new SQLException(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @date 2018年11月21日 上午11:47:54
	 * @author lgk
	 * @Description: 缓存分页查询 一般用来缓存前十页的高频查询
	 * @param cacheName
	 * @param key
	 * @param clazz
	 * @param query
	 * @param pageNumber
	 * @param pageSize
	 * @param selectInner
	 * @return
	 *
	 */
	public <T extends Model<T>> Page<T> pageCache(String cacheName, String key,
			Class<T> clazz, Query query, int pageNumber, int pageSize,
			String... selectInner) {

		List<Object> params = new ArrayList<>();

		Table table = tableMap.getTable(clazz);

		String select = this.initializeSelect(selectInner);
		String inner = this.initializeInner(selectInner);

		try {

			T model = clazz.newInstance();
			StringBuilder preSql = new StringBuilder(
					" FROM " + table.getName() + " " + inner);

			String sql = prepareParms(preSql, query, params);

			return model.paginateByCache(cacheName, key, pageNumber, pageSize,
					sql.contains(GROUP_BY), select, sql, params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			new SQLException(e.getMessage());
		}
		return null;
	}

	public int updateEasy(Class<? extends Model<?>> clazz, Update update,
			Query query) {

		Assert.notNull(query, "query is null");

		Assert.notNull(query.getWhereParam(), "whereParam is null");

		Table table = tableMap.getTable(clazz);

		List<Object> params = new ArrayList<>();

		StringBuilder preSql = new StringBuilder(
				"update " + table.getName() + " set ");

		this.preUpdateSql(preSql, params, update);

		String sql = prepareParms(preSql, query, params);

		Object[] obj = params.toArray();

		Config config = DbKit.getConfig(clazz);

		return Db.use(config.getName()).update(sql.toString(), obj);

	}

	/**
	 * 
	 * @Description:删除
	 * @param clazz
	 * @param whereParam
	 * @param nexus
	 */
	public int delete(Class<? extends Model<?>> clazz, Query query) {

		if (query == null || MapUtils.isEmpty(query.getWhereParam())) {
			throw new RuntimeException("whereParam必须不为空");
		}

		Table table = tableMap.getTable(clazz);

		List<Object> params = new ArrayList<>();

		StringBuilder preSql = new StringBuilder(
				"delete from " + table.getName());

		String sql = this.prepareParms(preSql.append(" " + WHERE_FLAG + ""),
				query, params);

		Object[] obj = params.toArray();

		Config config = DbKit.getConfig(clazz);

		return Db.use(config.getName()).update(sql.toString(), obj);
	}

	/**
	 * 
	 * @Description: 判断表是否存在
	 * @param tableName
	 * @return
	 */
	public static boolean tableExists(String tableName) {
		return Db.findFirst(" show tables like '" + tableName + "'") != null;
	}

	/**
	 * 
	 * @date 2018年11月16日 上午11:25:04
	 * @author lgk
	 * @Description: 获取表名
	 * @param clazz
	 * @return
	 *
	 */
	public static <T extends Model<?>> String getName(Class<T> clazz) {
		Table t = TableMapping.me().getTable(clazz);
		return t == null ? null : t.getName() + " ";

	}

	/**
	 * 传入一个类的集合以及拼接符号还有类中需要拼接的字段名称,将集合用传入的符号拼接成字符串返回
	 * 
	 * @param <T
	 *            extends Model>
	 * @param list
	 * @param symbol
	 *            符号
	 * @param name
	 *            需要拼接的字段名称
	 * @return
	 * 
	 */
	public static <T extends Model<T>> String mergeList(List<T> list,
			String symbol, String name) {
		if (list == null || list.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (T t : list) {
			sb.append(t.get(name) + ",");
		}
		// 由于最后一个会是逗号,所以将逗号去除
		return sb.toString().substring(0, sb.length() - 1);
	}

	/**
	 * 将传入的时间段判断是否为空,如果传入的时间段为空则不添加 如果传入的时间段不为空则将拆分成开始时间和结束时间 将时间加入到传入的map的判断条件中
	 * 
	 * @param theTime
	 *            传入的时间段
	 * @param sql
	 *            传入的sql语句
	 * @param startTimeKey
	 *            传入开始时间的条件
	 * @param endTimeKey
	 *            传入结束时间的条件 cpx
	 */
	public static void addWhereParamTime(String theTime,
			HashMap<String, Object> whereParamMap, String startTimeKey,
			String endTimeKey) {
		if (theTime != null && !"".equals(theTime.trim())) {
			String theTimes[] = theTime.split(" - ");// 将时间进行拆分
			String startTime = theTimes[0];// 0为开始时间
			String endTime = theTimes[1];// 1为结束时间
			if (startTime != null && endTime != null) {
				whereParamMap.put(startTimeKey, startTime);
				whereParamMap.put(endTimeKey, endTime);
			}
		}
	}

	private String initializeSelect(String[] selectInner) {

		String select = SELECT_ALL_FIELD;

		if (selectInner != null && selectInner.length > 0) {
			select = selectInner[0];
		}

		return select;
	}

	private String initializeInner(String[] selectInner) {

		String inner = "";

		if (selectInner != null && selectInner.length > 1) {
			inner = selectInner[1];
		}
		return inner;
	}

	private static String removeFirstNexus(String key) {
		key = key.trim();
		Pattern pattern = Pattern.compile("^(?:and|or)\\b",
				Pattern.CASE_INSENSITIVE);
		return pattern.matcher(key).replaceAll("");
	}

	public String preUpdateSql(StringBuilder preSql, List<Object> params,
			Update update) {

		Assert.notNull(update);
		Map<String, Object> updateParam = update.getUpdateParam();
		Assert.notNull(updateParam);

		for (Entry<String, Object> entry : updateParam.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value == null || value.toString().trim().equals("")) {

				if (update.isNotEmpty(key) || value == null) {
					continue;
				}

				if (value.toString().trim().equals(""))
					preSql.append(key + "= null ,");

			} else {
				preSql.append(key + " =  ? ,");
				params.add(value);
			}
		}

		preSql.deleteCharAt(preSql.length() - 1);

		return preSql.toString();
	}

	/**
	 * 
	 * @date 2018年12月29日 上午11:43:28
	 * @author lgk
	 * @Description: 核心动态拼接sql方法
	 * @param preSql
	 * @param whereParam
	 * @param params
	 * @return
	 *
	 */
	public String prepareParms(StringBuilder preSql, Query query,
			List<Object> params) {
		boolean isFirst = true;
		StringBuilder whereSql = new StringBuilder();

		int len = 0;
		if (query != null && !MapUtils.isEmpty(query.getWhereParam())) {
			LinkedHashMap<String, Object> whereParam = query.getWhereParam();
			for (Entry<String, Object> entry : whereParam.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (value == null || value.toString().trim().equals("")) {
					if (query.isMust(key)) {
						throw new RuntimeException("key:" + key + " 参数为必填项");
					}
					continue;
				} else {

					if (isFirst) {
						key = removeFirstNexus(key);
						isFirst = false;
					}
					len++;
					if (key.contains("(")) {
						List<String> inList = (List<String>) value;
						if (CollectionsUtils.isEmpty(inList)) {
							continue;
						}
						whereSql.append(key);
						params.addAll(inList);
					} else {
						whereSql.append(key + " ? ");
						params.add(value);
					}

				}
			}
		}

		String sql = this.parseGroupBy(preSql.toString());
		if (!CollectionsUtils.isEmpty(params) && len > 0) {
			if (sql.contains(WHERE_FLAG))
				return sql.replace(WHERE_FLAG, " where " + whereSql);
			else
				return sql + " where " + whereSql;

		} else {
			return sql.replace(WHERE_FLAG, " ");
		}
	}

	private String parseGroupBy(String sql) {
		Pattern pattern = Pattern.compile("\\sgroup\\b\\sby\\b",
				Pattern.CASE_INSENSITIVE);
		return pattern.matcher(sql).replaceAll(GROUP_BY);
	}

	public static String setLimit(Integer page, Integer pageSize,
			List<Object> params) {
		String limit = "";
		if (page != null && page > 0 && pageSize != null && pageSize > 0) {
			limit = " limit ?,?";
			int start = (page - 1) * pageSize;
			params.add(start);
			params.add(pageSize);
		}

		return limit;
	}

}
