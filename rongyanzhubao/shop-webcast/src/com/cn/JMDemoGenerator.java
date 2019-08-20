package com.cn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.cn.jm.core.JMConsts;
import com.cn.jm.core.db.JMDbSourceMeta;
import com.cn.jm.core.gen.JMGeneratorKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * 在数据库表有任何变动时，运行�?�? main 方法，极速响应变化进行代码重�?
 */
public class JMDemoGenerator {

	public static String basePackageName = "com.cn.jm._gen";
	public static String mPackageName = basePackageName + ".model";
	public static String vPackageName = basePackageName + ".dao2";
	public static String cPackageName = basePackageName + ".controller";

	//public static String baseOutputDir = PathKit.getWebRootPath() + "/../src/com/cn/jm/_gen";
	public static String baseInputDir = System.getProperty("user.dir") + "/src/com/cn/jm/temp";
	public static String baseOutputDir = System.getProperty("user.dir") + "/src/com/cn/_gen";
	/*
	 * public static String modelOutputDir = baseOutputDir + "/model"; public static
	 * String daoOutputDir = baseOutputDir + "/dao"; public static String cOutputDir
	 * = baseOutputDir + "/controller";
	 */
	

	public static DataSource getDataSource() {
		DruidPlugin c3p0Plugin = new DruidPlugin(PropKit.get("base_jdbcUrl"), PropKit.get("base_user"), PropKit.get("base_password").trim());
		c3p0Plugin.start();
		return c3p0Plugin.getDataSource();
	}
	
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("base_jdbcUrl"), PropKit.get("base_user"), PropKit.get("base_password").trim());
	}
	
	public static List<JMDbSourceMeta> createDbSourceMeta(){
		List<JMDbSourceMeta> dbSourceMetaList = new ArrayList<JMDbSourceMeta>();
		JMDbSourceMeta dbSourceMeta1 =  new JMDbSourceMeta(); 
		dbSourceMeta1.setDbConfigName("base");
		dbSourceMeta1.setUrl(PropKit.get("base_jdbcUrl"));
		dbSourceMeta1.setUserName(PropKit.get("base_user"));
		dbSourceMeta1.setPassword(PropKit.get("base_password"));
		dbSourceMetaList.add(dbSourceMeta1);
		return dbSourceMetaList;
	}

	public static void main(String[] args) throws Exception {
		PropKit.use("db.properties");
		JMConsts.dbSourceMetaList = createDbSourceMeta();
		for (int i = 0; i < JMConsts.dbSourceMetaList.size(); i++) {
			JMDbSourceMeta dbSourceMeta = JMConsts.dbSourceMetaList.get(i);
			gen(dbSourceMeta);
		}
	}

	public static void gen(JMDbSourceMeta dbSourceMeta ) {
		//System.err.println(modelOutputDir + "/base");
		JMGeneratorKit gernerator = new JMGeneratorKit(getDataSource());
		gernerator.setDbConfigName(dbSourceMeta.getDbConfigName());
		// 添加不需要生成的表名
//		gernerator.addExcludedTable("adv", "happy_history_order");
		// 设置是否�? Model 中生�? dao 对象
		// gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		// gernerator.setGenerateDataDictionary(false);
		// 设置�?要被移除的表名前�?用于生成modelName。例如表�? "osc_user"，移除前�? "osc_"后生成的model名为
		// "User"而非 OscUser
		gernerator.setRemovedTableNamePrefixes("tb_", "shop_", "system_", "webcast_");
		// 生成
		try {
			gernerator.generate(baseInputDir,baseOutputDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
