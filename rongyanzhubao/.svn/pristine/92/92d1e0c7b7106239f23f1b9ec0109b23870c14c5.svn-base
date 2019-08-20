package com.cn.jm._dao.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cn._gen.dao.AccountDao;
import com.cn._gen.model.Account;
import com.cn.jm.core.db.JMToolSql;
import com.cn.jm.core.web.dao.JMBaseDao;
import com.cn.jm.information.ConstantThirdParty;
import com.cn.jm.util.SqlUtil;
import com.cn.jm.util.sqltool.JMCommonDao;
import com.cn.jm.util.sqltool.Query;
import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * 账户业务
 * 
 * @author 李劲
 *
 *         2017年9月17日 下午4:56:40
 */
public class JMAccountDao extends AccountDao {
	
	private JMCommonDao commonDao = JMCommonDao.jmd;
	
	/**
	 * 根据手机号码获取账号
	 * 
	 * @param mobile
	 * @return
	 */
	public Account getByMobile(String mobile, Integer areaId) {
		String sql = "SELECT tb_account.* FROM tb_account "
				+ "INNER JOIN  tb_account_user ON tb_account.id = tb_account_user.accountId "
				+ " where tb_account.mobile = ? AND tb_account_user.areaId = ? LIMIT 1";
		return Account.dao.findFirst(sql, mobile, areaId);
	}

	public Account getByMobile(String mobile) {
		String sql = "SELECT tb_account.* FROM tb_account "
				+ "INNER JOIN  tb_account_user ON tb_account.id = tb_account_user.accountId "
				+ " where tb_account.mobile = ? LIMIT 1";
		return Account.dao.findFirst(sql, mobile);
	}
	
	public Account getByIdentificationID(String IdentificationID){
		HashMap<String, Object> param = new HashMap<>();
		param.put("IdentificationID", IdentificationID);
		return get(param,false);
	}
	

	/**
	 * 根据账号获取
	 * 
	 * @param account
	 * @return
	 */
	public Account getByAccount(String account) {
		String sql = "SELECT * FROM tb_account where account = ? LIMIT 1";
		return Account.dao.findFirst(sql, account);
	}


	public Account get(String mobile, String password) {
		String sql = "SELECT * FROM tb_account where mobile = ? AND password = ? LIMIT 1";
		return Account.dao.findFirst(sql, mobile, password);
	}

	public Account getAccount(String sessionId){
		String sql = "SELECT a.* FROM tb_account AS a LEFT JOIN tb_account_session AS b ON a.id = b.accountId WHERE b.sessionId = ? limit 1";
		return Account.dao.findFirst(sql, sessionId);
	}
	
	/**
	 * 管理员列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Account> pageByAdmin(int pageNumber, int pageSize) {
		HashMap<String, Object> andpm = new HashMap<String, Object>();
		andpm.put("type", 0);
		return pageByAndByOrByLIKE(pageNumber, pageSize, andpm, null, null, JMBaseDao.DESC);
	}

	/**
	 * 普通用户列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Account> pageByAccount(int pageNumber, int pageSize) {
		HashMap<String, Object> andpm = new HashMap<String, Object>();
		andpm.put("type", 1);
		return pageByAndByOrByLIKE(pageNumber, pageSize, andpm, null, null, JMBaseDao.DESC);
	}

	/**
	 * 商家列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Account> pageByMerchant(int pageNumber, int pageSize) {
		HashMap<String, Object> andpm = new HashMap<String, Object>();
		andpm.put("type", 2);
		return pageByAndByOrByLIKE(pageNumber, pageSize, andpm, null, null, JMBaseDao.DESC);
	}

	public Page<Account> pageByAndByOrByLIKE(int pageNumber, int pageSize,HashMap<String, Object> andpm,HashMap<String, Object> orpm,HashMap<String, Object> likepm,String orderDirection) {
		return page(pageNumber, pageSize,JMToolSql.whereAND(andpm).toString()+JMToolSql.whereOR(orpm).toString()+JMToolSql.whereLike(likepm).toString(), "id", orderDirection);
	}

	public Page<Account> page(int pageNumber, int pageSize,String where ,String orderField, String orderDirection) {
		return dao.paginate(pageNumber, pageSize, "select * ", String.format("from tb_account where 1=1 %s order by %s %s", where, orderField, orderDirection));
	}
	
	static List<String> urlList = new ArrayList<>();
	static {
		urlList.add(0,ConstantThirdParty.LOGIN_HTML);
		urlList.add(1,ConstantThirdParty.SHOP_HTML);
	}
	
	/**
	 * 判断手机号是否重复,并且校验长度是否为11位
	 * @param mobile
	 * @return 
	 */
	public boolean isRepeatMobile(String mobile) {
		if(mobile.length() != 11 || getByMobile(mobile.trim()) != null) {
			return true;
		}
		return false;
	}

	public int deleteATByAccountIdAndType(Integer accountId,Integer type, Integer ids) {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM tb_account_temporary WHERE ");
		sql.append(" accountId = ").append(accountId);
		sql.append(" AND type = ").append(type);
		SqlUtil.addWhere(sql," AND ids = ",ids);
		return Db.update(sql.toString());
	}

	public Account getByParentIdAndId(int id, Integer parentId) {
		return Account.dao.findFirst("SELECT * FROM tb_account WHERE id = ? AND parentId = ?",id,parentId);
	}

	/**
	 * 根据sessionid获取用户信息
	 * @param sessionId
	 * @return
	 */
	public Account getBySessionId(String sessionId) {
		return Account.dao.findFirst("SELECT account.* FROM tb_account_session accountSession INNER JOIN tb_account account ON account.id = accountSession.accountId WHERE accountSession.sessionId = ?",sessionId);
	}

	public Account getByKeyAndValue(String key, String value) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM tb_account WHERE ");
		sql.append(key).append(" = ").append(value);
		return Account.dao.findFirst(sql.toString());
	}

	public Page<Account> page(Integer pageNumber, Integer pageSize,Integer id,String keyword,String startTime,String endTime,boolean isCache){
		StringBuilder selectSql = new StringBuilder("SELECT id ");
		StringBuilder fromSql = new StringBuilder(" FROM ");
		fromSql.append(tableName);
		fromSql.append(" :WHERE ");
		SqlUtil.addWhere(fromSql," AND id = ",id);
		SqlUtil.addLike(fromSql," AND name","%",keyword,"%");
		SqlUtil.addBetweenTime(fromSql, startTime, endTime, " AND createTime");
		fromSql.append(" ORDER BY id DESC");
		SqlUtil.changeWhere(fromSql);
		String strFromSql = fromSql.toString();
		String strSelectSql = selectSql.toString();
		return isCache?dao.paginateByCache(cacheName,HashKit.md5(selectSql.append(strFromSql).toString()), pageNumber, pageSize,false,strSelectSql, strFromSql)
			:dao.paginate(pageNumber, pageSize, false, strSelectSql,strFromSql);
	}
	
	public Account selectAccount(String mobile,Integer type) {
		Query query = new Query();
		query.put("and a.mobile = ", mobile,false);
		query.put("and a.type=", type, false);
		String select = "select a.id,b.nick,b.head";
		String inner = "a inner join tb_account_user b on a.id = b.accountId";
		return commonDao.selectOne(Account.class, query, select, inner);
	}

	public List<Account> selectShop() {
		return Account.dao.find("SELECT ta.id,tau.nick FROM tb_account ta INNER JOIN tb_account_user tau ON tau.accountId = ta.id WHERE ta.type = " + AccountEnum.TYPE_SHOP.getValue());
	}
	
}
