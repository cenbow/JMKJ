
package com.cn.jm._dao.refund;

import java.util.HashMap;
import java.util.List;

import com.cn._gen.dao.RefundOrderDao;
import com.cn._gen.model.Account;
import com.cn._gen.model.Order;
import com.cn._gen.model.OrderGoods;
import com.cn._gen.model.RefundGoods;
import com.cn._gen.model.RefundOrder;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm._dao.order.JMOrderGoodsDao;
import com.cn.jm._dao.order.OrderState;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.service.JMOrderService;
import com.cn.jm.util.SqlUtil;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;


/**
 * Generated by 广州小跑robot.
 */
public class JMRefundOrderDao extends RefundOrderDao{
	
	@Inject
	public JMRefundGoodsDao refundGoodsDao ;
	@Inject
	public JMOrderDao orderDao ;
	@Inject
	public JMOrderGoodsDao orderGoodsDao ;
	
	/**
	 * 
	 * @date 2019年1月8日 上午10:11:56
	 * @author JaysonLee
	 * @Description: 我的退款、售后列表
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 * @param accountId
	 * @param pageNumber
	 * @param pageSize
	 * @param state
	 * @param type
	 * @return
	 *
	 */
	public Page<RefundOrder> pageMy(int accountId,int pageNumber,int pageSize,int state,int type){
		HashMap<String, Object> andpm = new HashMap<>();
		andpm.put("userId", accountId);
		if(state != -1){
			andpm.put("state",state);
		}
		if(type != -1){
			andpm.put("type",type);
		}
		Page<RefundOrder> page = page(pageNumber, pageSize,"*", andpm,"id", "DESC", true);
		for(RefundOrder order : page.getList()){
			order.put("goodsList",refundGoodsDao.listByRefundOrderId(order.getId()));
		}
		return page ;
	}
	
	/**
	 * 
	 * @date 2019年2月21日 下午5:06:20
	 * @author JaysonLee
	 * @Description: 退款订单详情
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 * @param accountId
	 * @param id
	 * @return
	 *
	 */
	public RefundOrder detail(int id){
		HashMap<String, Object> andpm = new HashMap<>();
		andpm.put("id", id);
		RefundOrder refundOrder = get(andpm,true);
		refundOrder.put("goodsList",refundGoodsDao.listByRefundOrderId(id));
		return refundOrder ;
	}
	
	
	
	/**
	 * 
	 * @date 2019年1月8日 下午3:57:33
	 * @author JaysonLee
	 * @Description: 关闭售后申请
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 * @param accountId
	 * @param refundOrderId
	 * @return
	 *
	 */
	public JMResult cancel(int accountId,int refundOrderId){
		HashMap<String, Object> andpm = new HashMap<>();
		andpm.put("userId", accountId);
		andpm.put("state",0);//申请中
		RefundOrder refundOrder = get(andpm,false);
		if(refundOrder == null){
			return JMResult.create().code(JMResult.FAIL).desc("没有此订单");
		}
		refundOrder.setState(3);//关闭申请
		update(refundOrder);
		
		Order order = orderDao.getById(refundOrder.getOrderId());
		if(order == null){
			return JMResult.create().code(JMResult.FAIL).desc("没有此订单");
		}
//		order.setState(10);//售后关闭
		order.setRefundState(OrderState.NORMAL_REFUND_STATE.getCode());
		orderDao.update(order);
		
		List<RefundGoods> list = refundGoodsDao.listByRefundOrderId(refundOrderId);
		for(RefundGoods refundGoods : list){
			OrderGoods orderGoods = orderGoodsDao.getById(refundGoods.getOrderGoodsId());
			if(orderGoods != null){
				orderGoods.setState(0);//售后关闭
				orderGoodsDao.update(orderGoods);
			}
		}
		return JMResult.create().code(JMResult.SUCCESS).desc("关闭售后申请成功");
	}

	public Page<RefundOrder> shopRefundPage(Integer accountId, int pageNumber, int pageSize, int state, int type) {
		StringBuilder fromSql = new StringBuilder();
		fromSql.append(" FROM shop_refund_order sro ")
			.append(" INNER JOIN shop_refund_goods srg ON srg.refundOrderId = sro.id")
			.append(" INNER JOIN shop_goods sg ON srg.goodsId = sg.id")
			.append(" WHERE sg.userId = ").append(accountId);
		SqlUtil.addWhere(fromSql, " AND sro.state =", state, -1);
		SqlUtil.addWhere(fromSql, " AND sro.type = ", type, -1);
		fromSql.append(" ORDER BY sro.id DESC");
		return RefundOrder.dao.paginate(pageNumber, pageSize, false, "SELECT sro.*,sg.price goodsPrice,sg.name goodsName,sg.thumbnail ", fromSql.toString());
	}
	
	
	public Page<RefundOrder> webcastRefundPage(Account account, int pageNumber, int pageSize, int state, int type) {
		StringBuilder fromSql = new StringBuilder();
		Integer roomId = new JMOrderService().getRoomIdByAccountType(account.getType(), account.getId());
		fromSql.append(" FROM shop_refund_order sro ")
		.append(" INNER JOIN shop_refund_goods srg ON srg.refundOrderId = sro.id")
		.append(" INNER JOIN shop_goods sg ON srg.goodsId = sg.id")
		.append(" WHERE sg.roomId = ").append(roomId);
		SqlUtil.addWhere(fromSql, " AND sro.state =", state, -1);
		SqlUtil.addWhere(fromSql, " AND sro.type = ", type, -1);
		return RefundOrder.dao.paginate(pageNumber, pageSize, false, "SELECT sro.*,sg.price goodsPrice,sg.name goodsName,sg.thumbnail ", fromSql.toString());
	}

	/**
	 * 根据订单状态获取订单列表
	 * @param state
	 * @return 
	 */
	public List<RefundOrder> selectByState(String state,String day,String timeField) {
		return RefundOrder.dao.find(String.format("SELECT userId,id,type FROM shop_refund_order WHERE state = %s AND DATE_SUB(CURDATE(), INTERVAL %s DAY) > DATE(%s)",state,day,timeField));
	}

}
