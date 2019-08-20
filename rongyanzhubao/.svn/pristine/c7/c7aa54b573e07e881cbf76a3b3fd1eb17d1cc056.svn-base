package com.cn.jm._dao.room;

import java.text.ParseException;
import java.util.List;

import com.cn._gen.dao.RoomDao;
import com.cn._gen.model.Account;
import com.cn._gen.model.AccountUser;
import com.cn._gen.model.Room;
import com.cn._gen.model.RoomRecord;
import com.cn.jm._dao.follow.FollowType;
import com.cn.jm._dao.label.LabelEnum;
import com.cn.jm.util.DateUtils;
import com.cn.jm.util.sqltool.JMCommonDao;
import com.cn.jm.util.sqltool.Query;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class JMRoomDao extends RoomDao{
	private JMCommonDao commonDao = JMCommonDao.jmd;
	
	public Room selectRoom(Integer accountId) {
		String sql = "select * from webcast_room where accountId = ?";
		return Room.dao.findFirst(sql,accountId);
	}
	
	public Page<Room> selectRoomPage(Integer pageNumber,Integer pageSize,Integer labelId,Integer isRecommend,String keyword){
		Query query = new Query();
//		query.put("and r.state=", RoomEnum.ONLINE.getValue());
		query.put("and r.isRecommend=", isRecommend, false);
		query.putLike("and r.name", keyword, false);
		query.put("and l.type=", LabelEnum.WEBCAST_COLUMN_TYPE.getCode());
		query.put("and lr.labelId=", labelId ,false);
		query.put("and r.state!=",RoomEnum.FZ.getValue());
		String select = "select r.*,l.name as labelName,au.nick,au.head";
		String inner = "r left join tb_label_relation lr on lr.ids=r.id"
				+ " left join tb_label l on lr.labelId = l.id"
				+ " left join tb_account_user au on au.accountId = r.accountId :where order by r.state asc,followNumber desc,createTime desc";
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
		query.put("and wr.id=", id);
		String select = "select au.nick,au.head,wr.*,rr.playUrl,rr.pushUrl,rr.watchNumber,rr.beginTime";
		String inner = "wr inner join webcast_room_record rr on wr.id = rr.roomId"
				+ " inner join tb_account_user au on au.accountId = wr.accountId "
				+ ":where order by rr.createTime desc";
		return commonDao.selectOne(Room.class, query, select, inner);
	}
	
	public List<Room> selectLiveRoomList(){
		Query query = new Query();
		query.put("and wr.state = ", RoomEnum.ONLINE.getValue());
		query.put("and rr.state = ", RoomRecordEnum.LIVE.getValue());
		String select = "select au.nick,au.head,wr.*,rr.playUrl,rr.pushUrl,rr.watchNumber,rr.beginTime";
		String inner = "wr inner join webcast_room_record rr on wr.id = rr.roomId"
				+ " inner join tb_account_user au on au.accountId = wr.accountId "
				+ ":where order by rr.createTime desc";
		return commonDao.selectList(Room.class, query, select, inner);
	}
	
	public List<AccountUser> selectMyManage(Integer anchorAccountId){
		Query query = new Query();
		String select = "select au.nick,ac.mobile,au.accountId,au.head";
		String inner = " au inner join webcast_room_management rm on rm.manageAccountId=au.accountId"
				+ " inner join webcast_room_merchant me on me.merchantAccountId = rm.merchantAccountId"
				+ " inner join tb_account ac on au.accountId = ac.id";
		query.put("and me.anchorAccountId=", anchorAccountId);
		return commonDao.selectList(AccountUser.class, query, select ,inner);
	}
	
	
	public List<AccountUser> selectMyMerchant(Integer anchorAccountId){
		Query query = new Query();
		String select = "select au.nick,ac.mobile,au.accountId,au.head";
		String inner = "au inner join webcast_room_merchant me on me.merchantAccountId = au.accountId"
				+ " inner join tb_account ac on au.accountId = ac.id ";
		query.put("and anchorAccountId=", anchorAccountId);
		return commonDao.selectList(AccountUser.class, query, select ,inner);
	}
	
	public Page<Room> selectSystemPage(Integer pageNumber, Integer pageSize, String mobile, String name, Integer state,
			Integer isRecommend, String startTime, String endTime, String merchantName, String merchantMobile) throws ParseException{
		Query query = new Query();
		query.putLike("and r.name", name, false);
		query.putLike("and ac.mobile", mobile ,false);
		query.put("and r.isRecommend=", isRecommend, false);
		query.put("and r.state=", state,false);
		query.putLike("and mu.nick", merchantName, false);
		query.putLike("and mc.mobile", merchantMobile, false);
		query.put("and r.createTime >=",
				DateUtils.stringToDate(startTime, DateUtils.FORMAT_SHORT),
				false);
		query.put("and r.createTime <=",
				DateUtils.stringToDate(endTime, DateUtils.FORMAT_SHORT), false);
		String select = "select r.*,au.nick,ac.mobile,mu.nick as merchantName,mc.mobile as merchantMobile";
		String inner = "r inner join tb_account_user au on au.accountId = r.accountId"
				+ " left join webcast_room_merchant rm on rm.anchorAccountId = au.accountId"
				+ " left join tb_account ac on ac.id = au.accountId"
				+ " left join tb_account_user mu on mu.accountId = rm.merchantAccountId"
				+ " left join tb_account mc on mc.id = mu.accountId :where order by  r.createTime desc";
		return commonDao.page(Room.class, query, pageNumber, pageSize, select, inner);
	}
	
	public Room selectSystemRoom(Integer id) {
		String sql = "select r.*,au.nick,ac.mobile from webcast_room r inner join "
				+ "tb_account_user au on au.accountId = r.accountId "
				+ "inner join tb_account ac on ac.id = au.accountId where r.id = ?";
		return Room.dao.findFirst(sql,id);
	}
	
	/**
	 * 根据房管的用户id查询出对应的直播间id,如果查询不出则为空
	 * @param accountId
	 * @return 
	 */
	public Integer selectRoomIdByManagerId(Integer accountId) {
		Room room = dao.findFirst("SELECT r.id FROM webcast_room_management management"
				+ " INNER JOIN webcast_room_merchant merchant ON merchant.merchantAccountId = management.merchantAccountId"
				+ " INNER JOIN webcast_room r on r.accountId = merchant.anchorAccountId"
				+ " WHERE management.manageAccountId = " + accountId);
		return room != null ? room.getId() : null;
	}
	
	public Room selectRoomByMerchantId(Integer accountId) {
		String sql = "SELECT * FROM webcast_room r inner join webcast_room_merchant m on r.accountId = m.anchorAccountId "
				+ "where m.merchantAccountId = ?";
		return dao.findFirst(sql,accountId);
	}
	
	public Page<Room> selectMyFollowRoom(Account account,Integer pageNumber,Integer pageSize){
		Query query = new Query();
		String select = "select r.*,au.nick,au.head";
		String inner = "r inner join tb_follow f on r.id = f.ids"
				+ " inner join tb_account_user au on au.accountId = r.accountId";
		query.put("and f.type=", FollowType.ROOM.getCode());
		query.put("and r.state!=",RoomEnum.FZ.getValue());
//		query.put("and r.state=", RoomEnum.ONLINE.getValue());
		query.put("and f.accountId=", account.getId());
		return commonDao.page(Room.class, query, pageNumber, pageSize, select,inner);
	}
	
	public int addZan(Integer roomId) {
		String sql = "update webcast_room set zanNumber = zanNumber + 1 where id = ?";
		return Db.update(sql,roomId);
	}
	
	
	public RoomRecord selectLiveRoomRecord(Integer roomId) {
		String sql = "select * from webcast_room_record where roomId = ? and state = ?";
		return RoomRecord.dao.findFirst(sql, roomId ,RoomRecordEnum.LIVE.getValue());
	}
	
	
}
