package com.cn.jm._dao._base.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.alibaba.druid.util.StringUtils;
import com.cn.jm.core.utils.util.CollectionsUtils;
import com.cn.jm.core.utils.util.MapUtils;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

@SuppressWarnings("unchecked")
public class JMCommonDao {

	public final static JMCommonDao jmd = new JMCommonDao();

	public static final String IN = " in ";

	public static final String NOT_IN = " not in ";

	public static TableMapping tableMap = TableMapping.me();

	public static final String WHERE_FLAG = " :where ";

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
	public <T extends Model<T>> T selectOne(Class<T> clazz,
			LinkedHashMap<String, Object> whereParam, String... selectInner) {

		List<T> modelList = this.selectList(clazz, whereParam, selectInner);

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
			Class<T> clazz, LinkedHashMap<String, Object> whereParam,
			String... selectInner) {

		List<T> modelList = this.selectCacheList(cacheName, key, clazz,
				whereParam, selectInner);

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
	 * @param whereParam
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> List<T> selectList(Class<T> clazz,
			LinkedHashMap<String, Object> whereParam, String... selectInner) {
		return selectList(clazz, whereParam, null, null, selectInner);
	}

	/**
	 * 
	 * @date 2018年11月16日 上午11:05:37
	 * @author lgk
	 * @Description: 查询list
	 * @param clazz
	 * @param whereParam
	 * @param page
	 * @param pageSize
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> List<T> selectList(Class<T> clazz,
			LinkedHashMap<String, Object> whereParam, Integer page,
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

			String sql = prepareParms(preSql, whereParam, params);
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
	 * @param whereParam
	 * @param selectInner
	 * @return
	 *
	 */
	public <T extends Model<T>> List<T> selectCacheList(String cacheName,
			String key, Class<T> clazz,
			LinkedHashMap<String, Object> whereParam, String... selectInner) {
		return selectCacheList(cacheName, key, clazz, whereParam, null, null,
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
	 * @param whereParam
	 * @param page
	 * @param pageSize
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> List<T> selectCacheList(String cacheName,
			String key, Class<T> clazz,
			LinkedHashMap<String, Object> whereParam, Integer page,
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

			String sql = prepareParms(preSql, whereParam, params);

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
	 * @param whereParam
	 *            whereParam.put("and a.id= " id)
	 * @param pageNumber
	 * @param pageSize
	 * @param selectInner
	 *            select * , inner a inner join xxx on xxx.id = xxx.id :where
	 *            group by xxx.id order by xxx.id 第一个是select 第二个是左右连接
	 * @return
	 *
	 */
	public <T extends Model<T>> Page<T> page(Class<T> clazz,
			LinkedHashMap<String, Object> whereParam, int pageNumber,
			int pageSize, String... selectInner) {

		List<Object> params = new ArrayList<>();

		Table table = tableMap.getTable(clazz);

		String select = this.initializeSelect(selectInner);
		String inner = this.initializeInner(selectInner);

		try {

			T model = clazz.newInstance();
			StringBuilder preSql = new StringBuilder(
					" FROM " + table.getName() + " " + inner);
			String sql = prepareParms(preSql, whereParam, params);
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
	 * @param whereParam
	 * @param pageNumber
	 * @param pageSize
	 * @param selectInner
	 * @return
	 *
	 */
	public <T extends Model<T>> Page<T> pageCache(String cacheName, String key,
			Class<T> clazz, LinkedHashMap<String, Object> whereParam,
			int pageNumber, int pageSize, String... selectInner) {

		List<Object> params = new ArrayList<>();

		Table table = tableMap.getTable(clazz);

		String select = this.initializeSelect(selectInner);
		String inner = this.initializeInner(selectInner);

		try {

			T model = clazz.newInstance();
			StringBuilder preSql = new StringBuilder(
					" FROM " + table.getName() + " " + inner);

			String sql = prepareParms(preSql, whereParam, params);

			return model.paginateByCache(cacheName, key, pageNumber, pageSize,
					sql.contains(GROUP_BY), select, sql, params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			new SQLException(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @date 2018年11月17日 上午10:38:40
	 * @author lgk
	 * @Description: 动态更新参数 强校验模式 whereparam必须完整
	 * @param clazz
	 * @param updateParam
	 * @param whereParam
	 *            参数不允许为空 如需空则调用另外一个方法或者自己写sql
	 * @return
	 *
	 */
	public int updateOne(Class<? extends Model<?>> clazz,
			Map<String, Object> updateParam,
			LinkedHashMap<String, Object> whereParam) {

		if (MapUtils.isEmpty(whereParam)) {
			throw new RuntimeException("whereParam必须不为空");
		}

		return this.update(clazz, updateParam, whereParam, whereParam.size());
	}

	/**
	 * 
	 * @date 2018年11月17日 上午10:38:40
	 * @author lgk
	 * @Description: 动态更新参数 弱校验模式 whereparam 参数允许动态为空 但必须要有一个
	 * @param clazz
	 * @param updateParam
	 * @param whereParam
	 *            参数不允许为空 如需空则调用另外一个方法或者自己写sql
	 * @return
	 *
	 */
	public int updateEasy(Class<? extends Model<?>> clazz,
			Map<String, Object> updateParam,
			LinkedHashMap<String, Object> whereParam) {
		if (MapUtils.isEmpty(whereParam)) {
			throw new RuntimeException("whereParam必须不为空");
		}

		return this.update(clazz, updateParam, whereParam, 1);
	}

	/**
	 * 
	 * @date 2018年11月20日 下午1:04:33
	 * @author lgk
	 * @Description: 批量更新
	 * @param clazz
	 * @param updateParam
	 * @return
	 *
	 */
	@Deprecated
	public int batchUpdate(Class<? extends Model<?>> clazz,
			Map<String, Object> updateParam) {
		return this.update(clazz, updateParam, null, -1);
	}

	protected int update(Class<? extends Model<?>> clazz,
			Map<String, Object> updateParam,
			LinkedHashMap<String, Object> whereParam, int vaildLen) {

		if (MapUtils.isEmpty(updateParam)) {
			throw new RuntimeException("updateParam必须不为空");
		}

		Table table = tableMap.getTable(clazz);

		List<Object> params = new ArrayList<>();

		StringBuilder preSql = new StringBuilder(
				"update " + table.getName() + " set ");

		this.preUpdateSql(preSql, params, updateParam);

		if (CollectionsUtils.isEmpty(params)) {
			return 0;
		}

		int len = params.size();

		String sql = prepareParms(preSql, whereParam, params);

		len = params.size() - len;

		// 判断条件
		if (len < vaildLen)
			throw new RuntimeException("whereParam 参数不能为空");

		Object[] obj = params.toArray();

		Config config = DbKit.getConfig(clazz);

		return Db.use(config.getName()).update(sql.toString(), obj);

	}

	/**
	 * 
	 * @Description:删除 强校验模式
	 * @param clazz
	 * @param whereParam
	 * @param nexus
	 */
	public int delete(Class<? extends Model<?>> clazz,
			LinkedHashMap<String, Object> whereParam) {

		if (MapUtils.isEmpty(whereParam)) {
			throw new RuntimeException("whereParam必须不为空");
		}

		Table table = tableMap.getTable(clazz);

		List<Object> params = new ArrayList<>();

		StringBuilder preSql = new StringBuilder(
				"delete from " + table.getName());

		String sql = this.prepareParms(preSql.append(WHERE_FLAG), whereParam,
				params);

		if (params.size() != params.size())
			throw new RuntimeException("whereParam 参数不能为空");

		Object[] obj = params.toArray();

		Config config = DbKit.getConfig(clazz);

		return Db.use(config.getName()).update(sql.toString(), obj);
	}

	/**
	 * 
	 * @Description:JMCOMMONDao put like
	 * @param whereParam
	 * @param key
	 * @param value
	 */
	public static void putLike(Map<String, Object> whereParam, String key,
			String value) {

		if (StringUtils.isEmpty(value)) {
			return;
		}
		whereParam.put(key + " like ", "%" + value + "%");
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

	public static int putIn(Map<String, Object> whereParam, String key,
			String ids) {
		return putNotOrIN(whereParam, key, ids, IN);
	}

	public static int putIn(Map<String, Object> whereParam, String ids) {
		return putIn(whereParam, "id", ids);
	}

	public static int putNotIn(Map<String, Object> whereParam, String ids) {
		return putNotIn(whereParam, "id", ids);
	}

	public static int putNotIn(Map<String, Object> whereParam, String key,
			String ids) {
		return putNotOrIN(whereParam, key, ids, NOT_IN);
	}

	private static int putNotOrIN(Map<String, Object> whereParam, String key,
			String ids, String inNot) {

		if (StringUtils.isEmpty(ids))
			return 0;

		String[] idstrs = ids.split(",");

		List<String> param = new ArrayList<>();

		if (idstrs.length == 1) {
			param.add(idstrs[0]);
			whereParam.put(key + inNot + "( ? )", param);
			return 1;
		}

		int index = 1;

		String sql = "";

		for (String idstr : idstrs) {

			if (StringUtils.isEmpty(idstr))
				continue;

			if (index == 1)
				sql = key + inNot + "( ?";
			else
				sql += ",?";

			if (index == idstrs.length)
				sql += ")";
			param.add(idstr);
			index++;
		}
		whereParam.put(sql, param);

		return idstrs.length;
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

	private String preUpdateSql(StringBuilder preSql, List<Object> params,
			Map<String, Object> updateParam) {
		for (Entry<String, Object> entry : updateParam.entrySet()) {
			if (entry.getValue() == null
					|| entry.getValue().toString().trim().equals(""))
				continue;
			else {
				preSql.append(entry.getKey() + " =  ? ,");
				params.add(entry.getValue());
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
	public String prepareParms(StringBuilder preSql,
			LinkedHashMap<String, Object> whereParam, List<Object> params) {

		boolean isFirst = true;

		StringBuilder whereSql = new StringBuilder();

		if (!MapUtils.isEmpty(whereParam)) {

			for (Entry<String, Object> entry : whereParam.entrySet()) {

				if (entry.getValue() == null
						|| entry.getValue().toString().trim().equals(""))
					continue;
				else {

					String key = entry.getKey();

					if (isFirst) {
						key = removeFirstNexus(key);
						isFirst = false;
					}

					if (entry.getKey().contains("(")) {

						List<String> inList = (List<String>) entry.getValue();

						if (CollectionsUtils.isEmpty(inList)) {
							continue;
						}
						whereSql.append(key);
						params.addAll(inList);

					} else {
						whereSql.append(key + " ? ");
						params.add(entry.getValue());
					}

				}
			}
		}

		String sql = this.parseGroupBy(preSql.toString());

		if (!CollectionsUtils.isEmpty(params)) {

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

	private static String setLimit(Integer page, Integer pageSize,
			List<Object> params) {
		String limit = "";

		if (page != null && page > 0 && pageSize != null && pageSize > 0) {

			limit = " limit ?,?";

			int start = (page - 1) * pageSize;

			int end = (page - 1) * pageSize + pageSize;

			params.add(start);

			params.add(end);
		}

		return limit;
	}

}
