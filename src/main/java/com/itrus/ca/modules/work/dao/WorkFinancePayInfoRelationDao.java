/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkPayInfo;

/**
 * 支付信息统计报表DAO接口
 * @author HUHAO
 * @version 2014-06-16
 */
public interface WorkFinancePayInfoRelationDao extends WorkFinancePayInfoRelationDaoCustom, CrudRepository<WorkFinancePayInfoRelation, Long> {
	@Modifying
	@Query("delete WorkFinancePayInfoRelation where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface WorkFinancePayInfoRelationDaoCustom extends BaseDao<WorkFinancePayInfoRelation> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class WorkFinancePayInfoRelationDaoImpl extends BaseDaoImpl<WorkFinancePayInfoRelation> implements WorkFinancePayInfoRelationDaoCustom {

}
