
package com.cn._gen.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by 小跑科技robot.
 */
@SuppressWarnings("serial")
public abstract class BaseSuggest<M extends BaseSuggest<M>> extends Model<M> implements IBean {

		public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setAccountId(java.lang.Integer accountId) {
		set("accountId", accountId);
	}

	public java.lang.Integer getAccountId() {
		return get("accountId");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return get("content");
	}

	public void setContact(java.lang.String contact) {
		set("contact", contact);
	}

	public java.lang.String getContact() {
		return get("contact");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}



}