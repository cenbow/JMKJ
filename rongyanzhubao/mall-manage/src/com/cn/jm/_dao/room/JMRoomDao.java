package com.cn.jm._dao.room;

import com.cn._gen.dao.RoomDao;
import com.cn._gen.model.Room;
import com.cn.jm._dao.label.LabelEnum;
import com.cn.jm.util.sqltool.JMCommonDao;
import com.cn.jm.util.sqltool.Query;
import com.jfinal.plugin.activerecord.Page;

public class JMRoomDao extends RoomDao{
	private JMCommonDao commonDao = JMCommonDao.jmd;
	
	public Room selectRoom(Integer accountId) {
		String sql = "select * from webcast_room where accountId = ?";
		return Room.dao.findFirst(sql,accountId);
	}
	
	public Page<Room> selectRoomPage(Integer pageNumber,Integer pageSize,Integer labelId){
		Query query = new Query();
		query.put("and r.state=", RoomEnum.ONLINE.getValue());
		query.put("and l.type = ", LabelEnum.WEBCAST_COLUMN_TYPE.getCode());
		query.put("and lr.labelId= ", labelId ,false);
		String select = "select r.*,lr.name as labelName";
		String inner = "r left join tb_label_relation lr on lr.ids=r.id"
				+ " left join tb_label l on lr.labelId = l.id";
		return commonDao.page(Room.class, query, pageNumber, pageSize, select , inner);
	}
	
	
	public Room selectRoomById(Integer id) {
		String sql = "select * from webcast_room where id = ?";
		return Room.dao.findFirst(sql,id);
	}
	
	//当前直播的房间
	public Room selectLiveRoom(Integer id) {
		Query query = new Query();
		query.put("and wr.state = ", RoomEnum.ONLINE.getValue());
		query.put("and rr.state = ", RoomRecordEnum.LIVE.getValue());
		String select = "select *";
		String inner = "wr inner join webcast_room_record rr on wr.id = rr.roomId :where order by createTime desc";
		return commonDao.selectOne(Room.class, query, select, inner);
	}
	
	
	
	
}
