package com.cn.jm.util.pay.wechat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jdom.JDOMException;

import com.cn.jm.core.pay.PayException;
import com.cn.jm.core.utils.util.AmountUtils;
import com.cn.jm.information.ConstantThirdParty;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;

import net.sf.json.JSONObject;

public class WechatPay {

	private static String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 退款
	private static String refundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 汇率
	public static String exchangeRateUrl = "https://api.mch.weixin.qq.com/pay/queryexchagerate";
	// 企业付款
	private static String transfers = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

	private static String key;
	private static String mchId;
	private static String appId;

	private SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
	//退款证书加载路径
	private static SSLConnectionSocketFactory sslsf;
	private static ContentType contentType = ContentType.create(ContentType.TEXT_XML.getMimeType(), "iso-8859-1");
	
	public static void init(String mchId, String appId, String key, String certificatePath) {
		FileInputStream instream = null;
		try {
			WechatPay.mchId = mchId;
			WechatPay.appId = appId;
			WechatPay.key = key;
			instream = new FileInputStream(certificatePath);
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(instream, mchId.toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
			sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		} catch(IllegalStateException e){
			System.out.println("读取文件出错");
		} catch (Exception e) {
		} finally {
			try {
				if(instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 支付接口
	 * 
	 * @param appKey
	 *            商户信息
	 * @param appId
	 *            商户信息
	 * @param mchId
	 *            商户信息
	 * @param out_trade_no
	 *            订单号,会在回调中返回
	 * @param body
	 *            支付详情
	 * @param total_fee
	 *            支付金额
	 * @param spbill_create_ip
	 *            ip
	 * @param url
	 *            回调地址
	 * @param trade_type
	 *            支付类型:APP(app支付),JSAPI(公众号或者小程序),NATIVE(扫码)
	 * @param openId
	 *            如果不是微信公众号和小程序则传空
	 * @return
	 * @throws PayException
	 */
	public static Map<String, String> app(String out_trade_no, String body, String total_fee, String spbill_create_ip,
			String url, String openid) throws PayException {
		total_fee = AmountUtils.changeY2F(total_fee);
		String trade_type = "JSAPI";
		Map<String, String> data = new HashMap<String, String>();
		try {
			data = weichatPay(ConstantThirdParty.weixin_app_key, ConstantThirdParty.weixin_app_appid,
					ConstantThirdParty.weixin_app_mch_id, body, out_trade_no, total_fee, spbill_create_ip, url,
					trade_type, openid);
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String return_code = data.get("return_code");
		String return_msg = data.get("return_msg");
		if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
			throw new PayException(return_msg);
		}
		String result_code = data.get("result_code");
		if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
			throw new PayException(return_msg);
		}
		// 这里的支付需要区分大小写，
		Map<String, String> map = new HashMap<String, String>();
		map.put("appId", data.get("appid"));// 应用ID 65
		map.put("nonceStr", System.currentTimeMillis() + "");// 随机字符串78
		map.put("signType", "MD5");// 83
		map.put("sign", data.get("sign"));// 签名
		map.put("timeStamp", Long.toString(new Date().getTime() / 1000));// 时间戳84
		// map.put("package","Sign=WXPay");//扩展字段 80
		map.put("package", "prepay_id=" + data.get("prepay_id"));// 扩展字段 86
		String sign = PaymentKit.createSign(map, ConstantThirdParty.weixin_app_key);
		map.put("sign", sign);
		return map;
	}

	/**
	 * 
	 * @param key
	 *            商家key
	 * @param appid
	 *            应用ID
	 * @param mch_id
	 *            商户号
	 * @param nonce_str
	 *            随机字符串
	 * @param body
	 *            商品或支付单简要描述
	 * @param out_trade_no
	 *            总金额
	 * @param total_fee
	 *            总金额
	 * @param spbill_create_ip
	 *            终端IP
	 * @param notify_url
	 *            通知地址
	 * @param trade_type
	 * @param openId
	 */
	public static Map<String, String> weichatPay(String key, String appid, String mch_id, String body,
			String out_trade_no, String total_fee, String spbill_create_ip, String notify_url, String trade_type,
			String openId) throws JDOMException, IOException {
		WechatPay pay = new WechatPay();
		pay.setParams(appid, mch_id, body, out_trade_no, total_fee, spbill_create_ip, notify_url, trade_type, openId);
		return pay.doRequest();
	}

	/**
	 * 
	 * @param appid
	 *            应用ID
	 * @param mch_id
	 *            商户号
	 * @param nonce_str
	 *            随机字符串
	 * @param sign
	 *            签名
	 * @param body
	 *            商品或支付单简要描述
	 * @param out_trade_no
	 *            总金额
	 * @param total_fee
	 *            总金额
	 * @param spbill_create_ip
	 *            终端IP
	 * @param notify_url
	 *            通知地址
	 * @param trade_type
	 * @param openId
	 * @param openId
	 */
	public void setParams(String appid, String mch_id, String body, String out_trade_no, String total_fee,
			String spbill_create_ip, String notify_url, String trade_type, String openId) {
		parameters.put("appid", appid);
		parameters.put("mch_id", mch_id);
		parameters.put("nonce_str", System.currentTimeMillis() + "");
		parameters.put("body", body);
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("total_fee", total_fee);
		parameters.put("spbill_create_ip", spbill_create_ip);
		parameters.put("notify_url", notify_url);
		parameters.put("trade_type", trade_type);
		if (openId != null) {
			parameters.put("openid", openId);
		}
		parameters.put("sign", createSign("UTF-8", parameters));
	}

	public Map<String, String> doRequest() throws JDOMException, IOException {
		String requestXML = getRequestXml(parameters);
		String result = httpsRequest(url, "POST", requestXML);
		return XMLUtil.doXMLParse(result);
	}

	public Map<String, String> doRequest(SortedMap<Object, Object> map, String refundUrl)
			throws JDOMException, IOException {

		String requestXML = getRequestXml(map);
		String result = httpsRequest(refundUrl, "POST", requestXML);
		return XMLUtil.doXMLParse(result);
	}

	private static String createSign(String characterEncoding, SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<Object, Object>> es = parameters.entrySet();
		Iterator<Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding);
		return sign;
	}

	private static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<Entry<Object, Object>> es = parameters.entrySet();
		Iterator<Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			System.err.println("连接超时：{}" + ce);
		} catch (Exception e) {
			System.err.println("https请求异常：{}" + e);
		}
		return null;
	}

	/**
	 * APP退款 
	 * 方法转为不用传入appKey和appId
	 * @param appKey
	 * @param appId
	 * @param mchId
	 * @param out_refund_no
	 * @param body
	 * @param total_fee
	 * @param refund_fee
	 * @param transaction_id
	 * @param certificatePath
	 * @return
	 * @throws PayException
	 */
	@Deprecated
	public static Map<String, String> appRefund(String appKey, String appId, String mchId, String out_refund_no,
			String body, String total_fee, String refund_fee, String transaction_id, String certificatePath)
			throws PayException {
		total_fee = AmountUtils.changeY2F(total_fee);
		refund_fee = AmountUtils.changeY2F(refund_fee);
		SortedMap<Object, Object> SortedMap = new TreeMap<Object, Object>();

		SortedMap.put("appid", appId);
		SortedMap.put("mch_id", mchId);
		SortedMap.put("nonce_str", System.currentTimeMillis() + "");
		SortedMap.put("transaction_id", transaction_id);
		SortedMap.put("out_refund_no", out_refund_no);
		SortedMap.put("total_fee", total_fee);
		SortedMap.put("refund_fee", refund_fee);

		String sign = createSign("UTF-8", SortedMap);
		SortedMap.put("sign", sign);

		Map<String,String> resultMap = null;

		String requestXml = getRequestXml(SortedMap);
		String result = null;

		try {
			result = WechatPay.WeixinSendPost(requestXml);
			// 解析返回的xml
			resultMap = XMLUtil.doXMLParse(result);
			System.out.println(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 该方法现在只支付一个商户号
	 * 微信退款
	 * @param out_refund_no 退款单号,自己生成,不能重复
	 * @param body 退款备注
	 * @param total_fee 支付总金额
	 * @param refund_fee 退款金额
	 * @param transaction_id 支付成功时返回的id
	 * @return
	 * @throws PayException
	 */
	public static Map<String, String> appRefund(String out_refund_no,String body, String total_fee, String refund_fee, String transaction_id)
			throws PayException {
		total_fee = AmountUtils.changeY2F(total_fee);
		refund_fee = AmountUtils.changeY2F(refund_fee);
		SortedMap<Object, Object> SortedMap = new TreeMap<Object, Object>();

		SortedMap.put("appid", appId);
		SortedMap.put("mch_id", mchId);
		SortedMap.put("nonce_str", System.currentTimeMillis() + "");
		SortedMap.put("transaction_id", transaction_id);
		SortedMap.put("out_refund_no", out_refund_no);
		SortedMap.put("total_fee", total_fee);
		SortedMap.put("refund_fee", refund_fee);

		String sign = createSign("UTF-8", SortedMap);
		SortedMap.put("sign", sign);

		Map<String,String> resultMap = null;
		String requestXml = getRequestXml(SortedMap);
		String result = null;
		try {
			result = WechatPay.WeixinSendPost(requestXml);
			// 解析返回的xml
			resultMap = XMLUtil.doXMLParse(result);
			System.out.println(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 
	 * @param appId
	 *            appId
	 * @param mchId
	 *            商务id
	 * @param fee_type
	 *            转换的币种类型
	 * @param key
	 *            app私钥
	 * @return 如果汇率不存在的时候会返回空值
	 */
	public static BigDecimal getExchangeRate(String appId, String mchId, String fee_type, String key) throws Exception {
		WechatPay wechatPay = new WechatPay();
		SortedMap<Object, Object> SortedMap = new TreeMap<Object, Object>();

		SortedMap.put("appid", appId);
		SortedMap.put("mch_id", mchId);
		SortedMap.put("fee_type", fee_type);
		SortedMap.put("date", getDateStr());

		String sign = createSign("UTF-8", SortedMap);
		SortedMap.put("sign", sign);

		String requestXml = getRequestXml(SortedMap);
		Map<String,String> map = XMLUtil.doXMLParse(wechatPay.httpsRequest(exchangeRateUrl, "POST", requestXml));
		Object returnMsg = map.get("return_msg");
		if (!returnMsg.equals("OK")) {
			return null;
		}
		String rate = map.get("rate").toString();
		int lackLength = 9 - rate.length();
		for (int i = 0; i < lackLength; i++) {
			rate = 0 + rate;
		}
		return new BigDecimal(rate.substring(0, 1) + "." + rate.substring(1)).stripTrailingZeros();
	}

	/**
	 * 返回当前时间字符串
	 * 
	 * @return yyyyMMddHHmmss
	 */
	public static String getDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	/**
	 * 获取accountToken
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 */
	public static Object getAccessToken(String appid, String secret) {
		String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid
				+ "&secret=" + secret;
		JSONObject request = JSONObject.fromObject(HttpKit.post(accessTokenUrl, null));
		return request != null ? request.get("access_token") : null;
	}

	/**
	 * 获取jsapiTicket
	 * 
	 * @param accessToken
	 * @return
	 */
	public static Object getJsapiTicket(Object accessToken) {
		String jsapiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken
				+ "&type=jsapi";
		JSONObject request = JSONObject.fromObject(HttpKit.post(jsapiTicketUrl, null));
		return request != null ? request.get("ticket") : null;
	}

	/**
	 * 获取签名
	 * 
	 * @param noncestr
	 *            随机字符串
	 * @param jsapiTicket
	 * @param timestamp
	 *            时间戳
	 * @param url
	 * @return
	 */
	public static Object getSignature(String noncestr, Object jsapiTicket, long timestamp, String url) {
		String signature = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url="
				+ url;
		return SHA1.encode(signature);
	}

	/**
	 * 企业付款参数,如果为noCheck则为不校验用户真实姓名直接提现,如果需要校验请查看文档
	 */
	static final String CHECK_NAME = "NO_CHECK";
	/**
	 * 
	 * 当返回错误码为“SYSTEMERROR”时，请不要更换商户订单号，一定要使用原商户订单号重试，否则可能造成重复支付等资金风险。
	 * XML具有可扩展性，因此返回参数可能会有新增，而且顺序可能不完全遵循此文档规范，如果在解析回包的时候发生错误，请商户务必不要换单重试，请商户联系客服确认付款情况。如果有新回包字段，会更新到此API文档中。
	 * 因为错误代码字段err_code的值后续可能会增加，所以商户如果遇到回包返回新的错误码，请商户务必不要换单重试，请商户联系客服确认付款情况。如果有新的错误码，会更新到此API文档中。
	 * 错误代码描述字段err_code_des只供人工定位问题时做参考，系统实现时请不要依赖这个字段来做自动化处理。
	 * 
	 * !!!!!!!所有传入的参数中不要包含中文否则将生成签名错误!!!!!!!
	 * @param mchAppid 商户号绑定对应平台的appId
	 * @param openId 用户的信息的openId
	 * @param nonceStr 随机字符串
	 * @param partnerTradeNo 提现订单号
	 * @param amount 金额 单位为元
	 * @param ip 该ip没有任何意义但是必传
	 * @param desc 提现备注(建议不要输入中文)
	 * @throws IOException 
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 * @throws KeyManagementException 
	 * @return 返回简单校验是否企业付款成功
	 */
	public static boolean transfers(String mchAppid,String openId,String nonceStr,String partnerTradeNo,String amount,String desc,String ip) throws ClientProtocolException, IOException {
		SortedMap<Object, Object> SortedMap = new TreeMap<Object, Object>();
		SortedMap.put("mch_appid", mchAppid);
		SortedMap.put("mchid", mchId);
		SortedMap.put("nonce_str",nonceStr);
		SortedMap.put("partner_trade_no", partnerTradeNo);
		SortedMap.put("openid",openId);
		SortedMap.put("check_name",CHECK_NAME);
		SortedMap.put("amount",AmountUtils.changeY2F(amount));
		SortedMap.put("desc",desc);
		SortedMap.put("spbill_create_ip",ip);
		String sign = createSign("UTF-8", SortedMap);
		SortedMap.put("sign", sign.toUpperCase());
		String requestXml = getRequestXml(SortedMap);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpPost = new HttpPost(transfers);
			ContentType contentType = ContentType.create(ContentType.TEXT_XML.getMimeType(),Consts.ISO_8859_1);
			HttpEntity xmlData = new StringEntity(requestXml, contentType);
			httpPost.setEntity(xmlData);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");
				System.out.println("响应"+result+"\n请求"+requestXml);
				return result.indexOf("<result_code><![CDATA[SUCCESS]]></result_code>") != -1;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}
	

	/**
	 * 
	 * @param xmlObj
	 *            所需参数
	 * @param mch_id
	 *            商户号
	 * @param certificatePath
	 *            商户证书路径
	 * @return
	 * @throws Exception
	 */
	public static String WeixinSendPost(String xmlObj) throws Exception {
		String result = "";
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpPost = new HttpPost(refundUrl);
			HttpEntity xmlData = new StringEntity(xmlObj, contentType);
			httpPost.setEntity(xmlData);
			System.out.println("executing request" + httpPost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
				System.out.println(response.getStatusLine());
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		// 去除空格
		return result.replaceAll(" ", "");
	}
}
