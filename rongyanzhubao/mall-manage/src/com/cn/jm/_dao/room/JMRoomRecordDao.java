package com.cn.jm._dao.room;


import com.cn._gen.dao.RoomRecordDao;
import com.cn._gen.model.RoomRecord;
import com.cn.jm.core.exception.BusinessException;
import com.jfinal.plugin.activerecord.Db;

public class JMRoomRecordDao extends RoomRecordDao{
//	private JMCommonDao commonDao = JMCommonDao.jmd;
	
	
	
	public void increaseRoomWatchNumber(Integer roomId) throws BusinessException {
		String sql = "update webcast_room_record rr inner join "
				+ "webcast_room wr on wr.id = rr.roomId set watchNumber=watchNumber+1 where rr.state = ? and wr.state = ? and wr.id = ?";
		int i = Db.update(sql,RoomRecordEnum.LIVE.getValue(),RoomEnum.ONLINE.getValue());
		if(i!=1) {
			throw new BusinessException("增加直播间人数失败"+roomId);
		}
	}
	
	
	public void reduceRoomWatchNumber(Integer roomId) throws BusinessException {
		String sql = "update webcast_room_record rr inner join "
				+ "webcast_room wr on wr.id = rr.roomId set watchNumber=watchNumber-1 where rr.state = ? and wr.state = ? and wr.id = ?";
		int i = Db.update(sql,RoomRecordEnum.LIVE.getValue(),RoomEnum.ONLINE.getValue());
		if(i!=1) {
			throw new BusinessException("减少直播间人数失败"+roomId);
		}
	}
	
	
	public RoomRecord selectRoomRecord(Integer roomId,RoomRecordEnum roomRecordEnum) {
		String sql = "select * from webcast_room_record where roomId = ? and state = ? order by createTime desc";
		return RoomRecord.dao.findFirst(sql,roomId,roomRecordEnum.getValue());
	}

}
