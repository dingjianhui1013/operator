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
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;

/**
 * 供应商产品配置DAO接口
 * @author ZhangShaoBo
 * @version 2014-06-09
 */
public interface ConfigSupplierProductRelationDao extends ConfigSupplierProductRelationDaoCustom, CrudRepository<ConfigSupplierProductRelation, Long> {

	
}

/**
 * DAO自定义接口
 * @author ZhangShaoBo
 */
interface ConfigSupplierProductRelationDaoCustom extends BaseDao<ConfigSupplierProductRelation> {

}

/**
 * DAO自定义接口实现
 * @author ZhangShaoBo
 */
@Component
class ConfigSupplierProductRelationDaoImpl extends BaseDaoImpl<ConfigSupplierProductRelation> implements ConfigSupplierProductRelationDaoCustom {

}
