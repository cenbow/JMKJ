package com.cn.jm.util;

import javax.servlet.http.HttpServletRequest;

import com.cn.jm.information.RedisInformation;
//import com.cn.jm.listener.Channels;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

//import redis.clients.jedis.Jedis;

public class RedisUtil{
	
	public static Cache redis = Redis.use(PropKit.get("redis_cacheName"));
	
	private static final String COOKIE_TO_KENKEY = "manage_toKen";
	
//	public static void sendMessage(Channels channels,String message) {
//		Jedis jedis = null;
//		try {
//			jedis = redis.getJedis();
//			jedis.publish(channels.getChannel(), message);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			if(jedis != null) {
//				jedis.close();
//			}
//		}
//	}
	
	/**
	 * 根据ip和用户表头生成toKen获取用户信息
	 * @param ip
	 * @param userAgent
	 * @return
	 */
	public static Integer getUserByToKen(Controller controller) {
		String toKen = generateToKen(controller); 
		return redis.get(RedisInformation.TO_KEN_BY_ACCOUNT_ID + toKen);
	}
	/**
	 * 根据用户id获取toKen
	 */
	public static String getToKenByUser(Object accountId) {
		return redis.get(RedisInformation.ACCOUNT_ID_BY_TO_KEN + accountId);
	}
	/**
	 * 设置用户信息跟登录的token相关联
	 */
	public static void setUserAndToKen(Object accountId,Controller controller) {
		String toKen = generateToKen(controller);
		redis.setex(RedisInformation.TO_KEN_BY_ACCOUNT_ID + toKen,RedisInformation.SYSTEM_LOGIN_OVER_TIME, accountId);
		redis.setex(RedisInformation.ACCOUNT_ID_BY_TO_KEN + accountId,RedisInformation.SYSTEM_LOGIN_OVER_TIME, toKen);
	}
	/**
	 * 根据校验toKen删除用户id
	 * @param accountId
	 */
	public static void removeUserIdByToKen(String toKen) {
		redis.del(RedisInformation.TO_KEN_BY_ACCOUNT_ID + toKen);
	}
	/**
	 * 根据用户id删除校验toKen
	 * @param accountId
	 */
	public static void removeToKenByUserId(Object accountId) {
		redis.del(RedisInformation.ACCOUNT_ID_BY_TO_KEN + accountId);
	}
	public static void loginOut(Object accountId) {
		String toKen = getToKenByUser(accountId);
		removeUserIdByToKen(toKen);
		removeToKenByUserId(accountId);
	}
	
	/**
	 * 根据ip和用户表头生成toKen
	 * @param ip
	 * @param userAgent
	 * @return
	 */
	public static String generateToKen(Controller controller) {
		HttpServletRequest request = controller.getRequest();
		String cookie_toKen = controller.getCookie(COOKIE_TO_KENKEY);
		if(StrKit.isBlank(cookie_toKen)) {
			String saltToKen = HashKit.generateSaltForSha256();
			controller.setCookie(COOKIE_TO_KENKEY, saltToKen, RedisInformation.SYSTEM_LOGIN_OVER_TIME);
		}
		String userAgent = request.getHeader("User-Agent");
		return HashKit.md5(cookie_toKen + userAgent);
	}

	/**
	 * 设置用户id校验旧手机成功信息
	 * @param accountId
	 */
	public static void putCheckOldMobile(Integer accountId) {
		redis.setex(RedisInformation.CHECK_OLD_MOBILE_SUCCESS + accountId, RedisInformation.CHECK_OLD_MOBILE_TIME, new Object());
	}
	/**
	 * 根据用户id校验旧手机是否校验成功过
	 * @param accountId
	 * @return
	 */
	public static boolean getCheckOldMobile(Integer accountId) {
		Object obj = redis.get(RedisInformation.CHECK_OLD_MOBILE_SUCCESS + accountId);
		return obj != null;
	}
	/**
	 * 如果手机号校验成功后,需要自己手动删除成功信息
	 */
	public static void removeCheckOldMobile(Integer accountId) {
		redis.del(RedisInformation.CHECK_OLD_MOBILE_SUCCESS + accountId);
	}
}
