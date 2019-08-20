
package com.cn.jm._dao.order;

import java.util.HashMap;
import java.util.List;

import com.cn._gen.dao.OrderGoodsDao;
import com.cn._gen.model.OrderGoods;


/**
 * Generated by 广州小跑robot.
 */
public class JMOrderGoodsDao extends OrderGoodsDao{
	
	public List<OrderGoods> listByOrderId(int orderId){
		HashMap<String, Object> andpm = new HashMap<>();
		andpm.put("orderId",orderId);
		return list("*", andpm, "id","DESC", true);
	}
	
}
