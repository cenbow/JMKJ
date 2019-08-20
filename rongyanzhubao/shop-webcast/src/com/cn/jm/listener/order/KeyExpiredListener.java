package com.cn.jm.listener.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.jm._dao.order.JMOrderDao;
import com.jfinal.aop.Aop;

import redis.clients.jedis.JedisPubSub;

public class KeyExpiredListener extends JedisPubSub {

	public JMOrderDao orderDao  =Aop.get(JMOrderDao.class) ;
	
    protected Logger log = LoggerFactory.getLogger(KeyExpiredListener.class);


    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        log.error("onPSubscribe pattern:{} subscribedChannels:{}", pattern, subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
    	log.error("onPMessage pattern:{} channel",pattern , channel);
    	System.out.println(message);
    	if(message.startsWith(JMOrderDao.GOODS_ORDER)){//商品订单定时取消
    		System.out.println("订单取消"+orderDao.cancelByNo(message));
    	}else if(message.startsWith(JMOrderDao.REFUND_ORDER)){//退款订单自动处理退款
    		System.out.println("系统自动退款"+orderDao.automaticRefund(message));
    	}
    }


}