
package com.cn.jm.controller.shop.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.AccountUser;
import com.cn._gen.model.Goods;
import com.cn._gen.model.GoodsStar;
import com.cn._gen.model.Label;
import com.cn._gen.model.Sku;
import com.cn._gen.model.Spec;
import com.cn._gen.model.SpecAttribute;
import com.cn.jm._dao.goods.JMGoodsDao;
import com.cn.jm._dao.goods.JMGoodsModelsDao;
import com.cn.jm._dao.goods.JMGoodsStarDao;
import com.cn.jm._dao.img.ImgEnum;
import com.cn.jm._dao.label.JMLabelDao;
import com.cn.jm._dao.label.JMLabelRelationDao;
import com.cn.jm._dao.label.LabelEnum;
import com.cn.jm._dao.sku.JMSkuAttrDao;
import com.cn.jm._dao.sku.JMSkuDao;
import com.cn.jm._dao.spec.JMSpecAttributeDao;
import com.cn.jm._dao.spec.JMSpecDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.utils.util.JMUploadKit;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.core.web.dao.JMBaseDao;
import com.cn.jm.information.BasicsInformation;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.interceptor.SystemSubmitInterceptor;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.service.JMAccountUserService;
import com.cn.jm.service.JMImgService;
import com.cn.jm.service.JMLabelService;
import com.cn.jm.service.JMRoomService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 商品管理
 */
@JMRouterMapping(url = SystemGoodsController.url)
@Before(value = { SystemLoginInterceptor.class })
public class SystemGoodsController extends JMBaseSystemController {

	public static final String path = JMConsts.base_view_url + "/system/shop/goods";
	public static final String url = "/system/goods";

	@Inject
	public JMGoodsDao goodsDao;
	@Inject
	public JMLabelDao labelDao;
	@Inject
	public JMLabelRelationDao labelRelationDao;
	// @Inject
	// public JMAccountExpandDao accountExpandDao;
	@Inject
	public JMGoodsModelsDao goodsModelDao;
	@Inject
	public JMSpecAttributeDao specAttributeDao;
	@Inject
	public JMSpecDao specDao;
	@Inject
	public JMSkuDao skuDao;
	@Inject
	public JMSkuAttrDao skuAttrDao;
	@Inject
	public JMGoodsStarDao goodsStarDao;

	@Inject
	JMImgService imgService;
	@Inject
	JMAccountUserService accountUserService;
	@Inject
	JMLabelService labelService;
	@Inject
	JMRoomService roomService;
	@Inject
	JMAccountService accountService;

	public void index() {
		page();
	}

	public void page() {
		String keyword = getPara("keyword", "");
		Integer state = getParaToInt("state", -1);
		String startTime = getPara("startTime", "");
		String endTime = getPara("endTime", "");
		String artNo = getPara("artNo");
		Integer labelId = getParaToInt("labelId", -1);

		Integer pageNumber = getParaToInt(0, 1);
		Integer pageSize = JMConsts.pageSize;

		Page<Goods> page = goodsDao.page(pageNumber, pageSize, keyword, artNo, startTime, endTime, state, labelId,
				false);
		setAttr("state", state);
		setAttr("labelId", labelId);
		setAttr("artNo", artNo);
		setAttr("keyword", keyword);
		setAttr("startTime", startTime);
		setAttr("endTime", endTime);
		setAttr("page", page);
		setAttr("labelList", labelDao.all(JMLabelDao.TYPE_SHOP));
		jump();
		render(path + "/page.html");
	}
	
	public void selectBroadcastPage() {
		String keyword = getPara("keyword", "");
		Integer state = getParaToInt("state", -1);
		String startTime = getPara("startTime", "");
		String endTime = getPara("endTime", "");
		String artNo = getPara("artNo");
		Integer labelId = getParaToInt("labelId", -1);
		Integer pageNumber = getParaToInt(0, 1);
		Integer pageSize = JMConsts.pageSize;
		Page<Goods> page = goodsDao.selectBroadcastPage(pageNumber, pageSize, keyword,
				artNo, startTime, endTime, state, labelId,false);
		setAttr("state", state);
		setAttr("labelId", labelId);
		setAttr("artNo", artNo);
		setAttr("keyword", keyword);
		setAttr("startTime", startTime);
		setAttr("endTime", endTime);
		setAttr("page", page);
		setAttr("labelList", labelDao.all(JMLabelDao.TYPE_SHOP));
		jump();
		render(path + "/broadcastPage.html");

	}

	public void selectGoodsPage() {
		String keyword = getPara("keyword", "");
		Integer id = getParaToInt("id");
		Integer state = getParaToInt("state", -1);

		String startTime = getPara("startTime", "");
		String endTime = getPara("endTime", "");

		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize", 10);

		HashMap<String, Object> andpm = new HashMap<String, Object>();
		HashMap<String, Object> likepm = new HashMap<String, Object>();
		HashMap<String, Object> orpm = new HashMap<String, Object>();

		if (id == null) {
			if (JMToolString.isNotEmpty(keyword)) {
				likepm.put("name", keyword);
			}
		} else {
			andpm.put("id", id);
		}
		if (state != -1) {
			andpm.put("state", state);
		}
		Page<Goods> page = null;

		if (JMToolString.isNotEmpty(startTime) && JMToolString.isNotEmpty(endTime)) {
			page = goodsDao.page(pageNumber, pageSize, "", andpm, orpm, likepm, startTime, endTime, "id",
					JMBaseDao.DESC, true);
		} else {
			page = goodsDao.page(pageNumber, pageSize, "", andpm, orpm, likepm, "id", JMBaseDao.DESC, true);
		}
		for (Goods goods : page.getList()) {
			if (goods != null) {
				Integer accountId = goods.getUserId();// 创建人
				if (accountId != null) {
					List<AccountUser> accountUser = accountUserService.selectByKeyAndValue(" accountId =", accountId);
					goods.put("accountUser", accountUser);
				}
				Integer labelId = goods.getLabelId();
				if (labelId != null) {
					Label label = labelDao.getById(labelId);
					goods.put("label", label);
				}
			}
		}
		JMResult.success(this, page, "获取成功");
	}

	public void add() {
		setAttr("labelList", labelDao.all(JMLabelDao.TYPE_SHOP));
		setAttr("label", new Label());
		setAttr("isEdit", false);
		setAttr("action", JMConsts.ACTION_ADD);
		setAttr("goods", new Goods());
		setAttr("isEditSku", false);
		setAttr("goodsModelsList", goodsModelDao.list(false));
		setAttr("serviceList", labelDao.shopServiceLabelList(null));
		setAttr("columnList", labelService.selectByType(LabelEnum.SHOP_COLUMN_TYPE.getCode()));
		setAttr("roomList", roomService.selectAll());
		setAttr("accountList", accountService.selectShop());
		render(path + "/add.html");
	}

	@Before(SystemSubmitInterceptor.class)
	public void save() {
		String[] detailImageNames = getParaValues("detailImageNames");
		String[] goodsImageNames = getParaValues("goodsImageNames");
		ArrayList<String> detailImageList = UploadUtil.uploadImgsByNames(this, "/goods", detailImageNames);
		ArrayList<String> goodsImageList = UploadUtil.uploadImgsByNames(this, "/goods", goodsImageNames);
		String thumbnail = UploadUtil.uploadImg(this, "thumbnail", "/image");
		Integer columnId = getParaToInt("columnId");
		int skuNum = getParaToInt("skuNum", 0);
		List<HashMap<String, Object>> skuList = new ArrayList<>();
		for (int i = 0; i < skuNum; i++) {
			Integer[] attr = getParaValuesToInt("attr" + i);
			String attrIds = JMToolString.reSplit(attr, ",");
			String name = getPara("name" + i);
			HashMap<String, Object> skuMap = new HashMap<>();
			skuMap.put("attrids", attrIds);
			skuMap.put("name", name);
			skuList.add(skuMap);
		}
		Goods goods = getModel(Goods.class);
		Integer roomId = goods.getRoomId();
		if(new Integer(-1).equals(roomId)) {
			goods.remove("roomId");
		}
		goods.setCreateTime(new Date());
		goods.setThumbnail(thumbnail);
		Account goodsAccount = accountService.selectById(goods.getUserId());
		if (goodsAccount != null) {
			goods.setAccountType(goodsAccount.getType());
		}
		goodsDao.setGoodsRoomName(goods);
		if (goodsDao.save(goods)) {
			labelService.updateShopColumn(goods.getId(), columnId);
			imgService.inserImages(goods.getId(), detailImageList, goodsImageList);
			skuDao.add(goods.getId(), skuList);
			JMResult.success(this, "创建成功");
		} else {
			JMResult.fail(this, "创建失败");
		}
	}

	public void edit() {
		setToken();
		int id = getParaToInt("id");

		Goods goods = goodsDao.getById(id);
		List<Label> labelList = labelDao.all(JMLabelDao.TYPE_SHOP);
		setAttr("detailImageList", imgService.selectByTypeAndIds(id, ImgEnum.GOODS_DETAILS_TYPE));
		setAttr("goodsImageList", imgService.selectByTypeAndIds(id, ImgEnum.GOODS_OWNER_TYPE));
		setAttr("labelList", labelList);
		setAttr("skuAttrIds", skuAttrDao.listByGoodsId(id));
		setAttr("skuList", skuDao.listSKuByGoodsId(id));
		setAttr("specList", skuDao.listByGoodsId(id));
		setAttr("isEditSku", true);
		Integer labelId = goods.getLabelId();
		if (labelId != null) {
			setAttr("label", labelId);
		} else {
			setAttr("label", new Label());
		}
		setAttr("columnList", labelService.selectByType(LabelEnum.SHOP_COLUMN_TYPE.getCode()));
		setAttr("roomList", roomService.selectAll());
		setAttr("serviceList", labelDao.shopServiceLabelList(id));
		setAttr("column", labelRelationDao.getByIdsAndType(LabelEnum.SHOP_COLUMN_TYPE, id));
		setAttr("goods", goods);
		setAttr("isEdit", true);
		setAttr("goodsModelsList", goodsModelDao.list(false));
		setAttr("accountList", accountService.selectShop());
		// setAttr("freightTemplateList",
		// freightService.selectTemplateList(goods.getTemplateId()));//运费列表
		render(path + "/edit.html");
	}
	
	public void editBroadcastGood() {
		setToken();
		int id = getParaToInt("id");

		Goods goods = goodsDao.getById(id);
		List<Label> labelList = labelDao.all(JMLabelDao.TYPE_SHOP);
		setAttr("detailImageList", imgService.selectByTypeAndIds(id, ImgEnum.GOODS_DETAILS_TYPE));
		setAttr("goodsImageList", imgService.selectByTypeAndIds(id, ImgEnum.GOODS_OWNER_TYPE));
		setAttr("labelList", labelList);
		setAttr("skuAttrIds", skuAttrDao.listByGoodsId(id));
		setAttr("skuList", skuDao.listSKuByGoodsId(id));
		setAttr("specList", skuDao.listByGoodsId(id));
		setAttr("isEditSku", true);
		Integer labelId = goods.getLabelId();
		if (labelId != null) {
			setAttr("label", labelId);
		} else {
			setAttr("label", new Label());
		}
		setAttr("columnList", labelService.selectByType(LabelEnum.SHOP_COLUMN_TYPE.getCode()));
		setAttr("roomList", roomService.selectAll());
		setAttr("serviceList", labelDao.shopServiceLabelList(id));
		setAttr("column", labelRelationDao.getByIdsAndType(LabelEnum.SHOP_COLUMN_TYPE, id));
		setAttr("goods", goods);
		setAttr("isEdit", true);
		setAttr("goodsModelsList", goodsModelDao.list(false));
		setAttr("accountList", accountService.selectShop());
		// setAttr("freightTemplateList",
		// freightService.selectTemplateList(goods.getTemplateId()));//运费列表
		render(path + "/editBroadcastGood.html");
	}

	public void update() {
		String[] detailImageNames = getParaValues("detailImageNames");
		String[] goodsImageNames = getParaValues("goodsImageNames");
		String[] detailImages = getParaValues("detailImages");
		String[] goodsImages = getParaValues("goodsImages");
		Integer nowSkuNum = getParaToInt("nowSkuNum", 0);
		ArrayList<String> detailImageList = UploadUtil.uploadImgsByNames(this, "/goods", detailImageNames);
		ArrayList<String> goodsImageList = UploadUtil.uploadImgsByNames(this, "/goods", goodsImageNames);
		String thumbnail = UploadUtil.uploadImg(this, "thumbnail", "/image");
		Integer columnId = getParaToInt("columnId");
		// Integer[] servicesIds = getParaValuesToInt("serviceList");
		int skuNum = getParaToInt("oldSkuNum", 0);
		Goods goods = getModel(Goods.class);
		Integer roomId = goods.getRoomId();
		if(new Integer(-1).equals(roomId)) {
			goods.setRoomId(null);
		}
		for (int i = 0; i < skuNum; i++) {
			int skuId = getParaToInt("skuId" + i);
			String name = getPara("name" + i);
			Sku sku = skuDao.getById(skuId, false);
			if (sku == null) {
				continue;
			}
			sku.setName(name);
			skuDao.update(sku);
		}
		List<HashMap<String, Object>> skuList = new ArrayList<>();

		for (int i = 0; i < nowSkuNum; i++) {
			Integer[] attr = getParaValuesToInt("attr" + i);
			String attrIds = JMToolString.reSplit(attr, ",");
			String name = getPara("nowName" + i);
			HashMap<String, Object> skuMap = new HashMap<>();
			skuMap.put("attrids", attrIds);
			skuMap.put("name", name);
			skuList.add(skuMap);
		}
		skuDao.add(goods.getId(), skuList);

		// }
		if (StrKit.notBlank(thumbnail)) {
			goods.setThumbnail(thumbnail);
		}
		Account goodsAccount = accountService.selectById(goods.getUserId());
		if (goodsAccount != null) {
			goods.setAccountType(goodsAccount.getType());
		}
		goodsDao.setGoodsRoomName(goods);
		if (goodsDao.update(goods)) {
			labelService.updateShopColumn(goods.getId(), columnId);
			imgService.deleteGoodsImage(detailImages, goodsImages, goods.getId());
			imgService.inserImages(goods.getId(), detailImageList, goodsImageList);
			// labelRelationDao.editServiceList(servicesIds,goods.getId());//修改服务关系
			JMResult.success(this, "修改成功");
		} else {
			JMResult.fail(this, "修改失败");
		}
	}

	public void upload() {
		List<String> imageList = JMUploadKit.batchUploadImg(6, this, "img", "goods/img");
		HashMap<String, Object> data = new HashMap<>();
		data.put("imageList", imageList);
		JMResult.success(this, data, "上传成功");
	}

	/**
	 * 
	 * @date 2019年2月14日 上午11:36:32
	 * @author JaysonLee
	 * @Description: 冻结和解冻
	 * @reqMethod post
	 * @paramter
	 * @pDescription
	 *
	 */
	public void fz() {
		int id = getParaToInt("id");
		Goods goods = goodsDao.getById(id);
		if (goods != null) {
			goods.setState(goods.getState().equals(BasicsInformation.STATUS_FROZEN)? BasicsInformation.STATUS_NORMAL : BasicsInformation.STATUS_FROZEN);
			goodsDao.update(goods);
			JMResult.success(this, "操作成功");
		} else {
			JMResult.fail(this, "操作失败");
		}
	}

	/**
	 * 
	 * @date 2019年2月14日 下午2:43:21
	 * @author JaysonLee
	 * @Description: 批量下架
	 * @reqMethod post
	 * @paramter
	 * @pDescription
	 *
	 */
	public void fzs() {
		Integer[] ids = getParaValuesToInt("ids");
		if (ids != null) {
			String idString = JMToolString.reSplit(ids, ",");
			boolean result = goodsDao.fzsbyIds(idString, ids.length);
			if (result) {
				JMResult.success(this, "下架成功");
			} else {
				JMResult.fail(this, "下架失败");
			}
		} else {
			JMResult.fail(this, "请选择需要操作的数据");
		}
	}

	/**
	 * 
	 * @date 2019年2月14日 下午2:43:35
	 * @author JaysonLee
	 * @Description: 批量上架
	 * @reqMethod post
	 * @paramter
	 * @pDescription
	 *
	 */
	public void unfzs() {
		Integer[] ids = getParaValuesToInt("ids");
		if (ids != null) {
			String idString = JMToolString.reSplit(ids, ",");
			boolean result = goodsDao.unfzByIds(idString, ids.length);
			if (result) {
				JMResult.success(this, "上架成功");
			} else {
				JMResult.fail(this, "上架失败");
			}
		} else {
			JMResult.fail(this, "请选择需要操作的数据");
		}
	}

	/**
	 * 
	 * @date 2019年3月18日 下午4:35:36
	 * @author JaysonLee
	 * @Description: 批量设置热门
	 * @reqMethod post
	 * @paramter
	 * @pDescription
	 *
	 */
	public void setHots() {
		Integer[] ids = getParaValuesToInt("ids");
		int state = getParaToInt("state", 0);
		if (ids != null) {
			String idString = JMToolString.reSplit(ids, ",");
			boolean result = goodsDao.setHotByIds(idString, ids.length, state);
			if (result) {
				JMResult.success(this, "设置成功");
			} else {
				JMResult.fail(this, "设置失败");
			}
		} else {
			JMResult.fail(this, "请选择需要操作的数据");
		}
	}

	/**
	 * 
	 * @date 2019年3月18日 下午4:35:46
	 * @author JaysonLee
	 * @Description: 批量设置推荐
	 * @reqMethod post
	 * @paramter
	 * @pDescription
	 *
	 */
	public void setRecommend() {
		Integer[] ids = getParaValuesToInt("ids");
		int state = getParaToInt("state", 0);
		if (ids != null) {
			String idString = JMToolString.reSplit(ids, ",");
			boolean result = goodsDao.setRecommendByIds(idString, ids.length, state);
			if (result) {
				JMResult.success(this, "设置成功");
			} else {
				JMResult.fail(this, "设置失败");
			}
		} else {
			JMResult.fail(this, "请选择需要操作的数据");
		}
	}

	public void delete() {
		int id = getParaToInt("id");

		if (goodsDao.deleteById(id)) {
			JMResult.success(this, "删除成功");
		} else {
			JMResult.fail(this, "删除失败");
		}
	}

	public void dels() {
		Integer[] ids = getParaValuesToInt("ids");
		if (ids != null) {
			boolean result = goodsDao.deleteByIds(ids);
			if (result) {
				JMResult.success(this, "删除成功");
			} else {
				JMResult.fail(this, "删除失败");
			}
		} else {
			JMResult.fail(this, "请选择需要删除的数据");
		}
	}

	public void listSpec() {
		List<Spec> list = specDao.listSpec();
		JMResult.success(this, list, "获取成功");
	}

	public void listSKUAttrs() {
		String specAttrs[] = getParaValues("specAttrs");
		List<Spec> list = specDao.listByAttrs(JMToolString.reSplit(specAttrs, ","));
		List<List<SpecAttribute>> list2 = new ArrayList<>();
		for (Spec spec : list) {
			List<SpecAttribute> list3 = specAttributeDao.listAttrsBySpecIdAndAttrs(spec.getId(),
					JMToolString.reSplit(specAttrs, ","));
			list2.add(list3);
		}
		HashMap<String, Object> data = new HashMap<>();
		data.put("specList", list);
		data.put("attrList", list2);
		JMResult.success(this, data, "获取成功");
	}

	/**
	 * 
	 * @date 2019年4月13日 上午10:37:30
	 * @author JaysonLee
	 * @Description: 评论列表
	 * @reqMethod post
	 * @paramter
	 * @pDescription
	 *
	 */
	public void starspage() {
		int goodsId = getParaToInt("id");
		String keyword = getPara("keyword", "");
		Integer pageNumber = getParaToInt(0, 1);
		Integer pageSize = JMConsts.pageSize;
		String startTime = getPara("startTime", "");
		String endTime = getPara("endTime", "");
		Page<GoodsStar> page = goodsStarDao.pageForSys(goodsId, pageSize, pageNumber, keyword, startTime, endTime);
		setAttr("goodsId", goodsId);
		setAttr("keyword", keyword);
		setAttr("startTime", startTime);
		setAttr("endTime", endTime);
		setAttr("page", page);
		setAttr("goodsInfo", goodsDao.getById(goodsId, false));
		jump();
		render(path + "/goodsstar/page.html");
	}

	public void uploadVideo() {
		JMResultUtil.success(UploadUtil.uploadVideo(this, "videoFile")).renderJson(this);
	}

	public void deleteGoods() {
		Integer id = getParaToInt("id");
		goodsDao.deleteGoodsById(id);
		JMResult.success(this, "删除成功");
	}
	
}