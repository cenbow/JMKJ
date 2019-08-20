package com.cn.jm.method.timer;

import com.cn._gen.dao.OrderDao;
import com.cn._gen.model.Order;
import com.cn.jm._dao.order.OrderState;
import com.cn.jm._dao.order.PayEnum;
import com.cn.jm.core.utils.cache.JMCacheKit;
import com.cn.jm.service.JMOrderService;
import com.jfinal.kit.StrKit;

public class UpdateOrderNotPayMethod implements TimerMethod{

	@Override
	public void apply(String orderId) {
		JMOrderService orderService = new JMOrderService();
		if(StrKit.isBlank(orderId)) {
			return;
		}
		Order order = orderService.selectById(Integer.valueOf(orderId));
		if(order != null && OrderState.WAIT_SERVE_STATE.equals(order.getState()) && !PayEnum.BANK.equals(order.getPayType())) {
			order.setState(OrderState.WAIT_PAY_STATE.getCode());
			orderService.update(order);
			JMCacheKit.removeAll(OrderDao.cacheName);
		}
		
	}

}
