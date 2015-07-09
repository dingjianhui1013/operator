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
import com.itrus.ca.modules.profile.entity.ConfigSupplier;

/**
 * 供应商DAO接口
 * @author ZhangJingtao
 * @version 2014-06-08
 */
public interface ConfigSupplierDao extends ConfigSupplierDaoCustom, CrudRepository<ConfigSupplier, Long> {

	@Modifying
	@Query("update ConfigSupplier set delFlag="+ConfigSupplier.DEL_FLAG_DELETE+"  where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface ConfigSupplierDaoCustom extends BaseDao<ConfigSupplier> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class ConfigSupplierDaoImpl extends BaseDaoImpl<ConfigSupplier> implements ConfigSupplierDaoCustom {

}
