package com.cn.jm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
//import com.aliyuncs.exceptions.ClientException;
//import com.aliyuncs.profile.DefaultProfile;
//import com.aliyuncs.profile.IClientProfile;

public class ToolMessageUtil {

	private static Logger logger = Logger.getLogger(ToolMessageUtil.class);

	/**
	 * 凌凯发送验证码信息
	 */
	private final static String CORP_ID = "";// 账户名
	private final static String PWD = "";// 密码

	public static boolean sendCode(String mobile, int code) {
		String content = "尊敬的客户:您好！您的手机验证码为:" + code + ",有效期为5分钟,请勿将验证码告诉他人。如非本人操作请忽略。\r\n" + "";
		int i = send(mobile, content, "", "");
		if (i > 0) {
			return true;
		} else {
			logger.error("短信发送失败：" + mobile + ",错误码为：" + i);
			return false;
		}
	}

	public static void sendMessage(String mobiles, String message) {
		String content = "尊敬的客户:您好！" + message;
		int i = send(mobiles, content, "", "");
		if (i <= 0)
			logger.error("短信发送失败：" + mobiles + ",错误码为：" + i);
	}

	public static boolean sendMessageToMaster(final String mobiles, String message) {
		String content = "尊敬的大师:您好！" + message;
		int i = send(mobiles, content, "", "");
		if (i > 0) {
			return true;
		} else {
			logger.error("短信发送失败：" + mobiles + ",错误码为：" + i);
			return false;
		}
	}

	/**
	 * 凌凯发送验证码
	 * @param mobile
	 *            发送手机号码（号码之间用英文逗号隔开，建议100个号码）
	 * @param content
	 *            发送内容
	 * @param cell
	 *            扩展号(必须是数字或为空)
	 * @param sendTime
	 *            定时发送时间(可为空)
	 *            固定14位长度字符串，比如：20060912152435代表2006年9月12日15时24分35秒，为空表示立即发送
	 * @return
	 */
	public static int send(String mobile, String content, String cell, String sendTime) {
		String inputLine = "";
		int value = -2;
		try {
			content = URLEncoder.encode(content.replaceAll("<br/>", " "), "GBK");// 发送内容
			String strUrl = "https://sdk2.028lk.com/sdk2/BatchSend2.aspx";
			String param = "CorpID=" + CORP_ID + "&Pwd=" + PWD + "&Mobile=" + mobile + "&Content=" + content + "&Cell="
					+ cell + "&SendTime=" + sendTime;
			inputLine = sendPost(strUrl, param);
			value = new Integer(inputLine).intValue();
		} catch (Exception e) {
			value = -2;
		}
		return value;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
//	 //产品名称:云通信短信API产品,开发者无需替换
//	private static final String PRODUCT = "Dysmsapi";
//    //产品域名,开发者无需替换
//    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
//
//    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
//    private static final String AUTOGRAPH = "鞋卫士";
//    private static final String ACCESS_KEY_ID = "LTAIgOpE2UA7pFJM";
//    private static final String ACCESS_KEY_SECRET = "7d9ifXCEbX47awQh3ACNfMFzf0bM6d";
//    public static final String TEMPLATE_CODE = "SMS_162546339";//创建用户的模版CODE
//    public static final String SEND_VERIFICATION_CODE = "SMS_162455929";//发送登录短信验证码CODE
//    public static final String SAVE_ACCOUNT_TEMPLATE_PARAM =  "{\"phone\":\"${phone}\", \"password\":\"${password}\", \"invitationcode\":\"${invitationcode}\"}";
//    public static final String CODE_TEMPLATE_PARAM =  "{\"code\":\"${code}\"}";
//    public static final String CODE = "${code}";
//    public static final String PHONE =  "${phone}";
//    public static final String PASSWORD =  "${password}";
//    public static final String INVITATIONCODE =  "${invitationcode}";
//    
//    /**
//     * 
//     * @param mobile 手机号
//     * @param code 验证码
//     * @param templateCode 短信模板
//     * @return
//     * @throws ClientException
//     */
//    public static SendSmsResponse aliSendSms(String mobile,String templateCode,String templateParam) throws ClientException {
//
//        //可自助调整超时时间
//        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//
//        //初始化acsClient,暂不支持region化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
//        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//
//        //组装请求对象-具体描述见控制台-文档部分内容
//        SendSmsRequest request = new SendSmsRequest();
//        //必填:待发送手机号
//        request.setPhoneNumbers(mobile);
//        //必填:短信签名-可在短信控制台中找到
//        request.setSignName(AUTOGRAPH);
//        //必填:短信模板-可在短信控制台中找到
//        request.setTemplateCode(templateCode);
//        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//        request.setTemplateParam(templateParam);
//
//        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
//        //request.setSmsUpExtendCode("90997");
//
//        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        //request.setOutId("yourOutId");
//
//        //hint 此处可能会抛出异常，注意catch
//        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
//
//        return sendSmsResponse;
//    }
//
//    public static void sendSaveAccount(String phone,String password,String invitationcode) throws ClientException {
//    	aliSendSms(phone,TEMPLATE_CODE,SAVE_ACCOUNT_TEMPLATE_PARAM.replace(PHONE,phone).replace(PASSWORD,password).replace(INVITATIONCODE,invitationcode));
//    }
//
//    /**
//     * 发送登录验证码
//     * @param phone
//     * @param code
//     * @throws ClientException
//     */
//    public static void sendCode(String phone,String code) throws ClientException {
//    	aliSendSms(phone,SEND_VERIFICATION_CODE,CODE_TEMPLATE_PARAM.replace(CODE,code));
//    }
//    
//    public static void main(String[] args) throws ClientException, InterruptedException {
////    	int code=5488;
////        //发短信
////        SendSmsResponse response = aliSendSms("15767318059",TEMPLATE_CODE,SAVE_ACCOUNT_TEMPLATE_PARAM.replace(PHONE,"15767318059").replace(PASSWORD,"123456").replace(INVITATIONCODE,"123456"));
////        System.out.println("短信接口返回的数据----------------");
////        System.out.println("Code=" + response.getCode());
////        System.out.println("Message=" + response.getMessage());
////        System.out.println("RequestId=" + response.getRequestId());
////        System.out.println("BizId=" + response.getBizId());
////
////        Thread.sleep(3000L);
//    	sendCode("15767318059", "1234");
////        //查明细
////        if(response.getCode() != null && response.getCode().equals("OK")) {
////            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
////            System.out.println("短信明细查询接口返回数据----------------");
////            System.out.println("Code=" + querySendDetailsResponse.getCode());
////            System.out.println("Message=" + querySendDetailsResponse.getMessage());
////            int i = 0;
////            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
////            {
////                System.out.println("SmsSendDetailDTO["+i+"]:");
////                System.out.println("Content=" + smsSendDetailDTO.getContent());
////                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
////                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
////                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
////                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
////                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
////                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
////                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
////            }
////            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
////            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
////        }
//
//    }
//	凌凯测试
//	public static void main(String[] args) throws MalformedURLException, UnsupportedEncodingException {
//		int code = (int) (Math.random() * 9000 + 1000);
//		int result = send("15767318059", "尊敬的客户:您好！您的手机验证码为:" + code + ",有效期为5分钟,请勿将验证码告诉他人并确定该申请是您本人", "", "");
//		System.out.println(result);
//	}

}
