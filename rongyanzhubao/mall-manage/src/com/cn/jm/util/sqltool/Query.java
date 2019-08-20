package com.cn.jm.util.sqltool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.cn.jm.core.utils.util.MapUtils;

public class Query {

	private LinkedHashMap<String, Object> whereParam;

	private Map<String, Boolean> notMustParam;

	public Query() {
		this.whereParam = new LinkedHashMap<>();
		this.notMustParam = new HashMap<>();
	}

	public Query put(String key, Object value) {
		this.whereParam.put(key, value);
		return this;
	}

	public Query put(String key, Object value, boolean isMust) {
		this.whereParam.put(key, value);
		if (!isMust) {
			this.notMustParam.put(key, isMust);
		}
		return this;
	}

	public LinkedHashMap<String, Object> getWhereParam() {
		return whereParam;
	}

	public void setWhereParam(LinkedHashMap<String, Object> whereParam) {
		this.whereParam = whereParam;
	}

	public Map<String, Boolean> getNotMustParam() {
		return notMustParam;
	}

	public void setNotMustParam(Map<String, Boolean> notMustParam) {
		this.notMustParam = notMustParam;
	}

	public boolean isMust(String key) {
		Boolean b = this.notMustParam.get(key);
		return b == null || b;
	}

	/**
	 * 
	 * @Description:JMCOMMONDao put like
	 * @param whereParam
	 * @param key
	 * @param value
	 */
	public void putLike(String key, String value, boolean isMust) {
		if (StringUtils.isEmpty(value)) {
			return;
		}

		String sql = key + " like ";

		whereParam.put(sql, "%" + value + "%");

		if (!isMust) {
			this.notMustParam.put(sql, isMust);
		}
	}

	public void putSuffixLike(String key, String value, boolean isMust) {
		if (StringUtils.isEmpty(value)) {
			return;
		}
		String sql = key + " like ";
		whereParam.put(sql, value + "%");
		if (!isMust) {
			this.notMustParam.put(sql, isMust);
		}
	}

	public int putIn(String key, String ids) {
		return putIn(key, ids, true);
	}

	public int putIn(String key, String ids, boolean isMust) {
		return putNotOrIN(key, ids, InOrNo.IN, isMust);
	}

	public int putNotIn(String key, String ids) {
		return putNotIn(key, ids, true);
	}

	public int putNotIn(String key, String ids, boolean isMust) {
		return putNotOrIN(key, ids, InOrNo.NOT_IN, isMust);
	}

	public int putIn(String key, Object[] ids) {
		return putIn(key, ids, true);
	}

	public int putIn(String key, Object[] ids, boolean isMust) {
		return putNotOrIN(key, ids, InOrNo.IN, isMust);
	}

	public int putNotIn(String key, Object[] ids) {
		return putNotIn(key, ids, true);
	}

	public int putNotIn(String key, Object[] ids, boolean isMust) {
		return putNotOrIN(key, ids, InOrNo.NOT_IN, isMust);
	}

	public int putNotOrIN(String key, String ids, InOrNo neux, boolean isMust) {
		if (StringUtils.isEmpty(ids))
			return 0;

		String[] idstrs = ids.split(",");

		String[] fifter = new String[idstrs.length];

		// 过滤掉空串
		int count = 0;
		for (String idstr : idstrs) {
			if (StringUtils.isEmpty(idstr))
				continue;
			fifter[count] = idstr;
			count++;
		}

		idstrs = fifter;
		List<Object> param = new ArrayList<>();
		String sql = "";
		if (idstrs.length == 1) {
			param.add(idstrs[0]);
			sql = key + "=";
		} else {
			int index = 1;
			for (String idstr : idstrs) {
				if (StringUtils.isEmpty(idstr))
					continue;

				if (index == 1)
					sql = key + neux.getNeux() + "( ?";
				else
					sql += ",?";

				if (index == idstrs.length)
					sql += ")";
				param.add(idstr);
				index++;
			}
		}

		whereParam.put(sql, param.size() == 1 ? param.get(0) : param);

		if (!isMust) {
			this.notMustParam.put(sql, isMust);
		}
		return idstrs.length;
	}

	public int putNotOrIN(String key, Object[] value, InOrNo neux,
			boolean isMust) {
		if (value == null || value.length == 0)
			return 0;

		List<Object> param = new ArrayList<>();
		String sql = "";
		if (value.length == 1) {
			param.add(value[0]);
			sql = key + "=";
		} else {
			int index = 1;
			for (Object idstr : value) {

				if (index == 1)
					sql = key + neux.getNeux() + "( ?";
				else
					sql += ",?";

				if (index == value.length)
					sql += ")";
				param.add(idstr);
				index++;
			}
		}

		whereParam.put(sql, param.size() == 1 ? param.get(0) : param);
		if (!isMust) {
			this.notMustParam.put(sql, isMust);
		}
		return value.length;
	}

	public static String putNotOrIN(String key, String ids, InOrNo neux,
			List<Object> params) {

		if (StringUtils.isEmpty(ids))
			return null;

		String[] idstrs = ids.split(",");

		String[] fifter = new String[idstrs.length];

		// 过滤掉空串
		int count = 0;
		for (String idstr : idstrs) {

			if (StringUtils.isEmpty(idstr))
				continue;

			fifter[count] = idstr;
			count++;
		}

		idstrs = fifter;

		String sql = "";

		if (idstrs.length == 1) {
			params.add(idstrs[0]);
			sql = key + neux.getNeux() + "( ? )";
		}

		else {
			int index = 1;

			for (String idstr : idstrs) {

				if (StringUtils.isEmpty(idstr))
					continue;

				if (index == 1)
					sql = key + neux.getNeux() + "( ?";
				else
					sql += ",?";

				if (index == idstrs.length)
					sql += ")";
				params.add(idstr);
				index++;
			}
		}

		return sql;
	}

	public Query clear() {
		if (!MapUtils.isEmpty(whereParam)) {
			this.whereParam.clear();
		}
		if (!MapUtils.isEmpty(notMustParam)) {
			this.notMustParam.clear();
		}
		return this;
	}

}
