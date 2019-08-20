package com.cn.jm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.Json;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.utils.Charsets;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.jfinal.weixin.sdk.utils.RetryUtils;

public class SignatureUtil {
	  private static String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	  private static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}";

    /**
     * 组装签名的字段
     * @param params 参数
     * @param urlEncoder 是否urlEncoder
     * @return String
     */
    public static String packageSign(Map<String, String> params, boolean urlEncoder) {
        // 先将参数以其参数名的字典序升序进行排序
        TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Entry<String, String> param : sortedParams.entrySet()) {
            String value = param.getValue();
            if (StrKit.isBlank(value)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(param.getKey()).append("=");
            if (urlEncoder) {
                try { value = urlEncode(value); } catch (UnsupportedEncodingException e) {}
            }
            sb.append(value);
        }
        return sb.toString();
    }
    
    /**
     * urlEncode
     * @param src 微信参数
     * @return String
     * @throws UnsupportedEncodingException 编码错误
     */
    public static String urlEncode(String src) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, Charsets.UTF_8.name()).replace("+", "%20");
    }
    
    public static Map<String, String> getSignature(String appId, String secret ,String url) {
    	
    	String access_token = get_access_token(appId, secret);
        
    	Map<String, String> map = new HashMap<String,String>();
    	map.put("jsapi_ticket",get_jsapi_ticket(access_token));//83
		map.put("noncestr",  System.currentTimeMillis() + "");//随机字符串78
		map.put("timestamp",Long.toString(new Date().getTime()/1000));//时间戳84
		map.put("url",url);//扩展字段 86
        String stringA = packageSign(map, false);
        map.put("signature", HashKit.sha1(stringA));
        map.remove("url");
        map.remove("jsapi_ticket");
        return map;
    }
    
    public static String get_access_token(String appId, String secret ){
    	final String accessTokenUrl = access_token_url.replace("{appid}", appId).replace("{secret}", secret);
    	String json = HttpUtils.get(accessTokenUrl);
        JSONObject parseObject = JSONObject.parseObject(json);
        
        System.err.println("get_access_token");
        System.err.println(parseObject.toJSONString());
        if(parseObject.get("access_token")!=null){
        	return (String) parseObject.get("access_token");
        } else {
        	System.err.println(parseObject.toJSONString());
        	return null;
        }
    }
    
    
    public static String get_jsapi_ticket(String access_token) {
        final String get_jsapi_ticket_url = url.replace("ACCESS_TOKEN", access_token);

        String json = HttpUtils.get(get_jsapi_ticket_url);
        JSONObject parseObject = JSONObject.parseObject(json);
        if(parseObject.get("errmsg").equals("ok")){
        	return (String) parseObject.get("ticket");
        } else {
        	 System.err.println("get_jsapi_ticket");
        	System.err.println(parseObject.toJSONString());
        	return null;
        }
    }
}
