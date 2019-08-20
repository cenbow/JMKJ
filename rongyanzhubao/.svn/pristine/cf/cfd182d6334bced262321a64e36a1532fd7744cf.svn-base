package com.cn.jm._dao.zan;

import com.cn._gen.dao.ZanDao;
import com.cn._gen.model.Zan;
import com.jfinal.plugin.activerecord.Db;

public class JMZanDao extends ZanDao{

	public Zan getZan(int ids, Integer accountId, int type) {
		return dao.findFirst("SELECT id FROM tb_zan WHERE ids = ? AND accountId = ? AND `type` = ?", ids, accountId, type);
	}
	
	public int countZan(int ids, Integer accountId, int type) {
		String sql = "SELECT count(*) FROM tb_zan WHERE ids = ? AND accountId = ? AND `type` = ?";
		return Db.queryInt(sql,ids, accountId, type);
	}
}

