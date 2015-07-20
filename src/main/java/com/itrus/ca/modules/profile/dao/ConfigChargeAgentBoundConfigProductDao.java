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
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;

/**
 * 计费策略模版详情历史DAO接口
 * @author HuHao
 * @version 2015-07-17
 */
public interface ConfigChargeAgentBoundConfigProductDao extends ConfigChargeAgentBoundConfigProductDaoCustom, CrudRepository<ConfigChargeAgentBoundConfigProduct, Long> {

	@Modifying
	@Query("update ConfigChargeAgentBoundConfigProduct set delFlag='" + ConfigChargeAgentBoundConfigProduct.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	

	@Modifying
	@Query("delete ConfigChargeAgentBoundConfigProduct where agent.id = ?1")
	public int deleteByAgentId(Long agentId);
	
	
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface ConfigChargeAgentBoundConfigProductDaoCustom extends BaseDao<ConfigChargeAgentBoundConfigProduct> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class ConfigChargeAgentBoundConfigProductDaoImpl extends BaseDaoImpl<ConfigChargeAgentBoundConfigProduct> implements ConfigChargeAgentBoundConfigProductDaoCustom {

}
