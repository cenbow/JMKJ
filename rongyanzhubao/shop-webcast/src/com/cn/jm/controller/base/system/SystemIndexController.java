package com.cn.jm.controller.base.system;

import java.util.HashMap;

import com.cn._gen.model.Notice;
import com.cn.jm._dao.notice.JMNoticeDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.core.web.dao.JMBaseDao;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

@JMRouterMapping(url = SystemIndexController.url)
public class SystemIndexController extends JMBaseSystemController{
	
	public static final String url = "/system/home";
	public static final String path = JMConsts.base_view_url+"/system/home";
	
	@Inject
	public JMNoticeDao noticeDao;
	
	@Before(value={SystemLoginInterceptor.class})
	public void notice() {
		Integer pageNumber = getParaToInt(0, 1);
		Integer pageSize = JMConsts.pageSize;
		
		HashMap<String, Object> andpm = new HashMap<>();
		andpm.put("type", JMNoticeDao.TYPE_SYSTEM_ADMIN);
		Page<Notice> page = noticeDao.page(pageNumber, pageSize, "", andpm,"id", JMBaseDao.DESC, true);
//		Page<Notice> page = noticeDao.page(pageNumber, pageSize, true);
		
		setAttr("page", page);
		jump();
		render(path+"/notice.html");
	}

}
