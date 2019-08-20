package com.cn.jm._dao.label;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cn._gen.dao.LabelDao;
import com.cn._gen.model.Label;
import com.cn._gen.model.LabelRelation;
import com.cn.jm.core.common.JMCommonDao;
import com.cn.jm.core.common.Query;
import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.util.SqlUtil;
import com.jfinal.aop.Aop;
import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

/**
 * 标签
 *
 * @author 李劲
 * @创建时间：2017年7月12日 上午7:07:39
 */
public class JMLabelDao extends LabelDao {
	
	public static final int TYPE_SYSYEM = 0;//系统默认
	public static final int TYPE_ARTICLE = 1;//文章
	public static final int TYPE_SHOP = 2;//商品
	public static final int TYPE_USER = 3;//用户
	
	private JMLabelRelationDao labelRelationDao = Aop.get(JMLabelRelationDao.class);
	
	public JMCommonDao commonDao = JMCommonDao.jmd;
	
	public List<Label> list(HashMap<String, Object> param) {
		return list("", param, "sort", DESC, true);
	}
	
	/**
	 * 保存
	 * @param name 名称
	 * @param image 图片相对路径
	 * @param desc 描述
	 * @param parent 父标签Id
	 * @param series 层级
	 * @param sort 排序
	 * @param url 第三方超链接
	 */
	public JMResult save(String name,String image,String desc,Integer parent,Integer series,Integer sort,String url){
		if(JMToolString.isEmpty(name))
			return JMResult.create().fail("标签名称不能为空");
		Label label = new Label();
		label.setName(name);
		if(JMToolString.isNotEmpty(image))
			label.setImage(image);
		if(JMToolString.isNotEmpty(desc))
			label.setDesc(desc);
		if(parent != null)
			label.setParent(parent);
		if(series != null)
			label.setSeries(series);
		if(sort != null)
			label.setSort(sort);
		if(JMToolString.isNotEmpty(url))
			label.setUrl(url);
		if(label.save())
			return JMResult.create().success(label,"创建成果");
		else
			return JMResult.create().fail("创建失败");
	}
	
	
	/**
	 * 系统默认的标签
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Label> pageBySystem(int pageNumber, int pageSize){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("type", 0);
		return page(pageNumber, pageSize, "", param, "id", DESC, false);
	}
	
	/**
	 * 用户创建的标签列表
	 * @param pageNumber
	 * @param pageSize
	 * @param accountId
	 * @return
	 */
	public Page<Label> pageByAccount(int pageNumber, int pageSize,Integer accountId){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("type", 1);
		param.put("accountId", accountId);
		return page(pageNumber, pageSize, "", param, "id", DESC, false);
	}
	
	
	/**
	 * 获取第一层菜单
	 * @return
	 */
	public List<Label> one(int type){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("series", 0);
		param.put("type", type);
		List<Label> labelList = list(param);
		return labelList;
	} 
	
	/**
	 * 获取某栏目第二层栏目
	 * @return
	 */
	public List<Label> second(int labelId,int type){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parent", labelId);
		param.put("series", 1);
		param.put("type", type);
		List<Label> labelList = list(param);
		return labelList;
	} 
	
	/**
	 * 获取所有菜单
	 * @return
	 */
	public List<Label> all(int type){
		List<Label> labelList = one(type);
		next(labelList);
		return labelList;
	}
	
	public List<Label> all(int parent,int type){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parent", parent);
		param.put("type", type);
		List<Label> labelList = list(param);
		next(labelList);
		return labelList;
	}
	
	/**
	 * 获取下一层的菜单
	 * @param parent
	 * @return
	 */
	public List<Label> sublabelList(long labelId,int series){
		HashMap<String, Object> param = new HashMap<String, Object>();
		if(labelId != -1){
			param.put("parent", labelId);
		}
		param.put("series", series);
		List<Label> list = list(param);
		return list;
	}
	
	private void next(List<Label> labelList){
		for(Label label : labelList){
			List<Label> nextList = sublabelList(label.getId(), label.getSeries()+1);
			label.put("nextList", nextList);
			if(!nextList.isEmpty()){
				next(nextList);
			}
		}
	}
	
	/**
	 * 删除菜单，含所有子菜单
	 * @param series
	 * @param id
	 */
	public void delete(int id){
		List<Label> list = new ArrayList<Label>();
		nextByDel(list, id);
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				deleteById(id);
				if(!list.isEmpty()){
					delete(list);
					List<LabelRelation> labelRelationList = new ArrayList<LabelRelation>();
					
					LabelRelation lr = labelRelationDao.getByLabelId(id);
					if(lr != null){
						labelRelationList.add(lr);
					}
					for (int i = 0; i < list.size(); i++) {
						LabelRelation lr2 = labelRelationDao.getByLabelId(list.get(i).getId());
						if(lr2 != null){
							labelRelationList.add(lr2);
						}
					}
					if(!labelRelationList.isEmpty() && labelRelationList != null){
						labelRelationDao.delete(labelRelationList);
					}
				}
				return true;
			}
		});
	}
	
	public void deleteByAll(Integer[] ids) {
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				for (int i = 0; i < ids.length; i++) {
					delete(ids[i]);
				}
				return true;
			}
		});
	}
	
	
	private void nextByDel(List<Label> list,long parent){
		List<Label> mList = next(parent);
		if(mList.size() > 0){
			list.addAll(mList);
			for (int i = 0; i < mList.size(); i++) {
				Label label = mList.get(i);
				nextByDel(list, label.getId());
			}
			
		}
	}
	
	/**
	 * 所有栏目含cl
	 * @return
	 */
	public List<Label> allCl(int type){
		List<Label> mList = new ArrayList<Label>();
		List<Label> list = one(type);
		for (int i = 0; i < list.size(); i++) {
			Label cl = list.get(i);
			cl.put("clName", cl.getName());
			mList.add(cl);
			nextCl(cl,mList);
		}
		return mList;	
	}
	
	/**
	 * 获取下一层所有菜单
	 */
	public List<Label> allByParent(int parent, int series){
		List<Label> mList = new ArrayList<Label>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("series", series);
		param.put("parent", parent);
		List<Label> list = list(param);
		for (int i = 0; i < list.size(); i++) {
			Label cl = list.get(i);
			cl.put("clName", cl.getName());
			mList.add(cl);
			nextCl(cl,mList);
		}
		return mList;
	}
	
	/**
	 * 某标签下一级标签列表
	 * @param parent
	 * @return
	 */
	public List<Label> next(long parent){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parent", parent);
		return list(param);
	}
	
	private void nextCl(Label label,List<Label> labelList){
		List<Label> list = sublabelList(label.getId(),label.getSeries()+1);
		for (int i = 0; i < list.size(); i++) {
			Label cl = list.get(i);
			cl.put("clName",label.get("clName")+"_" + cl.getName());
			labelList.add(cl);
			nextCl(cl,labelList);
		}
	}
	
	/**
	 * 
	 * @date 2019年6月21日 下午5:54:40
	 * @author JaysonLee
	 * @Description: TODO
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 * @param goodsId
	 * @return
	 *
	 */
	public List<Label> shopServiceLabelList(Integer goodsId){
		Query query = new Query();
		query.put("and l.type=", 4);
		String leftJoin = "l left join tb_label_relation lr on l.id = lr.labelId ";
		if(goodsId != null)
			leftJoin += "and lr.ids="+goodsId;
		List<Label> list = commonDao.selectList(Label.class, query,"select l.*,lr.id as isChecked",
				leftJoin);
		return list ;
	}

	public Page<Label> pageColumn(Integer pageNumber, Integer pageSize,Integer id,String keyword,String startTime,String endTime,boolean isCache){
		StringBuilder selectSql = new StringBuilder("SELECT * ");
		StringBuilder fromSql = new StringBuilder(" FROM ");
		fromSql.append(tableName);
		fromSql.append(" WHERE (`type` = 4 OR `type` = 5) ");
		SqlUtil.addWhere(fromSql," AND id = ",id);
		SqlUtil.addLike(fromSql," AND name","%",keyword,"%");
		SqlUtil.addBetweenTime(fromSql, startTime, endTime, " AND createTime");
		SqlUtil.changeWhere(fromSql);
		String strFromSql = SqlUtil.getSql(fromSql);
		String strSelectSql = selectSql.toString();
		return isCache?dao.paginateByCache(cacheName,HashKit.md5(selectSql.append(strFromSql).toString()), pageNumber, pageSize,false,strSelectSql, strFromSql)
			:dao.paginate(pageNumber, pageSize, false, strSelectSql,strFromSql);
	}
	
}
