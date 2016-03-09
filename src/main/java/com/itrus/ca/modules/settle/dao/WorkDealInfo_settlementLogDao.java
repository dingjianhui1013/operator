/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.settle.entity.WorkDealInfo_settlementLog;

/**
 * 年费结算保存DAO接口
 * @author DingJianHui
 * @version 2016-03-09
 */
public interface WorkDealInfo_settlementLogDao extends WorkDealInfo_settlementLogDaoCustom, CrudRepository<WorkDealInfo_settlementLog, Long> {

	@Modifying
	@Query("update WorkDealInfo_settlementLog set delFlag='" + WorkDealInfo_settlementLog.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author DingJianHui
 */
interface WorkDealInfo_settlementLogDaoCustom extends BaseDao<WorkDealInfo_settlementLog> {

}

/**
 * DAO自定义接口实现
 * @author DingJianHui
 */
@Component
class WorkDealInfo_settlementLogDaoImpl extends BaseDaoImpl<WorkDealInfo_settlementLog> implements WorkDealInfo_settlementLogDaoCustom {

}
