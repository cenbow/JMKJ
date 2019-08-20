package com.cn.jm._dao.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cn._gen.dao.RoleDao;
import com.cn._gen.model.Role;
import com.cn.jm._dao.account.JMAccountDao;
import com.jfinal.aop.Inject;

/**
 * 
 * @author 李劲
 *
 * 2017年9月25日 上午12:16:45
 */
public class JMRoleDao extends RoleDao {
	
	public static final JMRoleDao jmd = new JMRoleDao();
	
	@Inject
	JMAccountDao accountDao;// = Aop.get(JMAccountDao.class);
	
	/**
	 * 第一层角色
	 * @return
	 */
	public List<Role> one(){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("series", 0);
		List<Role> roleList = list("", param, "id", DESC, true);
		return roleList;
	} 
	
	/**
	 * 获取所有角色
	 * @return
	 */
	public List<Role> all(){
		List<Role> RoleList = one();
		next(RoleList);
		return RoleList;
	}
	
	public List<Role> all(Integer parent){
		HashMap<String, Object> param = new HashMap<String, Object>();
		if(parent != null){
			param.put("parent", parent);
		}
		List<Role> RoleList = list("", param, "id", DESC, true);
		next(RoleList);
		return RoleList;
	}
	
	private void next(List<Role> roleList){
		for(Role role : roleList){
			List<Role> nextList = next(role.getId());
			role.put("nextList", nextList);
			if(!nextList.isEmpty()){
				next(nextList);
			}
		}
	}
	
	/**
	 * 删除角色，含所有子角色
	 * @param id
	 */
	public void delete(int id){
		List<Role> list = new ArrayList<Role>();
		list.add(getById(id));
		
		nextByDel(list, id);
		delete(list);
	}
	
	
	private void nextByDel(List<Role> list,Integer parent){
		List<Role> mList = next(parent);
		if(mList.size() > 0){
			list.addAll(mList);
			for (int i = 0; i < mList.size(); i++) {
				Role role = mList.get(i);
				nextByDel(list, role.getId());
			}
			
		}
	}
	
	/**
	 * 下级角色列表
	 * @param roleId
	 * @return
	 */
	public List<Role> next(int roleId){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parent", roleId);
		Integer series = jmd.getById(roleId).getSeries();
		if(series != null)
			param.put("series", series+1);
		return list("", param, "id", DESC, true);
	}

	public Role selectByAccountId(Integer accountId) {
		return Role.dao.findFirst("SELECT sr.* FROM system_role sr INNER JOIN system_role_account sra ON sr.id = sra.roleId WHERE sra.accountId = " + accountId);
	}
	
}
