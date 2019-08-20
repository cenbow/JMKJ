package com.cn.jm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 发送手机验证码工具
 * 
 * @author Administrator
 *
 */
public class MobileCodeUtil {


	// 产品名称:云通信短信API产品,开发者无需替换
	private static final String PRODUCT = "Dysmsapi";
	// 产品域名,开发者无需替换
	private static final String DOMAIN = "dysmsapi.aliyuncs.com";

	// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	private static final String AUTOGRAPH = "容妍珠宝";//签名
	private static final String ACCESS_KEY_ID = "LTAIzpoWjaOMF1RX";//账号
	private static final String ACCESS_KEY_SECRET = "L1Wtt5z27aoyDQBybbXDtG3bC0HLML";//用户密码
	private static final String SEND_VERIFICATION_CODE = "SMS_170625006";// 发送登录短信验证码CODE
	private static final String CODE_TEMPLATE_PARAM = "{\"code\":\"%s\"}";

	private static final Pattern CHINA_PATTERN = Pattern
			.compile("^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$");
	/**
	 * 校验中国的手机号是否符合发送条件
	 * 
	 * @param mobile
	 */
	public static boolean checkMobile(String mobile) {
		if (mobile.length() != 11) {
			return false;
		} else {
			Matcher m = CHINA_PATTERN.matcher(mobile);
			return m.matches();
		}
	}

	/**
	 * 发送登录方法,未定义第三方,默认返回true
	 * 
	 * @param mobile
	 * @param code
	 * @return
	 */
	public static boolean sendCode(String mobile, String code) {
		boolean flag = false;
		try {
			aliSendSms(mobile, SEND_VERIFICATION_CODE, String.format(CODE_TEMPLATE_PARAM, code));
			flag=true;
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 阿里云发送登录验证码
	 * 
	 * @param phone
	 * @param code
	 * @throws ClientException
	 */
	public static void aliSendCode(String phone, String code) throws ClientException {
		aliSendSms(phone, SEND_VERIFICATION_CODE, String.format(CODE_TEMPLATE_PARAM, code));
	}

	/**
	 * 
	 * @param mobile
	 *            手机号
	 * @param code
	 *            验证码
	 * @param templateCode
	 *            短信模板
	 * @return
	 * @throws ClientException
	 */
	public static SendSmsResponse aliSendSms(String mobile, String templateCode, String templateParam)
			throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(mobile);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(AUTOGRAPH);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam(templateParam);

		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("yourOutId");

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		return sendSmsResponse;
	}
}
