package com.cn.jm.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import com.cn._gen.model.Account;
import com.cn._gen.model.AccountSession;
import com.cn._gen.model.AccountUser;
import com.cn._gen.model.RoleAccount;
import com.cn._gen.model.Room;
import com.cn._gen.model.RoomManagement;
import com.cn._gen.model.RoomMerchant;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm._dao.account.JMAccountDao;
import com.cn.jm._dao.account.JMAccountSessionDao;
import com.cn.jm._dao.account.JMAccountUserDao;
import com.cn.jm._dao.goods.JMGoodsDao;
import com.cn.jm._dao.role.JMRoleAccountDao;
import com.cn.jm._dao.room.JMRoomDao;
import com.cn.jm._dao.room.JMRoomMerchantDao;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.cache.JMCacheKit;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.BasicsInformation;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.information.RedisInformation;
import com.cn.jm.information.ZbMessage;
import com.cn.jm.method.code.CodeMethod;
import com.cn.jm.method.login.LoginMethod;
import com.cn.jm.method.login.LoginParam;
import com.cn.jm.util.BasicsMethodUtil;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.MailUtil;
import com.cn.jm.util.MobileCodeUtil;
import com.cn.jm.util.RedisUtil;
import com.cn.jm.util.ZbUserSig;
import com.cn.jm.util.rongyun.RyService;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public class JMAccountService extends BasicsService<Account>{
	JMAccountDao accountDao = Aop.get(JMAccountDao.class);
	JMAccountUserDao accountUserDao = Aop.get(JMAccountUserDao.class);
	JMRoleAccountDao roleAccountDao = Aop.get(JMRoleAccountDao.class);
	JMAccountSessionDao accountSessionDao = Aop.get(JMAccountSessionDao.class);
	JMRoomDao roomDao = Aop.get(JMRoomDao.class);
	JMRoomMerchantDao merchantDao = Aop.get(JMRoomMerchantDao.class);
	
	JMAccountUserService accountUserService = Aop.get(JMAccountUserService.class);
	JMRoleAccountService roleAccountService = Aop.get(JMRoleAccountService.class);
	JMAccountSessionService accountSessionService = Aop.get(JMAccountSessionService.class);
	JMNoService noService = Aop.get(JMNoService.class);
	JMGoodsDao goodsDao = Aop.get(JMGoodsDao.class);
	
	/**
	 * 登陆
	 * @param mobile
	 * @param password
	 * @param ip
	 * @param userAgent
	 * @param ids 
	 * @param type 
	 * @return
	 */
	public JMResult login(String account, String password, String ip,String userAgent, String ids, String code, String loginType, int registerType) {
		//根据type获取登录方法
		LoginMethod loginMethod = BasicsMethodUtil.createMethods(BasicsMethodUtil.LOGIN_METHOD_CLASS_PATH, loginType, LoginMethod.class);
		
		if(loginMethod == null) {
			return JMResultUtil.fail(PromptInformationEnum.PARAMETER_ERROR);
		}
		//执行登录方法
		JMResult result = loginMethod.login(new LoginParam(ids, account, password, code, registerType));
		if(result == null) {
			return JMResultUtil.fail(PromptInformationEnum.FAIL);
		}
		if(result.getCode() != JMResult.SUCCESS) {
			return result;
		}
		return login((Account)result.getData(), ip,userAgent);
	}

	/**
	 * 登录
	 * 
	 * @param account
	 * @param keepLogin
	 * @param loginIp
	 * @return
	 */
	@Before(Tx.class)
	private JMResult login(Account account, String loginIp,String accountAgent) {
		if (account == null) {
			return JMResult.create().fail(PromptInformationEnum.EMAIL_OR_PASSWORD_ERROR.getDesc());
		}
		if (isFrozen(account)) {
			return JMResult.create().fail(PromptInformationEnum.ACCOUNTS_HAVE_BEEN_FROZEN.getDesc());
		}
		
		String sessionId = StrKit.getRandomUUID();
		String salt = account.getSalt();
		String sessionSalt = HashKit.sha256(salt + sessionId+loginIp+accountAgent);
			
		// 保存登录 session 到数据库
		if (!accountSessionService.check(account.getId(), sessionId,sessionSalt)) {
			return JMResult.create().fail(PromptInformationEnum.CREATE_SESSION_ID_FAIL.getDesc());
		}
		account.put("sessionId", sessionId);
		
		JMCacheKit.put(JMAccountDao.cacheName, sessionId, account);
		JMCacheKit.put(JMAccountDao.cacheName, account.getId(), account);

		HashMap<String, Object> msg = new HashMap<String, Object>();
		msg.put("ip", loginIp);
		msg.put("account", account);
		AccountUser accountUser = accountUserService.selectOneByKeyAndValue("accountId =", account.getId());
		account.put("nick", accountUser.getNick());
		account.put("head", accountUser.getHead());
		
		//如果是主播,判断有没有绑定商家
		if(account.getType()==AccountEnum.TYPE_ANCHOR.getValue()) {
			RoomMerchant merchant = merchantDao.selectRoomMerchant(account.getId());
			if(merchant!=null) {
				account.put("isBindMerchant",1);
				AccountUser merchantAccount = accountUserService.selectByAccountId(merchant.getMerchantAccountId());
				account.put("merchantNick",merchantAccount.getNick());
			}else {
				account.put("isBindMerchant",0);
			}
		}
		removeSensitiveInfo(account);
		
		
		
		return JMResultUtil.success(account); 
	}
	

	/**
	 * 根据邮箱更新密码
	 * @param mail
	 * @param password
	 * @return
	 */
	public JMResult updatePwdByMail(String mail, String password) {
		if (StrKit.isBlank(password)) {
			return JMResultUtil.fail(PromptInformationEnum.PASSWORD_NOT_NULL);
		}
		Account account = selectOneByKeyAndValue("mail =",mail);
		if (account == null) {
			return JMResultUtil.fail(PromptInformationEnum.USER_NOT_EXISTS);
		}
		return updatePassword(account, password) ? JMResultUtil.success(PromptInformationEnum.SUCCESS) :JMResultUtil.fail(PromptInformationEnum.FAIL);
	}
	
	/**
	 * 根据手机号更新密码
	 * @param mail
	 * @param password
	 * @return
	 */
	public JMResult updatePwdByMobile(String mobile, String password, Integer registerType) {
		if (StrKit.isBlank(password)) {
			return JMResultUtil.fail(PromptInformationEnum.PASSWORD_NOT_NULL);
		}
		Account account = selectAccount(" mobile =", mobile, registerType);
		if (account == null) {
			return JMResultUtil.fail(PromptInformationEnum.USER_NOT_EXISTS);
		}
		return updatePassword(account, password) ? JMResultUtil.success(PromptInformationEnum.SUCCESS) :JMResultUtil.fail(PromptInformationEnum.FAIL);
	}

	public boolean updatePassword(Account account,String password) {
		String salt = HashKit.generateSaltForSha256();
		account.setSalt(salt);
		account.setPassword(get256Pwd(salt, password));
		return update(account);
	}
	
	
	public boolean update(AccountUser accountUser, Account account, String password) {
		return Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				// 密码加密
				if (null != password && !password.trim().equals("")) {
					String salt = getSalt();
					account.setSalt(salt);
					account.setPassword(get256Pwd(salt, password));
				}
				// 更新用户
				if (!account.getMobile().equals(accountUser.getMobile())) {
					accountUser.setMobile(account.getMobile());
					account.setAccount(account.getMobile());
				}
				try {
					update(account);
					accountUserDao.update(accountUser);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}

	/**
	 * 校验sessionId
	 * @param sessionId
	 * @param loginIp
	 * @return
	 */
	public  boolean validSessionId(String sessionId, String loginIp,String userAgent) {
		Account account = getBySessionId(sessionId);
		return validSessionId(sessionId, loginIp, account,userAgent);
	}


	/**
	 * 登出
	 * 
	 * @param sessionId
	 */
	public void logout(Controller controller, String sessionId) {
		if (JMToolString.isNotEmpty(sessionId)) {
			JMCacheKit.remove("Account", sessionId);
			accountSessionDao.getBySessionId(sessionId).delete();
		}
	}

	/**
	 * 登出
	 * 
	 * @param sessionId
	 */
	public void logout(String sessionId) {
		if (sessionId != null) {
			if (JMCacheKit.get("Account", sessionId) != null)
				JMCacheKit.remove("Account", sessionId.trim());
			AccountSession AccountSession = accountSessionService.getBySessionId(sessionId);
			if (AccountSession != null) {
				AccountSession.delete();
			}
		}
	}

	public void fz(Integer AccountId, int state) {
		Account account = selectById(AccountId);
		if (account != null) {
			account.setState(state);
			update(account);
		}
	}

	/**
	 * 获取account
	 * 
	 * @param sessionId
	 * @return
	 */
	public Account getBySessionId(String sessionId) {
		AccountSession session = accountSessionDao.getBySessionId(sessionId);
		if (session == null) {
			return null;
		}

//		if (accountSessionService.isExpired(session)) { // session 已过期
//			session.delete(); // 被动式删除过期数据，此外还需要定时线程来主动清除过期数据
//			return null;
//		}
		
		Account account = accountDao.getById(session.getAccountId());
		removeSensitiveInfo(account);
		account.put("sessionId", sessionId);
		return account;
	}
	/**
	 * 验证密码是否正确
	 * 
	 * @param AccountName
	 * @param passWord
	 * @return
	 */
	public boolean valiPassWord(Account account, String password) {
		if (account == null) {
			return false;
		}
		String pwd = account.getPassword();
		String mPwd = getMd5Pwd(account.getSalt(), password);
		return pwd != null && pwd.trim().equals(mPwd.trim());
	}
	/**
	 * 密码变更
	 * 
	 * @param AccountName
	 * @param passOld
	 * @param passNew
	 */
	public JMResult passChange(String mobile, String passOld, String passNew) {
		HashMap <String, Object> param = new HashMap<String, Object>();
		param.put(" AND mobile =", mobile);
		param.put(" AND password =", passOld);
		Account account = selectOneByMap(param);
		if (account == null) {
			return JMResult.create().code(JMResult.FAIL).desc("用户不存在");
		} else {
			String mdPassword = HashKit.md5(account.getSalt() + passNew);
			account.setPassword(mdPassword);
			if (update(account)) {
				return JMResult.create().code(JMResult.SUCCESS).desc("更新成功");
			} else {
				return JMResult.create().code(JMResult.FAIL).desc("更新密码失败");
			}
		}
	}

	/**
	 * 后台登录
	 */
	public JMResult sysLogin(String account, String password,Controller controller) {
		account = account.toLowerCase().trim();
		password = password.trim();
		Account ac = getByAccount(account);
		if (ac == null || ac.getType() > 1) {
			return JMResultUtil.fail(PromptInformationEnum.USER_NOT_EXISTS);
		}
		if (!valiPassWord(ac, password)) {
			return JMResultUtil.fail(PromptInformationEnum.ACCOUNT_OR_PASSWORD_ERROR);
		}
		if (ac.getState() == BasicsInformation.STATUS_FROZEN) {
			return JMResultUtil.fail(PromptInformationEnum.ACCOUNTS_HAVE_BEEN_FROZEN);
		}
		removeToKen(ac, controller);
		return JMResultUtil.success(ac);
	}
	
	/**
	 * 商家后台登录
	 */
	public JMResult sysShopLogin(String mobile, String password, Controller controller, int registerType) {
		password = password.trim();
		Account account = getByMobile(mobile, registerType);
		if (account == null || !AccountEnum.TYPE_SHOP.equals(account.getType())) {
			return JMResultUtil.fail(PromptInformationEnum.USER_NOT_EXISTS);
		}
		if (!checkAccountPassword(account, password)) {
			return JMResultUtil.fail(PromptInformationEnum.ACCOUNT_OR_PASSWORD_ERROR);
		}
		if (account.getState() == BasicsInformation.STATUS_FROZEN) {
			return JMResultUtil.fail(PromptInformationEnum.ACCOUNTS_HAVE_BEEN_FROZEN);
		}
		removeToKen(account, controller);
		return JMResultUtil.success(account);
	}
	
	private void removeToKen(Account account, Controller controller) {
		//删除前面一个的用户
		RedisUtil.loginOut(account.getId());
		//设置新的登录信息
		RedisUtil.setUserAndToKen(account.getId(), controller);
		removeSensitiveInfo(account);
	}
	/**
	 * 批量冻结/解冻
	 * 
	 * @param ids
	 */
	@Before(Tx.class)
	public JMResult fzs(Integer[] ids, int fz) {
		if (ids.length == 0) {
			return JMResult.create().code(JMResult.FAIL).desc("参数不能为空");
		}
		List<Account> accountList = new ArrayList<Account>();
		for (int i = 0; i < ids.length; i++) {
			Account account = selectById(ids[i]);
			if (account != null) {
				account.setState(fz);
				accountList.add(account);
			}
		}
		Db.batchUpdate(accountList, 1000);
		return JMResult.create().code(JMResult.SUCCESS).desc("冻结完成");
	}

	/**
	 * 校验sessionId
	 * @param sessionId
	 * @param loginIp
	 * @param account
	 * @return
	 */
	public  boolean validSessionId(String sessionId, String loginIp,Account account,String userAgent) {
		AccountSession accountSession = accountSessionService.getBySessionId(sessionId);
		String sessionSalt = accountSession.getSessionSalt().toString();
		String salt = account.getSalt();
		String mSessionSalt = HashKit.sha256(salt + sessionId + loginIp + userAgent);
		return JMToolString.isNotEmpty(sessionSalt) && JMToolString.isNotEmpty(mSessionSalt) && sessionSalt.equals(mSessionSalt);
	}

	/**
	 * 
	 * @date 2019年1月23日 下午3:18:28
	 * @author JaysonLee
	 * @Description: 保存管理员
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 * @param account
	 * @param password1
	 * @param roleId
	 * @return
	 *
	 */
	public boolean saveAdmin(Account account ,String password1,int roleId){
		try {
			return Db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {
					String salt = HashKit.generateSaltForSha256();
					String password = HashKit.md5(salt + password1);
					account.setSalt(salt);
					account.setPassword(password);
					if(save(account)){
						return roleAccountDao.addRoleAccount(account.getId(),roleId,new Date());
					}else return false ;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}
		
	}
	/**
	 * 退出登录
	 */
	public JMResult logout(String sessionId, String loginIp,String userAgent) {
		if (sessionId == null) {
			return JMResult.create().fail("sessionId不能为空");
		}
		Account account = accountDao.getBySessionId(sessionId);
		if (account != null && accountSessionService.deleteBySessionId(sessionId)) {
			
			JMCacheKit.remove(JMAccountDao.cacheName, sessionId);;
			JMCacheKit.remove(JMAccountDao.cacheName, account.getId());

			HashMap<String, Object> msg = new HashMap<String, Object>();
			msg.put("ip", loginIp);
			msg.put("account", account);

			return JMResultUtil.success("退出成功!");
		} else
			return JMResultUtil.fail("退出失败");
	}

	public JMResult bdMail(String mail, int code, String ip, String userAgent, String ids, String bdType, int registerType){
		if(isMailExistence(mail, registerType) || isExistence(bdType, ids, registerType)) {
			return JMResultUtil.fail(PromptInformationEnum.USER_EXISTS);
		}
		/**绑定生成的用户信息密码随机*/
		Account account = new Account();
		account.set(bdType, ids);
//		account.setMail(mail);
		if(saveAccount(getSalt(),AccountEnum.TYPE_USER,account)) {
			return login(account,ip,userAgent);
		}
		return JMResultUtil.fail(PromptInformationEnum.FAIL);
	}
	
	public JMResult bdMobile(String mobile, String code, String ip, String userAgent, String ids, String bdType, int registerType){
		Account account = accountDao.selectAccount("mobile =", mobile, registerType);
		boolean accountIsNull = false;
		if(isExistence(bdType, ids, registerType) || (accountIsNull = account != null && account.get(bdType) != null)) {
			return JMResultUtil.fail(PromptInformationEnum.USER_EXISTS);
		}
		if(!accountIsNull) {
			account = new Account();
			account.set(bdType, ids);
			account.setMobile(mobile);
			account.setRegisterType(registerType);
			if(saveAccount(BasicsInformation.DEFAULT_PASSWORD, AccountEnum.TYPE_USER, account)) {
				return login(account,ip,userAgent);
			}
		}else {
			account.set(bdType, ids);
			update(account);
			return login(account,ip,userAgent);
		}
		return JMResultUtil.fail(PromptInformationEnum.FAIL);
	}

	/**
	 * 判断该用户是否为冻结状态,如果是冻结状态则返回true
	 * @param account
	 * @return
	 */
	public boolean isFrozen(Account account) {
		return account!=null && account.getState() == BasicsInformation.STATUS_FROZEN;
	}
	
	public boolean isSystem(Account account) {
		Integer type = account.getType();
		return type == AccountEnum.TYPE_SYSTEM.getValue() || type == AccountEnum.TYPE_SUPER.getValue();
	}

	public Account removeSensitiveInfo(Account Account) {
		Account.remove("password", "salt");
		return Account;
	}

	public Account getByAccount(String account) {
		return selectOneByKeyAndValue("account=",account);
	}

	public Account getByMobile(String mobile, int registerType) {
		return accountDao.selectAccount("mobile=", mobile, registerType);
	}


	/**
	 * 根据字段和值获取用户,如果用户存在则返回true
	 * @param key 传入的key 需加入判断符号 例如 id= , id!= ,如果不存在判断符号则默认拼接上 =号
	 * @param value
	 * @param registerType 
	 * @param mobile 
	 * @return
	 */
	public boolean isExistence(String key,String value, int registerType) {
		return accountDao.selectAccount(key, value, registerType) != null;
	}

	/**
	 * 根据字段和值获取用户,如果用户存在则返回true
	 * @param key 传入的key 自己加入判断符号 例如 id= , id!=
	 * @param value
	 * @return
	 */
	public boolean isMailExistence(String value, int registerType) {
		return isExistence("mail =", value, registerType);
	}

	/**
	 * 根据字段和值获取用户,如果用户存在则返回false
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean isNotExistence(String key, String value, int registerType) {
		return !isExistence(key, value, registerType);
	}


	/**
	 * 第三方公用登录方法
	 * @param field 数据库中第三方的id字段
	 */
	public JMResult login(LoginParam loginParam,String field) {
		Account account = accountDao.selectAccount(field, loginParam.getIds(), loginParam.getRegisterType());
		if(account == null) {
			return JMResultUtil.needBinding(loginParam);
		}
		return JMResultUtil.success(account);
	}
	
	/**
	 * 新增用户
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public JMResult saveAccountByMail(String mail, String password, AccountEnum type, int registerType){
		synchronized (mail) {
			if(isMailExistence(mail, registerType)) {
				return JMResultUtil.success(PromptInformationEnum.REGISTER_ACCOUNT_SUCCESS);
			}
			Account account = new Account();
//			account.setMail(mail);
			if(saveAccount(password, type, account)) {
				return JMResultUtil.success(PromptInformationEnum.REGISTER_ACCOUNT_SUCCESS);
			}
			return JMResultUtil.fail(PromptInformationEnum.REGISTER_ACCOUNT_FAIL);
		}
	}
	
	/**
	 * 新增用户
	 * 
	 * @param account
	 * @param password
	 * @param invitationCode 邀请码
	 * @param registerType 注册类型0用户端1直播端2商家端
	 * @return
	 */
	public JMResult saveAccountByMobile(String mobile, String password,AccountEnum type, String invitationCode, Integer registerType){
		Integer parentId = null;
		if(StrKit.notBlank(invitationCode)) {
			Account parentAccount = selectOneByKeyAndValue("id", "invitationCode =", invitationCode);	
			if(parentAccount == null) 
				return JMResultUtil.fail(PromptInformationEnum.PARENT_ACCOUNT_NOT_EXISTENT);
			parentId = parentAccount.getId();
		}
		if(isExistence("mobile =", mobile, registerType)) {
			return JMResultUtil.success(PromptInformationEnum.REGISTER_ACCOUNT_SUCCESS);
		}
		Account account = new Account();
		account.setMobile(mobile);
		account.setParentId(parentId);
		account.setRegisterType(registerType);
		if(saveAccount(password, type, account)) {
			return JMResultUtil.success(account, PromptInformationEnum.REGISTER_ACCOUNT_SUCCESS);
		}
		return JMResultUtil.fail(PromptInformationEnum.REGISTER_ACCOUNT_FAIL);
	}
	
	private boolean saveAccount(String password,AccountEnum type,Account account) {
		return Db.tx( () -> {
			try {
				Date nowTime = new Date();
				String salt = getSalt();
				account.setSalt(salt);
				account.setPassword(get256Pwd(salt, password));
				account.setCreateTime(nowTime);
				account.setType(type.getValue());
				account.setState(BasicsInformation.STATUS_NORMAL);
				String accountNo = noService.getNo();
				account.setInvitationCode(accountNo);
				if(save(account)) {
					//发送创建关联用户表的信息
					AccountUser accountUser = new AccountUser();
					accountUser.setAccountId(account.getId());
					accountUser.setCreateTime(nowTime);
					accountUser.setMobile(account.getMobile());
					accountUser.setHead(BasicsInformation.DEFAULT_HEAD);
					accountUser.setNick(String.format(BasicsInformation.DEFAULT_NICK, accountNo));
					account.setRyToken(new RyService().getToken(account.getId(), account.getInvitationCode()));
					return accountUserService.save(accountUser) && update(account);
				}
			} catch (Exception e) {
			}
			return false;
		});
	}
	
	/**
	 * 校验用户信息跟传入的密码校验,成功返回true
	 * @param account
	 * @param password
	 * @return
	 */
	public boolean checkAccountPassword(Account account, String password) {
		String hashPassword = get256Pwd(account.getSalt(), password);
		// 通过密码验证
		return StrKit.notBlank(account.getPassword()) && account.getPassword().equals(hashPassword);
	}
	
	/**
	 * 获取密码盐
	 * @return 
	 */
	public String getSalt() {
		return HashKit.generateSaltForSha256();
	}
	/**
	 * 根据密码盐加上明文密码加密生成储存入数据库的密码,用户登录
	 * @param salt
	 * @param password
	 * @return
	 */
	public String get256Pwd(String salt,String password) {
		return HashKit.sha256(salt + password);
	}
	/**
	 * 根据密码盐加上明文密码加密生成储存入数据库的密码,后台管理登录
	 * @param salt
	 * @param password
	 * @return
	 */
	public String getMd5Pwd(String salt,String password) {
		String md5 = HashKit.md5(salt + password);
		return md5;
	}

	/**
	 * 根据邮箱发送验证码
	 * @param mail
	 * @return 
	 */
	public JMResult sendMailCode(String mail,String codeType, int registerType) {
		if (!MailUtil.check(mail)) { //校验发送邮箱的是否正确
			return JMResultUtil.fail(PromptInformationEnum.MAIL_FORMAT_ERROR);
		}
		return sendCode(mail, codeType, BasicsMethodUtil.MAIL_CODE_METHOD_CLASS_PATH, (code) -> {
			try {
				MailUtil.send(mail, code.toString());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}, registerType);
	}

	/**
	 * 根据手机号发送验证码
	 * @param mail
	 * @return 
	 */
	public JMResult sendMobildeCode(String mobile,String codeType, int registerType) {
		if (!MobileCodeUtil.checkMobile(mobile)) { //校验发送手机号的是否正确
			return JMResultUtil.fail(PromptInformationEnum.MOBILE_FORMAT_ERROR);
		}
		return sendCode(mobile, codeType, BasicsMethodUtil.MOBILE_CODE_METHOD_CLASS_PATH,
				(code) -> MobileCodeUtil.sendCode(mobile, code.toString()), registerType);
	}
	
	/**
	 * 根据发送类型获取校验发送方法判断是否发送成功
	 * @param account 发送账号
	 * @param codeType 发送类名
	 * @param methodPath 发送类的路径
	 * @param sendCodeFunction 调用第三方发送的方法,第一个参数是账号,第二个参数是发送的验证码
	 * @return
	 */
	public JMResult sendCode(String account, String codeType, String classPath, Function<String,Boolean> sendCodeFunction, int registerType) {
		//获取是否符合发送验证码方法
		CodeMethod codeMethod = BasicsMethodUtil.createMethods(classPath, codeType, CodeMethod.class);
		if(codeMethod == null) {
			System.err.println("codeType : " + codeType);
			return JMResultUtil.fail(PromptInformationEnum.PARAMETER_ERROR);
		}
		JMResult result = codeMethod.check(account, registerType);
		if(result.getCode() == JMResult.FAIL) {
			return result;
		}
		
		Cache redis = Redis.use();
		Integer code = (int) (Math.random()*9000+1000);
//		Integer code = 1234;
		redis.setex(result.getData() + account, RedisInformation.CODE_TIME, code);
		System.err.println("验证码---"+code);
		try {
			if(sendCodeFunction.apply(code.toString()))
				return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JMResultUtil.fail(PromptInformationEnum.FAIL);
	}
	
	public boolean updateSystemAccount(Account account, int roleId) {
		return Db.tx( () -> {
				boolean result = false;
				String newPassword = account.getPassword();
				if (JMToolString.isNotEmpty(account.getPassword().trim())) {
					account.setPassword(getMd5Pwd(account.getSalt(), newPassword));
					result = update(account);
				}else {
					account.remove("password");
					result = update(account);
				}
				
				RoleAccount ra = roleAccountService.getByAccountId(account.getId());
				if (ra == null) {
					ra = new RoleAccount();
					ra.setRoleId(roleId);
					ra.setAccountId(account.getId());
					ra.setCreateTime(new Date());
					result = roleAccountService.save(ra);
				}else {
					ra.setRoleId(roleId);
					ra.setCreateTime(new Date());
					result = roleAccountDao.update(ra);
				}
				return result;
			}
		);
	}


	/**
	 * 校验手机号码登录
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMobileLoginCode(String mobile, String inputCode, int registerType) {
		return checkCode(RedisInformation.MOBILE_LOGIN_CODE, mobile, inputCode, registerType);
	}
	
	/**
	 * 校验手机号码更换
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMobileReplaceCode(String mobile, String inputCode, int accountId, int registerType) {
		boolean checkFlag = checkCode(RedisInformation.MOBILE_REPLACE_CODE, mobile, inputCode, registerType);
		if(checkFlag) {
			RedisUtil.putCheckOldMobile(accountId);
		}
		return checkFlag;
	}

	/**
	 * 校验手机号码更换
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMobileReplaceCode(String mobile, String inputCode, int registerType) {
		return checkCode(RedisInformation.MOBILE_REPLACE_CODE, mobile, inputCode, registerType);
	}

	/**
	 * 校验手机号码注册
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMobileRegisterCode(String mobile, String inputCode, int registerType) {
		return checkCode(RedisInformation.MOBILE_REGISTER_CODE, mobile, inputCode, registerType);
	}
	/**
	 * 校验手机号码绑定
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMobileBindingCode(String mobile, String inputCode, int registerType) {
		return checkCode(RedisInformation.MOBILE_BINDING_CODE, mobile, inputCode, registerType);
	}
	/**
	 * 校验手机号码找回密码
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMobileForgetCode(String mobile, String inputCode, int registerType) {
		return checkCode(RedisInformation.MOBILE_FORGET_CODE, mobile, inputCode, registerType);
	}

	/**
	 * 校验邮箱登录
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMailLoginCode(String mail, String inputCode, int registerType) {
		return checkCode(RedisInformation.MAIL_LOGIN_CODE, mail, inputCode, registerType);
	}

	/**
	 * 校验邮箱注册
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMailRegisterCode(String mail, String inputCode, int registerType) {
		return checkCode(RedisInformation.MAIL_REGISTER_CODE, mail, inputCode, registerType);
	}
	/**
	 * 校验邮箱绑定
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMailBindingCode(String mail, String inputCode, int registerType) {
		return checkCode(RedisInformation.MAIL_BINDING_CODE, mail, inputCode, registerType);
	}
	/**
	 * 校验邮箱找回密码
	 * 校验用户输入的验证码跟发送的验证码是否一致
	 * @param mobile
	 * @param inputCode
	 * @return
	 */
	public boolean checkMailForgetCode(String mail, String inputCode, int registerType) {
		return checkCode(RedisInformation.MAIL_FORGET_CODE, mail, inputCode, registerType);
	}
	/**
	 * 校验验证码是否一致,如果一致则返回true失败返回false
	 * @param redisKey 储存在redis中的key
	 * @param m 手机号或者邮箱
	 * @param code 用户传入验证码
	 * @return
	 */
	private boolean checkCode(String checkType, String mobile, String inputCode, int registerType) {
		Cache redis = Redis.use();
		String redisKey = checkType + mobile;
		Integer code = redis.get(redisKey);
		//验证码只校验一次,一次之后将键删除
		if(code != null && code.toString().equals(inputCode)) {
			redis.del(redisKey);
			return true;
		}
		return false;
	}
	
	public Page<Account> page(Integer pageNumber, Integer pageSize,Integer id,String keyword,String startTime,String endTime){
		return accountDao.page(pageNumber,pageSize,id,keyword,startTime,endTime,false);
	}

	public Page<Account> page(int pageNumber, int pageSize, String columns, HashMap<String, Object> andpm, HashMap<String, Object> orpm, HashMap<String, Object> likepm, String orderField, String orderDirection, boolean useCache) {
		return accountDao.page(pageNumber, pageSize, columns, andpm, orpm, likepm, orderField,orderDirection, useCache);
	}

	public Page<Account> page(int pageNumber, int pageSize, String columns, HashMap<String, Object> andpm, HashMap<String, Object> orpm, HashMap<String, Object> likepm, String startTime, String endTime, String orderField, String orderDirection, boolean useCache) {
		return accountDao.page(pageNumber, pageSize, columns, andpm, orpm, likepm, startTime,endTime,orderField,orderDirection, useCache);
	}
	
	
	public Account selectAccountByType(String mobile,Integer type){
		HashMap<String,Object> whereMap = new HashMap<>();
		whereMap.put(" AND mobile =", mobile);
		whereMap.put(" AND `type` =", type);
		return selectOneByMap(whereMap);
	}

	public JMResult replaceMobile(Account account, String mobile, String code, int registerType) {
		if(isExistence("mobile =", mobile, registerType)) {
			return JMResultUtil.fail(PromptInformationEnum.MOBILE_COVER_OCCUPY);
		}
		Integer accountId = account.getId();
		if(!RedisUtil.getCheckOldMobile(accountId)) {
			return JMResultUtil.fail(PromptInformationEnum.CHECK_OLD_MOBILE_FAIL);
		}
		if(!checkMobileReplaceCode(mobile, code, registerType)) {
			return JMResultUtil.fail(PromptInformationEnum.CODE_FAIL);
		}
		
		account.setMobile(mobile);
		update(account);
		RedisUtil.removeCheckOldMobile(accountId);
		accountUserDao.updateMobileByAccountId(mobile, accountId);
		return JMResultUtil.success();
	}
	
	
	public JMResult editAccountUserSig(Account account,int expire) {
		String userSig = ZbUserSig.genUserSig(account.getId().toString(), expire);
		account.setUserSig(userSig);
		this.update(account);
		return JMResultUtil.success(userSig);
	}
	
	public Account selectAccount(String mobile,Integer type, int registerType) {
		return accountDao.selectAccount(mobile, type, registerType);
	}

	public List<Account> selectShop() {
		return accountDao.selectShop();
	}
	
	//----------管理后台
	public Page<Account> selectSystemPage(Integer pageNumber,Integer pageSize,
			String mobile,String nick,Integer state,String startTime,String endTime, Integer type){
		Page<Account> page = accountDao.selectSystemPage(pageNumber, pageSize, mobile,
				nick, state, startTime, endTime, type);
		for(Account account:page.getList()) {
			if(account.getType()==AccountEnum.TYPE_SHOP.getValue()) {
				Account bindAccount = accountDao.selectBindByMerchant(account.getId());
				if(bindAccount!=null) {
					account.put("manageMobile",bindAccount.getStr("manageMobile"));
					account.put("anchorMobile",bindAccount.getStr("anchorMobile"));
				}
			}
			if(account.getType()==AccountEnum.TYPE_ANCHOR.getValue()) {
				Account bindAccount = accountDao.selectBindByAnchor(account.getId());
				if(bindAccount!=null) {
					account.put("merchantMobile",bindAccount.getStr("merchantMobile"));
					account.put("manageMobile",bindAccount.getStr("manageMobile"));
				}
			}
			if(account.getType()==AccountEnum.TYPE_MANAGER.getValue()) {
				Account bindAccount = accountDao.selectBindByManage(account.getId());
				if(bindAccount!=null) {
					account.put("merchantMobile",bindAccount.getStr("merchantMobile"));
					account.put("anchorMobile",bindAccount.getStr("anchorMobile"));
				}
			}
		}
		return page;
		
	}

	public Account selectAccountDetail(Integer accountId) {
		return accountDao.selectAccountDetail(accountId);
	}
	
	public boolean editState(Integer accountId) {
		Account account = accountDao.getById(accountId);
		if(account.getState()==0) {
			account.setState(1);
		}else {
			account.setState(0);
		}
		accountDao.clearCache();
		return this.update(account);
	}
	
	@Before(Tx.class)
	public void batchEditState(String ids,Integer state){
		String[] idArray = ids.split(",");
		List<Account> list = new ArrayList<>();
		for(String id:idArray) {
			Account account = new Account();
			account.setId(Integer.parseInt(id));
			account.setState(state);
			list.add(account);
		}
		Db.batchUpdate(list, list.size());
		accountDao.clearCache();
	}
	
	@Before(Tx.class)
	public void insertAccount(Account account,AccountUser accountUser) {
		Date date = new Date();
		account.setCreateTime(date);
		String salt = getSalt();
		account.setSalt(salt);
		account.setPassword(get256Pwd(salt, account.getPassword()));
		account.setCreateTime(date);
		account.setType(AccountEnum.TYPE_USER.getValue());
		account.setState(BasicsInformation.STATUS_NORMAL);
		account.setInvitationCode(noService.getNo());
		account.save();
		account.setRyToken(new RyService().getToken(account.getId(), account.getInvitationCode()));
		account.update();
		accountUser.setCreateTime(date);
		accountUser.setAccountId(account.getId());
		accountUser.setMobile(account.getMobile());
		accountUser.save();
	}
	
	
	@Before(Tx.class)
	public void editAccount(Account account,AccountUser accountUser) {
		if(StrKit.notBlank(account.getPassword())) {
			String salt = getSalt();
			account.setSalt(salt);
			account.setPassword(get256Pwd(salt, account.getPassword()));
		}
		account.update();
		AccountUser user = accountUserDao.getByAccountId(account.getId());
		accountUser.setId(user.getId());
		accountUser.update();
		accountDao.clearCache();
	}
	
	public List<Account> selectAccountListByType(AccountEnum accountEnum){
		return accountDao.selectAccountListByType(accountEnum.getValue());
	}
 
	@Before(Tx.class)
	public JMResult bind(Integer id,Integer anchorId, Integer manageId,Integer type) {
		Account account = accountDao.getById(id);
		if(account.getType()!=AccountEnum.TYPE_USER.getValue()) {
			return JMResultUtil.failDesc(JMMessage.ERROR);
		}
		if(type==AccountEnum.TYPE_SHOP.getValue()) {
			if(account.getRegisterType()!=AccountEnum.REGISTERTYPE_SHOP.getValue()) {
				return JMResultUtil.failDesc(ZbMessage.REGISTER_TYPE_ERROR);
			}
		}
		if(type==AccountEnum.TYPE_ANCHOR.getValue()||type==AccountEnum.TYPE_MANAGER.getValue()) {
			if(account.getRegisterType()!=AccountEnum.REGISTERTYPE_ANCHOR.getValue()) {
				return JMResultUtil.failDesc(ZbMessage.REGISTER_TYPE_ERROR);
			}
		}
		if(type==AccountEnum.TYPE_SHOP.getValue()) {
			//先关联
			RoomManagement manage = merchantDao.selectRoomManage(manageId);
			if(manage!=null) {
				return JMResultUtil.failDesc(ZbMessage.IS_BIND_MANAGE);
			}
			RoomMerchant selectRoomMerchant = merchantDao.selectRoomMerchant(anchorId);
			if(selectRoomMerchant!=null) {
				return JMResultUtil.failDesc(ZbMessage.IS_BIND_ANCHOR);
			}
			RoomManagement roomManagement = new RoomManagement();
			roomManagement.setCreateTime(new Date());
			roomManagement.setManageAccountId(manageId);
			roomManagement.setMerchantAccountId(account.getId());
			RoomMerchant roomMerchant = new RoomMerchant();
			roomMerchant.setCreateTime(new Date());
			roomMerchant.setAnchorAccountId(anchorId);
			roomMerchant.setMerchantAccountId(account.getId());
			Room room = roomDao.selectRoom(anchorId);
			if(room!=null) {
				roomMerchant.setRoomId(room.getId());
			}
			roomManagement.save();
			roomMerchant.save();
		}
		goodsDao.updateGoodsType(id, type);
		account.setType(type);
	    account.update();
	    accountDao.clearCache();
	    return JMResultUtil.successDesc(JMMessage.SUCCESS);
	}
	
	@Before(Tx.class)
	public JMResult editManage(Integer merchantAccountId,Integer manageId) {
		RoomManagement manage = merchantDao.selectRoomManage(manageId);
		if(manage!=null) {
			return JMResultUtil.failDesc(ZbMessage.IS_BIND_MANAGE);
		}
		RoomManagement roomManagement = merchantDao.selectManageByMerchantId(merchantAccountId);
		if(roomManagement!=null) {
			roomManagement.delete();
		}
		if(manageId==null) {
			return JMResultUtil.successDesc(JMMessage.SUCCESS);
		}
		RoomManagement management = new RoomManagement();
		management.setCreateTime(new Date());
		management.setMerchantAccountId(merchantAccountId);
		management.setManageAccountId(manageId);
		management.save();
		return JMResultUtil.successDesc(JMMessage.SUCCESS);
	}
	
	@Before(Tx.class)
	public JMResult editAnchor(Integer merchantAccountId,Integer anchorId) {
		RoomMerchant selectRoomMerchant = merchantDao.selectRoomMerchant(anchorId);
		if(selectRoomMerchant!=null) {
			return JMResultUtil.failDesc(ZbMessage.IS_BIND_ANCHOR);
		}
		RoomMerchant roomMerchant = merchantDao.selectMerchantByMerchantId(merchantAccountId);
		if(roomMerchant!=null) {
			roomMerchant.delete();
		}
		if(anchorId==null) {
			return JMResultUtil.successDesc(JMMessage.SUCCESS);
		}
		RoomMerchant merchant = new RoomMerchant();
		merchant.setCreateTime(new Date());
		merchant.setMerchantAccountId(merchantAccountId);
		merchant.setAnchorAccountId(anchorId);
		merchant.save();
		return JMResultUtil.successDesc(JMMessage.SUCCESS);
	}
	
	public List<Account> selectAccountList(String mobile,String nick,Integer state,String startTime,String endTime, Integer type){
		List<Account> list = accountDao.selectSystemList(mobile, nick, state, startTime, endTime, type);
		HashMap<Integer,String> typeMap = new HashMap<>();
		typeMap.put(-1, "超级管理员");
		typeMap.put(0, "系统管理员");
		typeMap.put(1, "普通用户");
		typeMap.put(2, "商家");
		typeMap.put(3, "主播");
		typeMap.put(4, "房管");
		for(Account account:list) {
			account.put("identity",typeMap.get(account.getType()));
			if(account.getType()==AccountEnum.TYPE_SHOP.getValue()) {
				Account bindAccount = accountDao.selectBindByMerchant(account.getId());
				if(bindAccount!=null) {
					account.put("manageMobile",bindAccount.getStr("manageMobile"));
					account.put("anchorMobile",bindAccount.getStr("anchorMobile"));
				}
			}
			if(account.getType()==AccountEnum.TYPE_ANCHOR.getValue()) {
				Account bindAccount = accountDao.selectBindByAnchor(account.getId());
				if(bindAccount!=null) {
					account.put("merchantMobile",bindAccount.getStr("merchantMobile"));
					account.put("manageMobile",bindAccount.getStr("manageMobile"));
				}
			}
			if(account.getType()==AccountEnum.TYPE_MANAGER.getValue()) {
				Account bindAccount = accountDao.selectBindByManage(account.getId());
				if(bindAccount!=null) {
					account.put("merchantMobile",bindAccount.getStr("merchantMobile"));
					account.put("anchorMobile",bindAccount.getStr("anchorMobile"));
				}
			}
		}
		return list;
	}

	public void replaceState(Integer id) {
		Account account = selectById(id);
		int state = AccountEnum.STATE_FZ.equals(account.getState())? BasicsInformation.STATUS_NORMAL : BasicsInformation.STATUS_FROZEN;
		account.setState(state);
		update(account);
	}

	public Account selectAccount(String field, String value, Integer registerType) {
		return accountDao.selectAccount(field, value, registerType);
	}
	
}
