package com.cn.jm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.aliyuncs.exceptions.ClientException;
import com.cn._gen.model.Account;
import com.cn._gen.model.AccountUser;
import com.cn._gen.model.AssociateRequest;
import com.cn._gen.model.BroadcastRequest;
import com.cn._gen.model.RoomMerchant;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm._dao.account.JMAccountDao;
import com.cn.jm._dao.account.JMAccountUserDao;
import com.cn.jm._dao.room.JMAssociateRequestDao;
import com.cn.jm._dao.room.JMBroadcastRequestDao;
import com.cn.jm._dao.room.JMRoomDao;
import com.cn.jm._dao.room.JMRoomMerchantDao;
import com.cn.jm._dao.room.RequerstEnum;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.MessageTemplate;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.information.ZbMessage;
import com.cn.jm.util.AliMessageUtil;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.tio.JMTio;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

public class JMRoomMerchantService extends BasicsService<RoomMerchant>{
	
	JMRoomMerchantDao merchantDao = Aop.get(JMRoomMerchantDao.class);
	
	JMRoomDao roomDao = Aop.get(JMRoomDao.class);
	
	JMBroadcastRequestDao requestDao = Aop.get(JMBroadcastRequestDao.class);
	
	JMAccountDao accountDao = Aop.get(JMAccountDao.class);
	
	JMAssociateRequestDao associateRequestDao = Aop.get(JMAssociateRequestDao.class);
	
	JMAccountUserDao accountUserDao = Aop.get(JMAccountUserDao.class);
	
	public static HashMap<Integer, String> myAnchorMap = new HashMap<>();
	
	private String flag = "{XXX}";
	
	static {
		myAnchorMap.put(0, "您还没有入驻平台，不能关联主播");
		myAnchorMap.put(1, "成功");
		myAnchorMap.put(2, "已向{XXX}主播发送了关联请求，等待对方同意");
		myAnchorMap.put(3, "{XXX}主播拒绝了您的关联请求");
		myAnchorMap.put(4, "您还没有关联任何主播");
	}
	@Before(Tx.class)
	public boolean editRequest(Integer id,Integer state) throws ClientException {
		BroadcastRequest broadcastRequest = requestDao.getById(id);
		broadcastRequest.setState(state);
	    broadcastRequest.update();
	    //主播账号
	    Account account = accountDao.getById(broadcastRequest.getAccountId());
//	    AccountUser merchantAccountUser = accountUserDao.getByAccountId(broadcastRequest.getMerchantAccountId());
	    if(state==RequerstEnum.AGREE.getValue()) {
	    	AliMessageUtil.sendSms(account.getMobile(), MessageTemplate.MERCHANT_AGREE, "");
	    }
	    if(state==RequerstEnum.DISAGREE.getValue()) {
	    	AliMessageUtil.sendSms(account.getMobile(), MessageTemplate.MERCHANT_DISAGREE, "");
	    }
		return true;
	}
	
	
	public BroadcastRequest selectCurrentRequest(Account account) {
		return requestDao.selectRequest(account.getId(), RequerstEnum.REQUEST);
	}
	
	//主播用
	public AssociateRequest selectCurrentAssociateRequest(Account anchorAccount) {
		return associateRequestDao.selectCurrentAssociate(anchorAccount.getId(),null,RequerstEnum.REQUEST);
	}
	//商家用
	public AssociateRequest selectMyAssociateRequest(Account account) {
		return associateRequestDao.selectCurrentAssociate(account.getId(),null,null);
	}
	
	@Before(Tx.class)
	public boolean insertAssociateRequest(Account account,Integer anchorAccountId) {
		AssociateRequest associateRequest = new AssociateRequest();
		associateRequest.setAnchorAccountId(anchorAccountId);
		associateRequest.setCreateTime(new Date());
		associateRequest.setMerchantAccountId(account.getId());
		if(associateRequest.save()) {
			HashMap<String,Object> result = new HashMap<>();
			result.put("id", associateRequest.getId());
			JMTio.send(anchorAccountId,JMResultUtil.getJMResult(JMResultUtil.ASSOCIATE_REQUEST, 
	 				result, PromptInformationEnum.ASSOCIATE_REQUEST));
			return true;
		}
		return false;
	}
	
	
	public JMResult selectMyManage(Account account){
		if(account.getType()!=AccountEnum.TYPE_SHOP.getValue()) {
			return JMResultUtil.failDesc(myAnchorMap.get(0));
		}
		List<AccountUser> myManage = merchantDao.selectMyManage(account.getId());
		return JMResult.success(myManage);
	}
	
	
	public JMResult selectMyAnchor(Account account){
		HashMap<String, Object> result = new HashMap<>();
		if(account.getType()!=AccountEnum.TYPE_SHOP.getValue()) {
			result.put("state", 0);
			return JMResultUtil.success(myAnchorMap.get(0), result, JMResult.SUCCESS);
		}
		AssociateRequest associateRequest = associateRequestDao.selectCurrentAssociate
				(null, account.getId(), null);
		if(associateRequest!=null) {
			AccountUser anchorAccountUser = accountUserDao.getByAccountId(associateRequest.getAnchorAccountId());
			if(associateRequest.getState()==RequerstEnum.REQUEST.getValue()) {
				result.put("state", 2);
				return JMResultUtil.success(myAnchorMap.get(2).replace(flag,anchorAccountUser.getNick()), result, JMResult.SUCCESS);
			}
			if(associateRequest.getState()==RequerstEnum.DISAGREE.getValue()) {
				result.put("state", 3);
				return JMResultUtil.success(myAnchorMap.get(3).replace(flag,anchorAccountUser.getNick()), result, JMResult.SUCCESS);
			}
		}
		List<AccountUser> myAnchor = merchantDao.selectMyAnchor(account.getId());
		if(myAnchor.size()>0) {
			result.put("state",1);
			result.put("myAnchor",myAnchor);
			return JMResultUtil.success(myAnchorMap.get(1), result, JMResult.SUCCESS);
		}
		result.put("state",4);
		return JMResultUtil.success(myAnchorMap.get(4), result, JMResult.SUCCESS);
	}
	
	

}
