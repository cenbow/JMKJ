package com.cn.jm.method.login;

import com.cn._gen.model.Account;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.MailUtil;
import com.jfinal.aop.Aop;

public class MailLoginMethod implements LoginMethod{

	JMAccountService accountService = Aop.get(JMAccountService.class);

	@Override
	public JMResult login(LoginParam loginParam) {
		String mail =loginParam.getLoginAccount();
		String password = loginParam.getPassword();
		
		if(!MailUtil.check(mail)) {
			return JMResultUtil.fail(PromptInformationEnum.EMAIL_OR_PASSWORD_ERROR);
		}
		Account loginAccount = accountService.selectAccount(" mail = ", loginParam.getLoginAccount(), loginParam.getRegisterType());
		//如果是管理员身份则不通过登录
		if(loginAccount == null) {
			return JMResultUtil.fail(PromptInformationEnum.EMAIL_OR_PASSWORD_ERROR);
		}
		if (accountService.isSystem(loginAccount)) {
			return JMResultUtil.fail(PromptInformationEnum.EMAIL_OR_PASSWORD_ERROR);
		}
		// 未通过密码验证
		if (!accountService.checkAccountPassword(loginAccount, password)) {
			return JMResultUtil.fail(PromptInformationEnum.EMAIL_OR_PASSWORD_ERROR);
		}		
		return JMResultUtil.success(loginAccount);
	}

}
