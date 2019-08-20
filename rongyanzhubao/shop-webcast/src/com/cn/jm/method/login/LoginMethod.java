package com.cn.jm.method.login;

import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.method.BasicsMethod;

public interface LoginMethod extends BasicsMethod{
	JMResult login(LoginParam loginParam);
}
