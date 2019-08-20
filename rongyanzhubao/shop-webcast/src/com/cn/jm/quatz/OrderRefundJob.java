package com.cn.jm.quatz;


import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cn._gen.model.RefundOrder;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm._dao.refund.JMRefundOrderDao;
import com.jfinal.aop.Aop;

/**
 *
 * 2017年10月29日 上午10:55:17
 */
public class OrderRefundJob implements Job{

	JMRefundOrderDao refundOrderDao = Aop.get(JMRefundOrderDao.class);
	JMOrderDao orderDao = Aop.get(JMOrderDao.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<RefundOrder> refundOrderList = refundOrderDao.selectByState("0","7","createTime");
		for (RefundOrder refundOrder : refundOrderList) {
			Integer id = refundOrder.getId();
//			if(refundOrder.getType() == 0 ){//仅退款(发货前)
//				//state 1已通过等待退款（针对仅退款）2不通过申请5已通过等待上传物流单号退货
//				orderDao.dealAllRefund(id, 5,"");
//			}else
			if(refundOrder.getType() == 1){//退货退款
				orderDao.dealGoodsRefund(id, 8,"");
			}else if(refundOrder.getType() == 2 || refundOrder.getType() == 0 ){
				orderDao.dealAllRefund(id, 5,"");
			}
		}
	}

}
