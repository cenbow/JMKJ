
package com.cn.jm.controller.base.api;

import com.cn._gen.model.Account;
import com.cn._gen.model.Notice;
import com.cn.jm._dao.notice.JMNoticeAccountDao;
import com.cn.jm._dao.notice.JMNoticeDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.valid.rule.JMVaildLength;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 *
 * @date 2019年6月15日 上午9:41:12
 * @author Administrator
 * @Description: 系统通知模块
 *ApiShareController
 */
@API
@JMRouterMapping(url = ApiNoticeController.url)
public class ApiNoticeController extends JMBaseApiController {
	
	public static final String url ="/api/notice";
	@Inject
	public JMNoticeDao noticeDao;
	@Inject
	public JMNoticeAccountDao noticeAccountDao;
	
	
	/**
	 * 
	 * @date 2019年6月15日 上午9:26:42
	 * @author JaysonLee
	 * @Description: 系统消息列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter pageNumber,int,当前页数,r:t
	 * @paramter pageSize,int,当前页大小,r:t
	 * @paramter state,int,状态：-1全部 0未读 1已读,r:f,p:-1,d:-1
	 * @pDescription	
	 *
	 */
	@Before(JMApiAccountInterceptor.class)
	public void page() {
		Account account = getAttr("account");
		int state  = getParaToInt("state",-1);//-1全部 0未读 1已读
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		
		Page<Notice> page = noticeDao.page(pageNumber, pageSize, account.getId(), state);
		JMResult.success(this, page, "获取成功");
	}
	
	/**
	 * 
	 * @date 2019年6月15日 上午9:34:46
	 * @author JaysonLee
	 * @Description: 阅读消息
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter noticeId,int,消息id,r:t,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"noticeId"}),
		@JMRuleVaild(fields={},ruleClass = JMVaildLength.class,maxlength = {},minlength = {})
	})
	public void read() {
		Account account = getAttr("account");
		
		int noticeId = getParaToInt("noticeId");
		
		if (noticeAccountDao.read(noticeId, account.getId())) {
			JMResult.success(this,"阅读成功");
		}else {
			JMResult.fail(this, "该消息已读");
		}
	}
	
	/**
	 * 
	 * @date 2019年6月18日 上午10:35:56
	 * @author JaysonLee
	 * @Description: 删除消息
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter noticeId,int,消息id,r:t,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"noticeId"}),
		@JMRuleVaild(fields={},ruleClass = JMVaildLength.class,maxlength = {},minlength = {})
	})
	public void delMsg() {
		Account account = getAttr("account");
		int noticeId = getParaToInt("noticeId");
		noticeAccountDao.delete(noticeId, account.getId()).renderJson(this);
	}

	/**
	 * 
	 * @date 2019年7月16日 15:10:19
	 * @Description: 未读消息总数
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription	
	 *
	 */
	@Before(JMApiAccountInterceptor.class)
	public void unReadCount() {
		Account account = getAttr("account");
//		HashMap<String, Object> param = new HashMap<>();
//		param.put("accountId", account.getId());
//		param.put("state",0);
//		long count = noticeAccountDao.count(param);
//		JMResult.success(this,count, "阅读成功");
		JMResult.success(this,noticeAccountDao.unReadCount(account.getId()), "获取成功");
	}
	
}