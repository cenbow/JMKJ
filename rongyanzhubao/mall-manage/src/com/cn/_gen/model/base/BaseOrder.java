
package com.cn._gen.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by 小跑科技robot.
 */
@SuppressWarnings("serial")
public abstract class BaseOrder<M extends BaseOrder<M>> extends Model<M> implements IBean {

		public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setOrderNo(java.lang.String orderNo) {
		set("orderNo", orderNo);
	}

	public java.lang.String getOrderNo() {
		return get("orderNo");
	}

	public void setAccountId(java.lang.Integer accountId) {
		set("accountId", accountId);
	}

	public java.lang.Integer getAccountId() {
		return get("accountId");
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

	public void setMoney(java.math.BigDecimal money) {
		set("money", money);
	}

	public java.math.BigDecimal getMoney() {
		return get("money");
	}

	public void setIntegral(java.math.BigDecimal integral) {
		set("integral", integral);
	}

	public java.math.BigDecimal getIntegral() {
		return get("integral");
	}

	public void setFreight(java.math.BigDecimal freight) {
		set("freight", freight);
	}

	public java.math.BigDecimal getFreight() {
		return get("freight");
	}

	public void setExpressNo(java.lang.String expressNo) {
		set("expressNo", expressNo);
	}

	public java.lang.String getExpressNo() {
		return get("expressNo");
	}

	public void setCoupon(java.math.BigDecimal coupon) {
		set("coupon", coupon);
	}

	public java.math.BigDecimal getCoupon() {
		return get("coupon");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return get("type");
	}

	public void setState(java.lang.Integer state) {
		set("state", state);
	}

	public java.lang.Integer getState() {
		return get("state");
	}

	public void setRefundState(java.lang.Integer refundState) {
		set("refundState", refundState);
	}

	public java.lang.Integer getRefundState() {
		return get("refundState");
	}

	public void setRefundReason(java.lang.String refundReason) {
		set("refundReason", refundReason);
	}

	public java.lang.String getRefundReason() {
		return get("refundReason");
	}

	public void setPayType(java.lang.Integer payType) {
		set("payType", payType);
	}

	public java.lang.Integer getPayType() {
		return get("payType");
	}

	public void setAddressName(java.lang.String addressName) {
		set("addressName", addressName);
	}

	public java.lang.String getAddressName() {
		return get("addressName");
	}

	public void setAddress(java.lang.String address) {
		set("address", address);
	}

	public java.lang.String getAddress() {
		return get("address");
	}

	public void setAddressPhone(java.lang.String addressPhone) {
		set("addressPhone", addressPhone);
	}

	public java.lang.String getAddressPhone() {
		return get("addressPhone");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public void setPayTime(java.util.Date payTime) {
		set("payTime", payTime);
	}

	public java.util.Date getPayTime() {
		return get("payTime");
	}

	public void setSendTime(java.util.Date sendTime) {
		set("sendTime", sendTime);
	}

	public java.util.Date getSendTime() {
		return get("sendTime");
	}

	public void setRefundTime(java.util.Date refundTime) {
		set("refundTime", refundTime);
	}

	public java.util.Date getRefundTime() {
		return get("refundTime");
	}

	public void setFinishTime(java.util.Date finishTime) {
		set("finishTime", finishTime);
	}

	public java.util.Date getFinishTime() {
		return get("finishTime");
	}

	public void setTransId(java.lang.String transId) {
		set("transId", transId);
	}

	public java.lang.String getTransId() {
		return get("transId");
	}

	public void setTimeEnd(java.lang.String timeEnd) {
		set("timeEnd", timeEnd);
	}

	public java.lang.String getTimeEnd() {
		return get("timeEnd");
	}

	public void setIsDel(java.lang.Integer isDel) {
		set("isDel", isDel);
	}

	public java.lang.Integer getIsDel() {
		return get("isDel");
	}

	public void setGoodsId(java.lang.Integer goodsId) {
		set("goodsId", goodsId);
	}

	public java.lang.Integer getGoodsId() {
		return get("goodsId");
	}

	public void setOrderType(java.lang.Integer orderType) {
		set("orderType", orderType);
	}

	public java.lang.Integer getOrderType() {
		return get("orderType");
	}



}