package com.cn;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.cn._gen.model._BaseMappingKit;
import com.cn.jm.core.JMConfig;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.db.JMDbSourceMeta;
import com.cn.jm.core.interceptor.JMApiExceptionInterceptor;
import com.cn.jm.core.interceptor.JMCommonInterceptor;
import com.cn.jm.core.plugin.quartz.QuartzFactory;
import com.cn.jm.core.plugin.quartz.QuartzPlugin;
import com.cn.jm.information.BasicsInformation;
import com.cn.jm.listener.RedisCacheMsgListener;
//import com.cn.jm.listener.RedisCacheMsgListener;
import com.cn.jm.listener.order.OrderTimer;
import com.cn.jm.quatz.AccountNoJob;
import com.cn.jm.quatz.LiveRoomJob;
import com.cn.jm.quatz.OrderReceivingGoodsJob;
import com.cn.jm.quatz.OrderRefundJob;
import com.cn.jm.quatz.SubstitutePaymentJob;
import com.cn.jm.util.SocketUtil;
import com.cn.jm.util.pay.ali.AliPay;
import com.cn.jm.util.pay.wechat.WechatPay;
import com.cn.jm.util.tio.config.JMServerConfig;
import com.cn.jm.util.tio.config.TioPlugin;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ModelRecordElResolver;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

public class JMBaseConfig extends JMConfig {
	@Override
	public void configConstant(Constants constants) {
		constants.setBaseDownloadPath(BasicsInformation.UPLOAD_FILE_PATH);
		constants.setMaxPostSize(BasicsInformation.VIDEO_MAX_SIZE);
		super.configConstant(constants);
	}
	
	@Override
	public void configHandler(Handlers handlers) {
		super.configHandler(handlers);
		
	}
	
	/* (non-Javadoc)
	 * @see com.cn.jm.core.JMConfig#configPlugin(com.jfinal.config.Plugins)
	 */
	@Override
	public void configPlugin(Plugins me) {
		RedisPlugin redisPlugin = new RedisPlugin(PropKit.get("redis_cacheName"),PropKit.get("redis_url"),PropKit.getInt("redis_port"));
		me.add(redisPlugin);
		me.add(new QuartzPlugin());
		me.add(new EhCachePlugin());
		try {
			if(!SocketUtil.isPortUsing(JMServerConfig.SERVER_PORT)) {
				me.add(new TioPlugin());
			}else {
				System.out.println("心跳端口被占用,启动失败");
			}
		} catch (UnknownHostException e) { }
		super.configPlugin(me);
	}
	
	@Override
	public void configInterceptor(Interceptors interceptors) {
		super.configInterceptor(interceptors);
		// 添加全局异常
		interceptors.addGlobalActionInterceptor(new JMApiExceptionInterceptor());
		interceptors.add(new JMCommonInterceptor());
	}
	
	@Override
	protected void setViewType(Constants constants) {
		constants.setViewType(ViewType.JFINAL_TEMPLATE);
	}

	@Override
	public void onJfinalStartAfter() {
		// 每天凌晨2点执行的事情
		QuartzFactory.startJobCron("0 0 2 * * ?", 1, "accountNo", "no", AccountNoJob.class);
		QuartzFactory.startJobCron("0/15 * * * * ?", 2, "liveRoom", "room", LiveRoomJob.class);
		// 订单10天后自动从待收货变成确认收货
		QuartzFactory.startJobCron("0 0/30 * * * ?", 1, "orderReceivingGoods", "orderReceivingGoods", OrderReceivingGoodsJob.class);
		// 退款订单7天后自动从申请中变成退款成功
		QuartzFactory.startJobCron("0 0/30 * * * ?", 1, "orderRefund", "orderRefund", OrderRefundJob.class);
		// 待支付订单1天后自动从待支付变成取消订单
		QuartzFactory.startJobCron("0 0/30 * * * ?", 1, "substitutePayment", "substitutePayment", SubstitutePaymentJob.class);
		PropKit.append("thirdparty.properties");
		//微信信息初始化
		wechatInit();
		aliInit();
	}
	
	private void aliInit() {
		AliPay.initAliPay(PropKit.get("ali_appid"), PropKit.get("ali_rsa_private"), PropKit.get("ali_public"));
	}

	private void wechatInit(){
//		ApiConfig ac = new ApiConfig();
        // 配置微信 API 相关参数
//        ac.setAppId(weixin_app_appid);
//        ac.setAppSecret(weixin_app_secret);
        // 多个公众号时，重复调用ApiConfigKit.putApiConfig(ac)依次添加即可，第一个添加的是默认
//        ApiConfigKit.putApiConfig(ac);
        // 开发时使用线上AccessToken
//        ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache());
		WechatPay.init(PropKit.get("weixin_app_mch_id"), PropKit.get("weixin_app_appid"), PropKit.get("weixin_app_key"), PropKit.get("weixin_certificate_path"));
	}
	
	@Override
	public List<JMDbSourceMeta> createDbSourceMeta() {
		List<JMDbSourceMeta> dbSourceMetaList = new ArrayList<JMDbSourceMeta>();
		JMDbSourceMeta dbSourceMeta1 =  new JMDbSourceMeta(); 
		dbSourceMeta1.setDbConfigName("base");
		dbSourceMeta1.setUrl(PropKit.get("base_jdbcUrl"));
		dbSourceMeta1.setUserName(PropKit.get("base_user"));
		dbSourceMeta1.setPassword(PropKit.get("base_password"));
		dbSourceMeta1.setMappingKit(new _BaseMappingKit());
		dbSourceMetaList.add(dbSourceMeta1);
		return dbSourceMetaList;
	}

	
	@Override
	public void afterJFinalStart() {
		ModelRecordElResolver.setResolveBeanAsModel(true);
//		super.wallFilter.getConfig().setSelectUnionCheck(false);
		JMConsts.projectName = PropKit.get("projectName");
		this.onJfinalStartAfter();
		/*启动redis消息推送事件*/
//		new Thread(new RedisCacheMsgListener()).start();
		RedisCacheMsgListener.start();
		OrderTimer.start();
	}

	@Override
	public void onJfinalStopBefore() {}
	
	public void beforeJFinalStop(){
	    super.beforeJFinalStop();
		QuartzFactory.stopJob();
		RedisCacheMsgListener.stop();
		OrderTimer.stop();
		System.out.println("停止订单定时器和消息推送");
	}

	@Override
	public void configEngine(Engine me) {}
	
	
	public static void main(String args[]){
		 UndertowServer.start(JMBaseConfig.class, 8082, true); 
	}
	

}
