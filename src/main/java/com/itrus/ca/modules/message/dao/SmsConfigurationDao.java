/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.message.entity.SmsConfiguration;

/**
 * 短信配置DAO接口
 * @author qt
 * @version 2015-11-27
 */
public interface SmsConfigurationDao extends SmsConfigurationDaoCustom, CrudRepository<SmsConfiguration, Long> {

	@Modifying
	@Query("update SmsConfiguration set delFlag='" + SmsConfiguration.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author qt
 */
interface SmsConfigurationDaoCustom extends BaseDao<SmsConfiguration> {

}

/**
 * DAO自定义接口实现
 * @author qt
 */
@Component
class SmsConfigurationDaoImpl extends BaseDaoImpl<SmsConfiguration> implements SmsConfigurationDaoCustom {

}
