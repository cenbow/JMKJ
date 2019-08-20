package com.cn.jm.controller.shop.api;

import java.util.HashMap;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.cn._gen.model.Account;
import com.cn._gen.model.Order;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.pay.ali.AliPay;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.valid.rule.JMVaildRegex;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMOrderService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;



/**
 * 
 *
 * @date 2019年1月3日 下午4:36:57
 * @author Administrator
 * @Description: 支付模块
 *
 */
@API
@ParseOrder(5)
@JMRouterMapping(url = ApiPayController.url)
public class ApiPayController extends JMBaseApiController  {
	
	public static final String url ="/api/pay";
	
	@Inject
	public JMOrderService orderSerivce;
	@Inject
	public JMOrderDao orderDao ;
	
	/**
	 * 
	 * @date 2019年3月11日 上午11:13:38
	 * @author JaysonLee
	 * @Description: 支付宝回调
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 *
	 */
	@API(isScran= false)
	public void aliNotifyUrl() {
		Map<String, String> params = AliPay.returnCheck(getRequest());
		boolean signVerified = false;
		try {
			signVerified = AlipaySignature.rsaCheckV1(params, PropKit.get("ali_public"),"UTF-8", "RSA2");
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		if (!signVerified) {// 验签不通过
			renderText("fail");
			return;
		}
		//订单编号
		String orderNo = params.get("out_trade_no");
		//支付宝支付编号
		String trade_no = params.get("trade_no");
		
		String timeEnd = params.get("gmt_payment");
		
		boolean b = false;
		Order order = orderDao.getByOrderNO(orderNo);
		if(order == null){
			renderText("fail");
			return ;
		}
		order.setTransId(trade_no);
		order.setTimeEnd(timeEnd);
		//TODO 业务
		b = orderSerivce.payCallback(order,params);
		
		if (b)
			renderText("success");
		renderText("fail");

	}
	
	/**
	 * 
	 * @date 2019年1月3日 下午3:32:25
	 * @author JaysonLee
	 * @Description: 支付订单
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderNo,String,订单编号,r:t,p:G20181221144447209981738,d:G20181221144447209981738
	 * @paramter payPsw,String,支付密码（余额支付使用）,r:f,p:123456,d:123456
	 * @paramter payType,int,支付方式：0支付宝1微信2余额4银行卡支付,r:t,p:2,d:2
	 * @pDescription	code:0失败1成功2未登录,desc:描述,data:签名数据
	 *
	 */
	@API
	@Before({JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields="orderNo"),
		// 校验支付类型  0支付宝 1微信 2 余额 4银行
		@JMRuleVaild(ruleClass=JMVaildRegex.class,fields="payType",regex= {"[0-4]"})
	})
	public void pay() {
		String orderNo = getPara("orderNo");
		Account account = getAttr("account");
		Integer payType = getParaToInt("payType");
		String payPsw = getPara("payPsw",null);
		//String ip = IpKit.getRealIp(getRequest());
		String ip = "119.130.170.83";
		
		//TODO 支付订单业务
		orderSerivce.pay(orderNo, account, payType, ip,payPsw).renderJson(this);
	}

	/**
	 * 
	 * @date 2019年7月20日 11:04:46
	 * @Description: 取消支付
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderNo,String,订单编号,r:t,p:G20181221144447209981738,d:G20181221144447209981738
	 * @pDescription	code:0失败1成功2未登录,desc:描述,data:签名数据
	 *
	 */
	@API
	@Before({JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields="orderNo"),
	})
	public void cancelPay() {
		String orderNo = getPara("orderNo");
		Account account = getAttr("account");
		//TODO 支付订单业务
		orderSerivce.cancelPay(orderNo, account.getId());
		JMResultUtil.success().renderJson(this);
	}
	/**
	 * 
	 * @date 2019年3月11日 上午11:13:49
	 * @author JaysonLee
	 * @Description: 微信支付回调
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 *
	 */
	@API(isScran= false)
	public void weixinNotifyUrl() {
		
		String xmlMsg = HttpKit.readData(getRequest());
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);

		String return_code = params.get("return_code");
		String result_code = params.get("result_code");
		
		if ((!"SUCCESS".equals(return_code) && !result_code.equals("SUCCESS"))) {
			return;
		}
		
		//订单编号
		String orderNo = params.get("out_trade_no");
		//微信支付编号
		String transId = params.get("transaction_id");
		
		String timeEnd = params.get("time_end");
		
		boolean b = false;
		
		Order order = orderDao.getByOrderNO(orderNo);
		if(order == null){
			return ;
		}
		order.setTransId(transId);
		order.setTimeEnd(timeEnd);
		//TODO 业务
		b = orderSerivce.payCallback(order,params);
		
		
		//商品

		if (b) {
			Map<String, String> xml = new HashMap<String, String>();
			xml.put("return_code", "SUCCESS");
			xml.put("return_msg", "OK");
			renderText(PaymentKit.toXml(xml));
		}
		

	}
}
