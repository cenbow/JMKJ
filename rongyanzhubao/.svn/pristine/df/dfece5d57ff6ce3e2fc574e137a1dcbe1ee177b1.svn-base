package com.cn.jm.method.login;

import com.cn._gen.model.Account;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.BasicsInformation;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.util.JMResultUtil;
import com.jfinal.aop.Aop;

public class MobileCodeLoginMethod implements LoginMethod{

	JMAccountService accountService = Aop.get(JMAccountService.class);
	
	@Override
	public JMResult login(LoginParam loginParam) {
		String inputCode = loginParam.getCode();
		String mobile = loginParam.getLoginAccount();
		int registerType = loginParam.getRegisterType();
		if(!accountService.checkMobileLoginCode(mobile, inputCode, registerType)) {
			return JMResultUtil.fail(PromptInformationEnum.CODE_FAIL);
		}
		Account account = accountService.getByMobile(mobile, registerType);
		if(account == null) {
			JMResult result = accountService.saveAccountByMobile(mobile, BasicsInformation.DEFAULT_PASSWORD, AccountEnum.TYPE_USER, null, registerType);
			if(result.getCode() == JMResult.FAIL) {
				return result;
			}
			account = (Account) result.getData();
		}
		return JMResultUtil.success(account);
	}

}
