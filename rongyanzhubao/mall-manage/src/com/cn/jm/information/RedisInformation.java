package com.cn.jm.information;

public interface RedisInformation {
	/**后台管理登录缓存时间*/
	int SYSTEM_LOGIN_OVER_TIME = 60 * 60 * 12;
	/** 储存前端验证码储存时间 */
	int CODE_TIME = 60 * 15; 
	/** 储存后台验证码储存时间 */
	int SYSTEM_CODE_TIME = 60 * 5; 
	/**tio绑定用户时间**/
	int TIO_TIME= 60 * 60 * 12;//12小时
	/** 储存旧手机号校验是否成功时间*/
	int CHECK_OLD_MOBILE_TIME = 60 * 30;//30分钟
	/** 忘记密码验证码(邮箱获取)*/
	String MAIL_FORGET_CODE = "wj001";
	/** 注册验证码(邮箱获取)*/
	String MAIL_REGISTER_CODE = "zc002";
	/** 绑定邮箱验证码(邮箱获取)*/
	String MAIL_BINDING_CODE = "bd003";
	/** 用于ip加上请求头生成的toKen时的前缀,查询出用户id*/
	String TO_KEN_BY_ACCOUNT_ID = "ta004";
	/** 用户id作为key时的前缀,避免与其他key重复,查询出校验toKen*/
	String ACCOUNT_ID_BY_TO_KEN = "au005";
	/** 后台管理验证码*/
	String SYSTEM_CODE = "sc006";
	/** 手机登录获取验证码(手机号码获取)*/
	String MOBILE_LOGIN_CODE = "ml007";
	/** 注册验证码(手机号码获取)*/
	String MOBILE_REGISTER_CODE = "mr008";
	/** 忘记密码验证码(手机号码获取)*/
	String MOBILE_FORGET_CODE = "mf009";
	/** 绑定手机验证码(手机号码获取)*/
	String MOBILE_BINDING_CODE = "mb010";
	/** 邮箱登录获取验证码(邮箱获取)*/
	String MAIL_LOGIN_CODE = "ml011";
	/** 更换绑定手机号获取验证码(手机获取)*/
	String MOBILE_REPLACE_CODE = "mr012";
	/** 校验旧手机号的key,成功则拼接上用户id就存在值*/
	String CHECK_OLD_MOBILE_SUCCESS = "co013";
}
