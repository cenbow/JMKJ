package com.cn.jm._dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cn.jm.util.SqlUtil;
//import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/**
 * @author Administrator
 */
public class BasicsDao {
	/**
	 * 查询对应表名的全部信息 暂时不提供缓存
	 * 
	 * @param tableName
	 * @param m
	 * @return
	 */
	public <M extends Model<M>> List<M> selectAll(String tableName, M m) {
		return m.find(getSelectSql(SqlUtil.ALL, tableName).toString());
	}

	/**
	 * 根据键值对获取对应的第一条属性
	 * 
	 * @param key 传入的key 自己加入判断符号
	 * @param value 传入的值不能为空
	 * @return
	 */
	public <M extends Model<M>> List<M> selectByKeyAndValue(String key, Object value, String tableName, M model) {
		return selectByKeyAndValue(SqlUtil.ALL, key, value, tableName, model);
	}

	/**
	 * 根据键值对获取对应的第一条属性
	 * 
	 * @param key 传入的key 自己加入判断符号
	 * @param value 传入的值不能为空
	 * @return
	 */
	public <M extends Model<M>> M selectOneByKeyAndValue(String key, Object value, String tableName, M model) {
		return selectOneByKeyAndValue(SqlUtil.ALL, key, value, tableName, model);
	}

	/**
	 * 根据键值对获取对应的第一条属性
	 * 
	 * @param fields 需搜索出的字段
	 * @param key 传入的key 自己加入判断符号
	 * @param value 传入的值不能为空
	 * @return
	 */
	public <M extends Model<M>> M selectOneByKeyAndValue(String fields, String key, Object value, String tableName,
			M model) {
		StringBuilder sql = getSelectSql(fields, tableName, SqlUtil.WHERE, key, SqlUtil.replaceObj(value),
				SqlUtil.LIMIT, 1);
		return model.findFirst(sql.toString());
	}

	/**
	 * 根据键值对获取对应的第一条属性
	 * 
	 * @param fields 需搜索出的字段
	 * @param key 传入的key 自己加入判断符号
	 * @param value 传入的值不能为空
	 * @return
	 */
	public <M extends Model<M>> List<M> selectByKeyAndValue(String fields, String key, Object value, String tableName,
			M model) {
		StringBuilder sql = getSelectSql(fields, tableName, SqlUtil.WHERE, key, SqlUtil.replaceObj(value));
		return model.find(sql.toString());
	}

	/**
	 * 根据多个字段对应的值搜索出信息 如果存在字段多于值的时候将多余的字段去除 值多于字段则去除多余的值
	 * 
	 * @param keys 需要搜索的多个字段 键值需要开头需要自己加入 and(or),结尾加入判断符号
	 * @param values 需要搜索的多个字段对应值
	 * @param tableName 表名
	 * @param model 对应的实体
	 * @return
	 */
	public <M extends Model<M>> List<M> selectByKeysAndValues(String[] keys, Object[] values, String tableName,
			M model) {
		return selectByKeysAndValues(SqlUtil.ALL, keys, values, tableName, model);
	}

	/**
	 * 根据多个字段对应的值搜索出信息 如果存在字段多于值的时候将多余的字段去除 值多于字段则去除多余的值
	 * 
	 * @param fields 需搜索出的字段
	 * @param keys 需要搜索的多个字段 键值需要开头需要自己加入 and(or),结尾加入判断符号
	 * @param values  需要搜索的多个字段对应值
	 * @param tableName 表名
	 * @param model  对应的实体
	 * @return
	 */
	public <M extends Model<M>> List<M> selectByKeysAndValues(String fields, String[] keys, Object[] values,
			String tableName, M model) {
		int minNum = keys.length > values.length ? values.length : keys.length;
		StringBuilder selectSql = getSelectSql(fields, tableName, SqlUtil._WHERE);
		for (int i = 0; i < minNum; i++) {
			SqlUtil.addWhere(selectSql, keys[i], values[i]);
		}
		SqlUtil.changeWhere(selectSql);
		return model.find(selectSql.toString());
	}

	/** 根据id集合删除对应的信息,对传入的ids重复进行处理,返回成功删除数量 */
	public int deleteByIds(Integer[] ids, String tableName) {
		StringBuilder deleteSql = new StringBuilder();
		deleteSql.append(SqlUtil.DELETE).append(SqlUtil.FROM).append(tableName).append(SqlUtil.WHERE).append("id")
				.append(SqlUtil.IN).append(SqlUtil.changeArrayNotRepeat(ids));
		return Db.update(deleteSql.toString());
	}

	public <M extends Model<M>> M selectById(Integer id, M model) {
		return model.findById(id);
	}

	public <M extends Model<M>> M selectOneByMap(Map<String, Object> map, String fields, String tableName, M model) {
		StringBuilder sql = getSelectSql(fields, tableName).append(SqlUtil._WHERE);
		for (Entry<String, Object> entry : map.entrySet()) {
			SqlUtil.addWhere(sql, entry.getKey(), entry.getValue());
		}
		sql.append(SqlUtil.LIMIT).append(1);
		SqlUtil.changeWhere(sql);
		return model.findFirst(sql.toString());
	}

	public <M extends Model<M>> List<M> selectByMap(Map<String, Object> map, String fields, String orderField,
			String groupField, String tableName, M model) {
		StringBuilder sql = getSelectSql(fields, tableName).append(SqlUtil._WHERE);
		for (Entry<String, Object> entry : map.entrySet()) {
			SqlUtil.addWhere(sql, entry.getKey(), entry.getValue());
		}
		SqlUtil.addSql(sql, SqlUtil.GROUP_BY, groupField);
		SqlUtil.addOrderBySql(sql, orderField, SqlUtil.DESC);
		SqlUtil.changeWhere(sql);
		return model.find(sql.toString());
	}

	// 暂未开放缓存获取信息
	// private <M extends Model<M>> List<M> selectByCache(M model,String
	// cacheName,String sql) {
	// return model.findByCache(cacheName,HashKit.md5(sql), sql);
	// }
	//
	// private <M extends Model<M>> M selectOneByCache(M model,String
	// cacheName,String sql) {
	// return model.findFirstByCache(cacheName,HashKit.md5(sql), sql);
	// }
	//
	// private StringBuilder getSelectOneByKeyAndValueSql(String fields,String
	// tableName,String key,String value) {
	// return getSelectSql(fields,tableName,SqlUtil.WHERE,key,value);
	// }

	public <M extends Model<M>> boolean save(M m) {
		return m.save();
	}

	public <M extends Model<M>> boolean update(M m) {
		return m.update();
	}

	public <M extends Model<M>> boolean delete(M m) {
		return m.delete();
	}

	public <M extends Model<M>> boolean deleteById(Integer id, M m) {
		return m.deleteById(id);
	}

	private StringBuilder getSelectSql(String fields, String tableName, Object... appendSqls) {
		StringBuilder selectSql = new StringBuilder();
		selectSql.append(SqlUtil.SELECT).append(fields).append(SqlUtil.FROM).append(tableName);
		for (Object appendSql : appendSqls) {
			selectSql.append(appendSql);
		}
		return selectSql;
	}

}
