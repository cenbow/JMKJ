package com.cn.jm._dao.role;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cn._gen.dao.RoleAccountDao;
import com.cn._gen.model.Account;
import com.cn._gen.model.Role;
import com.cn._gen.model.RoleAccount;
import com.cn.jm._dao.account.JMAccountDao;
import com.cn.jm.core.db.JMToolSql;

/**
 * 
 * @author 李劲
 *
 * 2017年9月25日 上午12:17:29
 */
public class JMRoleAccountDao extends RoleAccountDao {
	
	public static final JMRoleAccountDao jmd = new JMRoleAccountDao();
	
	public RoleAccount getByAccountId(int accountId){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("accountId", accountId);
		return JMRoleAccountDao.jmd.get(param,true);
	}
	
	public List<RoleAccount> getByRoleId(Long roleId){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("roleId", roleId);
		return JMRoleAccountDao.jmd.list("", param, "id", DESC, true);
	}
	
	public RoleAccount getByRoleId(Long accountId,Long roleId){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("roleId", roleId);
		param.put("accountId", accountId);
		return get(param,true);
	}
	
	public List<Account> getAccountList(int roleId){
		List<Account> accountList = new ArrayList<Account>();
		List<Role> roleList = JMRoleDao.jmd.next(roleId);
		for (int i = 0; i < roleList.size(); i++) {
			Role role = roleList.get(i);
			if (role != null) {
				String sql = "SELECT a.* FROM tb_account AS a LEFT JOIN system_role_account AS b ON a.id = b.accountId WHERE b.roleId = %s  ORDER BY a.id DESC";
				sql = JMToolSql.format(sql,role.getId());
				List<Account> mAccountList =  JMToolSql.list(cacheName, JMAccountDao.dao, sql);
				
				if (mAccountList != null) {
					accountList.addAll(mAccountList);
				}
			}
		}
		return accountList;
	}
	
	
	public Role getRole(int accountId,int roleId){
		String sql = "SELECT a.* FROM system_role AS a LEFT JOIN system_role_account AS b ON a.id = b.roleId WHERE b.accountId = %s AND b.roleId = %s ORDER BY a.id DESC";
		sql = JMToolSql.format(sql,accountId,roleId);
		return JMToolSql.get(cacheName, JMRoleDao.dao, sql);
	}
	
	public Role getRoleByAccountId(int accountId){
		String sql = "SELECT a.* FROM system_role AS a LEFT JOIN system_role_account AS b ON a.id = b.roleId WHERE b.accountId = %s  ORDER BY a.id DESC";
		sql = JMToolSql.format(sql,accountId);
		return JMToolSql.get(cacheName, JMRoleDao.dao, sql);
	}

	/**
	 * 新增一个角色用户对应的关联信息
	 * @param id
	 * @param date
	 * @return 
	 */
	public boolean addRoleAccount(Integer accountId,Integer roleId, Date date) {
		RoleAccount ra = new RoleAccount();
		ra.setRoleId(roleId);
		ra.setAccountId(accountId);
		ra.setCreateTime(new Date());
		return save(ra);
	}

}
