/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentHistory;

/**
 * 计费策略模版详情历史DAO接口
 * @author HuHao
 * @version 2015-07-16
 */
public interface ConfigChargeAgentHistoryDao extends ConfigChargeAgentHistoryDaoCustom, CrudRepository<ConfigChargeAgentHistory, Long> {

//	@Modifying
//	@Query("update ConfigChargeAgentHistory set delFlag='" + ConfigChargeAgentHistory.DEL_FLAG_DELETE + "' where id = ?1")
//	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface ConfigChargeAgentHistoryDaoCustom extends BaseDao<ConfigChargeAgentHistory> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class ConfigChargeAgentHistoryDaoImpl extends BaseDaoImpl<ConfigChargeAgentHistory> implements ConfigChargeAgentHistoryDaoCustom {

}
