package com.cn.jm._dao.room;

import com.cn._gen.dao.AssociateRequestDao;
import com.cn._gen.model.AssociateRequest;
import com.cn.jm.util.sqltool.JMCommonDao;
import com.cn.jm.util.sqltool.Query;

public class JMAssociateRequestDao extends AssociateRequestDao{
	
	private JMCommonDao commonDao = JMCommonDao.jmd;
	
	public AssociateRequest selectCurrentAssociate(Integer anchorAccoutId,
			Integer merchantAccountId,RequerstEnum requerstEnum) {
		Query query = new Query();
		query.put("and merchantAccountId=", merchantAccountId,false);
		query.put("and anchorAccoutId=", anchorAccoutId,false);
		query.put("and state = ", requerstEnum.getValue(),false);
		return commonDao.selectOne(AssociateRequest.class, query);
	}

}
