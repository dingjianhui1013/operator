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
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.sys.entity.SysConfig;

/**
 * RA配置详情DAO接口
 * @author ZhangJingtao
 * @version 2014-06-10
 */
public interface ConfigAgentOfficeRelationDao extends ConfigAgentOfficeRelationDaoCustom, CrudRepository<ConfigAgentOfficeRelation, Long> {
	
	@Modifying
	@Query("DELETE FROM ConfigAgentOfficeRelation c WHERE c.configCommercialAgent.id=?1")
	public void deleteByRaletionId(Long id);


}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface ConfigAgentOfficeRelationDaoCustom extends BaseDao<ConfigAgentOfficeRelation> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class ConfigAgentOfficeRelationDaoImpl extends BaseDaoImpl<ConfigAgentOfficeRelation> implements ConfigAgentOfficeRelationDaoCustom {

}
