package com.cn.jm._dao.label;

import java.util.HashMap;
import java.util.List;

import com.cn._gen.dao.LabelRelationDao;
import com.cn._gen.model.Label;
import com.cn._gen.model.LabelRelation;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.util.sqltool.JMCommonDao;
import com.cn.jm.util.sqltool.Query;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;


/**
 * 
 * @author 李劲
 *
 * 2017年10月3日 下午3:13:26
 */
public class JMLabelRelationDao extends LabelRelationDao {

	private JMCommonDao commonDao = JMCommonDao.jmd;
	/**
	 * 保存
	 * @param labelId
	 * @param ids
	 * @param jmDataType
	 * @return
	 */
	public JMResult save(Integer labelId,Integer ids,String jmDataType){
		if(labelId == null || ids == null || jmDataType == null)
			return JMResult.create().fail("labelId/ids/jmDataType属性不能为空");
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("labelId", labelId);
		param.put("ids", ids);
		param.put("jmDataType", jmDataType);
		LabelRelation relation = get(param,true);
		if(relation != null)
			return JMResult.create().fail("关系已存在,不能重复绑定");
		relation = new LabelRelation();
		relation.setLabelId(labelId);
		relation.setIds(ids);
		if(relation.save())
			return JMResult.create().success(relation, "创建成功");
		else
			return JMResult.create().fail("创建失败");
	}
	
	
	
	/**
	 * 某内容的标签列表
	 * @param pageNumber
	 * @param pageSize
	 * @param select
	 * @param ids
	 * @param jmDataType
	 * @return
	 */
	public Page<Label> page(int pageNumber,int pageSize,String select, Integer ids,String jmDataType){
		String sql = "FROM tb_label AS a LEFT JOIN tb_label_relation AS b ON a.id = b.userId WHERE b.jmDataType = ? and b.ids = ? ORDER BY a.id DESC";
		return Label.dao.paginate(pageNumber, pageSize, select, sql,jmDataType,ids);
	}
	
	
	
	public LabelRelation getByLabelId(long labelId){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("labelId", labelId);
		return get(param,true);
	}
	
	public LabelRelation getByIds(int ids, String jmDataType){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		param.put("jmDataType", jmDataType);
		return get(param,true);
	}
	
	/**
	 * 
	 * @date 2019年6月21日 下午6:20:12
	 * @author JaysonLee
	 * @Description: 新增服务标签关系
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 * @param ids
	 * @param goodsId
	 *
	 */
	public void addServiceList(Integer ids[],int goodsId) {
		if(ids == null) {
			return ;
		}
		for(Integer sid : ids) {
			HashMap<String,Object> paraMap = new HashMap<>();
			paraMap.put("labelId",sid);
			paraMap.put("ids",goodsId);
			LabelRelation labelRelation = get(paraMap, false);
			if(labelRelation != null) {
				continue ;
			}
			labelRelation = new LabelRelation();
			labelRelation.setLabelId(sid);
			labelRelation.setIds(goodsId);
			save(labelRelation);
		}
	}
	
	/**
	 * 
	 * @date 2019年6月21日 下午6:32:57
	 * @author JaysonLee
	 * @Description: 修改服务标签关系
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 * @param ids
	 * @param goodsId
	 *
	 */
	public void editServiceList(Integer ids[],int goodsId) {
		Db.update("delete from tb_label_relation where labelId in (select id from tb_label where type = 4) and ids=?",goodsId);//删除旧的
		if(ids != null)
			addServiceList(ids, goodsId);
	}
	
	
	public List<LabelRelation> selectRelationList(LabelEnum labelEnum,Integer ids){
		Query query = new Query();
		query.put("and l.type = ", labelEnum.getCode(), false);
		query.put("and lr.ids =", ids, false);
		String select = "select lr.*,l.name";
		String inner = "lr inner join tb_label l on lr.labelId = l.id";
		return commonDao.selectList(LabelRelation.class, query, select ,inner);
	}
	
	/**
	 * 删除ids的关联标签
	 */
	public void delRelationList(LabelEnum labelEnum,Integer ids) {
		String sql = "delete from tb_label_relation a inner join tb_label b"
				+ " on a.labelId = b.id where a.ids = ? and b.type = ?";
		Db.update(sql, ids ,labelEnum.getCode());
	}

	/**
	 * 
	 * @param labelEnum
	 * @param ids
	 * @return
	 */
	public LabelRelation getByIdsAndType(LabelEnum labelEnum, Integer ids) {
		return dao.findFirst("SELECT tlr.id,tlr.ids,tlr.labelId FROM `tb_label` tl INNER JOIN `tb_label_relation` tlr  ON tl.id = tlr.labelId AND tl.type = ? WHERE tlr.ids = ?", labelEnum.getCode(), ids);
	}
}
