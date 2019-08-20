package com.cn.jm.method.code;

import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.method.BasicsMethod;

public interface CodeMethod extends BasicsMethod{
	/**
	 * 校验手机号是否通过校验,如果通过则返回true,失败则false
	 * @param accountNumber
	 * @return
	 */
	JMResult check(String m, int registerType);

}
