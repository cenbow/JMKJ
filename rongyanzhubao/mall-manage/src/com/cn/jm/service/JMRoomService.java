package com.cn.jm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.AssociateRequest;
import com.cn._gen.model.BroadcastRequest;
import com.cn._gen.model.LabelRelation;
import com.cn._gen.model.Room;
import com.cn._gen.model.RoomManagement;
import com.cn._gen.model.RoomMerchant;
import com.cn._gen.model.RoomRecord;
import com.cn.jm._dao.account.JMAccountDao;
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
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.ZbMessage;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.ZbUtil;
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
	
	JMNoDao noDao = Aop.get(JMNoDao.class);
	
	JMRoomRecordDao roomRecordDao = Aop.get(JMRoomRecordDao.class);
	
	JMAccountDao accountDao = Aop.get(JMAccountDao.class);
	
	JMAssociateRequestDao associateRequestDao = Aop.get(JMAssociateRequestDao.class);
	
	
	@Before(Tx.class)
	public JMResult createRoom(Room room,Integer accountId,String ids) {
		//把最新的开播设成已使用
		BroadcastRequest request = requestDao.selectAgreeRequest(accountId);
		request.setState(RequerstEnum.IS_SHOW.getValue());
		request.update();
		Room selectRoom = roomDao.selectRoom(accountId);
		Date date = new Date();
		//则更新信息
		if(selectRoom!=null) {
			selectRoom.setAddress(room.getAddress());
			selectRoom.setAnchorIntroduction(room.getAnchorIntroduction());
			selectRoom.setAnnouncement(room.getAnnouncement());
			selectRoom.setDesc(room.getDesc());
			selectRoom.setImage(room.getImage());
			selectRoom.setState(RoomEnum.ONLINE.getValue());
			selectRoom.setName(room.getName());
			selectRoom.setRoomIntroduction(room.getRoomIntroduction());
			room.setId(selectRoom.getId());
			this.update(selectRoom);
		}else {//新增
			room.setCreateTime(date);
			room.setRoomNumber(noDao.selectOne().getNo());
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
		//创建直播记录
		RoomRecord roomRecord = new RoomRecord();
		roomRecord.setCreateTime(date);
		roomRecord.setAccountId(accountId);
		roomRecord.setRoomId(room.getId());
		roomRecord.setBeginTime(date);
		roomRecord.setPlayUrl(ZbUtil.util.getPlayUrl(room.getRoomNumber()));
		roomRecord.setPushUrl(ZbUtil.util.getPushUrl(room.getRoomNumber(),true));
		if(selectRoom!=null) {
			roomRecord.setWatchNumber(selectRoom.getRobotNumber());
		}
		roomRecord.save();
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
	
	public Page<Room> selectRoomPage(Integer pageNumber,Integer pageSize,Integer labelId){
		//标签先按一对一来，先不去重
		return roomDao.selectRoomPage(pageNumber, pageSize, labelId);
	}
	
	
	public Room selectRoom(Account account) {
		Room room = roomDao.selectRoom(account.getId());
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

	
	
	

}
