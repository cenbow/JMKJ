package com.cn.jm.util.rongyun;

import java.util.List;

import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.util.rongyun.messages.InfoNtfMessage;
import com.cn.jm.util.rongyun.messages.RedMessage;
import com.cn.jm.util.rongyun.messages.RedTipMessage;
import com.cn.jm.util.rongyun.messages.TxtMessage;
import com.cn.jm.util.rongyun.models.ChatRoomInfo;
import com.cn.jm.util.rongyun.models.CodeSuccessResult;
import com.cn.jm.util.rongyun.models.GagGroupUser;
import com.cn.jm.util.rongyun.models.HistoryMessageResult;
import com.cn.jm.util.rongyun.models.ListGagGroupUserResult;
import com.cn.jm.util.rongyun.models.TokenResult;

public class RyService {
	
	private final String moren = "/upload/moren.png";
	
	private  RongCloud rongCloud;
	
	public RyService(){
		rongCloud = RongCloud.getInstance();
	}
	
	/** 
	 * 获取 Token 方法
	 */
	public String getToken(int userId, String no) {
		TokenResult userGetTokenResult = null;
		try {
			userGetTokenResult = rongCloud.user.getToken(String.valueOf(userId), no, RongCloud.domain+moren);
			return userGetTokenResult.getToken();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	public String getToken(int userId, AccountExpand  userExtend) {
//		TokenResult userGetTokenResult = null;
//		String name = userExtend.getNick();
//		if(JMToolString.isEmpty(name))
//			name = "新用户";
//		try {
//			userGetTokenResult = rongCloud.user.getToken(String.valueOf(userId), name, RongCloud.domain+moren);
//			return userGetTokenResult.getToken();
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
	/**
	 * 刷新用户信息方法
	 */
	public void refresh(int userId, String name, String portraitUri){
		if(JMToolString.isEmpty(portraitUri)){
			portraitUri = moren;
		}
		try {
			rongCloud.user.refresh(String.valueOf(userId), name, RongCloud.domain+portraitUri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建群组
	 * @throws Exception 
	 */
	public JMResult createGroup(String[] groupUserId, int groupId, String groupName) throws Exception{
		CodeSuccessResult groupCreateResult = rongCloud.group.create(groupUserId, String.valueOf(groupId), groupName);
		if(groupCreateResult.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).data(String.valueOf(groupId)).desc("创建成功");
		}else{
			throw new Exception(groupCreateResult.getErrorMessage());
		}
	}
	
	/**
	 * 加入群组
	 * @throws Exception 
	 */
	public JMResult joinGroup(String[] groupUserId, int groupId, String groupName) throws Exception{
		CodeSuccessResult groupJoinResult = rongCloud.group.join(groupUserId, String.valueOf(groupId), groupName);
		if(groupJoinResult.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).desc("加入成功");
		}else{
			throw new Exception(groupJoinResult.getErrorMessage());
		}
	}
	
	/**
	 * 退出群组
	 * @throws Exception 
	 */
	public JMResult quitGroup(String[] groupUserId, int groupId) throws Exception{
		CodeSuccessResult groupQuitResult = rongCloud.group.quit(groupUserId, String.valueOf(groupId));
		if(groupQuitResult.getCode() == 200){
			//CacheKit.removeAll(XPGroupUserDao.GROUP+groupId);
			return JMResult.create().code(JMResult.SUCCESS).desc("退出群组成功");
		}else{
			throw new Exception(groupQuitResult.getErrorMessage());
		}
	}
	
	/**
	 * 解散群组
	 * @throws Exception 
	 */
	public JMResult dismissGroup(int userId, int groupId) throws Exception{
		CodeSuccessResult groupDismissResult = rongCloud.group.dismiss(String.valueOf(userId), String.valueOf(groupId));
		if(groupDismissResult.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).desc("解散成功");
		}else{
			throw new Exception(groupDismissResult.getErrorMessage());
		}
	}
	
	/**
	 * 
	* method statement：添加禁言
	* createTime：2018年9月29日下午5:27:38
	* By JaysonLee
	* @param userId
	* @param groupId
	* @param byUserId
	* @param minute
	* @return
	* @throws Exception
	**
	 */
	public JMResult addGagGroupUser(int groupId,int byUserId,String minute) throws Exception{
		CodeSuccessResult groupResult = rongCloud.group.addGagUser(byUserId+"", groupId+"", minute);
		if(groupResult.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).desc("添加禁言成功");
		}else{
			throw new Exception(groupResult.getErrorMessage());
		}
	}
	
	/**
	 * 
	* method statement：移除禁言成员
	* createTime：2018年9月29日下午5:52:46
	* By JaysonLee
	* @param groupId
	* @param byUserId
	* @return
	* @throws Exception
	**
	 */
	public boolean removeGagGroupUser(int groupId,String byUserId[]) throws Exception{
		CodeSuccessResult groupResult = rongCloud.group.rollBackGagUser(byUserId, groupId+"");
		if(groupResult.getCode() == 200){
			return true ;
		}else{
			throw new Exception(groupResult.getErrorMessage());
		}
	}
	
	/**
	 * 
	* method statement：查询禁言成员
	* createTime：2018年9月30日上午10:32:04
	* By JaysonLee
	* @param groupId
	* @return
	* @throws Exception
	**
	 */
	public List<GagGroupUser> listGagGroupUser(int groupId) throws Exception{
		ListGagGroupUserResult list = rongCloud.group.lisGagUser(groupId+"");
		if(list.getCode() == 200){
			return list.getUsers();
		}else{
			throw new Exception(list.getErrorMessage());
		}
	}
	
	
	/**
	 * 刷新群组信息
	 */
	public JMResult refreshGroup(int groupId, String name){
		try {
			CodeSuccessResult groupRefreshResult = rongCloud.group.refresh(String.valueOf(groupId), name);
			if(groupRefreshResult.getCode() == 200){
				return JMResult.create().code(JMResult.SUCCESS).desc("修改群组信息成功");
			}else{
				return JMResult.create().code(JMResult.FAIL).desc(groupRefreshResult.getErrorMessage());
			}
		} catch (Exception e) {
			return JMResult.create().code(JMResult.FAIL).desc(e.getMessage());
		}
	}
	
	/**
	 * 创建聊天室
	 * @throws Exception 
	 */
	public JMResult createChatRoom(String chatroomId, String chatroomName) throws Exception{
		ChatRoomInfo[] chatroomCreateChatRoomInfo = {new ChatRoomInfo(chatroomId, chatroomName)};
		CodeSuccessResult chatroomCreateResult = rongCloud.chatroom.create(chatroomCreateChatRoomInfo);
		if(chatroomCreateResult.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).desc("创建聊天室成功");
		}else{
			throw new Exception(chatroomCreateResult.getErrorMessage());
		}
	}
	
	/**
	 * 销毁聊天室
	 * @throws Exception 
	 */
	public JMResult destroyChatroom(String chatroomId) throws Exception{
		String[] chatroomDestroyChatroomId = {chatroomId};
		CodeSuccessResult chatroomDestroyResult = rongCloud.chatroom.destroy(chatroomDestroyChatroomId);
		if(chatroomDestroyResult.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).desc("销毁聊天室成功");
		}else{
			throw new Exception(chatroomDestroyResult.getErrorMessage());
		}
	}
	
	/**
	 * 添加用户到黑名单
	 * @throws Exception 
	 */
	public JMResult userAddBlacklist(String userId1, String userId2) throws Exception{
		CodeSuccessResult userAddBlacklistResult = rongCloud.user.addBlacklist(userId1, userId2);
		if(userAddBlacklistResult.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).desc("添加用户到黑名单成功");
		}else{
			throw new Exception(userAddBlacklistResult.getErrorMessage());
		}
	}
	
	/**
	 * 从黑名单中移除用户
	 * @throws Exception 
	 */
	public JMResult userRemoveBlacklist(String userId1, String userId2) throws Exception{
		CodeSuccessResult userRemoveBlacklistResult = rongCloud.user.removeBlacklist(userId1, userId2);
		if(userRemoveBlacklistResult.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).desc("从黑名单中移除用户成功");
		}else{
			throw new Exception(userRemoveBlacklistResult.getErrorMessage());
		}
	}
	
	/**
	 * 发送红包
	 */
	public boolean sendOneRed(int userId, int toUserId, int redId, String name,int coinId) throws Exception{
		String[] messagePublishPrivateToUserId = {String.valueOf(toUserId)};
		RedMessage redMessage = new RedMessage(redId, name,coinId);
		CodeSuccessResult messagePublishPrivateResult = rongCloud.message.publishPrivate(String.valueOf(userId), messagePublishPrivateToUserId, redMessage, name, "{\"pushData\":\""+name+"\"}", "4", 0, 0, 0, 1);
		return messagePublishPrivateResult.getCode() == 200 ? true : false;
	}
	
	/**
	 * 
	* method statement：发送转账消息
	* createTime：2018年4月11日上午10:16:58
	* By JaysonLee
	* @param userId
	* @param toUserId
	* @param remark
	* @param moneyType
	* @param money
	* @return
	* @throws Exception
	**
	 */
	/*public boolean sendOneTransfer(int userId, int toUserId, String remark,String moneyType,BigDecimal money,int coinId) throws Exception{
		String[] messagePublishPrivateToUserId = {String.valueOf(toUserId)};
		UserExtend payUser = XPUserExtendDao.xpd.getByUserId(userId);
		UserExtend getUser = XPUserExtendDao.xpd.getByUserId(toUserId);
		TransferMessage transferMessage = new TransferMessage(payUser.getNick(),getUser.getNick(), money, "转账给"+getUser.getNick(), moneyType, remark, ToolDateTime.getNowDate(),userId,toUserId,coinId);
		CodeSuccessResult messagePublishPrivateResult = rongCloud.message.publishPrivate(String.valueOf(userId), messagePublishPrivateToUserId, transferMessage, "比特讯转账", "{\"pushData\":\""+"比特讯转账"+"\"}", "4", 0, 0, 0, 1);
		return messagePublishPrivateResult.getCode() == 200 ? true : false;
	}*/
	
	public boolean receiveOneRed(int redId, int receiveUserId, String receiveUserName, int sendUserId, String sendUserName,int type) throws Exception{
		String[] messagePublishPrivateToUserId = {String.valueOf(receiveUserId)};
		RedTipMessage redTipMessage = new RedTipMessage(redId, receiveUserId, receiveUserName, sendUserId, sendUserName, 1, true,type);
		CodeSuccessResult messagePublishPrivateResult = rongCloud.message.publishPrivate(String.valueOf(sendUserId), messagePublishPrivateToUserId, redTipMessage, null, null, "4", 0, 0, 0, 1);
		return messagePublishPrivateResult.getCode() == 200 ? true : false;
	}
	
	public boolean sendMoreRed(int userId, int groupId, int redId, String name,Integer coinId) throws Exception{
		String[] messagePublishGroupToGroupId = {String.valueOf(groupId)};
		RedMessage redMessage = new RedMessage(redId, name,coinId);
		CodeSuccessResult messagePublishGroupResult = rongCloud.message.publishGroup(String.valueOf(userId), messagePublishGroupToGroupId, redMessage, name, "{\"pushData\":\""+name+"\"}", 1, 1, 1);
		return messagePublishGroupResult.getCode() == 200 ? true : false;
	}
	
	/**
	 * 
	* method statement：发送签到消息
	* createTime：2018年9月14日下午4:46:01
	* By JaysonLee
	* @param userId
	* @param groupId
	* @param days
	* @param number
	* @return
	* @throws Exception
	**
	 */
	/*public boolean sendSign(int userId, int groupId, int days,int number) throws Exception{
		UserExtend userExtend = XPUserExtendDao.xpd.getNickHead(userId);
		String[] messagePublishGroupToGroupId = {String.valueOf(groupId)};
		SignMessage signMessage = new SignMessage(groupId, "签到", userId, days, number,userExtend.getNick(),userExtend.getHead());
		CodeSuccessResult messagePublishGroupResult = rongCloud.message.publishGroup(String.valueOf(userId), messagePublishGroupToGroupId, signMessage, "签到", "{\"pushData\":\""+"签到"+"\"}", 1, 1, 1);
		return messagePublishGroupResult.getCode() == 200 ? true : false;
	}*/
	
	/**
	 * 
	* method statement：发送空投消息
	* createTime：2018年8月2日上午10:14:39
	* By JaysonLee
	* @param userId
	* @param groupId
	* @param airId
	* @param name
	* @param coinId
	* @return
	* @throws Exception
	**
	 */
	/*public boolean sendAirdrop(int userId, int groupId, int airId, String name,Integer coinId,String groupIds) throws Exception{
		String[] messagePublishGroupToGroupId = {String.valueOf(groupId)};
		UserExtend userExtend = XPUserExtendDao.xpd.getNickHead(userId);
		Airdrop airdrop = XPAirDropDao.xpd.get(airId);
		AirdropMessage airdropMessage = new AirdropMessage(airId, name, coinId, userId,userExtend.getHead(),userExtend.getNick(),airdrop.getName(),airdrop.getRemark(),groupIds);
		CodeSuccessResult messagePublishGroupResult = rongCloud.message.publishGroup(String.valueOf(userId), messagePublishGroupToGroupId, airdropMessage, name, "{\"pushData\":\""+name+"\"}", 1, 1, 1);
		return messagePublishGroupResult.getCode() == 200 ? true : false;
	}*/
	
	/**
	 * 
	* method statement：发送小灰条到群
	* createTime：2018年8月7日下午12:15:37
	* By JaysonLee
	* @param userId
	* @param groupId
	* @param message
	* @return
	* @throws Exception
	**
	 */
	public boolean sendTipsToGroup(int userId, int groupId,String message) throws Exception{
		String[] messagePublishGroupToGroupId = {String.valueOf(groupId)};
		InfoNtfMessage infoNtfMessage = new InfoNtfMessage(message,null);
		CodeSuccessResult messagePublishGroupResult = rongCloud.message.publishGroup(String.valueOf(userId), messagePublishGroupToGroupId, infoNtfMessage, message, "{\"pushData\":\""+message+"\"}", 1, 1, 1);
		return messagePublishGroupResult.getCode() == 200 ? true : false;
	}
	
	/**
	 * 
	* method statement：全网空投
	* createTime：2018年8月4日下午7:27:31
	* By JaysonLee
	* @param userId
	* @param airId
	* @param name
	* @param coinId
	* @param toUserIds
	* @return
	* @throws Exception
	**
	 */
	/*public boolean sendAirdropToOne(int userId, int airId, String name,Integer coinId,String [] toUserIds,String groupIds) throws Exception{
		UserExtend userExtend = XPUserExtendDao.xpd.getNickHead(userId);
		Airdrop airdrop = XPAirDropDao.xpd.get(airId);
		AirdropMessage airdropMessage = new AirdropMessage(airId, name, coinId, userId,userExtend.getHead(),userExtend.getNick(),airdrop.getName(),airdrop.getRemark(),groupIds);
		CodeSuccessResult messagePublishGroupResult = rongCloud.message.publishPrivate(userId+"", toUserIds, airdropMessage, name, 
				"{\"pushData\":\""+name+"\"}",
				null,
				1, 
				1, 
				1, 
				1);
		return messagePublishGroupResult.getCode() == 200 ? true : false;
	}*/
	
	
	/*public boolean sendAirdropToOne(int userId, int airId, String name,Integer coinId,String [] toUserIds,String groupIds) throws Exception{
		UserExtend userExtend = XPUserExtendDao.xpd.getNickHead(userId);
		Airdrop airdrop = XPAirDropDao.xpd.get(airId);
		AirdropMessage airdropMessage = new AirdropMessage(airId, name, coinId, userId,userExtend.getHead(),userExtend.getNick(),airdrop.getName(),airdrop.getRemark(),groupIds);
		//创建聊天室
		
		//拉人进入聊天室
		
		//向聊天室发送空投消息
		
		
	}*/
	
	public boolean receiveMoreRed(int groupId, int redId, int receiveUserId, String receiveUserName, int sendUserId, String sendUserName, int redCount, boolean isLast,int type) throws Exception{
		String[] messagePublishGroupToGroupId = {String.valueOf(groupId)};
		RedTipMessage redTipMessage = new RedTipMessage(redId, receiveUserId, receiveUserName, sendUserId, sendUserName, redCount, isLast,type);
		CodeSuccessResult messagePublishGroupResult = rongCloud.message.publishGroup(String.valueOf(receiveUserId), messagePublishGroupToGroupId, redTipMessage, null, null, 1, 1, 1);
		return messagePublishGroupResult.getCode() == 200 ? true : false;
	}
	
	/**
	 * 
	* 方法说明：发送消息给某人
	* 创建时间：2018年1月5日上午11:06:25
	* By 李智胜
	* @param userId  发送人id
	* @param toUserId 某人id
	* @param content 发送内容
	* @return
	* @throws Exception
	**
	 */
	public JMResult sendToOne(int userId,int toUserId,String content) throws Exception{
		TxtMessage txtMessage = new TxtMessage(content,"");
		String toUserIds [] = {toUserId+""};
		CodeSuccessResult sendToOneResult = rongCloud.message.publishPrivate(userId + "", toUserIds, txtMessage, null, null, null,1,1,1,1);
		if(sendToOneResult.getCode() == 200){
			//XPMessageHistoryDao.xpd.save(userId,toUserId,0,EmojiFilter.filterEmoji(content));
			return JMResult.create().code(JMResult.SUCCESS).desc("发送消息成功");
		}else{
			throw new Exception(sendToOneResult.getErrorMessage());
		}
	}
	
	/**
	 * 
	* method statement：发送小灰条给某人
	* createTime：2018年9月21日下午4:21:44
	* By JaysonLee
	* @param userId
	* @param toUserId
	* @param content
	* @return
	* @throws Exception
	**
	 */
	public JMResult sendToOneTip(int userId,int toUserId,String content) throws Exception{
		InfoNtfMessage infoNtfMessage = new InfoNtfMessage(content,null);
		String toUserIds [] = {toUserId+""};
		CodeSuccessResult sendToOneResult = rongCloud.message.publishPrivate(userId + "", toUserIds, infoNtfMessage, null, null, null,1,1,1,0);
		if(sendToOneResult.getCode() == 200){
			//XPMessageHistoryDao.xpd.save(userId,toUserId,0,EmojiFilter.filterEmoji(content));
			return JMResult.create().code(JMResult.SUCCESS).desc("发送消息成功");
		}else{
			throw new Exception(sendToOneResult.getErrorMessage());
		}
	}
	
	
	
	/**
	 * 
	* 方法说明：发送消息给某群
	* 创建时间：2018年1月5日上午11:07:11
	* By 李智胜
	* @param userId
	* @param groupId
	* @param content
	* @return
	* @throws Exception
	**
	 */
	public JMResult sendToGroup(int userId,int groupId,String content) throws Exception{
		TxtMessage txtMessage = new TxtMessage(content,"");
		String groupIds [] = {groupId+""};
		CodeSuccessResult sendToGroupResult = rongCloud.message.publishGroup(userId+"",groupIds,txtMessage,null,null,null,null,1);
		if(sendToGroupResult.getCode() == 200){
			//XPMessageHistoryDao.xpd.save(userId,groupId,1,EmojiFilter.filterEmoji(content));
			return JMResult.create().code(JMResult.SUCCESS).desc("发送消息成功");
		}else{
			throw new Exception(sendToGroupResult.getErrorMessage());
		}
	}
	
	/**
	 * 
	* 方法说明：获取历史消息
	* 创建时间：2018年1月5日上午11:36:59
	* By 李智胜
	* @param date 指定北京时间某天某小时，格式为2014010101，表示获取 2014 年 1 月 1 日凌晨 1 点至 2 点的数据。（必传）
	* @return
	* @throws Exception
	**
	 */
	public  JMResult getHistory(String date) throws Exception{
		HistoryMessageResult history = 
				rongCloud
				.message
				.getHistory(date);
		System.out.println(history);
		if(history.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).desc("获取成功").data(history.getUrl());
		}else{
			throw new Exception(history.getErrorMessage());
		}
	}
	
	/**
	 * 
	* method statement：后台群发消息给用户（以系统消息的形式）
	* createTime：2018年1月19日下午3:52:16
	* By JaysonLee
	* @param userId
	* @param ids
	* @param message
	* @return
	* @throws Exception
	**
	 */
	public JMResult sendSysMessage(int userId,String ids[],String message) throws Exception{
		TxtMessage txtMessage = new TxtMessage(message,"");
		CodeSuccessResult sendToGroupResult = rongCloud.message.PublishSystem(userId+"",ids,txtMessage,null,null,null,null);
		if(sendToGroupResult.getCode() == 200){
			String idsStr = "";
			for(String id : ids){
				idsStr = idsStr + id +",";
			}
			idsStr = idsStr.substring(0,idsStr.length() - 1);
			//XPSysMessageDao.xpd.saveMessage(userId, idsStr, message);
			return JMResult.create().code(JMResult.SUCCESS).desc("发送消息成功");
		}else{
			throw new Exception(sendToGroupResult.getErrorMessage());
		}
	}
	
	/**
	 * 
	* method statement：发送群聊邀请确认消息
	* createTime：2018年10月9日下午3:41:15
	* By JaysonLee
	* @param userId
	* @param groupId
	* @param nick
	* @param users
	* @param head
	* @param reason
	* @return
	* @throws Exception
	**
	 */
	/*public boolean sendJoinTipToGroup(int userId, int groupId,List<HashMap<String, Object>> users,String reason,Integer joinType) throws Exception{
		String[] messagePublishGroupToGroupId = {String.valueOf(groupId)};
		UserExtend inviteUser = XPUserExtendDao.xpd.getNickHead(userId);
		JoinGroupTipMessage joinGroupTipMessage = new JoinGroupTipMessage(groupId, reason, userId,users.size(), inviteUser.getNick(), inviteUser.getHead(), users,0,OrderUtil.createOutTradeNo(),XPGroupUserDao.xpd.adminIds(groupId),joinType);
		CodeSuccessResult messagePublishGroupResult = rongCloud.message.publishGroup(String.valueOf(userId), messagePublishGroupToGroupId,joinGroupTipMessage, "群聊邀请确认", "{\"pushData\":\"群聊邀请确认\"}", 1, 1, 0);
		return messagePublishGroupResult.getCode() == 200 ? true : false;
	}*/
	
	
	/**
	 * 
	 * @date 2018年12月13日 下午2:28:15
	 * @author JaysonLee
	 * @Description: 发送邀请进群确认消息给被邀请者
	 * @reqMethod
	 * @paramter
	 * @pDescription	
	 * @param userId
	 * @param touserId
	 * @param groupId
	 * @param usersRemark
	 * @param joinType
	 * @return
	 * @throws Exception
	 *
	 */
	/*public JMResult sendJoinComfirmToOne(int userId, int touserId,int groupId,Integer joinType) throws Exception{
		String[] toUserId = {String.valueOf(touserId)};
		String fromUser = String.valueOf(userId);
		Group group = XPGroupDao.xpd.get(groupId);
		UserExtend inviteUser = XPUserExtendDao.xpd.getNickHead(userId);
		JoinGroupComfirmMessage joinGroupConfirmMessage = new JoinGroupComfirmMessage(groupId,group.getName(), userId, group.getNum(), inviteUser.getNick(), group.getImage(), 
				XPGroupUserDao.xpd.myFansGURemark(touserId,groupId), 0,
				XPGroupUserDao.xpd.inGroupCode(userId, touserId, groupId), touserId, joinType,inviteUser.getHead());
		CodeSuccessResult sendToOneResult = rongCloud.message.publishPrivate(fromUser, toUserId, joinGroupConfirmMessage, null, null, null,1,1,1,1);
		if(sendToOneResult.getCode() == 200){
			return JMResult.create().code(JMResult.SUCCESS).desc("发送消息成功");
		}else{
			throw new Exception(sendToOneResult.getErrorMessage());
		}
	}*/
	
	/**
	 * 
	 * @date 2018年11月16日 下午5:27:40
	 * @author JaysonLee
	 * @Description: 分享直播给个人
	 * @reqMethod
	 * @paramter
	 * @pDescription	
	 * @param userId
	 * @param toUserId
	 * @param liveId
	 * @return
	 * @throws Exception
	 *
	 */
	/*public JMResult sendGroupLiveToOne(int userId,int toUserId,int liveId) throws Exception{
		String toUserIds [] = {toUserId+""};
		Live live = XPLiveDao.xpd.get(liveId);
		if(live == null){
			return JMResult.create().code(JMResult.FAIL).desc("该直播不存在");
		}
		UserExtend inviteUser = XPUserExtendDao.xpd.getNickHead(userId);
		JoinGroupLiveMessage joinGroupLiveMessage = new JoinGroupLiveMessage(inviteUser.getNick(), 
				inviteUser.getHead(), null,live.getState(),live.getTitle(),live.getPlayUrl(),live.getImg(),
				live.getType(), live.getExamineState(),liveId);
		CodeSuccessResult sendToOneResult = rongCloud.message.publishPrivate(userId + "", 
				toUserIds, joinGroupLiveMessage, null, null, null,1,1,1,1);
		if(sendToOneResult.getCode() == 200){
			XPMessageHistoryDao.xpd.save(userId,toUserId,0,EmojiFilter.filterEmoji(live.getTitle()+"---直播消息"));
			return JMResult.create().code(JMResult.SUCCESS).desc("发送消息成功");
		}else{
			throw new Exception(sendToOneResult.getErrorMessage());
		}
	}*/
	
	/*public void sendPush(){
		String platform [] = {"ios","android"};
		String userIds [] = {"14056","14021"};
		PushMessage pushMessage = new PushMessage(platform, "14021", new TagObj(null,userIds, false), new MsgObj("收到没", "ceshi"), new Notification("哈哈哈",null,null));
		try {
			CodeSuccessResult codeSuccessResult = rongCloud.push.broadcastPush(pushMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	
}
