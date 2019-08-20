
package com.cn._gen.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by 小跑科技robot.
 */
@SuppressWarnings("serial")
public abstract class BaseRole<M extends BaseRole<M>> extends Model<M> implements IBean {

		public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setDesc(java.lang.String desc) {
		set("desc", desc);
	}

	public java.lang.String getDesc() {
		return get("desc");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public void setUpdateTime(java.util.Date updateTime) {
		set("updateTime", updateTime);
	}

	public java.util.Date getUpdateTime() {
		return get("updateTime");
	}

	public void setParent(java.lang.Integer parent) {
		set("parent", parent);
	}

	public java.lang.Integer getParent() {
		return get("parent");
	}

	public void setSeries(java.lang.Integer series) {
		set("series", series);
	}

	public java.lang.Integer getSeries() {
		return get("series");
	}



}