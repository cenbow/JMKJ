package com.cn.jm.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.tio.core.Tio;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.common.WsResponse;

import com.cn._gen.model.Account;
import com.cn._gen.model.Goods;
import com.cn._gen.model.Order;
import com.cn._gen.model.Room;
import com.cn._gen.model.RoomManagement;
import com.cn._gen.model.RoomMerchant;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm._dao.goods.JMGoodsDao;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm._dao.order.OrderState;
import com.cn.jm._dao.room.JMRoomDao;
import com.cn.jm._dao.room.JMRoomManagementDao;
import com.cn.jm._dao.room.JMRoomMerchantDao;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.constants.JMConstants;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.information.ZbMessage;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.tio.JMTio;
import com.cn.jm.util.tio.config.JMServerConfig;
import com.cn.jm.util.tio.config.JMWebsocketStarter;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

public class JMRoomManagementService extends BasicsService<RoomManagement> {
	JMRoomManagementDao roomManagementDao = Aop.get(JMRoomManagementDao.class);

	JMOrderDao orderDao = Aop.get(JMOrderDao.class);

	JMRoomMerchantDao roomMerchantDao = Aop.get(JMRoomMerchantDao.class);

	JMImgService imgService = Aop.get(JMImgService.class);

	JMGoodsDao goodsDao = Aop.get(JMGoodsDao.class);
	
	JMRoomDao roomDao = Aop.get(JMRoomDao.class);

	/**
	 * @author cyl
	 * @date 2019年7月15日 下午2:21:59
	 * @Description:发送直播订单并发送商品
	 * @reqMethod post
	 * @paramter
	 * @param account
	 * @param toAccountId
	 * @param detailImageList
	 * @param goodsImageList
	 * @param goods
	 * @return
	 * @throws Exception
	 */
	@Before(Tx.class)
	public JMResult sendOrderToAccount(Account account, Integer toAccountId, ArrayList<String> detailImageList,
			ArrayList<String> goodsImageList, Goods goods) throws Exception {
		Integer roomId = goods.getRoomId();
		Room room = roomDao.getById(roomId);
		if (account.getType() != AccountEnum.TYPE_MANAGER.getValue()
				&& account.getType() != AccountEnum.TYPE_ANCHOR.getValue()) {
			return JMResultUtil.failDesc(ZbMessage.IDENTITY_INCORRECT);
		}
		if (account.getType().equals(AccountEnum.TYPE_MANAGER.getValue())) {
			Integer selectRoomId = roomDao.selectRoomIdByManagerId(account.getId());
			if(selectRoomId==null||!selectRoomId.equals(roomId)) {
				return JMResultUtil.failDesc(ZbMessage.IDENTITY_INCORRECT);
			}
			RoomManagement roomManage = roomMerchantDao.selectRoomManage(account.getId());
			goods.setUserId(roomManage.getMerchantAccountId());
		}
		if (account.getType().equals(AccountEnum.TYPE_ANCHOR.getValue())) {
			if(!room.getAccountId().equals(account.getId())) {
				return JMResultUtil.failDesc(ZbMessage.IDENTITY_INCORRECT);
			}
			RoomMerchant roomMerchant = roomMerchantDao.selectRoomMerchant(account.getId());
			goods.setUserId(roomMerchant.getMerchantAccountId());
		}
		if (goodsImageList.size() > 0) {
			goods.setThumbnail(goodsImageList.get(0));
		}
		goods.setAccountType(AccountEnum.TYPE_MANAGER.getValue());
		goodsDao.save(goods);
		imgService.inserImages(goods.getId(), detailImageList, goodsImageList);
		JMResult orderResult = orderDao.create(goods.getId(), null, toAccountId, null, null,
				OrderState.WEBCAST_ORDER_TYPE);
		if (orderResult.getCode() == JMResult.FAIL) {
			try {
				throw new RuntimeException("生成直播订单异常");
			} catch (Exception e) {
				System.err.println(orderResult);
				return orderResult;
			}
		}
		HashMap<String, Object> result = new HashMap<>();
		String orderNo = orderResult.getData().toString();
		result.put("orderNo", orderNo);
		Order order = orderDao.getByOrderNo(orderNo);
		order.setRoomId(roomId);
		orderDao.update(order);
		//判断该用户在不在该群
		boolean inGroup = JMTio.isInGroup(roomId.toString(), toAccountId);
		result.put("orderId", order.getId());
		result.put("inGroup", inGroup);
		result.put("money", order.getMoney());
		result.put("roomId", roomId);
		JMTio.send(toAccountId,
				JMResultUtil.getJMResult(JMResultUtil.SEND_ORDER, result, PromptInformationEnum.SEND_ORDER));
		//发群消息
//		ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
//		if(groupContext!=null) {
//			WsResponse wsResponse = WsResponse.fromText(JMResultUtil.success
//					(ZbMessage.ZB_ROOM_NUMBER,result,JMResultUtil.ROOM_NUMBER_UPDATE).toString(), JMServerConfig.CHARSET);
//			System.err.println(result.toString());
//			//发送给群组
//			Tio.sendToGroup(groupContext, room.getId().toString(), wsResponse);
//		}
		return JMResultUtil.successDesc(JMMessage.SUCCESS);

	}

	public Room selectMyAnchorRoom(Account account) {
		Room room = roomManagementDao.selectMyAnchorRoom(account);
		return room;
	}

}
