package com.cn.jm.service;

import java.util.Date;

import com.cn._gen.model.Zan;
import com.cn.jm._dao.goods.JMGoodsStarDao;
import com.cn.jm._dao.zan.JMZanDao;
import com.cn.jm._dao.zan.ZanEnum;
import com.jfinal.aop.Inject;

public class JMZanService extends BasicsService<Zan>{

	@Inject
	JMZanDao zanDao;
	@Inject
	JMGoodsStarDao goodsStarDao;
	
	public boolean zan(int ids, Integer accountId, ZanEnum type) {
		Zan zan = zanDao.getZan(ids, accountId, type.getValue());
		if(zan != null) {
			updateRelationZanNum(type, -1, ids);
			return zanDao.delete(zan);
		}
		zan = new Zan();
		zan.setAccountId(accountId);
		zan.setCreateTime(new Date());
		zan.setIds(ids);
		zan.setType(type.getValue());
		updateRelationZanNum(type, 1, ids);
		return zanDao.save(zan);
	}
	
	public boolean isZan(int ids, Integer accountId, ZanEnum type) {
		return zanDao.getZan(ids, accountId, type.getValue()) != null;
	}
	
	public void updateRelationZanNum(ZanEnum type, int zanNum, int ids) {
		switch (type) {
		case GOODS_STAR_TYPE:
			goodsStarDao.clearCache();
			goodsStarDao.updateZanNum(zanNum, ids);
			break;
		default:
			break;
		}
	}

}


