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
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;

/**
 * RA配置DAO接口
 * @author ZhangJingtao
 * @version 2014-06-09
 */
public interface ConfigRaAccountDao extends ConfigRaAccountDaoCustom, CrudRepository<ConfigRaAccount, Long> {

	@Modifying
	@Query("delete ConfigRaAccount where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface ConfigRaAccountDaoCustom extends BaseDao<ConfigRaAccount> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class ConfigRaAccountDaoImpl extends BaseDaoImpl<ConfigRaAccount> implements ConfigRaAccountDaoCustom {

}
