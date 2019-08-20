package com.cn.jm.quatz;

import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cn._gen.model.Room;
import com.cn.jm.service.JMRoomService;
import com.cn.jm.util.ZbUtil;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;

public class LiveRoomJob implements Job{

	JMRoomService roomService = Aop.get(JMRoomService.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<Room> list = roomService.selectLiveRoomList();
		for(Room room:list) {
			String channelId = ZbUtil.util.getChannelId(room.getRoomNumber());
			boolean status = ZbUtil.util.selectChannelStatus(channelId);
			if(!status) {
				Db.tx(() ->{
						roomService.downSeeding(room.getId());
						return true;
				});
			}
		}
	}

}
