package com.cn.jm.controller.base.system;


import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.AccountUser;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.ConverUtils;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.information.ZbMessage;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.util.ExportExcelUtil;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
@JMRouterMapping(url = SystemAccountController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemAccountController extends JMBaseSystemController{
	public static final String path = JMConsts.base_view_url+"/system/account";
	public static final String url ="/system/account";
	
	@Inject
	JMAccountService accountService;
	
	@Clear(SystemLoginInterceptor.class)
	public void index() {
		render(path + "/list.html");
	}
	
	
	public void page() {
		Integer pageNumber = getParaToInt("pageNumber",1);
		Integer pageSize = getParaToInt("pageSize",10);
		String mobile = getPara("mobile");
		String nick = getPara("nick");
		Integer type = getParaToInt("type");
		Integer state = getParaToInt("state");
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		Page<Account> page = accountService.selectSystemPage(pageNumber, pageSize, mobile,
				nick, state, startTime, endTime, type);
		JMResult.success(this, page, JMMessage.SUCCESS);
	}
	
	@Clear(SystemLoginInterceptor.class)
	public void toAdd() {
		createToken();
		render(path + "/form.html");
	}

	@Before({JMVaildInterceptor.class})
	@JMRuleVaild(fields = "id")
	public void toEdit() {
		Integer accountId = getParaToInt("id");
		Account account = accountService.selectAccountDetail(accountId);
		this.setAttr("account", account);
		createToken();
		render(path + "/form.html");
	}
	
	public void insertAccount() {
		String head = UploadUtil.uploadOSSImg(this, "head", "head");
		Account account = ConverUtils.fullBean(Account.class, getRequest());
		AccountUser accountUser = ConverUtils.fullBean(AccountUser.class, getRequest());
		if(account.getPassword()==null) {
			JMResult.fail(this, ZbMessage.PASSWORD_NOT_NULL);
			return;
		}
		if(head!=null) {
			accountUser.setHead(head);
		}
		if(accountUser.getAmount().compareTo(new BigDecimal(0))<0) {
			JMResult.fail(this, JMMessage.ERROR);
			return;
		}
		accountService.insertAccount(account, accountUser);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	
	public void editAccount() {
		String head = UploadUtil.uploadOSSImg(this, "head", "head");
		Account account = ConverUtils.fullBean(Account.class, getRequest());
		AccountUser accountUser = ConverUtils.fullBean(AccountUser.class, getRequest());
		if(head!=null) {
			accountUser.setHead(head);
		}
		if(accountUser.getAmount().compareTo(new BigDecimal(0))<0) {
			JMResult.fail(this, JMMessage.ERROR);
			return;
		}
		accountService.editAccount(account, accountUser);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	
	public void fzs() {
		String ids = getPara("ids");
		Integer state = getParaToInt("state");
		accountService.batchEditState(ids, state);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	
	
	public void editState() {
		Integer accountId = getParaToInt("id");
		boolean flag = accountService.editState(accountId);
		if(flag) {
			JMResult.success(this,JMMessage.SUCCESS);
		}else {
			JMResult.fail(this, JMMessage.ERROR);
		}
	}
	
	public void toBind() {
		Integer id = getParaToInt("id");
		List<Account> anchorList = accountService.selectAccountListByType(AccountEnum.TYPE_ANCHOR);
		List<Account> manageList = accountService.selectAccountListByType(AccountEnum.TYPE_MANAGER);
//		List<Account> merchantList = accountService.selectAccountListByType(AccountEnum.TYPE_SHOP);
		setAttr("anchorList",anchorList);
		setAttr("manageList",manageList);
//		setAttr("merchantList",merchantList);
		setAttr("id",id);
		render(path + "/tobind.html");
	}
	
	public void bind() {
		Integer id = getParaToInt("id"); 
		Integer anchorId = getParaToInt("anchorId");
		Integer manageId = getParaToInt("manageId");
		Integer type = getParaToInt("type");
		accountService.bind(id,anchorId,manageId,type).renderJson(this);
	}
	
	public void editManage() {
		Integer id = getParaToInt("id");
		Integer selectId = getParaToInt("selectId");
		accountService.editManage(id, selectId).renderJson(this);
		
	}
	
	public void editAnchor() {
		Integer id = getParaToInt("id");
		Integer selectId = getParaToInt("selectId");
		accountService.editAnchor(id, selectId).renderJson(this);
	}
	
	public void toEditAnchor() {
		Integer id = getParaToInt("id");
		List<Account> anchorList = accountService.selectAccountListByType(AccountEnum.TYPE_ANCHOR);
		setAttr("anchorList",anchorList);
		setAttr("id",id);
		render(path + "/toEditAnchor.html");
	}
	
	public void toEditManage() {
		Integer id = getParaToInt("id"); 
		List<Account> manageList = accountService.selectAccountListByType(AccountEnum.TYPE_MANAGER);
		setAttr("manageList",manageList);
		setAttr("id",id);
		render(path + "/toEditManage.html");
	}
	
	@Clear
	public void exportAccountList() {
		String mobile = getPara("mobile");
		String nick = getPara("nick");
		Integer type = getParaToInt("type");
		Integer state = getParaToInt("state");
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		List<Account> list = accountService.selectAccountList(mobile,
				nick, state, startTime, endTime, type);
		String[] header = {"id","昵称","手机号","用户身份","关联主播手机号","关联房管手机号","关联商家手机号","余额","状态","注册时间"};
		List<String> attrList = new ArrayList<String>();
		attrList.add("id");
		attrList.add("nick");
		attrList.add("mobile");
		attrList.add("identity");
		attrList.add("anchorMobile");
		attrList.add("manageMobile");
		attrList.add("merchantMobile");
		attrList.add("amount");
		attrList.add("state");
		attrList.add("createTime");
		File file = ExportExcelUtil.exportFile(list, "用户列表", header, attrList);
		renderFile(file);
	}
	
	


}
