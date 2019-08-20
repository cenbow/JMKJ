package com.cn.jm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.Goods;
import com.cn._gen.model.Sku;
import com.cn.jm._dao.goods.GoodsEnum;
import com.cn.jm._dao.goods.JMGoodsDao;
import com.cn.jm._dao.img.ImgEnum;
import com.cn.jm._dao.img.JMImgDao;
import com.cn.jm._dao.sku.JMSkuDao;
import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.BasicsInformation;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.util.JMResultUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

import cn.hutool.core.date.DateUtil;

public class JMGoodsService extends BasicsService<Goods>{

	@Inject
	JMImgService imgService;
	@Inject
	JMGoodsDao goodsDao;
	@Inject
	JMImgDao imgDao;
	@Inject
	JMSkuDao skuDao;

	/**
	 * 
	 * @param goods 商品实体
	 * @param thumbnail 商品缩略图
	 * @param goodsImages 遗留的商品主图片数组
	 * @param detailImages 遗留的商品详情图数组
	 * @param goodsImageList 新上传的商品主图集合
	 * @param detailImageList 新上传的商品详情图集合
	 * @return
	 */
	public boolean update(Goods goods, String thumbnail, String[] goodsImages, String[] detailImages,
			ArrayList<String> detailImageList, ArrayList<String> goodsImageList) {
		return Db.tx(() ->{
			try {
				Integer goodsId = goods.getId();
				imgDao.deleteNotInByIdsAndType(goodsId, ImgEnum.GOODS_DETAILS_TYPE, detailImages);
				imgDao.deleteNotInByIdsAndType(goodsId, ImgEnum.GOODS_OWNER_TYPE, goodsImages);
				String formatDate = DateUtil.format(new Date(), BasicsInformation.DATE_FORMAT);
				if((detailImageList != null && goodsImageList != null) && (detailImageList.size() > 0 || goodsImageList.size() > 0)) {
					imgDao.inserGoodsImages(goodsId, detailImageList, goodsImageList, formatDate);
				}
				if(StrKit.notBlank(thumbnail)) {
					goods.setThumbnail(thumbnail);
				}
				return goodsDao.update(goods);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		});
	}

	public JMResult apiSaveGoods(Controller controller, int skuNum, Goods goods, Account account, ArrayList<String> detailImageList) {
		List<HashMap<String, Object>> skuList = new ArrayList<>();
		for(int i = 0;i<skuNum;i++){
			Integer[] attr = controller.getParaValuesToInt("attr"+i);
			String attrIds = JMToolString.reSplit(attr,",");
			String name = controller.getPara("name"+i);
			HashMap<String,Object> skuMap = new HashMap<>(3);
			skuMap.put("attrids", attrIds);
			skuMap.put("name", name);
			skuList.add(skuMap);
		}
		goods.setCreateTime(new Date());
		goods.setStock(1);
		goods.setUserId(account.getId());
		goods.setAccountType(account.getType());
		//选择第一张图片做缩略图
		goods.setThumbnail(detailImageList.get(0));
		if(Db.tx(() ->{
			try {
				if(goodsDao.save(goods)) {
					skuDao.add(goods.getId(), skuList);
					imgService.inserImages(goods.getId(), detailImageList, new ArrayList<>());
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}))
			return JMResultUtil.success();
		return JMResultUtil.fail();
	}

	public JMResult apiUpdateGoods(Controller controller, Integer nowSkuNum, Integer oldSkuNum,
			Goods goods, Account account, String[] detailImageIds, ArrayList<String> detailImageList) {
		List<HashMap<String, Object>> skuList = new ArrayList<>();
		for(int i = 0; i < nowSkuNum; i++){
			Integer[] attr = controller.getParaValuesToInt("attr"+i);
			String attrIds = JMToolString.reSplit(attr,",");
			String name = controller.getPara("nowName"+i);
			HashMap<String,Object> skuMap = new HashMap<>(3);
			skuMap.put("attrids", attrIds);
			skuMap.put("name", name);
			skuList.add(skuMap);
		}

		for(int i = 0; i< oldSkuNum; i++){
			int skuId = controller.getParaToInt("skuId"+i);
			String name = controller.getPara("name"+i);
			Sku sku = skuDao.getById(skuId,false);
			if(sku == null){
				continue ;
			}
			sku.setName(name);
			skuDao.update(sku);
		}
		skuDao.add(goods.getId(), skuList);
		goods.setCreateTime(new Date());
		if(Db.tx(() ->{
			try {
				if(goodsDao.update(goods)) {
					skuDao.add(goods.getId(), skuList);
					imgService.inserImages(goods.getId(), detailImageList, new ArrayList<>());
					imgService.deleteGoodsImage(detailImageIds, null, goods.getId());
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}))
			return JMResultUtil.success();
		return JMResultUtil.fail();
	}

	/**
	 * 假删除商品
	 * @param goodsId
	 * @param account
	 * @return
	 */
	public JMResult delteGoods(Integer goodsId, Account account) {
		Goods goods = selectById(goodsId);
		if(goods.getUserId().equals(account.getId())) {
			return JMResultUtil.fail(PromptInformationEnum.NOT_DELETE_OTHERS_GOODS);
		}
		goods.setState(GoodsEnum.DELETE_STATE.getCode());
		goodsDao.update(goods);		
		return JMResultUtil.success();
	}

	public JMResult setGoodsState(Integer goodsId, Account account) {
		Goods goods = selectById(goodsId);
		if(goods.getUserId().equals(account.getId())) {
			return JMResultUtil.fail(PromptInformationEnum.NOT_DELETE_OTHERS_GOODS);
		}
		GoodsEnum sellState = GoodsEnum.CAN_SELL_STATE.identical(goods.getSellState())?GoodsEnum.NOT_SELL_STATE:GoodsEnum.CAN_SELL_STATE;
		goods.setSellState(sellState.getCode());
		goodsDao.update(goods);
		return JMResultUtil.success();
	}
	
}
