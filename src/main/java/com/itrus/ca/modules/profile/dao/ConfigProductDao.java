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
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.modules.profile.entity.ConfigProduct;

/**
 * 应用DAO接口
 * @author HuHao
 * @version 2014-06-03
 */
public interface ConfigProductDao extends ConfigProductDaoCustom, CrudRepository<ConfigProduct, Long> {

	@Modifying
	@Query("update ConfigProduct set delFlag='" + ConfigProduct.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface ConfigProductDaoCustom extends BaseDao<ConfigProduct> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class ConfigProductDaoImpl extends BaseDaoImpl<ConfigProduct> implements ConfigProductDaoCustom {

}
