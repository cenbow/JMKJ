
package com.cn.jm._dao.notice;

import java.util.Date;

import com.cn._gen.dao.NoticeDao;
import com.cn._gen.model.Notice;
import com.cn._gen.model.NoticeAccount;
import com.cn.jm.core.db.JMToolSql;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.tio.JMTio;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;


/**
 * 消息
 * @author 李劲
 *
 */
public class JMNoticeDao extends NoticeDao{
	
	public static final int TYPE_SYSTEM = 0;//系统
	public static final int TYPE_ACCOUNT = 1;//用户
	public static final int TYPE_SYSTEM_ADMIN = 2;//系统后台消息
	
	public static final int MSG_TYPE_TEXT = 0;//文本
	public static final int MSG_TYPE_ORDER = 1;//订单
	public static final int MSG_TYPE_LINK = 2;//超链接
	public static final int MSG_TYPE_REPLY_TOPIC = 3;//回复话题
	public static final int MSG_TYPE_REPLY_COMMENT = 4;//回复评论
	
	@Inject
	private JMNoticeAccountDao noticeAccountDao;
	
	/**
	 * 
	 * @param title
	 * @param remark
	 * @param content
	 * @param type
	 * @param ids
	 * @param msgType
	 * @param userId
	 * @return
	 */
	public boolean save(String title,String remark,String content,int ids,int msgType) {
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setRemark(remark);
		notice.setContent(content);
		notice.setType(TYPE_SYSTEM);
		notice.setIds(ids);
		notice.setMsgType(msgType);
		notice.setCreateTime(new Date());
		return save(notice);
	}
	
	public boolean save(String title,String remark,String content,int ids,int msgType,int userId) {
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setRemark(remark);
		notice.setContent(content);
		notice.setType(TYPE_ACCOUNT);
		notice.setIds(ids);
		notice.setMsgType(msgType);
		notice.setCreateTime(new Date());
		
		boolean result = save(notice);
		if (result) {
			int noticeId = notice.getId();
			NoticeAccount noticeAccount = new NoticeAccount();
			noticeAccount.setNoticeId(noticeId);
			noticeAccount.setAccountId(userId);
			result = noticeAccountDao.save(noticeAccount);
		}
		return result;
	}
	
	
	public Page<Notice> page(int pageNumber, int pageSize,int accountId,int state){
		Page<Notice> page = null;
		String format = null;
		String form = null;
		switch (state) {
		case -1://全部
			format = " FROM tb_notice AS a LEFT JOIN tb_notice_account AS b ON a.id = b.noticeId WHERE (a.type = 1 AND b.accountId = %s) OR a.type = 0 GROUP BY a.id ORDER BY a.id DESC";
			form = JMToolSql.format(format, accountId);
			page =  page(pageNumber, pageSize, " a.type,a.id noticeId,a.title,a.content,a.image,a.createTime,b.state,b.readTime", form, true);
			break;
			
		case 0://未读
			format = " FROM tb_notice AS a LEFT JOIN tb_notice_account AS b ON a.id = b.noticeId WHERE (a.type = 1 AND b.accountId = %s AND b.state = 0) OR (a.type = 0 AND b.state IS NULL) GROUP BY a.id ORDER BY a.id DESC";
			form = JMToolSql.format(format, accountId);
			page =  page(pageNumber, pageSize, " a.type,a.id noticeId,a.title,a.content,a.image,a.createTime,b.state,b.readTime", form, true);
			break;
			
		case 1://已读
			format = " FROM tb_notice AS a LEFT JOIN tb_notice_account AS b ON a.id = b.noticeId WHERE a.type = 1 AND b.accountId = %s AND b.state = 1 GROUP BY a.id ORDER BY a.id DESC";
			form = JMToolSql.format(format, accountId);
			page =  page(pageNumber, pageSize, " a.type,a.id noticeId,a.title,a.content,a.image,a.createTime,b.state,b.readTime", form, true);
			break;

		default:
			break;
		}
		return page;
	}

//	private static final int SEND_STATE_ALL = 0;//发送全部
//	private static final int SEND_STATE_SHOP = 1;//发送店铺
//	private static final int SEND_STATE_USER = 2;//发送普通用户
	public boolean saveNotice(Notice notice) {
//		if(!save(notice)) {
//			return false;
//		}
//		Integer noticeId = notice.getId();
//		int sendState = notice.getSendState();
//		StringBuilder sql = new StringBuilder("SELECT * FROM tb_account WHERE `type` IN (");
//		if(sendState == SEND_STATE_ALL) {
//			sql.append("1,3");
//		}else if(sendState == SEND_STATE_SHOP) {
//			sql.append("1");
//		}else if(sendState == SEND_STATE_USER) {
//			sql.append("3");
//		}
//		sql.append(")");
//		List<Account> accountList = Account.dao.find(sql.toString());
//		List<NoticeAccount> naList = new ArrayList<NoticeAccount>();
//		for (Account account : accountList) {
//			NoticeAccount na = new NoticeAccount();
//			na.setAccountId(account.getId());
//			na.setNoticeId(noticeId);
//			na.setState(JMNoticeAccountDao.STATE_UN_READ);
//			naList.add(na);
//		}
//		try {
//			noticeAccountDao.saves(naList);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
		
		JMTio.sendAllUser(JMResultUtil.success("", JMResultUtil.SYSTEM_NOTICE));
		return save(notice);
	}
	
	public Notice createNotice(String content,String title,int type,Date nowTime) {
		Notice notice = new Notice();
		notice.setCreateTime(nowTime);
		notice.setContent(content);
		notice.setTitle(title);
		notice.setType(type);
		save(notice);
		return notice;
	}
	
}
