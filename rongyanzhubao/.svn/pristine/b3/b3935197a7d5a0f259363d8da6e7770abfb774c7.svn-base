package com.cn.jm.util.sqltool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cn.jm.core.db.JMMainTable;
import com.cn.jm.core.db.JMSubTable;



public class JMTableRelate {

	public static HashMap<String,JMMainTable> tableMap = new HashMap<String,JMMainTable>();

	public static void create() {
		tableMap.put("account", createAcount());
		tableMap.put("menu", createMenu());
		tableMap.put("role", createRole());
		tableMap.put("label", createLabel());
		tableMap.put("meta", createMeta());
		
		
		//oa
		tableMap.put("demand", createDemand());
		tableMap.put("finance", createFinanceDao());
	}
	

	private static JMMainTable createAcount() {
		List<JMSubTable> subTableList = new ArrayList<JMSubTable>();
		subTableList.add(new JMSubTable("system_role_account", "", JMSubTable.CASCADE));
		subTableList.add(new JMSubTable("tb_account_merchant", "", JMSubTable.CASCADE));
		subTableList.add(new JMSubTable("tb_account_session", "", JMSubTable.CASCADE));
		subTableList.add(new JMSubTable("tb_account_user", "", JMSubTable.CASCADE));
		subTableList.add(new JMSubTable("tb_area_user", "", JMSubTable.CASCADE));
		
		//oa
		subTableList.add(new JMSubTable("oa_demand_account", "", JMSubTable.CASCADE));
		
		JMMainTable table = new JMMainTable();
		table.setTableName("tb_account");
		table.setPrimaryKeyName("id");
		table.setSubKeyName("accountId");
		table.setSubTableNameList(subTableList);
		return table;
	}
	
	private static JMMainTable createMenu() {
		List<JMSubTable> subTableList = new ArrayList<JMSubTable>();
		subTableList.add(new JMSubTable("system_menu_power", "", JMSubTable.CASCADE));
		subTableList.add(new JMSubTable("system_role_menu", "", JMSubTable.CASCADE));
		subTableList.add(new JMSubTable("system_role_power", "", JMSubTable.CASCADE));
		
		JMMainTable table = new JMMainTable();
		table.setTableName("system_menu");
		table.setPrimaryKeyName("id");
		table.setSubKeyName("menuId");
		table.setSubTableNameList(subTableList);
		return table;
	}
	
	private static JMMainTable createRole() {
		List<JMSubTable> subTableList = new ArrayList<JMSubTable>();
		subTableList.add(new JMSubTable("system_role_account", "", JMSubTable.CASCADE));
		subTableList.add(new JMSubTable("system_role_menu", "", JMSubTable.CASCADE));
		subTableList.add(new JMSubTable("system_role_power", "", JMSubTable.CASCADE));
		subTableList.add(new JMSubTable("system_role_permission", "", JMSubTable.CASCADE));
		
		JMMainTable table = new JMMainTable();
		table.setTableName("system_role");
		table.setPrimaryKeyName("id");
		table.setSubKeyName("roleId");
		table.setSubTableNameList(subTableList);
		return table;
	}
	
	private static JMMainTable createLabel() {
		List<JMSubTable> subTableList = new ArrayList<JMSubTable>();
		subTableList.add(new JMSubTable("tb_label_relation", "", JMSubTable.CASCADE));
		
		JMMainTable table = new JMMainTable();
		table.setTableName("tb_label");
		table.setPrimaryKeyName("id");
		table.setSubKeyName("labelId");
		table.setSubTableNameList(subTableList);
		return table;
	}
	
	private static JMMainTable createMeta() {
		List<JMSubTable> subTableList = new ArrayList<JMSubTable>();
		subTableList.add(new JMSubTable("tb_meta", "", JMSubTable.CASCADE));
		
		JMMainTable table = new JMMainTable();
		table.setTableName("tb_meta_type");
		table.setPrimaryKeyName("id");
		table.setSubKeyName("metaTypeId");
		table.setSubTableNameList(subTableList);
		return table;
	}
	
	private static JMMainTable createDemand() {
		List<JMSubTable> subTableList = new ArrayList<JMSubTable>();
		subTableList.add(new JMSubTable("oa_demand_account", "", JMSubTable.CASCADE));
		
		JMMainTable table = new JMMainTable();
		table.setTableName("oa_demand");
		table.setPrimaryKeyName("id");
		table.setSubKeyName("demandId");
		table.setSubTableNameList(subTableList);
		return table;
	}
	
	private static JMMainTable createFinanceDao() {
		List<JMSubTable> subTableList = new ArrayList<JMSubTable>();
		subTableList.add(new JMSubTable("oa_finance_value", "", JMSubTable.CASCADE));
		
		JMMainTable table = new JMMainTable();
		table.setTableName("oa_finance");
		table.setPrimaryKeyName("id");
		table.setSubKeyName("financeId");
		table.setSubTableNameList(subTableList);
		return table;
	}


}
