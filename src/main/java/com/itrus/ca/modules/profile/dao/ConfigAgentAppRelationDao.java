/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 代理商业务关联DAO接口
 * @author WangHongwei
 * @version 2014-06-10
 * 
 */
public interface ConfigAgentAppRelationDao extends ConfigAgentAppRelationDaoCustom, CrudRepository<ConfigAgentAppRelation, Long> {
	@Modifying
	@Query("DELETE FROM ConfigAgentAppRelation c WHERE c.configCommercialAgent.id=?1")
	public void deleteByRaletionId(Long id);
}

/**
 * DAO自定义接口
 * @author WangHongwei
 */
interface ConfigAgentAppRelationDaoCustom extends BaseDao<ConfigAgentAppRelation> {

}

/**
 * DAO自定义接口实现
 * @author WangHongwei
 */
@Component
class ConfigAgentAppRelationDaoImpl extends BaseDaoImpl<ConfigAgentAppRelation> implements ConfigAgentAppRelationDaoCustom {

}
