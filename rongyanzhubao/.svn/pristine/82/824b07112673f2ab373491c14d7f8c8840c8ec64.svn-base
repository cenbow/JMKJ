package com.cn.jm.util;

import com.cn.jm.method.BasicsMethod;

public class BasicsMethodUtil {
	/**登录方法类路径*/
	public static final String LOGIN_METHOD_CLASS_PATH = "com.cn.jm.method.login.%sLoginMethod";
	/**获取邮箱验证码类路径*/
	public static final String MAIL_CODE_METHOD_CLASS_PATH = "com.cn.jm.method.code.%sMailCodeMethod";
	/**获取手机验证码类路径*/
	public static final String MOBILE_CODE_METHOD_CLASS_PATH = "com.cn.jm.method.code.%sMobileCodeMethod";
	/**监听消息类路径*/
	public static final String LISENER_CLASS_PATH = "com.cn.jm.method.listener.%sLisenerMethod";
	/** redis定时器方法*/
	public static final String TIMER_METHOD_CLASS_PATH = "com.cn.jm.method.timer.%sMethod";
	/**
	 * 
	 * @param methodClassPath 实际执行的类具体路径,包路径加类名
	 * @param typeName 将模糊的类路径被替换成某个类名具体路径
	 * @param typeClass 执行方法类的上级父类的class
	 * @return
	 */
	public static <T extends BasicsMethod> T createMethods(String methodClassPath,String typeName,Class <T> typeClass) {
		try {
			Class<?> clazz = Class.forName(String.format(methodClassPath, typeName));
			Object obj = clazz.newInstance();
			if(obj instanceof BasicsMethod) {
				return  typeClass.cast(obj);
			}
		}catch(NoClassDefFoundError e) {
			System.out.println("参数错误:" + typeName);
		}catch(ClassFormatError e) {
			System.out.println("参数错误:" + typeName);
		}catch (Exception e) {
			System.err.println("参数错误:" + typeName);
		}
		return null;
	}
	

//	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		while(true) {
//			System.out.println("请输入执行部分类名(注意大小写):");
//			LoginMethod loginMethod = createMethods(LOGIN_METHOD_CLASS_PATH,sc.nextLine(), LoginMethod.class);
//			if(loginMethod != null) {
//				loginMethod.login(new LoginParam("","",""));
//			}else {
//				System.out.println("执行的类不存在");
//			}
//		}
//	}
}
