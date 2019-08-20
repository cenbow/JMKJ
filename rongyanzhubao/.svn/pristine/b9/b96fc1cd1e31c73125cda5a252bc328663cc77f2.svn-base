package com.cn.jm.controller.webcast.system;

import com.cn._gen.model.Room;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.utils.util.ConverUtils;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.service.JMRoomService;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
@JMRouterMapping(url = SystemRoomController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemRoomController extends JMBaseSystemController{
	
	public static final String path = JMConsts.base_view_url+"/system/room";
	public static final String url ="/system/room";
	
	@Inject
	JMRoomService roomService;
	
	
	@Clear(SystemLoginInterceptor.class)
	public void index() {
		render(path + "/list.html");
	}
	
	public void page() {
		Integer pageNumber = getParaToInt("pageNumber",1);
		Integer pageSize = getParaToInt("pageSize",10);
		String name = getPara("name");
		String mobile = getPara("mobile");
		Integer state = getParaToInt("state");
		Integer isRecommend = getParaToInt("isRecommend");
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		String merchantMobile = getPara("merchantMobile");
		String merchantName = getPara("merchantName");
		Page<Room> page = roomService.selectSystemPage(pageNumber, pageSize, mobile ,name, state, 
				isRecommend, startTime, endTime ,merchantName, merchantMobile);
		JMResult.success(this, page, JMMessage.SUCCESS);
	}
	
	public void editState() {
		Integer id = getParaToInt("id");
		boolean flag = roomService.editState(id);
		if(flag) {
			JMResult.success(this, JMMessage.SUCCESS);
		}
	}
	
	public void batchEditState() {
		String ids = getPara("ids");
		Integer state = getParaToInt("state");
		roomService.batchEditState(ids,state);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	
	public void editRobotNumber() {
		Integer id = getParaToInt("id");
		Integer number = getParaToInt("number");
		boolean flag = roomService.editRobotNumber(id, number);
		if(flag) {
			JMResult.success(this, JMMessage.SUCCESS);
		}
	}
	
	public void editRecommend() {
		Integer id = getParaToInt("id");
		boolean flag = roomService.editIsRecommend(id);
		if(flag) {
			JMResult.success(this, JMMessage.SUCCESS);
		}
	}
	
	public void batchEditRecommend() {
		String ids = getPara("ids");
		Integer isRecommend = getParaToInt("isRecommend");
		roomService.batchEditRecommend(ids,isRecommend);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	
	public void toDetail() {
		Integer id = getParaToInt("id");
		Room room = roomService.selectSystemRoomDetail(id);
		setAttr("room",room);
		render(path + "/form.html");
	}
	
	public void editRoom() {
		String image = UploadUtil.uploadOSSImg(this, "image", "room");
		Room room = ConverUtils.fullBean(Room.class, getRequest());
		if(image!=null) {
			room.setImage(image);
		}
		boolean flag = roomService.editRoom(room);
		if(flag) {
			JMResult.success(this, JMMessage.SUCCESS);
		}
	}
	
	

}
