
package com.cn._gen.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by 小跑科技robot.
 */
@SuppressWarnings("serial")
public abstract class BaseSpecAttribute<M extends BaseSpecAttribute<M>> extends Model<M> implements IBean {

		public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setSpecId(java.lang.Integer specId) {
		set("specId", specId);
	}

	public java.lang.Integer getSpecId() {
		return get("specId");
	}

	public void setEnName(java.lang.String enName) {
		set("enName", enName);
	}

	public java.lang.String getEnName() {
		return get("enName");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setState(java.lang.Integer state) {
		set("state", state);
	}

	public java.lang.Integer getState() {
		return get("state");
	}

	public void setSpSort(java.lang.Integer spSort) {
		set("spSort", spSort);
	}

	public java.lang.Integer getSpSort() {
		return get("spSort");
	}



}