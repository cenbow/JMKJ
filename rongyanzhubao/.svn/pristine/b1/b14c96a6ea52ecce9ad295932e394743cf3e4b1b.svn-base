package com.cn.jm.quatz;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cn.jm._dao.order.JMOrderDao;
import com.jfinal.aop.Aop;

/**
 *
 * 2017年10月29日 上午10:55:17
 */
public class SubstitutePaymentJob implements Job{

	JMOrderDao orderDao = Aop.get(JMOrderDao.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		orderDao.setByState("0", "1", "createTime");
	}

}
