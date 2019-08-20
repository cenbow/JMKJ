
package com.cn.jm._dao.spec;

import com.cn._gen.dao.SpecModelsDao;
import com.cn._gen.model.SpecModels;


/**
 * 
 *
 * @date 2019年1月26日 下午3:01:46
 * @author Administrator
 * @Description: 商品模型与规格关联
 *
 */
public class JMSpecModelsDao extends SpecModelsDao{
	
	public boolean add(int specId,int modelsId){
		SpecModels specModels = new SpecModels();
		specModels.setSpecId(specId);
		specModels.setModelsId(modelsId);
		return save(specModels);
	}
	

}
