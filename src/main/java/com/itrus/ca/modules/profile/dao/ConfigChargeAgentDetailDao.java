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
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;

/**
 * 供应商计费type配置DAO接口
 * @author ZhangShaoBo
 * @version 2014-06-11
 */
public interface ConfigChargeAgentDetailDao extends ConfigChargeAgentDetailDaoCustom, CrudRepository<ConfigChargeAgentDetail, Long> {

	@Modifying
	@Query("delete ConfigChargeAgentDetail c where c.configChargeAgent.id=?1")
	public void deleteByAgentId(Long id);

}

/**
 * DAO自定义接口
 * @author ZhangShaoBo
 */
interface ConfigChargeAgentDetailDaoCustom extends BaseDao<ConfigChargeAgentDetail> {

}

/**
 * DAO自定义接口实现
 * @author ZhangShaoBo
 */
@Component
class ConfigChargeAgentDetailDaoImpl extends BaseDaoImpl<ConfigChargeAgentDetail> implements ConfigChargeAgentDetailDaoCustom {

}
