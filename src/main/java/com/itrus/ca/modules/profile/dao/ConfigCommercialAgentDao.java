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
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;

/**
 * 代理商DAO接口
 * @author ZhangJingtao
 * @version 2014-06-08
 */
public interface ConfigCommercialAgentDao extends ConfigCommercialAgentDaoCustom, CrudRepository<ConfigCommercialAgent, Long> {

	@Modifying
	@Query("update ConfigCommercialAgent set del_flag=1 where id = ?1")
	public int deleteById(Long id);
	

}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface ConfigCommercialAgentDaoCustom extends BaseDao<ConfigCommercialAgent> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class ConfigCommercialAgentDaoImpl extends BaseDaoImpl<ConfigCommercialAgent> implements ConfigCommercialAgentDaoCustom {

}
