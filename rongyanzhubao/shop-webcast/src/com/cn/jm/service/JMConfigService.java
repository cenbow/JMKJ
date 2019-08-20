package com.cn.jm.service;

import com.cn._gen.model.Configure;

public class JMConfigService extends BasicsService<Configure> {
	
	public Configure getByName(String name) {
		return selectOneByKeyAndValue("name = ", name);
	}
}
