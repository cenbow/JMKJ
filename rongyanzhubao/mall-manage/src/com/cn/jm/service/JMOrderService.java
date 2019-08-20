package com.cn.jm.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

//import com.alipay.api.response.AlipayTradeRefundResponse;
import com.cn._gen.model.Account;
//import com.cn._gen.model.AccountUser;
import com.cn._gen.model.Goods;
import com.cn._gen.model.Order;
import com.cn.jm._dao.goods.GoodsEnum;
import com.cn.jm._dao.goods.JMGoodsDao;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm._dao.order.OrderState;
import com.cn.jm._dao.order.PayEnum;
import com.cn.jm.core.JMMessage;
//import com.cn.jm.core.constants.JMConstants;
import com.cn.jm.core.constants.PayType;
//import com.cn.jm.core.pay.PayException;
//import com.cn.jm.core.pay.ali.AliPay;
//import com.cn.jm.core.pay.wechat.WechatPay;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.utils.util.PayUtils;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.util.JMResultUtil;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
//import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.tx.Tx;

public class JMOrderService {
	
	JMGoodsDao goodsDao = Aop.get(JMGoodsDao.class);
	JMOrderDao orderDao = Aop.get(JMOrderDao.class);
	JMOrderGoodsService orderGoodsService = Aop.get(JMOrderGoodsService.class);
	JMGoodsService goodsService = Aop.get(JMGoodsService.class);
	JMAccountUserService accountUserService = Aop.get(JMAccountUserService.class);
	
	/**
	 * 
	 * @date 2018年12月3日 下午2:10:54
	 * @author lgk
	 * @Description: 支付
	 * @param orderNo
	 * @param account
	 * @param payType
	 * @return
	 *
	 */
	@Before(Tx.class)
	public JMResult pay(String orderNo, Account account, int payType, String ip,String payPwd) {
		synchronized (orderNo) {
			// TODO 订单号查询
			Order order = orderDao.getByOrderNO(orderNo);
			JMResult result = vaildPay(order, account);
			if (result.getCode() != JMResult.SUCCESS)
				return result;
			order.setPayType(payType);
			orderDao.update(order);
			// 实际付款金额
			BigDecimal paid = order.getMoney();
			if (payType == PayType.BALANCE.getPayType()){
//			// TODO 余额支付 业务
//			return this.balancePay(order, account,payPwd);
			}else if (PayEnum.WEIXIN_APP.equals(payType)){
				return PayUtils.weichatAppPay(paid, orderNo, ip);
			}else if (PayEnum.ALI_APP_PAY.equals(payType)){
				return PayUtils.aliPay(paid, orderNo);
			}else if(PayEnum.WEIXIN_JSAPI.equals(payType)){//公众号支付
//			return PayUtils.weichatJSAPIPay(paid, orderNo, ip,account.getOpenId());
			}else if(PayEnum.BANK.equals(payType)) {
				return JMResultUtil.success();
			}
			return JMResult.create().fail(JMMessage.FAIL);
		}
	}
	
//	/**
//	 * 
//	 * @date 2018年12月3日 下午2:11:36
//	 * @author lgk
//	 * @Description: 余额支付
//	 * @param order
//	 * @param account
//	 * @return
//	 *
//	 */
//	@Before(Tx.class)
//	public JMResult balancePay(Order order,Account account,String payPwd) {
//		AccountUser accountUser = accountUserService.selectOneByKeyAndValue("accountId = ", account.getId());
//		
//		if(accountUser == null) {
//			return JMResult.create().fail(JMMessage.RECORD_NOT_EXIST);
//		}
//		
//		if(JMToolString.isEmpty(user.getPayPwd())){
//			return JMResult.create().code(JMResult.FAIL).desc("请先设置钱包密码");
//		}
//		if(userDao.checkPayPwd(user.getPayPwd(), payPwd) == false){
//			return JMResult.create().code(JMResult.FAIL).desc("钱包密码不正确");
//		}
//		
//		//获取余额
//		AccountBalance coinBalance = balanceDao.getByUserId(account.getId());
//		if(coinBalance == null){
//			return JMResult.create().code(JMResult.FAIL).desc(JMMessage.RECORD_NOT_EXIST);
//		}
//		if(coinBalance.getCount().compareTo(order.getMoney()) == -1)
//			return JMResult.create().fail(JMMessage.BALANCE_NOT_ENOUGH);
//		
//		boolean b = false ;
//		b = balanceDao.consume(account.getId(),order.getMoney());//扣除余额
//		if(b) {
//			this.paySuccesss(order);
//			//记录账单明细
//			balanceRecordDao.saveCoin(account.getId(),JMBalanceRecordDao.EXPEND,JMBalanceRecordDao.BUY_GOODS,
//					1,"购买商品支出（余额支出）",order.getMoney(), "RMB",order.getId(),coinBalance.getId());
//		}else return JMResult.create().fail("支付失败");
//		return JMResult.create().success(this, JMMessage.SUCCESS);
//	}
	
	
	
	//支付回调
	@Before(Tx.class)
	public boolean payCallback(Order order, Map<String, String> params) {
		String transId = order.getTransId();
		String timeEnd = order.getTimeEnd();
		//TODO 查询订单
		Order old = orderDao.getByOrderNO(order.getOrderNo());
		
		if(old == null)
			return true;
		
		if (!(OrderState.WAIT_PAY.equals(old.getState()) || OrderState.WAIT_SERVE.equals(old.getState()))) 
			return true;
		
		if(!PayUtils.payVail(params, old.getPayType(), old.getMoney()))
			return false;
		
		old.setTransId(transId);
		old.setTimeEnd(timeEnd);
//		if(bb) {
//			int expendType = JMBalanceRecordDao.ALI_EXPEND;
//			String msgString = "购买商品支出（支付宝支出）";
//			if(order.getPayType() ==(PayType.WEIXIN_APP.getPayType())) {
//				expendType = JMBalanceRecordDao.WECHAT_EXPEND ;
//				msgString = "购买商品支出（微信支出）";
//			}else if(order.getPayType() == PayType.ALI_APP_PAY.getPayType()){
//				expendType = JMBalanceRecordDao.ALI_EXPEND ;
//				msgString = "购买商品支出（支付宝支出）";
//			}
//			//记录账单明细
//			balanceRecordDao.saveCoin(order.getAccountId(),expendType,JMBalanceRecordDao.BUY_GOODS,
//					1,msgString,order.getMoney(), "RMB",order.getId(),-1);
//		}
		return paySuccesss(order);
	}
	
	/**
	 * 
	 * @date 2018年12月3日 下午2:11:53
	 * @author lgk
	 * @Description: 校验订单，用户状态
	 * @param order
	 * @param account
	 * @return
	 *
	 */
	private JMResult vaildPay(Order order,Account account) {
		if(order == null)
			return JMResult.create().fail(JMMessage.RECORD_NOT_EXIST);
		
		if(!order.getAccountId().equals(account.getId()))
			return JMResult.create().fail(JMMessage.PRIVILEGE_NOT_ENOUGH);
		
		//如果不是待付款
		if(order.getState() != OrderState.WAIT_PAY.getCode())
			return JMResult.create().fail(JMMessage.ORDER_STATE_ABNORMAL);
		//改商品是否正在被购买
		if(!canPay(order.getGoodsId())) {
			return JMResultUtil.fail(PromptInformationEnum.GOODS_FORM_TIES);
		}
		return JMResult.create().success(JMMessage.SUCCESS);
	}
	
	/**
	 * 判断该商品关联的订单能否支付
	 * @param goodsId
	 * @return
	 */
	private boolean canPay(Integer goodsId) {
		Goods goods = goodsDao.getById(goodsId);
		return orderDao.selectByCanPayGoodsId(goodsId) == null && GoodsEnum.CAN_SELL_STATE.identical(goods.getSellState());
	}

	public boolean paySuccesss(Order order) {
		order.setPayTime(new Date());
		order.setState(2);//已支付
		//将订单设置为已经支付
		order.update();
		//商品被买一次就不会有库存了
		Goods goods = goodsService.selectById(order.getGoodsId());
		if(goods == null) {
			return false;
		}
		goods.setStock(0);
		goods.setSellState(GoodsEnum.NOT_SELL_STATE.getCode());
		return goodsService.update(goods);
	}
	
//	
//	/**
//	 * 原路返回
//	 * 
//	 * @param orderId
//	 *            订单id
//	 * @param account
//	 *            用户信息
//	 * @param ip
//	 * @return 返回原路返回是否成功信息
//	 * @date 2018年8月7日
//	 */
//	public JMResult doubleBack(Order order, BigDecimal money, int accountId) {
//		AccountUser accountUser = accountUserService.selectOneByKeyAndValue("accountId = ", accountId);
//		
//		if(accountUser == null){
//			return JMResult.fail(this, "用户不存在");
//		}
//		
//		Integer payType = order.getPayType();// 支付方式
//
//		
//		if (payType == JMConstants.ZFB) {// 支付宝支付
//			String appid = PropKit.get("ali_appid");// 应用id
//			String privateKey = PropKit.get("ali_rsa_private");// 支付宝私钥
//			String publicKey = PropKit.get("ali_rsa_public");// 支付宝公钥
//			AlipayTradeRefundResponse request = AliPay.aliPayRefund(appid, privateKey, publicKey, order.getTransId(),
//					money.toPlainString());
//			// 传回参数为空,则访问退款接口失败
//			if (request == null || !request.isSuccess())
//				return JMResult.fail(request, "退款失败");
//			
//			
//			//记录账单明细
////			balanceRecordDao.saveCoin(order.getAccountId(),JMBalanceRecordDao.ALI_INCOME,JMBalanceRecordDao.RETURN_GOODS_ORDER,
////					1,"商品订单退款（支付宝原路退回）",order.getMoney(), "RMB",order.getId(),-1);
//			
//
//		} else if (payType == JMConstants.WEIXIN) {// 微信支付
//			String appid = PropKit.get("weixin_app_appid");// 应用id
//			String pid = PropKit.get("weixin_app_mch_id");// 商务号
//			String privateKey = PropKit.get("weixin_app_key");// 微信私钥
//			String certificatePath = PropKit.get("weixin_certificate_path");//微信证书路径
//			
//			Map<String, String> map = null;
//			try {
//				
//				map = WechatPay.appRefund(privateKey, appid, pid, order.getOrderNo(), "商品退款",
//						order.getMoney().stripTrailingZeros().toPlainString(),
//						money.stripTrailingZeros().toPlainString(), order.getTransId(),certificatePath);
//			} catch (PayException e) {
//				e.printStackTrace();
//			}
//			if (map == null || !map.get("return_code").equals("SUCCESS") || map.get("transaction_id") == null) {
//				// 传回参数不为空,则访问退款接口成功
//				return JMResult.fail(map, "退款失败");
//			}
//			
//			//记录账单明细
////			balanceRecordDao.saveCoin(order.getAccountId(),JMBalanceRecordDao.WECHAT_INCOME,JMBalanceRecordDao.RETURN_GOODS_ORDER,
////					1,"商品订单退款（微信原路退回）",order.getMoney(), "RMB",order.getId(),-1);
//			
//		}
////		else if(payType == JMConstants.BALANCE) {
////			boolean b = balanceDao.income(accountId,money);
////			if(b) {
////				//记录账单明细
////				balanceRecordDao.saveCoin(accountId,JMBalanceRecordDao.INCOME,JMBalanceRecordDao.RETURN_GOODS_ORDER,
////						1,"商品订单退款（余额收入）",money, "RMB",order.getId(),-1);
////			}
////		}
//
//		return JMResult.create().success(JMMessage.SUCCESS);
//		// 如果退款成功,则将订单设置为交易关闭
//		// 修改订单 业务
//	}
	
	/*protected void toIsPayState(Order order) {
		Date date = new Date();
		order.setPayTime(date);
		order.setState(2);//已支付
		order.update(); 
		//TODO 保留更新字段  状态，支付时间,第三方支付编号,乐观锁
		order.keep("id","state","payTime","transId","version");
		
		//TODO 更新订单  后面的是校验订单状态 状态待支付才能更新为支付
		orderDao.updateOrder(order, OrderState.WAIT_PAY);
		
		//TODO 移除半小时未支付失效订单队列  (业务)
		//removeWaitPayOrder(order);
	}*/

}
