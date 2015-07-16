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
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetailHistory;

/**
 * 计费策略模版详情历史DAO接口
 * @author HuHao
 * @version 2015-07-16
 */
public interface ConfigChargeAgentDetailHistoryDao extends ConfigChargeAgentDetailHistoryDaoCustom, CrudRepository<ConfigChargeAgentDetailHistory, Long> {

//	@Modifying
//	@Query("update ConfigChargeAgentDetailHistory set delFlag='" + ConfigChargeAgentDetailHistory.DEL_FLAG_DELETE + "' where id = ?1")
//	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface ConfigChargeAgentDetailHistoryDaoCustom extends BaseDao<ConfigChargeAgentDetailHistory> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class ConfigChargeAgentDetailHistoryDaoImpl extends BaseDaoImpl<ConfigChargeAgentDetailHistory> implements ConfigChargeAgentDetailHistoryDaoCustom {

}
