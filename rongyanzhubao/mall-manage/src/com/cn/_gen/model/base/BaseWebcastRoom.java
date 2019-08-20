
package com.cn._gen.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by 小跑科技robot.
 */
@SuppressWarnings("serial")
public abstract class BaseWebcastRoom<M extends BaseWebcastRoom<M>> extends Model<M> implements IBean {

		public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setImage(java.lang.String image) {
		set("image", image);
	}

	public java.lang.String getImage() {
		return get("image");
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

	public void setAddress(java.lang.String address) {
		set("address", address);
	}

	public java.lang.String getAddress() {
		return get("address");
	}

	public void setRoomIntroduction(java.lang.String roomIntroduction) {
		set("roomIntroduction", roomIntroduction);
	}

	public java.lang.String getRoomIntroduction() {
		return get("roomIntroduction");
	}

	public void setAnchorIntroduction(java.lang.String anchorIntroduction) {
		set("anchorIntroduction", anchorIntroduction);
	}

	public java.lang.String getAnchorIntroduction() {
		return get("anchorIntroduction");
	}

	public void setAnnouncement(java.lang.String announcement) {
		set("announcement", announcement);
	}

	public java.lang.String getAnnouncement() {
		return get("announcement");
	}

	public void setAccountId(java.lang.Integer accountId) {
		set("accountId", accountId);
	}

	public java.lang.Integer getAccountId() {
		return get("accountId");
	}

	public void setRoomNumber(java.lang.String roomNumber) {
		set("roomNumber", roomNumber);
	}

	public java.lang.String getRoomNumber() {
		return get("roomNumber");
	}

	public void setState(java.lang.Integer state) {
		set("state", state);
	}

	public java.lang.Integer getState() {
		return get("state");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public void setFollowNumber(java.lang.Integer followNumber) {
		set("followNumber", followNumber);
	}

	public java.lang.Integer getFollowNumber() {
		return get("followNumber");
	}

	public void setZanNumber(java.lang.Integer zanNumber) {
		set("zanNumber", zanNumber);
	}

	public java.lang.Integer getZanNumber() {
		return get("zanNumber");
	}

	public void setRobotNumber(java.lang.Integer robotNumber) {
		set("robotNumber", robotNumber);
	}

	public java.lang.Integer getRobotNumber() {
		return get("robotNumber");
	}

	public void setPeakNumber(java.lang.Integer peakNumber) {
		set("peakNumber", peakNumber);
	}

	public java.lang.Integer getPeakNumber() {
		return get("peakNumber");
	}

	public void setRoomRecordId(java.lang.Integer roomRecordId) {
		set("roomRecordId", roomRecordId);
	}

	public java.lang.Integer getRoomRecordId() {
		return get("roomRecordId");
	}



}