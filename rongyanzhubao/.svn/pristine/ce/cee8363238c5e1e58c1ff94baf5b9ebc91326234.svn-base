package com.cn.jm.util.sqltool;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Update {

	private LinkedHashMap<String, Object> updateParam;

	private Map<String, Boolean> notEmpty;

	private Map<String, Boolean> mustParam;

	public Update() {
		this.updateParam = new LinkedHashMap<>();
		this.notEmpty = new HashMap<>();
	}

	public Update put(String key, Object value) {
		this.updateParam.put(key, value);
		return this;
	}

	public Update put(String key, Object value, boolean notEmpty) {
		this.updateParam.put(key, value);
		if (!notEmpty) {
			this.notEmpty.put(key, notEmpty);
		}
		return this;
	}

	public LinkedHashMap<String, Object> getUpdateParam() {
		return updateParam;
	}

	public void setUpdateParam(LinkedHashMap<String, Object> updateParam) {
		this.updateParam = updateParam;
	}

	public Map<String, Boolean> getNotEmpty() {
		return notEmpty;
	}

	public void setNotEmpty(Map<String, Boolean> notEmpty) {
		this.notEmpty = notEmpty;
	}

	public Map<String, Boolean> getMustParam() {
		return mustParam;
	}

	public void setMustParam(Map<String, Boolean> mustParam) {
		this.mustParam = mustParam;
	}

	public boolean isNotEmpty(String key) {
		Boolean b = this.notEmpty.get(key);
		return b == null || b;
	}

	public boolean isMust(String key) {
		Boolean b = this.mustParam.get(key);
		return b != null && b;
	}

}
