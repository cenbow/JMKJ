package com.cn.jm.method.login;

public class LoginParam {

	public LoginParam(String ids, String loginAccount, String password, String code, int registerType) {
		this.loginAccount = loginAccount;
		this.password = password;
		this.code = code;
		this.ids = ids;
		this.registerType = registerType;
	}

	// 邮箱
	private String loginAccount;
	// 密码
	private String password;
	// 第三方id
	private String ids;
	// 验证码
	private String code;
	//
	private int registerType;

	public void setRegisterType(int registerType) {
		this.registerType = registerType;
	}
	
	public int getRegisterType() {
		return registerType;
	}
	
	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "LoginParam [loginAccount=" + loginAccount + ", password=" + password + ", ids=" + ids + ", code=" + code
				+ ", registerType=" + registerType + "]";
	}
}
