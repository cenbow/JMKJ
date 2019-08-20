package com.cn.jm.util.pay.ali;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.jfinal.kit.StrKit;

/**
 * 支付宝支付
 */
public class AliPay {

	static AlipayClient alipayClient;
	public static void initAliPay(String appid, String rsaPrivateKey, String aliPublicKey) {
		alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appid, rsaPrivateKey, "json", "UTF-8", aliPublicKey, "RSA2");
	}
//	public static void main(String[] args) {
//		initAliPay("2019071265854119", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQ7a0xHiYPRrGONhRhNQfYfF7Kt0xHAXC9K0qmw4KG6T/65eGojn31WwaRzAiOue7lL9zF5cHgcPR5tKECxG3SLr58vsn7r1FePffAx8hmGw51IG3AUs6GuClzliWgMXNfVgG1nGS1MIun5bRuTZch4BHd+OQUnEggNtaQOJNhR0lIr/QB7cMxTMeYVx9swUOtjEjXHaxxAvSdzQmensW0sgotIQw5ac5EreyyqgJUDPNxaKqJflY1cN6KQOXd17wRLHM1W3rgNejKwIr6zYd5vmIsQDoaA6nAw6hMtEu8MKXppjmIuDg8K9XZHVsOleygA4VuhvZm68If/DFpGktRAgMBAAECggEAR9Ad04rW3RbwgEE30/sNCi6GYfh1qXGDRZkgAoY6NVN6yn4CVR1WOK+ED9aEHwTu5U3UHZN9gBM4cN4aMl7W9CEJbpKzTy1OKl6WV0YnfMDHZy5Mc9IUWBkFISszNa+LNaBOdIksNcP6T5W41DdKPhTjhZQAoF3YN240333JOZf0O1w+e1tsmDTBs3U5FXJ8zA6Ev+J94ztkl+Y2aZlQDdYU9hBiiqDYczZkkt5wTcFEdLXcmjqMd2jpUHPGGkFb+/uPsd02GI0Do/sIiMzfGO4QQdP1e4qjLQSDftfU9oJqrseNEuuIJn/gmQ2hswcUPzeXpubTFVWcMM0NfDciYQKBgQDFdvJHZpLEfr3bF2B+9CoAhZbB2u5FGS9N87O3pluOyjhD2x+iCvG96Av2vvlDndhQI6qi4uPpX3L8W9ddnO5afjf6kW4EE7ExJW4H4N8Xf/TjIvlINuTaLFBw2KJcQohSZO4Zr7E5JzCFMyMAKW+BrTiEdAtxf1K2/zEaGZjzVQKBgQC74+ZuiZZfLOU242LX8qyHg+IImRVL1TEJgozev/6F1XExAsS5ksu6XwG6WEG1VESLauxrk/FC5b9RBkcsOXlf1ENiilDTGP4mLTVG7Ao3/ZfqyPrUcHtCfRIjc2nFnNJVnyhh+/4Yf0tygeL/vngD0VJyVdxzk3pxy8lav94wDQKBgDcTOPoPMupZS0zbVU+zfC+PEwBYttJQ9u+wBDU50hmeb1kFuenxUYGdQhD9LPreAKJBgGtUCythMuoOF6CFj7lvsN9TRrMeZvKFNQ4/exnL15que4bXwLbVW05OLeK7r9gDTKGfbje5pX/RRnbaF768eKIA19w9He3qeWCFOTLNAoGBAIVgICFzddUWD5hJDc9xqtt/2SJ1WNK+AdNYYxt0ev8SJwm9h1ycQhaIfreDExHfQdEeE7nCCU+Nu6OOq1NPcUN/cF4xBXL0mOUMzncL860PTZoTFUtclYL7ObcEpJ7s1bqGAqqbsSI+qpYcccFNufAr1TTokldQ52iEWukarmS5AoGACW9Z+MmKbAfQVvlcetmT6qFlkE8glBJvN8VtOTvmBFgboEdktee5BGfED09PancGeSbuKUNVeKfejJuoOOUuiVnMb/43MfOPJkic5O7aximDGewh1bG0skEBYtcCDZGcEMYsLicuYkVO0D5eMLGHVrB6OW8MJ3oetdMhXglAU5s=",
//				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAocwFpek1Vhl9hUA6CXv8rbpFJohps5LRfkvns92fFeZ1wEDtfBmM5LTF1m2YNT5AEcb+x1g08Zqscy759P8mU8/isIwyimjfINHsIwadjVjiAtPuJUfSEY5uXA77x6mXw6F+dUmw8MATVt3iUO+hqfZV8yrDbEhFyMAkIKUMldQh/pQvCfqGtfn/63NHzFYgDu+LT09FKmiy9ZWlgxfh4BjrC7R7LW20MmVG8MwUjh5HIByn7Z9ePDLckQ+/dCNUFAo9xWq+GpWRYnpT1fTZ+cD4UOAinII4OcDWDir6KNNeYRU9VGJUZQfqqTeiDkupiC6HmirFQ3Aq/ES2Y488PQIDAQAB");
//		try {
//			Thread.sleep(10000L);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		AlipayTradeRefundResponse aliPayRefund = aliPayRefund("2019072422001470960542219424", "0.01");
//		System.out.println(aliPayRefund.isSuccess());
//	}
	
	/**
	 * @param total_amount
	 *            价格单位：元
	 * @param subject
	 *            商品的标题/交易标题/订单标题/订单关键字等
	 * @param body
	 *            可为空。对一笔交易的具体描述信息
	 * @param out_trade_no
	 *            订单号
	 */
	public static String app(String appId, String rsaPrivate, String total_amount, String subject, String out_trade_no,
			String notify_url) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> keyValues = new HashMap<String, String>();

		keyValues.put("app_id", appId);

		keyValues.put("biz_content",
				"{\"timeout_express\":\"15m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\""
						+ total_amount + "\",\"subject\":\"" + subject + "\",\"out_trade_no\":\"" + out_trade_no
						+ "\"}");
		keyValues.put("charset", "utf-8");
		keyValues.put("method", "alipay.trade.app.pay");
		keyValues.put("sign_type", "RSA2");
		keyValues.put("timestamp", sdf.format(new Date()));
		keyValues.put("version", "1.0");
		keyValues.put("notify_url", notify_url);
		String sign;
		try {

			sign = AlipaySignature.rsa256Sign(AlipaySignature.getSignContent(keyValues), rsaPrivate, "UTF-8");

		} catch (AlipayApiException e) {
			sign = null;
			System.err.println("error:" + e.getMessage());
		}
		keyValues.put("sign", sign);
		return buildOrderParam(keyValues);
	}

	/**
	 * @param requestParams
	 *            支付宝回调返回的参数
	 */
	public static boolean notify(Map<String, String[]> requestParams, String aliRsaPublic) {
		Map<String, String> params = new HashMap<String, String>();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		boolean signVerified = false;
		try {
			signVerified = AlipaySignature.rsaCheckV1(params, aliRsaPublic, "UTF-8");
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return signVerified;
	}

	/**
	 * 对请求字符串的所有一级value进行encode
	 */
	public static String buildOrderParam(Map<String, String> map) {
		List<String> keys = new ArrayList<String>(map.keySet());

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			sb.append(buildKeyValue(key, value, true));
			sb.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		sb.append(buildKeyValue(tailKey, tailValue, true));

		return sb.toString();
	}

	/**
	 * 拼接键值对
	 * 
	 * @param key
	 * @param value
	 * @param isEncode
	 * @return
	 */
	private static String buildKeyValue(String key, String value, boolean isEncode) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("=");
		if (isEncode) {
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				sb.append(value);
			}
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	public static Map<String, String> returnCheck(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}

	/**
	 * @param tradeNo 验签
	 * @param refundMoney 返回给用户的金额
	 * @return 返回请求后的回调参数
	 * @date 2018年8月7日
	 */
	public static AlipayTradeRefundResponse aliPayRefund(String tradeNo, String refundMoney) {
		//参数解释:serverUrl 访问地址,appId 应用id,privateKey 私钥,format 参数返回格式(只支持json),charset 请求的编码格式,alipayPulicKey 公钥,signType 	商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent("{" +
		"\"trade_no\":\""+tradeNo+"\"," +
		"\"refund_amount\":" + refundMoney+
		" }");
		try {
			return alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param appid 应用id
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @param tradeNo 验签
	 * @param refundMoney 返回给用户的金额
	 * @return 返回请求后的回调参数
	 * @date 2018年8月7日
	 */
	public static AlipayTradeRefundResponse aliPayRefund(String appid, String privateKey, String publicKey, String tradeNo, String refundMoney) {
		//参数解释:serverUrl 访问地址,appId 应用id,privateKey 私钥,format 参数返回格式(只支持json),charset 请求的编码格式,alipayPulicKey 公钥,signType 	商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		AlipayClient alipayClient;
		if(StrKit.notBlank(appid) && StrKit.notBlank(privateKey) && StrKit.notBlank(publicKey)) {
			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",appid,privateKey,"json","UTF-8",publicKey,"RSA2");
		}else {
			alipayClient = AliPay.alipayClient;
		}
		request.setBizContent("{" +
				"\"trade_no\":\""+tradeNo+"\"," +
				"\"refund_amount\":" + refundMoney+
				" }");
		try {
			return alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param appId  应用id
	 * @param appPrivateKey 私钥
	 * @param aliPayPublicKey公钥
	 * @param tradeNo 订单号,不可重复
	 * @param refundMoney 支付金额
	 * @param returnUrl 同步返回地址，HTTP/HTTPS开头字符串
	 * @param notifyUrl 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
	 * @return
	 */
	public static String aliPcPay(String appId,String appPrivateKey,String aliPayPublicKey,String tradeNo,String refundMoney,String returnUrl,String notifyUrl) {
	    AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, appPrivateKey,"json","UTF-8", aliPayPublicKey, "RSA2"); //获得初始化的AlipayClient
	    AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
	    alipayRequest.setReturnUrl(returnUrl);
	    alipayRequest.setNotifyUrl(notifyUrl);//在公共参数中设置回跳和通知地址
	    alipayRequest.setBizContent("{" +
	        "\"out_trade_no\":\""+tradeNo+"\"," +
	        "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
	        "\"total_amount\":"+refundMoney+"," +
	        "\"subject\":\"世界模特小姐大赛投票\"" +
	        "  }");//填充业务参数
	    String form="";
	    try {
	        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
	    } catch (AlipayApiException e) {
	        e.printStackTrace();
	    }
	    return form;
	}
	
//	public static void main(String[] args) {
//		String aliPcPay = aliPcPay("2018082461149397",
//				"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQMRVOSduCzpUGTCbnbs+bH6OFDX2BPp1sV+GpEJLlGJxR0HrBsFEYOK2owGMZyg0h/Ocmaap9wlpJZKPaSLVR4Z9YgJ00k4CKSOYmjmf7Ho15RIWq11kCNrB0RGcpYUzKCdfZqyb8PCp2IFXOZQtPHOCguMrizJMHnASY7oJESpBRB9OK4WsKPdg59asiA0UMPR1sOtOCNiXI2ZejFAAiwsjspIpdOcIt/oaZGbEAHwxUnf/Sdf7bqQzujyGI5KwDfZ7ZjRp1ePTCRxJ7Sk0PUMRWp87X6jF24c17Kio+wFBFH2tlTLuyLFkztXlZcz+fw7xU2Cg8J2FmFH+bux7XAgMBAAECggEAQdioSiAgRhPgqgeMnJNV6xj/q3sFnUfiy982BeazrjsW56W06U6DOfRoGXAB0X0bkjHTvQ7z9IoUgP+cc8pQQxwtkFTrdV8GjB/OOj097sd9A6vu/p1o4EHpQq8aDSOJ1Zm5IIWwP++Z1Gffx2kNSQt+6OZ6gCe73PW6g7wncNeHrZYcfcHMk0mHh5mHQ9HX+0NNkRkYHc4dC3vlrjBF06k+KeA5UJ6ZLYg4DTXNRQF4KK5xa0xLwOhNAK91/2KmwgF/4lUlDEd3VfDZ5NAOI4DZd35XfG7FGOfaQqWWwDHKkMypbxsvpBJewG6bGJbkN5GuHrOhf1q2/URnovpOwQKBgQDzn6EkA9dpO9Uimh5l04sjX3vaMvobI4I05732TFlPJ7xr2Jzm6wQiDAlLgG7yC3wpC0q0USLOxJVN6JgC8P6eXw7yo0kLemYvNsSAkoqB/21Q0UTwn2fWIfqafOzyyyU24KaEvyU5yxXBOtWWIehxqb5JhUJwyOSxDxm3b/XMwwKBgQCXhFL1tFrfV4+N2zrY70W739g/jyUh1NRr1gOZ3KqB3dAVD/ASKP05G9sK3+M/pm44ZWTdHHozmqKUUGFEYIhRn29FvPaZnA14uCAZbB6ufvXilNps8aE0vUlNUVDGoMP/TFKhBcf9Fawo0QB/ua5HcGTK4NB/PjDGK1wvgcWUXQKBgAnJqwawZNIkhj6apIgAxMFKD8bHb4bxmUSY3ufrT1c/yHEoxbz9SjA3/ZOb+FkfHwDjJEA0VkqqWTlFE/5iQkiIlygmaUPdygcxkxl7SbdJVNH/Zl6oeNqAiPn19OsYQEmBIw+IQm2c/CXEQFqEyDrwz+VSRkYTvCRk7+qPw/dTAoGAUmDvegeFIMlKk6YOaMQu9ebOfEAnwC0nETpa2tzPF6yx5cG0lSsUAmjSvybbN1AVvTPjkxRYnJYGrvzE5ZhWhYpZxWDn8ryV1nzFdw0B4X3F0lt8Jp95NmNz50W+1T+zUKr+ge3Svhd6cvavfsJFrzG8m1GACq6Vdik9BddYxUECgYEA24O6EHs9azd0KJrtjcneA7OxMMsErZhHZTYBZVEJH/yBtYk9C42xeDTPYgpDjcVJafXDIid3zUcO0W4GwQahb6j/pOiaJx6xp3NSMkdpaCEzmBFySof2DXbBBDlED4ik6mgGs8EI/pUphI+i+i0oLJaTkFfomab+WkjhjsBIf68=",
//				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj3M8+TwPSUWs9kG7qHE+zXNL5Pmfr02hcOSTeUki2ORWln8uxRZI7stQQZEoeE1MPGqgz5pwgj1nilKCX0VLYmgHVF4W+9Tt87rsnB3rHW9sMW83k81OU5Ua6q3sF2KI4YWDdDKzVpyn73V/cS0znWPYI4nJa4scedDIqQQAF95ZRpbX+glTBPlbfcEZ8itS1lHuRWuMV2TQOiq71XiMuuJq8N8W2/cWww275tc3GwN8JPvM1Jeme8RGDocfe/AbGQEQTgXPhWmBB20nVIzFD63FIaGbyW9LbvM9GByOcrVZkC17pdJqDYA6E0m2ERdnQSytQvdFiI+owyG0WByS/QIDAQAB",
//				"101318102098670521",
//				"0.01",
//				"https://szsm.world-missmodel.com/WL/index.html",
//				"https://d64fcdc9.ngrok.io/worldmodel-web/api/shop/order/aliNotifyUrl");
//		System.out.println(aliPcPay);
//		
//	}
}
