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
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;

/**
 * 计费策略模版详情历史DAO接口
 * @author CYC
 * @version 2016-02-23
 */
public interface ConfigChargeAgentBoundConfigAppDao extends ConfigChargeAgentBoundConfigAppDaoCustom, CrudRepository<ConfigChargeAgentBoundConfigApp, Long> {

	@Modifying
	@Query("update ConfigChargeAgentBoundConfigProduct set delFlag='" + ConfigChargeAgentBoundConfigProduct.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	

	@Modifying
	@Query("delete ConfigChargeAgentBoundConfigProduct where agent.id = ?1")
	public int deleteByAgentId(Long agentId);
	
	
	
}

/**
 * DAO自定义接口
 * @author CYC
 */
interface ConfigChargeAgentBoundConfigAppDaoCustom extends BaseDao<ConfigChargeAgentBoundConfigApp> {

}

/**
 * DAO自定义接口实现
 * @author CYC
 */
@Component
class ConfigChargeAgentBoundConfigAppDaoImpl extends BaseDaoImpl<ConfigChargeAgentBoundConfigApp> implements ConfigChargeAgentBoundConfigAppDaoCustom {

}
