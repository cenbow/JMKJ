package com.cn.jm.listener;

import com.cn.jm.information.RedisInformation;
import com.cn.jm.method.listener.LisenerMethod;
import com.cn.jm.method.timer.TimerMethod;
import com.cn.jm.util.BasicsMethodUtil;
import com.jfinal.plugin.redis.Redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisCacheMsgListener extends JedisPubSub implements Runnable{
	public static RedisCacheMsgListener redisCacheMsgListener = new RedisCacheMsgListener();
	public static Jedis redis = Redis.use().getJedis();
	public static String [] channels = {/*Channels.SAVE_ACCOUNT.getChannel()*/};
	public static String [] patterns = {Channels.SAVE_ACCOUNT.getChannel()};
 
	@Override
	public void onMessage(String channel,String message) {
		System.out.println("onMessage : 监听消息推送事件启动 获取事件{"+channel+"},传入消息为:"+message);
		LisenerMethod lisenerMethod = BasicsMethodUtil.createMethods(BasicsMethodUtil.LISENER_CLASS_PATH, channel, LisenerMethod.class);
		if(lisenerMethod != null) {
			lisenerMethod.onMessage(message);
		}else {
			System.out.println("无法执行方法 : " + channel);
		}
	}
	
	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		System.out.println("启动消息监听 : channel-" + channel + ";subscribedChannels:" + subscribedChannels);
		super.onSubscribe(channel, subscribedChannels);
	}
	

	@Override 
	public void onPMessage(String pattern, String channel, String message) {
		System.out.println("onPMessage方法 : 事件定时器启动 获取事件消息为 : " + message);
		String[] methodAndMsg = RedisInformation.getTimingMethod(message);
		String methodName = methodAndMsg[0];
		String param = methodAndMsg[1];
		System.out.println("获取方法名称 : " + methodName + " ---- 获取参数值 : " + param);
		TimerMethod timerMethod = BasicsMethodUtil.createMethods(BasicsMethodUtil.TIMER_METHOD_CLASS_PATH, methodName, TimerMethod.class);
		if(timerMethod == null) {
			return;
		}
		timerMethod.apply(param);
	}

	@Override 
	public void onUnsubscribe(String channel, int subscribedChannels) {
		System.out.println("2channel" + channel +",subscribedChannels"+subscribedChannels);
	}

	@Override 
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		System.out.println("3pattern "+ pattern + ",subscribedChannels" + subscribedChannels);
	}

	@Override 
	public void onPSubscribe(String pattern, int subscribedChannels) {
		System.out.println("启动定时器事件 监听数据库位置:" + pattern + ",执行数量 : " + subscribedChannels);
	}

	@Override 
	public void onPong(String pattern) {
		System.out.println("5pattern "+ pattern);
	}
	
	/**
	 * notify-keyspace-events : 
	 * K 键空间通知，以__keyspace@<db>__为前缀
	 * E 键事件通知，以__keysevent@<db>__为前缀
	 * g del , expipre , rename 等类型无关的通用命令的通知, ...
	 * $ String命令
	 * l List命令
	 * s Set命令
	 * h Hash命令
	 * z 有序集合命令
	 * x 过期事件（每次key过期时生成）
	 * e 驱逐事件（当key在内存满了被清除时生成）
	 * A g$lshzxe的别名，因此”AKE”意味着所有的事件
	 * __keyevent@?__:expired : 
	 * __keyevent@后面的?表示第几个数据库，redis默认的数据库是0~15一共16个数据库，根据需求修改@后的数字。expired监听过期redis，可以换成*，监听所有。
	 */
	@Override
	public void run() {
		redis.configSet("notify-keyspace-events", "Ex");
//		redis.subscribe(redisCacheMsgListener,channels);
		redis.psubscribe(redisCacheMsgListener, "__keyevent@5__:expired");
	}
	
	public static void start() {
		new Thread(redisCacheMsgListener).start();
	}
	
	public static void stop() {
		redis.close();
	}

}
