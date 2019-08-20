package com.cn.jm.service;

import java.util.HashMap;

import com.cn._gen.model.Content;
import com.cn.jm._dao.content.ContentEnum;

public class JMContentService extends BasicsService<Content> {
	
	/**
	 * 获取开户行和收款账户名和账户
	 * @return 
	 */
	public HashMap<String, Content> bankContent() {
		HashMap<String,Content> resultMap = new HashMap<>();
		Content openingBank = selectById(ContentEnum.OPENING_BANK_ID.getValue());
		Content paymentName = selectById(ContentEnum.PAYMENT_NAME_ID.getValue());
		Content paymentAccount = selectById(ContentEnum.PAYMENT_ACCOUNT_ID.getValue());
		resultMap.put("openingBank", openingBank);
		resultMap.put("paymentName", paymentName);
		resultMap.put("paymentAccount", paymentAccount);
		return resultMap;
	}
	
}
