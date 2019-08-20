package com.cn.jm._dao.room;


import com.cn._gen.dao.RoomRecordDao;
import com.cn._gen.model.RoomRecord;
import com.cn.jm.core.exception.BusinessException;
import com.cn.jm.util.sqltool.JMCommonDao;
import com.jfinal.plugin.activerecord.Db;

public class JMRoomRecordDao extends RoomRecordDao{
	private JMCommonDao commonDao = JMCommonDao.jmd;
	
	
	
	public void increaseRoomWatchNumber(Integer roomId){
		String sql = "update webcast_room_record rr inner join "
				+ "webcast_room wr on wr.id = rr.roomId set watchNumber=watchNumber+1 where rr.state = ? and wr.state = ? and wr.id = ?";
		int i = Db.update(sql,RoomRecordEnum.LIVE.getValue(),RoomEnum.ONLINE.getValue(),roomId);
		System.err.println(roomId+"直播间加人"+i);
	}
	
	
	public void reduceRoomWatchNumber(Integer roomId) throws BusinessException {
		String sql = "update webcast_room_record rr inner join "
				+ "webcast_room wr on wr.id = rr.roomId set watchNumber=watchNumber-1 where rr.state = ? and wr.state = ? and wr.id = ?";
		int i = Db.update(sql,RoomRecordEnum.LIVE.getValue(),RoomEnum.ONLINE.getValue(),roomId);
		System.err.println(roomId+"直播间减人"+i);
	}
	
	
	public RoomRecord selectRoomRecord(Integer roomId,RoomRecordEnum roomRecordEnum) {
		String sql = "select * from webcast_room_record where roomId = ? and state = ? order by createTime desc";
		return RoomRecord.dao.findFirst(sql,roomId,roomRecordEnum.getValue());
	}

}
