package com.cn.jm._dao.power;

import java.util.List;

import com.cn._gen.dao.PowerDao;
import com.cn._gen.model.Power;

/**
 * 功能权限
 * @author 李劲
 *
 * 2017年9月25日 上午12:16:12
 */
public class JMPowerDao extends PowerDao {
	/**
	 * 根据菜单id获取菜单下的所有按钮
	 * @param id
	 * @return
	 */
	public List<Power> selectByMenuId(Integer menuId) {
		return Power.dao.find("SELECT * FROM system_power WHERE menuId = " + menuId);
	}

	public List<Power> listRolePowerByMenuId(Integer menuId, Integer roleId) {
		return Power.dao.find("SELECT sp.*,srp.id choice FROM system_power sp LEFT JOIN system_role_power srp ON srp.powerId = sp.id AND srp.roleId = " + roleId + " WHERE sp.menuId = " + menuId + " GROUP BY sp.id");
	}

	public Power selectByUri(Integer roleId, String requestUri) {
		return Power.dao.findFirst("SELECT sp.id,srp.id srpId FROM system_power sp"
				+ " LEFT JOIN system_role_power srp"
				+ " ON sp.id = srp.powerId AND srp.roleId = " + roleId + " WHERE sp.url LIKE '%"+requestUri+"%'");
	}
}
