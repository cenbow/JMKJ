package com.cn.jm.quatz;


import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cn._gen.model.Order;
import com.cn.jm._dao.order.JMOrderDao;
import com.jfinal.aop.Aop;

/**
 *
 * 2017年10月29日 上午10:55:17
 */
public class OrderReceivingGoodsJob implements Job{

	JMOrderDao orderDao = Aop.get(JMOrderDao.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<Order> orderList = orderDao.selectByState("7","10","sendTime");
		for (Order order : orderList) {
			orderDao.confirmReceipt(order.getAccountId(), order.getId());
		}
	}

}
