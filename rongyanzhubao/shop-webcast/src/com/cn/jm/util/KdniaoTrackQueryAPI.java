package com.cn.jm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



/**
 *
 * 快递鸟物流轨迹即时查询接口
 *
 * @技术QQ群: 456320272
 * @see: http://www.kdniao.com/YundanChaxunAPI.aspx
 * @copyright: 深圳市快金数据技术服务有限公司
 *
 *             DEMO中的电商ID与私钥仅限测试使用，正式环境请单独注册账号 单日超过500单查询量，建议接入我方物流轨迹订阅推送接口
 * 
 *             ID和Key请到官网申请：http://www.kdniao.com/ServiceApply.aspx
 */

public class KdniaoTrackQueryAPI {

	// DEMO
	public static void main(String[] args) {
		KdniaoTrackQueryAPI kdniaoTrackQueryAPI = new KdniaoTrackQueryAPI();
		String sp;
		String logistic;
		try {
			sp = kdniaoTrackQueryAPI.getShipperCode("75121384132040");
			System.out.println(sp);
			logistic=kdniaoTrackQueryAPI.getOrderTraces(sp.split(",")[0], "75121384132040");
			System.out.println(logistic);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * //电商ID private String EBusinessID="1344880"; //电商加密私钥，快递鸟提供，注意保管，不要泄漏
	 * private String AppKey="c4ffdaed-39b0-4a6f-94df-356567e02458";
	 */
	private String EBusinessID = "1546829";
	private String AppKey = "1df1ac59-59d1-47d0-8da4-b2f405515489";
	// 请求url
	private String ReqURL = "http://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";// 2018/12/7快递鸟域名修改
//	private String ReqURL="http://sandboxapi.kdniao.com:8080/kdniaosandbox/gateway/exterfaceInvoke.json";
	// private String
	// ReqURL="http://sandboxapi.kdniao.cc:8080/kdniaosandbox/gateway/exterfaceInvoke.json";

	// /**
	// * 根据物流单号和快递公司编号查看物流信息
	// * 2018/7/3
	// * @throws Exception
	// */
	public String getOrderTraces(String shipperCode, String logisticCode)
			throws Exception {
		String requestData = "{'OrderCode':'','ShipperCode':'" + shipperCode
				+ "','LogisticCode':'" + logisticCode + "'}";
		Map<String, String> params = new HashMap<String, String>();
		params.put("RequestData", urlEncoder(requestData, "UTF-8"));
		params.put("EBusinessID", EBusinessID);
		params.put("RequestType", "1002");
		String dataSign = encrypt(requestData, AppKey, "UTF-8");
		params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
		params.put("DataType", "2");
		String result = sendPost(ReqURL, params);
		// 根据公司业务处理返回的信息......
		return result;
	}

	/**
	 * 根据物流单号获取物流公司编号
	 * 
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public String getShipperCode(String logisticCode)
			throws UnsupportedEncodingException, Exception {
		String requestData = "{'LogisticCode':'" + logisticCode + "'}";

		Map<String, String> params = new HashMap<String, String>();
		params.put("RequestData", urlEncoder(requestData, "UTF-8"));
		params.put("EBusinessID", EBusinessID);
		params.put("RequestType", "2002");
		String dataSign = encrypt(requestData, AppKey, "UTF-8");
		params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
		params.put("DataType", "2");

		
		
		String result = sendPost(ReqURL, params);
		System.err.println(result);
		// 根据公司业务处理返回的信息......
//		JSONObject jsonObject = JSONObject.fromObject(result);
//		if (jsonObject != null) {
//			JSONObject shippers = jsonObject.getJSONArray("Shippers").isEmpty()
//					? null
//					: jsonObject.getJSONArray("Shippers").getJSONObject(0);
//			String shippersCode = shippers != null
//					? shippers.getString("ShipperCode")
//					: null;
//			String shippersName = shippers != null
//					? shippers.getString("ShipperName")
//					: null;
//			if (shippersCode == null || shippersName == null) {
//				return null;
//			}
//			return shippersCode + "," + shippersName;
//			// return shippersCode;
//		}
		JSONObject jsonObject = JSONObject.parseObject(result);
		JSONArray jsonArray = jsonObject.getJSONArray("Shippers");
		result=jsonArray.getJSONObject(0).getString("ShipperCode");
//		String ShipperName = jsonArray.getJSONObject(0).getString("ShipperName");
//		HashMap<String,Object> resultMap = new HashMap<>();
//		resultMap.put("ShipperCode", result);
//		resultMap.put("ShipperName", ShipperName);
		return result;
	}

	/**
	 * Json方式 查询订单物流轨迹
	 * 
	 * @throws Exception
	 */
	public String getOrderTracesByJson(String expCode, String expNo)
			throws Exception {
		String requestData = "{'OrderCode':'','ShipperCode':'" + expCode
				+ "','LogisticCode':'" + expNo + "'}";

		Map<String, String> params = new HashMap<String, String>();
		params.put("RequestData", urlEncoder(requestData, "UTF-8"));
		params.put("EBusinessID", EBusinessID);
		// params.put("RequestType", "8001");
		params.put("RequestType", "1002");
		String dataSign = encrypt(requestData, AppKey, "UTF-8");
		params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
		params.put("DataType", "2");
		String result = sendPost(ReqURL, params);
		// 根据公司业务处理返回的信息......
		return result;
	}

	/**
	 * MD5加密
	 * 
	 * @param str
	 *            内容
	 * @param charset
	 *            编码方式
	 * @throws Exception
	 */
	private String MD5(String str, String charset) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes(charset));
		byte[] result = md.digest();
		StringBuffer sb = new StringBuffer(32);
		for (int i = 0; i < result.length; i++) {
			int val = result[i] & 0xff;
			if (val <= 0xf) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(val));
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * base64编码
	 * 
	 * @param str
	 *            内容
	 * @param charset
	 *            编码方式
	 * @throws UnsupportedEncodingException
	 */
	private String base64(String str, String charset)
			throws UnsupportedEncodingException {
		String encoded = base64Encode(str.getBytes(charset));
		return encoded;
	}

	private String urlEncoder(String str, String charset)
			throws UnsupportedEncodingException {
		String result = URLEncoder.encode(str, charset);
		return result;
	}

	/**
	 * 电商Sign签名生成
	 * 
	 * @param content
	 *            内容
	 * @param keyValue
	 *            Appkey
	 * @param charset
	 *            编码方式
	 * @throws UnsupportedEncodingException
	 *             ,Exception
	 * @return DataSign签名
	 */

	private String encrypt(String content, String keyValue, String charset)
			throws UnsupportedEncodingException, Exception {
		if (keyValue != null) {
			return base64(MD5(content + keyValue, charset), charset);
		}
		return base64(MD5(content, charset), charset);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param params
	 *            请求的参数集合
	 * @return 远程资源的响应结果
	 */

	private String sendPost(String url, Map<String, String> params) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// POST方法
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			if (params != null) {
				StringBuilder param = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (param.length() > 0) {
						param.append("&");
					}
					param.append(entry.getKey());
					param.append("=");
					param.append(entry.getValue());
					// System.out.println(entry.getKey()+":"+entry.getValue());
				}
				// System.out.println("param:"+param.toString());
				out.write(param.toString());
			}
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
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
		return result.toString();
	}

	private static char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/'};

	public static String base64Encode(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;
		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4)
						| ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4)
					| ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
					| ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}
}
