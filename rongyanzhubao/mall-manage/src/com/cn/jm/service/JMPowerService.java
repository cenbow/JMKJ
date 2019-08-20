package com.cn.jm.service;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.cn._gen.model.Power;
import com.cn.jm._dao.power.JMPowerDao;
import com.cn.jm.util.SqlUtil;
import com.jfinal.aop.Aop;
import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.Page;

public class JMPowerService extends BasicsService<Power>{

	JMPowerDao powerDao = Aop.get(JMPowerDao.class);
	
	public void editPower(Power power) {
		update(power);
	}
	public void delPower(Power power) {
		delete(power);
	}
	public void savePower(Power power) {
		power.setCreateTime(new Date());
		save(power);
	}
	public Page<Power> pageByAndByOrByLIKE(Integer pageNumber, Integer pageSize, HashMap<String, Object> andpm,
			HashMap<String, Object> orpm, HashMap<String, Object> likepm, String desc) {
		return powerDao.page(pageNumber, pageSize,"*", andpm, orpm, likepm,desc, desc, false);
	}

	/**
	 * 根据请求路径和角色id判断是否有改权限
	 * @param roleId
	 * @param request
	 * @return
	 */
	public boolean needJurisdiction(Integer roleId,HttpServletRequest request) {
		String requestUri = request.getRequestURI().replace(request.getContextPath(),"");
		Power power = powerDao.selectByUri(roleId,requestUri);
		return power == null || power.get("srpId") == null;
	}

	public Page<Power> page(Integer pageNumber, Integer pageSize,Integer id,String keyword,String startTime,String endTime){
		StringBuilder selectSql = new StringBuilder("SELECT id ");
		StringBuilder fromSql = new StringBuilder(" FROM XXX ");
		fromSql.append(" :WHERE ");
		SqlUtil.addWhere(fromSql," AND id = ",id);
		SqlUtil.addLike(fromSql," AND name","%",keyword,"%");
		SqlUtil.addBetweenTime(fromSql, startTime, endTime, " AND createTime");
		String strFromSql = fromSql.toString();
		String strSelectSql = selectSql.toString();
		return model.paginateByCache(tableName,HashKit.md5(selectSql.append(strFromSql).toString()), pageNumber, pageSize,false,strSelectSql, strFromSql);
	}
}
