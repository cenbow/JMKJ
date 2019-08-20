package com.cn.jm.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.server.ServerGroupContext;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsResponse;

import com.aliyuncs.exceptions.ClientException;
import com.cn._gen.model.Account;
import com.cn._gen.model.AccountUser;
import com.cn._gen.model.AssociateRequest;
import com.cn._gen.model.BroadcastRequest;
import com.cn._gen.model.Goods;
import com.cn._gen.model.LabelRelation;
import com.cn._gen.model.Room;
import com.cn._gen.model.RoomManagement;
import com.cn._gen.model.RoomMerchant;
import com.cn._gen.model.RoomRecord;
import com.cn._gen.model.Zan;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm._dao.account.JMAccountDao;
import com.cn.jm._dao.follow.FollowType;
import com.cn.jm._dao.follow.JMFollowDao;
import com.cn.jm._dao.goods.JMGoodsDao;
import com.cn.jm._dao.label.JMLabelRelationDao;
import com.cn.jm._dao.label.LabelEnum;
import com.cn.jm._dao.no.JMNoDao;
import com.cn.jm._dao.room.JMAssociateRequestDao;
import com.cn.jm._dao.room.JMBroadcastRequestDao;
import com.cn.jm._dao.room.JMRoomDao;
import com.cn.jm._dao.room.JMRoomManagementDao;
import com.cn.jm._dao.room.JMRoomMerchantDao;
import com.cn.jm._dao.room.JMRoomRecordDao;
import com.cn.jm._dao.room.RequerstEnum;
import com.cn.jm._dao.room.RoomEnum;
import com.cn.jm._dao.room.RoomRecordEnum;
import com.cn.jm._dao.zan.JMZanDao;
import com.cn.jm._dao.zan.ZanEnum;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.constants.JMConstants;
import com.cn.jm.core.exception.BusinessException;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.MessageTemplate;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.information.ZbMessage;
import com.cn.jm.util.AliMessageUtil;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.ZbUtil;
import com.cn.jm.util.tio.JMTio;
import com.cn.jm.util.tio.config.JMServerConfig;
import com.cn.jm.util.tio.config.JMWebsocketStarter;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

public class JMRoomService extends BasicsService<Room>{
	
	JMRoomDao roomDao = Aop.get(JMRoomDao.class);
	
	JMRoomManagementDao roomManagementDao = Aop.get(JMRoomManagementDao.class);
	
	JMRoomMerchantDao roomMerchantDao = Aop.get(JMRoomMerchantDao.class);
	
	JMBroadcastRequestDao requestDao = Aop.get(JMBroadcastRequestDao.class);
	
	JMLabelRelationDao relationDao = Aop.get(JMLabelRelationDao.class);
	
	JMNoService noService = Aop.get(JMNoService.class);
	
	JMRoomRecordDao roomRecordDao = Aop.get(JMRoomRecordDao.class);
	
	JMAccountDao accountDao = Aop.get(JMAccountDao.class);
	
	JMAssociateRequestDao associateRequestDao = Aop.get(JMAssociateRequestDao.class);
	
	JMFollowDao followDao = Aop.get(JMFollowDao.class);
	
	JMZanDao zanDao = Aop.get(JMZanDao.class);
	
	JMGoodsDao goodsDao = Aop.get(JMGoodsDao.class);
	
	
	@Before(Tx.class)
	public JMResult createRoom(Room room,Integer accountId,String ids) {
		//把最新的开播设成已使用
//		BroadcastRequest request = requestDao.selectAgreeRequest(accountId);
//		request.setState(RequerstEnum.IS_SHOW.getValue());
//		request.update();
		//创建直播记录之前先修改结束正在直播的记录
		this.downSeeding(room.getId());
		Room selectRoom = roomDao.selectRoom(accountId);
		Date date = new Date();
		//则更新信息
		if(selectRoom!=null) {
			if(selectRoom.getState()==RoomEnum.FZ.getValue()) {
				return JMResultUtil.failDesc(ZbMessage.ROOM_FZ);
			}
			selectRoom.setAddress(room.getAddress());
			selectRoom.setAnchorIntroduction(room.getAnchorIntroduction());
			selectRoom.setAnnouncement(room.getAnnouncement());
			selectRoom.setDesc(room.getDesc());
			selectRoom.setImage(room.getImage());
			selectRoom.setState(RoomEnum.ONLINE.getValue());
			selectRoom.setName(room.getName());
			selectRoom.setRoomIntroduction(room.getRoomIntroduction());
			room.setId(selectRoom.getId());
			room.setRoomNumber(selectRoom.getRoomNumber());
			List<Goods> goodsList = goodsDao.selectGoodsByRoomId(selectRoom.getId());
			for(Goods goods:goodsList) {
				goods.setRoomName(room.getName());
				goods.keep("id","roomName");
			}
			Db.batchUpdate(goodsList, goodsList.size());
			this.update(selectRoom);
		}else {//新增
			room.setCreateTime(date);
			room.setRoomNumber(noService.getNo());
			room.setState(RoomEnum.ONLINE.getValue());
			this.save(room);
		}
		List<LabelRelation> relationList = relationDao.selectRelationList(LabelEnum.WEBCAST_COLUMN_TYPE, room.getId());
		relationDao.delete(relationList);
		//保存栏目关联
		String[] labelArray = ids.split(",");
		List<LabelRelation> labelRelationList = new ArrayList<>();
		for(String id:labelArray) {
			LabelRelation labelRelation = new LabelRelation();
			labelRelation.setLabelId(Integer.valueOf(id));
			labelRelation.setIds(room.getId());
			labelRelationList.add(labelRelation);
		}
		Db.batchSave(labelRelationList, labelRelationList.size());
		RoomRecord roomRecord = new RoomRecord();
		roomRecord.setCreateTime(date);
		roomRecord.setAccountId(accountId);
		roomRecord.setRoomId(room.getId());
		roomRecord.setBeginTime(date);
		roomRecord.setPlayUrl(ZbUtil.util.getPlayUrl(room.getRoomNumber()));
		roomRecord.setPushUrl(ZbUtil.util.getPushUrl(room.getRoomNumber(),false));
		roomRecord.save();
		ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
		SetWithLock<ChannelContext> channelContexts = Tio.getChannelContextsByUserid(groupContext, accountId.toString());
		if(channelContexts!=null) {
			Set<ChannelContext> channelContextSet = channelContexts.getObj();
			if(!channelContextSet.isEmpty()) {
				for(ChannelContext channelContext:channelContextSet) {
					System.err.println(channelContext);
					Tio.bindGroup(channelContext, room.getId().toString());
				}
			}
		}
		return JMResultUtil.success(roomRecord);
	}
	
	public Room selectRoomByAccountId(Integer accountId) {
		return roomDao.selectRoom(accountId);
	}
	
	
	
	@Before(Tx.class)
	public JMResult editAssociateRequest(Integer id,Integer state,Account account) {
		AssociateRequest associateRequest = associateRequestDao.getById(id);
		if(associateRequest==null) {
			return JMResultUtil.failDesc(JMMessage.ERROR);
		}
		if(account.getId()!=associateRequest.getAnchorAccountId()) {
			return JMResultUtil.failDesc(JMMessage.ERROR);
		}
		associateRequest.setState(state);
		if(state==RequerstEnum.AGREE.getValue()) {
			//绑定商家
			Room room = roomDao.selectRoom(associateRequest.getAnchorAccountId());
			if(room==null) {
				return JMResultUtil.failDesc(ZbMessage.ROOM_NOT_EXITS);
			}
			RoomMerchant merchant = new RoomMerchant();
			merchant.setAnchorAccountId(associateRequest.getAnchorAccountId());
			merchant.setCreateTime(new Date());
			merchant.setMerchantAccountId(associateRequest.getMerchantAccountId());
			merchant.setRoomId(room.getId());
			merchant.save();
			associateRequest.update();
			return JMResultUtil.successDesc(ZbMessage.BIND_MERCHANT_SUCCESS);
		}
		associateRequest.update();
		return JMResultUtil.successDesc(JMMessage.SUCCESS);
		
	}
	
	
	@Before(Tx.class)
	public JMResult setUpRoomManagement(Integer merchantAccountId,Integer selectAccountId) {
		boolean check = roomManagementDao.check(merchantAccountId);
		if(check) {
			return JMResultUtil.failDesc(ZbMessage.ROOM_EXITS_MANAGEMENT);
		}
		RoomManagement management = new RoomManagement();
		management.setMerchantAccountId(merchantAccountId);
		management.setManageAccountId(selectAccountId);
		management.setCreateTime(new Date());
		boolean flag = roomManagementDao.save(management);
		if(flag) {
			return JMResultUtil.successDesc(JMMessage.SUCCESS);
		}
		return JMResultUtil.failDesc(JMMessage.FAIL);
	}
	
	public boolean checkIsBindMerchant(Integer anchorAccountId) {
		return roomMerchantDao.checkIsBind(anchorAccountId);
	}
	
	
	public boolean checkIsBindManagement(Integer anchorAccountId) {
		return roomManagementDao.checkIsBind(anchorAccountId);
	}
	
	public BroadcastRequest selectBroadcastRequest(Integer accountId) {
		RoomMerchant roomMerchant = roomMerchantDao.selectRoomMerchant(accountId);
		BroadcastRequest broadcastRequest = requestDao.selectRequest(accountId, roomMerchant.getMerchantAccountId());
		return broadcastRequest;
	}
	
	//开播请求
	@Before(Tx.class)
	public JMResult insertBroadcastRequest(Account account) throws ClientException {
		Integer accountId = account.getId();
		RoomMerchant roomMerchant = roomMerchantDao.selectRoomMerchant(accountId);
		if(roomMerchant==null) {
			return JMResultUtil.failDesc(JMMessage.FAIL);
		}
		BroadcastRequest broadcastRequest = new BroadcastRequest();
		broadcastRequest.setAccountId(accountId);
		broadcastRequest.setCreateTime(new Date());
		broadcastRequest.setMerchantAccountId(roomMerchant.getMerchantAccountId());
		if(broadcastRequest.save()) {
			//发送请求短信
			//TODO
			Account merchantAccount = accountDao.getById(roomMerchant.getMerchantAccountId());
			AliMessageUtil.sendSms(merchantAccount.getMobile(), MessageTemplate.MERCHANT_REQUEST, "");
			//发送商家消息
			HashMap<String,Object> result = new HashMap<>();
			result.put("id", broadcastRequest.getId());
			JMTio.send(roomMerchant.getMerchantAccountId(), 
					JMResultUtil.getJMResult(JMResultUtil.BROADCAST_REQUEST, result, PromptInformationEnum.BROADCAST_REQUEST));
			return JMResultUtil.successDesc(ZbMessage.START_SHOW_SUCCESS);
		}else {
			return JMResultUtil.failDesc(JMMessage.FAIL);
		}
	}
	
	@Before(Tx.class)
	public JMResult selectRoomDetail(Integer roomId,Account account) throws BusinessException {
		Room room = roomDao.selectLiveRoom(roomId);
		if(room==null) {
			return JMResultUtil.failDesc(ZbMessage.ROOM_NOT_EXITS);
		}
		//获取通道，绑定组
		ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
		SetWithLock<ChannelContext> channelContexts = Tio.getChannelContextsByUserid(groupContext, account.getId().toString());
		if(channelContexts!=null) {
			Set<ChannelContext> channelContextSet = channelContexts.getObj();
			if(!channelContextSet.isEmpty()) {
				for(ChannelContext channelContext:channelContextSet) {
					System.err.println(channelContext);
					Tio.bindGroup(channelContext, room.getId().toString());
				}
			}
		}
		
		
//		ChannelContext channelContext = JMTio.getAccount(account.getId());
//		if(channelContext!=null) {
//			Tio.bindGroup(channelContext, roomId.toString());
//		}
		//增加人数
		boolean isFollow = followDao.check(room.getId(), account.getId(), FollowType.ROOM);
		room.put("isFollow",isFollow);
		Zan zan = zanDao.getZan(room.getId(), account.getId(), ZanEnum.ROOM_TYPE.getValue());
		room.put("isZan",zan==null?false:true);
		roomRecordDao.increaseRoomWatchNumber(room.getId());
		//发送当前人数给房间的人
		HashMap<String,Object> result = new HashMap<>();
		Integer roomNumber=room.getInt("watchNumber")+1+room.getRobotNumber();
		result.put("roomNumber", roomNumber);
		System.err.println("roomNumber----"+roomNumber);
		if(groupContext!=null) {
			WsResponse wsResponse = WsResponse.fromText(JMResultUtil.success
					(ZbMessage.ZB_ROOM_NUMBER,result,JMResultUtil.ROOM_NUMBER_UPDATE).toString(), JMServerConfig.CHARSET);
			//发送给群组
			Tio.sendToGroup(groupContext, room.getId().toString(), wsResponse);
		}
		//加上自己进去的人数和机器人数
		room.put("watchNumber",roomNumber);
		return JMResult.success(room);
		
	}
	
	@Before(Tx.class)
	public JMResult leaveRoom(Integer roomId,Account account) throws BusinessException {
		Room liveRoom = roomDao.selectLiveRoom(roomId);
		if(liveRoom==null) {
			return JMResultUtil.failDesc(ZbMessage.ROOM_NOT_EXITS);
		}
		RoomRecord roomRecord = roomRecordDao.selectRoomRecord(roomId, RoomRecordEnum.LIVE);
		//如果离开直播间的是主播,则为下播
		if(liveRoom.getAccountId().equals(account.getId())) {
			ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
			WsResponse wsResponse = WsResponse.fromText(JMResultUtil.success
					(ZbMessage.ANCHOR_LEAVE_ROOM,JMResultUtil.ROOM_CLOSE).toString(), JMServerConfig.CHARSET);
			//发送给群组
			Tio.sendToGroup(groupContext, liveRoom.getId().toString(), wsResponse);
			Date date = new Date();
			roomRecord.setEndTime(date);
			roomRecord.setState(RoomRecordEnum.CLOSE.getValue());
			roomRecord.update();
//			//解绑该群组的全部用户
			SetWithLock<ChannelContext> channelContextsByGroup = Tio.getChannelContextsByGroup(groupContext, roomId.toString());
			if(channelContextsByGroup!=null) {
				channelContextsByGroup.clear();
//				Set<ChannelContext> channelContextSet = channelContextsByGroup.getObj();
//				for(ChannelContext channelContext:channelContextSet) {
//					Tio.unbindGroup(roomId.toString(),channelContext);
//				}
			}
			
			Room room = roomDao.selectRoomById(roomId);
			if(room.getState()!=RoomEnum.FZ.getValue()) {
				room.setState(RoomEnum.OFFLINE.getValue());
			}
			room.update();
			return JMResultUtil.successDesc(JMMessage.SUCCESS);
		}
		
		
		HashMap<String,Object> result = new HashMap<>();
		result.put("roomNumber",(roomRecord.getWatchNumber()+liveRoom.getRobotNumber())<1?0:(roomRecord.getWatchNumber()+liveRoom.getRobotNumber())-1);
		//发送当前人数给房间的人
		ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
		WsResponse wsResponse = WsResponse.fromText(JMResultUtil.success
				(ZbMessage.ZB_ROOM_NUMBER,result,JMResultUtil.ROOM_NUMBER_UPDATE).toString(), JMServerConfig.CHARSET);
		//发送给群组
		System.err.println(result.toString()+roomId);
		Tio.sendToGroup(groupContext, liveRoom.getId().toString(), wsResponse);
		//获取通道，离开组
		SetWithLock<ChannelContext> channelContexts = Tio.getChannelContextsByUserid(groupContext, account.getId().toString());
		if(channelContexts!=null) {
			Set<ChannelContext> channelContextSet = channelContexts.getObj();
			if(!channelContextSet.isEmpty()) {
				for(ChannelContext channelContext:channelContextSet) {
					System.err.println(channelContext);
					Tio.unbindGroup(roomId.toString(),channelContext);
				}
			}
		}
		//减少人数
		roomRecordDao.reduceRoomWatchNumber(liveRoom.getId());
		return JMResultUtil.successDesc(JMMessage.SUCCESS);
	}
	
	
	public Page<Room> selectRoomPage(Integer pageNumber,Integer pageSize,Integer labelId,Integer isRecommend,String keyword){
		//标签先按一对一来，先不去重
		Page<Room> page = roomDao.selectRoomPage(pageNumber, pageSize, labelId,isRecommend,keyword);
		for(Room room:page.getList()) {
			RoomRecord roomRecord = roomDao.selectLiveRoomRecord(room.getId());
			if(roomRecord!=null) {
				room.put("watchNumber",roomRecord.getWatchNumber()+room.getRobotNumber());
			}
		}
		return page;
	}
	
	
	public Room selectRoom(Account account) {
		Room room = roomDao.selectRoom(account.getId());
		if(room!=null) {
			RoomRecord roomRecord = roomDao.selectLiveRoomRecord(room.getId());
			if(roomRecord!=null) {
				room.put("watchNumber",roomRecord.getWatchNumber());
				room.put("beginTime",roomRecord.getBeginTime());
			}
		}
		return room;
	}

	/**
	 * 查询该商品是否直播中
	 * @return
	 */
	public boolean isBroad(Integer roomId) {
		if(roomId != null) {
			Room room = selectById(roomId);
			if(room != null) {
				return RoomEnum.ONLINE.equals(room.getState().intValue());
			}
		}
		return false;
	}

	//主播查看房管
	public JMResult selectMyManage(Account account){
		if(account.getType()!=AccountEnum.TYPE_ANCHOR.getValue()) {
			return JMResultUtil.failDesc(ZbMessage.NOT_ANCHOR);
		}
		List<AccountUser> myManage = roomDao.selectMyManage(account.getId());
		return JMResult.success(myManage);
	}
	
	//主播查看商家
	public JMResult selectMyMerchant(Account account) {
		if(account.getType()!=AccountEnum.TYPE_ANCHOR.getValue()) {
			return JMResultUtil.failDesc(ZbMessage.NOT_ANCHOR);
		}
		List<AccountUser> myMerchant = roomDao.selectMyMerchant(account.getId());
		return JMResult.success(myMerchant);
	}
	
	
	//下播
	public boolean downSeeding(Integer roomId) {
		Room room = roomDao.getById(roomId);
		RoomRecord roomRecord = roomRecordDao.selectRoomRecord(roomId, RoomRecordEnum.LIVE);
		//如果离开直播间的是主播,则为下播
		if(roomRecord!=null) {
			ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
			Date date = new Date();
			roomRecord.setEndTime(date);
			roomRecord.setState(RoomRecordEnum.CLOSE.getValue());
			roomRecord.update();
			//解绑该群组的全部用户
			if(groupContext!=null) {
				WsResponse wsResponse = WsResponse.fromText(JMResultUtil.success
						(ZbMessage.ANCHOR_LEAVE_ROOM,JMResultUtil.ROOM_CLOSE).toString(), JMServerConfig.CHARSET);
//				发送给群组
				Tio.sendToGroup(groupContext, room.getId().toString(), wsResponse);
				SetWithLock<ChannelContext> channelContextsByGroup = Tio.getChannelContextsByGroup(groupContext, roomId.toString());
				if(channelContextsByGroup!=null) {
					channelContextsByGroup.clear();
//					Set<ChannelContext> channelContextSet = channelContextsByGroup.getObj();
//					for(ChannelContext channelContext:channelContextSet) {
//						Tio.unbindGroup(roomId.toString(),channelContext);
//					}
				}
			}
			room.setState(RoomEnum.OFFLINE.getValue());
			return room.update();
		}
		return true;
	}
	

	
	//-------------管理后台
	public Page<Room> selectSystemPage(Integer pageNumber, Integer pageSize, String mobile, String name, Integer state,
			Integer isRecommend, String startTime, String endTime, String merchantName, String merchantMobile) {
		try {
			return roomDao.selectSystemPage(pageNumber, pageSize, mobile, name, state, 
					isRecommend, startTime, endTime, merchantName, merchantMobile);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean editState(int id) {
		Room room = roomDao.getById(id);
		String roomNumber = room.getRoomNumber();
		String channelId = ZbUtil.util.getChannelId(roomNumber);
		if(room.getState()==RoomEnum.FZ.getValue()) {
			room.setState(RoomEnum.OFFLINE.getValue());
			ZbUtil.util.recoveryZb(channelId);
		}else {
			room.setState(JMConstants.FZ);
			JMResult jmResult = JMResultUtil.success(ZbMessage.ROOM_FZ,null,JMResultUtil.ROOM_FZ);
			JMTio.send(room.getAccountId(), jmResult);
			ZbUtil.util.stopZb(channelId);
		}
		return roomDao.update(room);
	}
	
	public boolean editIsRecommend(int id) {
		Room room = roomDao.getById(id);
		if(room.getIsRecommend()==1) {
			room.setIsRecommend(0);
		}else {
			room.setIsRecommend(1);
		}
		return roomDao.update(room);
	}
	
	public boolean editRobotNumber(int id,int number) {
		Room room = roomDao.getById(id);
		room.setRobotNumber(room.getRobotNumber()+number);
		ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
		if(groupContext!=null) {
			RoomRecord roomRecord = roomRecordDao.selectRoomRecord(room.getId(), RoomRecordEnum.LIVE);
			//如果当前正在直播
			if(roomRecord!=null) {
				HashMap<String,Object> result = new HashMap<>();
				System.err.println(roomRecord.getWatchNumber()+room.getRobotNumber());
				result.put("roomNumber", roomRecord.getWatchNumber()+room.getRobotNumber());
				WsResponse wsResponse = WsResponse.fromText(JMResultUtil.success
						(ZbMessage.ZB_ROOM_NUMBER,result,JMResultUtil.ROOM_NUMBER_UPDATE).toString(), JMServerConfig.CHARSET);
				System.err.println(result.toString()+room.getId());
				//发送给群组
				Tio.sendToGroup(groupContext, room.getId().toString(), wsResponse);
			}
		}
		return room.update();
	}
	
	
	
	public Room selectSystemRoomDetail(Integer id) {
		Room room = roomDao.selectSystemRoom(id);
		List<AccountUser> manageList = roomDao.selectMyManage(room.getAccountId());
		List<AccountUser> merchantList = roomDao.selectMyMerchant(room.getAccountId());
		room.put("manageList",manageList);
		room.put("merchantList",merchantList);
		return room;
	}
	
	public boolean editRoom(Room room) {
		return this.update(room);
	}
	
	public Page<Room> selectMyFollowRoom(Account account,Integer pageNumber,Integer pageSize){
		Page<Room> page = roomDao.selectMyFollowRoom(account, pageNumber, pageSize);
		for(Room room:page.getList()) {
			RoomRecord roomRecord = roomDao.selectLiveRoomRecord(room.getId());
			if(roomRecord!=null) {
				room.put("watchNumber",roomRecord.getWatchNumber());
			}
		}
		return page;
	}
	
	public void addZan(Integer roomId) {
		roomDao.addZan(roomId);
	}

	@Before(Tx.class)
	public void batchEditState(String ids, Integer state) {
		String[] idArray = ids.split(",");
		List<Room> roomList = new ArrayList<>();
		for(String id:idArray) {
			Room room = new Room();
			room.setId(Integer.valueOf(id));
			room.setState(state);
			roomList.add(room);
			Room selectRoom = roomDao.getById(Integer.valueOf(id));
			String roomNumber = selectRoom.getRoomNumber();
			String channelId = ZbUtil.util.getChannelId(roomNumber);
			if(state==RoomEnum.FZ.getValue()) {
				JMResult jmResult = JMResultUtil.success(ZbMessage.ROOM_FZ,null,JMResultUtil.ROOM_FZ);
				JMTio.send(selectRoom.getAccountId(), jmResult);
				ZbUtil.util.stopZb(channelId);
			}else {
				ZbUtil.util.recoveryZb(channelId);
			}
		}
		Db.batchUpdate(roomList, roomList.size());
		
	}

	public void batchEditRecommend(String ids, Integer isRecommend) {
		String[] idArray = ids.split(",");
		List<Room> roomList = new ArrayList<>();
		for(String id:idArray) {
			Room room = new Room();
			room.setId(Integer.valueOf(id));
			room.setIsRecommend(isRecommend);
			roomList.add(room);
		}
		Db.batchUpdate(roomList, roomList.size());
	}
	
	public List<Room> selectLiveRoomList(){
		return roomDao.selectLiveRoomList();
	}
	
	public RoomRecord selectLiveRoomRecord(Integer roomId) {
		RoomRecord roomRecord = roomRecordDao.selectRoomRecord(roomId, RoomRecordEnum.LIVE);
		return roomRecord;
	}

}
