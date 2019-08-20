package com.cn.jm.util.rongyun.messages;

import java.math.BigDecimal;
import java.util.Date;

import com.cn.jm.util.rongyun.util.GsonUtil;

/**
 * 自定义转账消息
 */
public class TransferMessage extends BaseMessage {
	
	private String getName;//收款人昵称
	private String payName;//付款人昵称
	private Integer getUserId;//收款人id
	private Integer payUserId;//付款人id
	private Integer coinId;//币种id
	private BigDecimal money ;//金额
	private String title ;//标题 ：转账给XXX
	private String moneyType;//类型：BTC SCY
	private String remark ;//留言备注
	private String incomeTime ;//到账时间
	private transient static final String TYPE = "XP:TransferMsg";

	

	public TransferMessage(String payName,String getName,BigDecimal money,String title,String moneyType,String remark,String incomeTime,Integer payUserId,Integer getUserId,Integer coinId) {
		this.payName = payName;
		this.getName = getName;
		this.money = money ;
		this.title = title ;
		this.moneyType = moneyType ;
		this.remark = remark ;
		this.incomeTime = incomeTime ;
		this.payUserId = payUserId ;
		this.getUserId = getUserId ;
		this.coinId = coinId ;
	}

	public Integer getCoinId() {
		return coinId;
	}

	public void setCoinId(Integer coinId) {
		this.coinId = coinId;
	}

	public String getGetName() {
		return getName;
	}

	public void setGetName(String getName) {
		this.getName = getName;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIncomeTime() {
		return incomeTime;
	}

	public void setIncomeTime(String incomeTime) {
		this.incomeTime = incomeTime;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	
	public Integer getGetUserId() {
		return getUserId;
	}

	public void setGetUserId(Integer getUserId) {
		this.getUserId = getUserId;
	}

	public Integer getPayUserId() {
		return payUserId;
	}

	public void setPayUserId(Integer payUserId) {
		this.payUserId = payUserId;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, TransferMessage.class);
	}
	

}
