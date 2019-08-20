package com.cn.jm.method.code;

import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.information.RedisInformation;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.util.JMResultUtil;
import com.jfinal.aop.Aop;

/**
 * 邮箱忘记密码获取验证码
 * 
 * @author Administrator
 *
 */
public class ForgetPwdMailCodeMethod implements CodeMethod {

	JMAccountService accountService = Aop.get(JMAccountService.class);

	@Override
	public JMResult check(String m, int registerType) {
		return accountService.isMailExistence(m, registerType)
				? JMResultUtil.success(RedisInformation.MAIL_FORGET_CODE, PromptInformationEnum.SUCCESS)
				: JMResultUtil.fail(PromptInformationEnum.USER_NOT_EXISTS);
	}

}
