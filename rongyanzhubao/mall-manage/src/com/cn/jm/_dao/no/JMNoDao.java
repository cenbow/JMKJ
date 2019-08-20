
package com.cn.jm._dao.no;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cn._gen.dao.NoDao;
import com.cn._gen.model.No;
import com.cn.jm.util.SqlUtil;
import com.jfinal.plugin.activerecord.Db;

public class JMNoDao extends NoDao{

    /**
     * 获取当前最大的值或者最小值,由于no值的最大或者最小会被获取,造成no重复,但是id不会,所以采用id
     * @return
     */
    public Long selectNumberByMethod(String method) {
        return Db.queryLong("SELECT " + method + "(id) FROM tb_no");
    }
 
    public void savesNo(Long minNo, Long maxNo) {
        StringBuilder saveSql = new StringBuilder("INSERT INTO tb_no(no) VALUES ");
        List <Long> valueList = new ArrayList<>();
        for(long no = minNo;no<maxNo;no++) {
            valueList.add(no);
        }
        Collections.shuffle(valueList);
        final char comma = ',';
        valueList.forEach(value -> saveSql.append(SqlUtil.LEFT_PARENTHESES).append(value).append(SqlUtil.RIGHT_PARENTHESES).append(comma));
        saveSql.setCharAt(saveSql.length()-1, ' ');
        Db.update(saveSql.toString());
    }
    
    
    public No selectByNo(Long no) {
        return dao.findFirst("SELECT * FROM tb_no WHERE no LIKE ?",no);
    }

	public No selectOne() {
		return dao.findFirst("SELECT * FROM `tb_no` LIMIT 1");
	}
	
}
