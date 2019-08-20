
package com.cn._gen.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by 小跑科技robot.
 */
@SuppressWarnings("serial")
public abstract class BaseRefundOrder<M extends BaseRefundOrder<M>> extends Model<M> implements IBean {

		public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setNo(java.lang.String no) {
		set("no", no);
	}

	public java.lang.String getNo() {
		return get("no");
	}

	public void setOrderId(java.lang.Integer orderId) {
		set("orderId", orderId);
	}

	public java.lang.Integer getOrderId() {
		return get("orderId");
	}

	public void setUserId(java.lang.Integer userId) {
		set("userId", userId);
	}

	public java.lang.Integer getUserId() {
		return get("userId");
	}

	public void setState(java.lang.Integer state) {
		set("state", state);
	}

	public java.lang.Integer getState() {
		return get("state");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return get("type");
	}

	public void setMoney(java.math.BigDecimal money) {
		set("money", money);
	}

	public java.math.BigDecimal getMoney() {
		return get("money");
	}

	public void setReason(java.lang.String reason) {
		set("reason", reason);
	}

	public java.lang.String getReason() {
		return get("reason");
	}

	public void setRefuseReason(java.lang.String refuseReason) {
		set("refuseReason", refuseReason);
	}

	public java.lang.String getRefuseReason() {
		return get("refuseReason");
	}

	public void setLogisticsNo(java.lang.String logisticsNo) {
		set("logisticsNo", logisticsNo);
	}

	public java.lang.String getLogisticsNo() {
		return get("logisticsNo");
	}

	public void setLogisticsName(java.lang.String logisticsName) {
		set("logisticsName", logisticsName);
	}

	public java.lang.String getLogisticsName() {
		return get("logisticsName");
	}

	public void setImg1(java.lang.String img1) {
		set("img1", img1);
	}

	public java.lang.String getImg1() {
		return get("img1");
	}

	public void setImg2(java.lang.String img2) {
		set("img2", img2);
	}

	public java.lang.String getImg2() {
		return get("img2");
	}

	public void setImg3(java.lang.String img3) {
		set("img3", img3);
	}

	public java.lang.String getImg3() {
		return get("img3");
	}

	public void setImg4(java.lang.String img4) {
		set("img4", img4);
	}

	public java.lang.String getImg4() {
		return get("img4");
	}

	public void setImg5(java.lang.String img5) {
		set("img5", img5);
	}

	public java.lang.String getImg5() {
		return get("img5");
	}

	public void setImg6(java.lang.String img6) {
		set("img6", img6);
	}

	public java.lang.String getImg6() {
		return get("img6");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public void setAuditTime(java.util.Date auditTime) {
		set("auditTime", auditTime);
	}

	public java.util.Date getAuditTime() {
		return get("auditTime");
	}

	public void setRefundTime(java.util.Date refundTime) {
		set("refundTime", refundTime);
	}

	public java.util.Date getRefundTime() {
		return get("refundTime");
	}



}