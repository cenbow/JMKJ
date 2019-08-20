package com.cn.jm.util;

public class ToolTransactSQL {
	
	/**
	 * sql注入简单过滤
	 */
	public static String injection(String str) {
		return str.replaceAll(".*([';]+|(--)+).*", "-");
	}

}
