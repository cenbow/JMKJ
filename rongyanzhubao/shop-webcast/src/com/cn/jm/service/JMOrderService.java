package com.cn.jm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.cn._gen.model.Account;
import com.cn._gen.model.AccountUser;
import com.cn._gen.model.Goods;
import com.cn._gen.model.Order;
import com.cn._gen.model.Room;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm._dao.goods.GoodsEnum;
import com.cn.jm._dao.goods.JMGoodsDao;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm._dao.order.OrderState;
import com.cn.jm._dao.order.PayEnum;
import com.cn.jm._dao.room.JMRoomDao;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.pay.PayException;
import com.cn.jm.core.pay.wechat.WechatPay;
import com.cn.jm.core.pay.wechat.WechatTradeType;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.utils.util.PayUtils;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.information.RedisInformation;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.RedisUtil;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

public class JMOrderService extends BasicsService<Order>{

	private JMGoodsDao goodsDao = Aop.get(JMGoodsDao.class);
	private JMOrderDao orderDao = Aop.get(JMOrderDao.class);
	private JMAccountUserService accountUserService = Aop.get(JMAccountUserService.class);
	// private JMOrderGoodsService orderGoodsService =
	// Aop.get(JMOrderGoodsService.class);
	private JMGoodsService goodsService = Aop.get(JMGoodsService.class);
	private JMRoomDao roomDao = Aop.get(JMRoomDao.class);

	private static HashMap<Integer,Object> lockMap = new HashMap<>();
	private static List<Integer> lockList = new ArrayList<>();
	
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
	static int i = 0;
	@Before(Tx.class)
	public JMResult pay(String orderNo, Account account, int payType, String ip, String payPwd) {
			// TODO 订单号查询
		Order order = orderDao.getByOrderNO(orderNo);
		if (order == null)
			return JMResult.create().fail(JMMessage.RECORD_NOT_EXIST);
		synchronized (getLock(order.getGoodsId())) { // (1.111 2.111 3.111) 4.112
			try {
				JMResult result = vaildPay(order, account);
				if (result.getCode() != JMResult.SUCCESS)
					return result;
				if (PayEnum.BANK.equals(order.getPayType())) {
					return JMResultUtil.fail(PromptInformationEnum.ORDER_BANK_PAYING);
				}
				result = getPayResult(payType, order, orderNo, ip);
				order.setState(OrderState.WAIT_SERVE_STATE.getCode());
				RedisUtil.putTimingData(RedisInformation.UPDATE_ORDER_NOT_PAY_TIME, order.getId().toString(), RedisInformation.SET_ORDER_NOT_PAY_STATE_METHOD);
				order.setPayType(payType);
				
				orderDao.update(order);
				return result;
			} finally {
				deleteLock(order.getGoodsId());
			}
		}
	}

	private JMResult getPayResult(int payType, Order order, String orderNo, String ip) {
		// 实际付款金额
		BigDecimal paid = order.getMoney();
		if (PayEnum.BALANCE.equals(payType)) {
			// // TODO 余额支付 业务
			// return this.balancePay(order, account,payPwd);
		} else if (PayEnum.WEIXIN_APP.equals(payType)) {

			try {
				return JMResult.create().success(WechatPay.app(PropKit.get("weixin_app_key"), PropKit.get("weixin_app_appid"),
						PropKit.get("weixin_app_mch_id"), orderNo, "购买商品", paid.stripTrailingZeros().toPlainString(), ip,
						PropKit.get("weixin_notice_url"), WechatTradeType.APP, (String) null), "操作成功");
			} catch (PayException e) {
				e.printStackTrace();
			}
			return JMResult.create().fail("支付失败");
//			return PayUtils.weichatAppPay(paid, orderNo, ip);
		} else if (PayEnum.ALI_APP_PAY.equals(payType)) {
			return PayUtils.aliPay(paid, orderNo);
		} else if (PayEnum.WEIXIN_JSAPI.equals(payType)) {// 公众号支付
			// return PayUtils.weichatJSAPIPay(paid, orderNo, ip,account.getOpenId());
		} else if (PayEnum.BANK.equals(payType)) {
			return JMResultUtil.success();
		}
		return JMResult.create().fail(JMMessage.FAIL);
	}

	// 支付回调
	@Before(Tx.class)
	public boolean payCallback(Order order, Map<String, String> params) {
		String transId = order.getTransId();
		String timeEnd = order.getTimeEnd();
		// TODO 查询订单
		Order old = orderDao.getByOrderNO(order.getOrderNo());

		if (old == null)
			return true;

		if (!(OrderState.WAIT_PAY_STATE.equals(old.getState()) || OrderState.WAIT_SERVE_STATE.equals(old.getState())))
			return true;

		if (!PayUtils.payVail(params, old.getPayType(), old.getMoney()))
			return false;

		old.setTransId(transId);
		old.setTimeEnd(timeEnd);
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
	private JMResult vaildPay(Order order, Account account) {
		if (!order.getAccountId().equals(account.getId()))
			return JMResult.create().fail(JMMessage.PRIVILEGE_NOT_ENOUGH);

		// 如果不是待付款
		if (!OrderState.WAIT_PAY_STATE.equals(order.getState()) && !OrderState.WAIT_SERVE_STATE.equals(order.getState()))
			return JMResult.create().fail(JMMessage.ORDER_STATE_ABNORMAL);
		// 改商品是否正在被购买
		if (!canPay(order.getGoodsId(), order.getId())) {
			return JMResultUtil.fail(PromptInformationEnum.GOODS_FORM_TIES);
		}
		return JMResult.create().success(JMMessage.SUCCESS);
	}

	/**
	 * 判断该商品关联的订单能否支付
	 * 
	 * @param goodsId
	 * @return
	 */
	public boolean canPay(Integer goodsId, Integer orderId) {
		Goods goods = goodsDao.getById(goodsId);
		return orderDao.selectByCanPayGoodsId(goodsId, orderId) == null
				&& GoodsEnum.CAN_SELL_STATE.identical(goods.getSellState());
	}

	public boolean paySuccesss(Order order) {
		order.setPayTime(new Date());
		order.setState(OrderState.TO_BE_RECEIVED_STATE.getCode());// 已支付
		// 将订单设置为已经支付
		order.update();
		// 商品被买一次就不会有库存了
		Goods goods = goodsService.selectById(order.getGoodsId());
		if (goods == null || GoodsEnum.NOT_SELL_STATE.identical(goods.getSellState())) {
			return false;
		}
		if(GoodsEnum.NOT_SELL_STATE.identical(goods.getSellState())) {
			order.setType(OrderState.ABNORMAL_TYPE.getCode());
		}
		goods.setStock(0);
		goods.setState(GoodsEnum.LOWER_SHELF_STATE.getCode());
		goods.setSellState(GoodsEnum.NOT_SELL_STATE.getCode());
		// 取消其他的用户下同一商品的订单
		orderDao.cancelOtherByGoodsId(goods.getId());
		return goodsDao.update(goods);
	}

	/**
	 * 原路返回
	 * 
	 * @param orderId
	 *            订单id
	 * @param account
	 *            用户信息
	 * @param ip
	 * @return 返回原路返回是否成功信息
	 * @date 2018年8月7日
	 */
	public JMResult doubleBack(Order order, BigDecimal money, int accountId) {
		AccountUser accountUser = accountUserService.selectOneByKeyAndValue("accountId = ", accountId);

		if (accountUser == null) {
			return JMResult.fail(this, "用户不存在");
		}

		Integer payType = order.getPayType();// 支付方式

		if (PayEnum.ALI_APP_PAY.equals(payType)) {// 支付宝支付
			AlipayTradeRefundResponse request = com.cn.jm.util.pay.ali.AliPay.aliPayRefund(order.getTransId(),
					money.toPlainString());
//			String appid = PropKit.get("ali_appid");// 应用id
//			String privateKey = PropKit.get("ali_rsa_private");// 支付宝私钥
//			String publicKey = PropKit.get("ali_rsa_public");// 支付宝公钥
//			AlipayTradeRefundResponse request = AliPay.aliPayRefund(appid, privateKey, publicKey, order.getTransId(),
//					money.toPlainString());
			// 传回参数为空,则访问退款接口失败
			if (request == null || !request.isSuccess())
				return JMResult.fail(request, "退款失败");

			// 记录账单明细
			// balanceRecordDao.saveCoin(order.getAccountId(),JMBalanceRecordDao.ALI_INCOME,JMBalanceRecordDao.RETURN_GOODS_ORDER,
			// 1,"商品订单退款（支付宝原路退回）",order.getMoney(), "RMB",order.getId(),-1);
		} else if (PayEnum.WEIXIN_APP.equals(payType)) {// 微信支付
//			String appid = PropKit.get("weixin_app_appid");// 应用id
//			String pid = PropKit.get("weixin_app_mch_id");// 商务号
//			String privateKey = PropKit.get("weixin_app_key");// 微信私钥
//			String certificatePath = PropKit.get("weixin_certificate_path");// 微信证书路径

			Map<String, String> map = null;
			try {
				map = com.cn.jm.util.pay.wechat.WechatPay.appRefund(order.getOrderNo(), "商品退款",
						order.getMoney().stripTrailingZeros().toPlainString(),
						money.stripTrailingZeros().toPlainString(), order.getTransId());
//				map = WechatPay.appRefund(privateKey, appid, pid, order.getOrderNo(), "商品退款",
//						order.getMoney().stripTrailingZeros().toPlainString(),
//						money.stripTrailingZeros().toPlainString(), order.getTransId(), certificatePath);
			} catch (PayException e) {
				e.printStackTrace();
			}
			if (map == null || !map.get("return_code").equals("SUCCESS") || map.get("transaction_id") == null) {
				// 传回参数不为空,则访问退款接口成功
				return JMResult.fail(map, "退款失败");
			}

			// 记录账单明细
			// balanceRecordDao.saveCoin(order.getAccountId(),JMBalanceRecordDao.WECHAT_INCOME,JMBalanceRecordDao.RETURN_GOODS_ORDER,
			// 1,"商品订单退款（微信原路退回）",order.getMoney(), "RMB",order.getId(),-1);

		}
		// else if(payType == JMConstants.BALANCE) {
		// boolean b = balanceDao.income(accountId,money);
		// if(b) {
		// //记录账单明细
		// balanceRecordDao.saveCoin(accountId,JMBalanceRecordDao.INCOME,JMBalanceRecordDao.RETURN_GOODS_ORDER,
		// 1,"商品订单退款（余额收入）",money, "RMB",order.getId(),-1);
		// }
		// }

		return JMResult.create().success(JMMessage.SUCCESS);
		// 如果退款成功,则将订单设置为交易关闭
		// 修改订单 业务
	}
	
	/**
	 * 搜索出直播间订单的信息
	 * 
	 * @param account
	 * @param state
	 * @param keyword
	 * @param minPrice
	 * @param maxPrice
	 * @param startTime
	 * @param endTime
	 * @param pageNumber
	 * @param pageSize
	 * @return 
	 */
	public Page<Order> pageMyRoomOrder(Account account, int state, String keyword, String minPrice, String maxPrice,
			String startTime, String endTime, Integer pageNumber, Integer pageSize) {
		Integer roomId = getRoomIdByAccountType(account.getType(), account.getId());
		return orderDao.pageMyRoomOrder(roomId, state, keyword, minPrice, maxPrice, startTime, endTime, pageNumber, pageSize);
	}

	/**
	 * 搜索我的直播端每个订单状态的个数
	 * @param account
	 * @param endTime 
	 * @param startTime 
	 * @param maxPrice 
	 * @param minPrice 
	 * @param keyword 
	 * @return 
	 */
	public HashMap<String, Integer> myWebCastOrderNum(Account account, String keyword, String minPrice, String maxPrice, String startTime, String endTime, Integer state) {
		List<Order> orderList = orderDao.myWebCastOrderNum(
				getRoomIdByAccountType(account.getType(), account.getId()),
				account.getId(), keyword, minPrice, maxPrice, startTime, endTime, state);
		return setOrderNum(orderList, userOrderNameByStateMap);
	}


	/**
	 * 搜索出商家端订单的信息
	 * 
	 * @param account
	 * @param state
	 * @param keyword
	 * @param minPrice
	 * @param maxPrice
	 * @param startTime
	 * @param endTime
	 * @param pageNumber
	 * @param pageSize
	 * @return 
	 */
	public Page<Order> pageMyShopOrder(Account account, int state, String keyword, String minPrice,
			String maxPrice, String startTime, String endTime, Integer pageNumber, Integer pageSize) {
		return orderDao.pageMyShopOrder(account.getId(), state, keyword, minPrice, maxPrice, startTime, endTime, pageNumber, pageSize);
	}

	/**
	 * 搜索我的用户端每个订单状态的个数
	 * @param account
	 * @param endTime 
	 * @param startTime 
	 * @param maxPrice 
	 * @param minPrice 
	 * @param keyword 
	 * @return 
	 */
	public HashMap<String, Integer> myOrderNum(Account account, String keyword, String minPrice, String maxPrice,
			String startTime, String endTime, Integer state) {
		List<Order> orderList = orderDao.myOrderNum(account.getId(),
				account.getId(), keyword, minPrice, maxPrice, startTime, endTime, state);
		return setOrderNum(orderList, userOrderNameByStateMap);
	}


	/**
	 * 搜索我的商城端每个订单状态的个数
	 * @param account
	 * @param endTime 
	 * @param startTime 
	 * @param maxPrice 
	 * @param minPrice 
	 * @param keyword 
	 * @return 
	 */
	public HashMap<String, Integer> myShopOrderNum(Account account, String keyword, String minPrice, String maxPrice,
			String startTime, String endTime, Integer state) {
		List<Order> orderList = orderDao.myShopOrderNum(account.getId(),
				account.getId(), keyword, minPrice, maxPrice, startTime, endTime, state);
		return setOrderNum(orderList, shopOrderNameByStateMap);
	}

	public Integer getRoomIdByAccountType(Integer accountType, Integer accountId) {
		if (AccountEnum.TYPE_SHOP.equals(accountType)) {
			Room room = roomDao.selectRoomByMerchantId(accountId);
			return room == null ? null : room.getId();
		} else if (AccountEnum.TYPE_ANCHOR.equals(accountType)) {
			Room room = roomDao.selectRoom(accountId);
			return room == null ? null : room.getId();
		} else if (AccountEnum.TYPE_MANAGER.equals(accountType)) {
			return roomDao.selectRoomIdByManagerId(accountId);
		}
		return null;
	}
	
	/**
	 * 
	 * 填充不同订单状态的个数
	 * @param orderList
	 * @return
	 */
	private HashMap<String, Integer> setOrderNum(List<Order> orderList, Map<Integer,String> nameByStateMap) {
		HashMap<String,Integer> resultMap = new HashMap<>();
		int allNum = 0;
		for (Order order : orderList) {
			String name = nameByStateMap.get(order.getState());// 获取对应的map中的key
			int num = order.getInt("num") == null ? 0 : order.getInt("num");// 获取属性中的个数
			allNum += num;
			if(StrKit.isBlank(name)) {// 如果名称为空则可能是退款状态
				if(OrderState.IN_AFTER_REFUND_STATE.equals(order.getRefundState())) {// 是退款状态就设置名称和加入总数
					name = "refund";
//					allNum += num;
				}else
					num = 0;
			}
//			else {
//				if(OrderState.AFTER_SUCCESS_REFUND_STATE.equals(order.getRefundState())) {
//				}else
//					num = 0;
//			}
			Integer orderNum = resultMap.get(name);
			if(orderNum == null) {
				resultMap.put(name, num);
			}else {
				resultMap.put(name, orderNum +num);
			}
		}
		resultMap.put("all", allNum);
		return resultMap;
	}

	public void cancelPay(String orderNo, Integer accountId) {
		Order order = orderDao.getByOrderNO(orderNo);
		if (order != null) {
			if(accountId.equals(order.getAccountId())) {
				if(OrderState.WAIT_SERVE_STATE.equals(order.getState())) {
					order.setState(OrderState.WAIT_PAY_STATE.getCode());
					orderDao.update(order);
				}
			}
		}
	}

	/**
	 * 
	 * @param goodsId
	 */
	private void deleteLock(Integer goodsId) {
		Iterator<Integer> iterator = lockList.iterator();
		while(iterator.hasNext()) {
			if(iterator.next().equals(goodsId)) {
				iterator.remove();
				return;
			}
		}
		lockMap.remove(goodsId);
	}
	/**
	 * 根据商品获取锁对象
	 * 如果对应的商品id存在则将锁的位置返回
	 * 不存在对应的商品id则创建新的锁名称
	 * 使用完锁后必须调用 deleteLock(goodsId) 方法将锁删除
	 * @param goodsId
	 * @return
	 */
	private synchronized Object getLock(Integer goodsId) {
		Object lock = lockMap.get(goodsId);
		lockList.add(goodsId);
		if(lock == null) {
			lock = new Object();
			lockMap.put(goodsId, lock);
			return lock;
		}
		return lock;
	}
	

	private static HashMap<Integer,String> shopOrderNameByStateMap = new HashMap<>();
	private static HashMap<Integer,String> userOrderNameByStateMap = new HashMap<>();
	static {
		//全部 待付款 待发货 已发货 已完成 退款
		//all noPay noDelivery delivery complete refund
		String noPay = "noPay";
		String noDelivery = "noDelivery";
		String delivery = "delivery";
		String complete = "complete";
		shopOrderNameByStateMap.put(0, noPay);
		shopOrderNameByStateMap.put(1, noPay);
		shopOrderNameByStateMap.put(2, noDelivery);
		shopOrderNameByStateMap.put(7, delivery);
		shopOrderNameByStateMap.put(8, complete);
		shopOrderNameByStateMap.put(9, complete);
		userOrderNameByStateMap.put(0, noPay);
		userOrderNameByStateMap.put(1, noPay);
		userOrderNameByStateMap.put(2, noDelivery);
		userOrderNameByStateMap.put(7, delivery);
		userOrderNameByStateMap.put(8, complete);
	}
	
	/*
	 * protected void toIsPayState(Order order) { Date date = new Date();
	 * order.setPayTime(date); order.setState(2);//已支付 order.update(); //TODO 保留更新字段
	 * 状态，支付时间,第三方支付编号,乐观锁 order.keep("id","state","payTime","transId","version");
	 * 
	 * //TODO 更新订单 后面的是校验订单状态 状态待支付才能更新为支付 orderDao.updateOrder(order,
	 * OrderState.WAIT_PAY);
	 * 
	 * //TODO 移除半小时未支付失效订单队列 (业务) //removeWaitPayOrder(order); }
	 */
}
