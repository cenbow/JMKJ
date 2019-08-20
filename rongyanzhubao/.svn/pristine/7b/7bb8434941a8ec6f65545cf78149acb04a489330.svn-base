package com.cn.jm.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.cn.jm._dao.BasicsDao;
import com.cn.jm.util.SqlUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

public class BasicsService<T extends Model<T>> {

	static final BasicsDao DAO = new BasicsDao();;
	/** 对应的实例属性 */
	protected Class<T> clazz;
	/** 实例对应的表名 */
	protected String tableName;
	/** 对应的实例化对象 */
	protected T model;

	/**
	 * 根据id搜索出对应的信息
	 * 
	 * @param id
	 * @return
	 */
	public T selectById(Integer id) {
		return DAO.selectById(id, model);
	}

	/**
	 * 搜索全部信息 暂时不提供缓存
	 * 
	 * @return
	 */
	public List<T> selectAll() {
		return DAO.selectAll(tableName, model);
	}

	/**
	 * 根据键值对获取对应的第一条所有属性 如果不传则返回空
	 * 
	 * @param key 传入的key 自己加入判断符号 例如 id= , id!=
	 * @param value 传入的值不能为空 例如 1
	 * @return
	 */
	public List<T> selectByKeyAndValue(String key, Object value) {
		return StrKit.isBlank(key) ? null : DAO.selectByKeyAndValue(key, value, tableName, model);
	}

	/**
	 * 根据键值对获取对应的第一条所有属性 如果不传则返回空
	 * 
	 * @param key 传入的key 自己加入判断符号 例如 id= , id!=
	 * @param value 传入的值不能为空 例如 1
	 * @return
	 */
	public T selectOneByKeyAndValue(String key, Object value) {
		return StrKit.isBlank(key) ? null : DAO.selectOneByKeyAndValue(key, value, tableName, model);
	}

	/**
	 * 根据键值对获取对应的所有对应的搜索字段值 如果不传则返回空
	 * 
	 * @param fields 需搜索出的字段
	 * @param key 传入的key 自己加入判断符号 例如 id= , id!=
	 * @param value 传入的值不能为空 例如 1
	 * @return
	 */
	public List<T> selectByKeyAndValue(String fields, String key, Object value) {
		return StrKit.isBlank(key) ? null : DAO.selectByKeyAndValue(fields, key, value, tableName, model);
	}

	/**
	 * 根据键值对获取对应的第一条对应的搜索字段值 如果不传则返回空
	 * 
	 * @param fields 需搜索出的字段
	 * @param key 传入的key 自己加入判断符号 例如 id= , id!=
	 * @param value 传入的值不能为空 例如 1
	 * @return
	 */
	public T selectOneByKeyAndValue(String fields, String key, Object value) {
		return StrKit.isBlank(key) ? null : DAO.selectOneByKeyAndValue(key, value, tableName, model);
	}

	/**
	 * 根据多个键值对获取对应信息
	 * 
	 * @param keys 需要搜索的多个字段 键值需要开头需要自己加入 and(or),结尾加入判断符号
	 * @param values  需要搜索的多个字段对应值
	 */
	public List<T> selectByKeysAndValues(String[] keys, Object[] values) {
		return selectByKeysAndValues(SqlUtil.ALL, keys, values);
	}
	
	/**
	 * 根据多个键值对获取对应信息
	 * @param fields 需搜索出的字段
	 * @param keys 需要搜索的多个字段 键值需要开头需要自己加入 and(or),结尾加入判断符号
	 * @param values  需要搜索的多个字段对应值
	 */
	public List<T> selectByKeysAndValues(String fields, String[] keys, Object[] values) {
		return keys == null || values == null ? selectAll() : DAO.selectByKeysAndValues(fields, keys, values, tableName, model);
	}

	/**
	 * 根据键值对获取对应的第一条所有属性 如果不传则返回空
	 * 
	 * @param key 传入的key 自己加入判断符号 例如 id= , id!=
	 * @param value 传入的值不能为空 例如 1
	 * @return
	 */
	public T selectOneByMap(Map<String, Object> map) {
		return selectOneByMap(map, SqlUtil.ALL);
	}

	/**
	 * 根据键值对的map获取对应的第一条所有属性 如果不传则发生错误
	 * 
	 * @param map  键为 链接符(AND或者OR ...) + 字段 + 判断符号(= != > < ...)组成 ,
	 *            值则传入判断值(值为空则不对该值加入判断条件)
	 * @return
	 */
	public List<T> selectByMap(Map<String, Object> map, String fields, String orderField, String groupField) {
		return DAO.selectByMap(map, fields, orderField, groupField, tableName, model);
	}

	/**
	 * 根据键值对的map获取对应的第一条所有属性 如果不传则发生错误
	 * 
	 * @param map 键为 链接符(AND或者OR ...) + 字段 + 判断符号(= != > < ...)组成 ,
	 *            值则传入判断值(值为空则不对该值加入判断条件)
	 * @param orderField 根据什么字段倒序 可空
	 * @param groupField 根据那个字段分组 可空
	 * @return
	 */
	public List<T> selectByMap(Map<String, Object> map, String orderField, String groupField) {
		return selectByMap(map, SqlUtil.ALL, orderField, groupField);
	}

	/**
	 * 根据键值对的map获取对应的第一条所有属性 如果不传则发生错误
	 * 
	 * @param map 键为 链接符(AND或者OR ...) + 字段 + 判断符号(= != > < ...)组成 ,
	 *            值则传入判断值(值为空则不对该值加入判断条件)
	 * @param fields 需要搜索出的字段
	 * @return
	 */
	public T selectOneByMap(Map<String, Object> map, String fields) {
		return DAO.selectOneByMap(map, fields, tableName, model);
	}

	public boolean save(T t) {
		return DAO.save(t);
	}

	public boolean update(T t) {
		return DAO.update(t);
	}
	
	public boolean delete(T t) {
		return DAO.delete(t);
	}

	public boolean deleteById(Integer id) {
		return DAO.deleteById(id, model);
	}

	public boolean deleteByIds(Integer[] ids) {
		if (ids == null || ids.length == 0) {
			return false;
		}
		try {
			DAO.deleteByIds(ids, tableName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void initSubclassesActualType() {
		// 获取真正实例的对象类型
		Class<?> clazz = getClass();
		// 如果类型不是超类Object
		while (clazz != Object.class) {
			// 获取父类的泛型信息
			Type type = clazz.getGenericSuperclass();
			// 判断父类有没有泛型的相关信息
			if (type instanceof ParameterizedType) {// 有,则将信息存储起来
				this.clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
				break;
			} else {// 没有的话再次对父类的父类进行获取信息
				clazz = clazz.getSuperclass();
			}
		}
	}

	/**
	 * 将子类的泛型类型获取实例化的DAO
	 * 
	 * @throws Exception
	 */
	private void initSubclassesMethodDao() {
		try {
			this.model = ((T) clazz.newInstance()).dao();
		} catch (Exception e) {
			System.out.println(getClass() + " 中实例化属性失败 : " + clazz);
			e.printStackTrace();
		}
	}

	public BasicsService() {
		init();
	}

	private void init() {
		initSubclassesActualType();
		initSubclassesMethodDao();
		tableName = TableMapping.me().getTable(clazz).getName();
	}
}
