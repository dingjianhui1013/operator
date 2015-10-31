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
import com.itrus.ca.modules.profile.entity.ConfigProjectType;

/**
 * 项目类型管理DAO接口
 * @author HuHao
 * @version 2015-10-31
 */
public interface ConfigProjectTypeDao extends ConfigProjectTypeDaoCustom, CrudRepository<ConfigProjectType, Long> {

	@Modifying
	@Query("update ConfigProjectType set delFlag='" + ConfigProjectType.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface ConfigProjectTypeDaoCustom extends BaseDao<ConfigProjectType> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class ConfigProjectTypeDaoImpl extends BaseDaoImpl<ConfigProjectType> implements ConfigProjectTypeDaoCustom {

}
