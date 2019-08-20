
package com.cn.jm._dao.goods;

import java.util.Date;

import com.cn._gen.dao.GoodsModelsDao;
import com.cn._gen.model.GoodsModels;

/**
 * 
 *
 * @date 2019年1月26日 下午3:04:44
 * @author Administrator
 * @Description: 商品模型
 *
 */
public class JMGoodsModelsDao extends GoodsModelsDao{
	
	public boolean add(String name){
		GoodsModels goodsModels = new GoodsModels();
		goodsModels.setName(name);
		goodsModels.setCreateTime(new Date());
		return save(goodsModels);
	}
	
}
