

package com.cn._gen.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.cn._gen.model.AccountSession;
import com.cn.jm._dao._base.common.JMTableRelate;
import com.cn.jm.core.db.JMMainTable;
import com.cn.jm.core.db.JMToolSql;
import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.cache.JMCacheKit;
import com.cn.jm.core.web.dao.JMBaseDao;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * Generated by 小跑科技robot  .
 */
public class AccountSessionDao extends JMBaseDao {
	
	public static final AccountSession dao = new AccountSession().dao();
	public static final String cacheName = "accountsession";
	public static final String tableName = "tb_account_session";
	
	public List<AccountSession> list(boolean useCache) {
		return list("",null,null,null,"id",JMBaseDao.DESC,useCache);
	} 
	
	public List<AccountSession> list(String columns,HashMap<String, Object> andpm,String orderField,String orderDirection,boolean useCache) {
		return list(columns, andpm, null, null, "", orderField, orderDirection, useCache);
	}
	
	public List<AccountSession> list(String columns,HashMap<String, Object> andpm,HashMap<String, Object> orpm,String orderField,String orderDirection,boolean useCache) {
		return list(columns, andpm, orpm, null, "", orderField, orderDirection, useCache);
	}
	
	public List<AccountSession> list(String columns,HashMap<String, Object> andpm,HashMap<String, Object> orpm,HashMap<String, Object> likepm,String orderField,String orderDirection,boolean useCache) {
		return list(columns, andpm, orpm, likepm, "", orderField, orderDirection, useCache);
	}
	
	public List<AccountSession> list(String columns,HashMap<String, Object> andpm,HashMap<String, Object> orpm,HashMap<String, Object> likepm,String appendSql,String orderField,String orderDirection,boolean useCache) {
		return list(tableName, dao, cacheName,columns,JMToolSql.whereAND(andpm).toString()+JMToolSql.whereOR(orpm).toString()+JMToolSql.whereLike(likepm).toString()+" "+appendSql, orderField, orderDirection,useCache);
	}
	
	public List<AccountSession> list(String sql,boolean useCache) {
		return list(tableName, dao, cacheName, sql, useCache);
	}
	
	public Page<AccountSession> page(int pageNumber, int pageSize,String columns,HashMap<String, Object> andpm,HashMap<String, Object> orpm,HashMap<String, Object> likepm,String startTime,String endTime,String orderField,String orderDirection, boolean useCache) {
		String appendSql = JMToolSql.format("AND createTime BETWEEN '%s' AND DATE_ADD('%s',INTERVAL 1 DAY)",startTime,endTime);
		return page(pageNumber, pageSize, columns, andpm, orpm, likepm, appendSql, orderField,orderDirection, useCache);
	}
	

	public Page<AccountSession> page(int pageNumber, int pageSize,boolean useCache) {
		return page(pageNumber, pageSize, "", null, null, null, "", "id",DESC, useCache);
	}
	
	public Page<AccountSession> page(int pageNumber, int pageSize,String columns,HashMap<String, Object> andpm,String orderField,String orderDirection,boolean useCache) {
		return page(pageNumber, pageSize, columns, andpm, null, null, orderField, orderDirection, useCache);
	}
	
	public Page<AccountSession> page(int pageNumber, int pageSize,String columns,HashMap<String, Object> andpm,HashMap<String, Object> orpm,String orderField,String orderDirection,boolean useCache) {
		return page(pageNumber, pageSize, columns, andpm, orpm, null, orderField, orderDirection, useCache);
	}

	public Page<AccountSession> page(int pageNumber, int pageSize,String columns,HashMap<String, Object> andpm,HashMap<String, Object> orpm,HashMap<String, Object> likepm,String orderField,String orderDirection, boolean useCache) {
		return page(pageNumber, pageSize, columns, andpm, orpm, likepm, "", orderField,orderDirection, useCache);
	}
	
	public Page<AccountSession> page(int pageNumber, int pageSize,String columns,HashMap<String, Object> andpm,HashMap<String, Object> orpm,HashMap<String, Object> likepm,String appendSql,String orderField, String orderDirection,boolean useCache) {
		String where = JMToolSql.whereAND(andpm).toString()+JMToolSql.whereOR(orpm).toString()+JMToolSql.whereLike(likepm).toString()+" "+appendSql;
		return page(pageNumber, pageSize,columns, where, orderField, orderDirection, useCache);
	}
	
	public Page<AccountSession> page(int pageNumber, int pageSize,String columns,String where ,String orderField, String orderDirection,boolean useCache) {
		return page(tableName, dao, cacheName, pageNumber, pageSize, columns, where, orderField, orderDirection, useCache);
	}
	
	public Page<AccountSession> page(int pageNumber, int pageSize,String columns,String from,boolean useCache) {
		return page(tableName, dao, cacheName, pageNumber, pageSize, columns, from, useCache);
	}

	public AccountSession getById(Integer id) {
		return getById(id, true);
	}
	
	public AccountSession getById(Integer id,boolean useCache) {
		return getById("", id, useCache);
	}
	
	@SuppressWarnings("unchecked")
	public AccountSession getById(String columns,Integer id,boolean useCache) {
		StringBuffer format = new StringBuffer("SELECT ");
		if (JMToolString.isEmpty(columns)) {
			format.append(" * ");
		}else {
			format.append(columns);
		}
		format = format.append(" FROM %s WHERE id = %s ORDER BY id DESC  LIMIT 1");
		String sql = String.format(format.toString(),tableName,id);
		return get(sql, useCache);
	}
	
	public AccountSession get(HashMap<String, Object> param,boolean useCache) {
		return get("", param, useCache);
	}
	
	public AccountSession get(String columns,HashMap<String, Object> param,boolean useCache) {
		StringBuffer format = new StringBuffer("SELECT ");
		if (JMToolString.isEmpty(columns)) {
			format.append(" * ");
		}else {
			format.append(columns);
		}
		format = format.append(" FROM %s WHERE 1=1 %s ORDER BY id DESC  LIMIT 1");
		String sql = String.format(format.toString(),tableName,JMToolSql.whereAND(param));
		return get(sql, useCache);
	}
	
	
	public AccountSession get(String sql,boolean useCache) {
		if (useCache) {
			return JMToolSql.get(cacheName, dao, sql);
		}else {
			return dao.findFirst(sql);
		}
	}
	
	public boolean save(AccountSession accountsession) {
		return Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result =  accountsession.save();
				if (result) {
					clearCache();
					result = isave(accountsession, result);
				}
				return result;
			}
		});
	}
	
	public boolean saves(List<AccountSession> accountsessionList) {
		int[] results = Db.use("base").batchSave(accountsessionList, 2000);
		clearCache();
		return isaves(accountsessionList, results);
	}
	
	public boolean update(AccountSession accountsession) {
		return Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result =  accountsession.update();
				if (result) {
					clearCache();
					result = iupdate(accountsession, result);
				}
				return result;
			}
		});
	}

	public boolean updates(List<AccountSession> accountsessionList) {
		int[] results = Db.use("base").batchUpdate(accountsessionList, 2000);
		clearCache();
		return iupdates(accountsessionList, results);
	}


	public boolean delete(AccountSession accountsession) {
		return deleteById(accountsession.getId());
	}

	public boolean deleteById(int idValue) {
		return Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result =  dao.deleteById(idValue);
				if (result) {
					clearCache();
					result =  idelete(idValue, result);
				}
				return result;
			}
		});
	}
	
	public boolean delete(List<AccountSession> accountsessionList) {
		if(accountsessionList.isEmpty()){
			return false;
		}
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < accountsessionList.size(); i++) {
			ids.add(accountsessionList.get(i).getId());
		}
		return deleteByIds(ids);
	}

	public boolean deleteByIds(Integer[] ids) {
		if(ids.length == 0){
			return false;
		}
		List<Integer> mids = Arrays.asList(ids);
		return deleteByIds(mids);
	}

	@Before(Tx.class)
	public boolean deleteByIds(List<Integer> ids) {
		if(ids.isEmpty()){
			return false;
		}
		StringBuilder sql = new StringBuilder(String.format("DELETE FROM %s WHERE id IN",tableName));
		JMToolSql.joinIds(ids, sql);
		boolean result = Db.use("base").update(sql.toString()) > 0;
		if (result) {
			clearCache();
			result =  ideletes(ids, result);
		}
		return result;
	}

	public long count(HashMap<String, Object> param) {
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE 1=1 %s",tableName,JMToolSql.whereAND(param));
		return Db.queryLong(sql);
	}
	
	@Override
	public boolean idelete(int id, boolean result) {
		if (result) {
			JMMainTable mainTable = JMTableRelate.tableMap.get(tableName);
			JMToolSql.onDelete(id, result, mainTable, "base");
			clearCache();
		}
		result = super.idelete(id, result);
		return result;
	}
	
	@Override
	public boolean ideletes(List<Integer> ids, boolean result) {
		if (result) {
			for (int i = 0; i < ids.size(); i++) {
				result = idelete(ids.get(i), result);
				if (!result) {
					break;
				}
			}
		}
		result = super.ideletes(ids, result);
		return result;
	}
	
	public List<AccountSession> list(HashMap<String, Object> param,String orderDirection) {
		return list(param,"id",orderDirection);
	}

	public List<AccountSession> list(HashMap<String, Object> param,String orderField,String orderDirection) {
		return dao.find(String.format("select * from tb_configure where 1=1 %s order by %s %s", JMToolSql.whereAND(param), orderField, orderDirection));
	}
	
	public void clearCache() {
		JMCacheKit.removeAll(cacheName);
	}

}