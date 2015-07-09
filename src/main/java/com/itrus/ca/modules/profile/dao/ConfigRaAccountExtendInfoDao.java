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
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;

/**
 * RA配置详情DAO接口
 * @author ZhangJingtao
 * @version 2014-06-09
 */
public interface ConfigRaAccountExtendInfoDao extends ConfigRaAccountExtendInfoDaoCustom, CrudRepository<ConfigRaAccountExtendInfo, Long> {

	@Modifying
	@Query("delete ConfigRaAccountExtendInfo where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface ConfigRaAccountExtendInfoDaoCustom extends BaseDao<ConfigRaAccountExtendInfo> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class ConfigRaAccountExtendInfoDaoImpl extends BaseDaoImpl<ConfigRaAccountExtendInfo> implements ConfigRaAccountExtendInfoDaoCustom {

}
