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
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;

/**
 * 计费策略模版关联业务表DAO接口
 * @author HuHao
 * @version 2015-07-27
 */
public interface ConfigAgentBoundDealInfoDao extends ConfigAgentBoundDealInfoDaoCustom, CrudRepository<ConfigAgentBoundDealInfo, Long> {

	@Modifying
	@Query("update ConfigAgentBoundDealInfo set delFlag='" + ConfigAgentBoundDealInfo.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface ConfigAgentBoundDealInfoDaoCustom extends BaseDao<ConfigAgentBoundDealInfo> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class ConfigAgentBoundDealInfoDaoImpl extends BaseDaoImpl<ConfigAgentBoundDealInfo> implements ConfigAgentBoundDealInfoDaoCustom {

}
