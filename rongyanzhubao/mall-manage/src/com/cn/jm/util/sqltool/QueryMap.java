package com.cn.jm.util.sqltool;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class QueryMap {

	protected List<Param> list;

	protected String nexus;

	public QueryMap() {
		this.list = new ArrayList<>();
	}

	public QueryMap(String nexus) {
		this.nexus = nexus;
		this.list = new ArrayList<>();
	}

	public QueryMap add(String key, Object value, String nexus) {
		Param param = new Param();
		param.setNexus(nexus);
		param.setKey(key);
		param.setValue(value);
		list.add(param);
		return this;
	}

	public QueryMap add(String key, Object value) {
		Param param = new Param();
		param.setNexus(StringUtils.isEmpty(nexus) ? " and " : nexus);
		param.setKey(key);
		param.setValue(value);
		list.add(param);
		return this;
	}

	class Param {

		private String nexus;

		private String key;

		private Object value;

		public String getNexus() {
			return nexus;
		}

		public void setNexus(String nexus) {
			this.nexus = nexus;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

	}

}
