package com.cn.jm._dao.follow;

import java.util.List;

import com.cn._gen.dao.FollowDao;
import com.cn._gen.model.Account;
import com.cn._gen.model.Follow;
import com.cn.jm.util.sqltool.JMCommonDao;
import com.cn.jm.util.sqltool.Query;
import com.jfinal.plugin.activerecord.Db;

public class JMFollowDao extends FollowDao{
	
	private JMCommonDao commonDao = JMCommonDao.jmd;

	public int cancelFollowRes(Follow pojo) {
		Query query = new Query();
		query.put("accountId=", pojo.getAccountId());
		query.put("and ids=", pojo.getIds());
		query.put("and type=", pojo.getType());
		return commonDao.delete(Follow.class, query);
	}

	public Follow selectFollowRes(Follow pojo) {
		Query query = new Query();
		query.put("accountId=", pojo.getAccountId());
		query.put("and ids=", pojo.getIds());
		query.put("and type=", pojo.getType());
		return commonDao.selectOne(Follow.class, query);
	}



	/**
	 * 
	 * @date 2018年12月8日 下午3:02:30
	 * @author lgk
	 * @Description: true 已经收藏
	 * @param ids
	 * @param type
	 * @return
	 *
	 */
	public boolean check(Integer ids, Integer accountId, FollowType type) {
		if (accountId == null)
			return false;
		Query query = new Query();
		query.put("accountId =", accountId);
		query.put("and ids=", ids);
		query.put("and type=", type.getCode());
		return commonDao.selectOne(Follow.class, query) != null;
	}

	public int selectFollowCount(Integer ids, FollowType type) {
		return Db.queryInt(
				"select count(*) from mbb_follow where ids = ? and type = ?",
				ids, type.getCode());
	}

	public List<Follow> selectAccountFollowCount(Integer accountId) {
		return Follow.dao.find(
				"select count(*) as num ,type from mbb_follow where accountId = ? group by type",
				accountId);
	}
	
	public int selectAccountFollowCount(Account account, FollowType type) {
		return Db.queryInt(
				"select count(*) from mbb_follow where accountId = ? and type = ?",
				account.getId(), type.getCode());
	}

}
