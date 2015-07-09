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
import com.itrus.ca.modules.profile.entity.ConfigChargeSupplierDetail;

/**
 * 供应商计费配置DAO接口
 * @author ZhangShaoBo
 * @version 2014-06-11
 */
public interface ConfigChargeSupplierDetailDao extends ConfigChargeSupplierDetailDaoCustom, CrudRepository<ConfigChargeSupplierDetail, Long> {


	
}

/**
 * DAO自定义接口
 * @author ZhangShaoBo
 */
interface ConfigChargeSupplierDetailDaoCustom extends BaseDao<ConfigChargeSupplierDetail> {

}

/**
 * DAO自定义接口实现
 * @author ZhangShaoBo
 */
@Component
class ConfigChargeSupplierDetailDaoImpl extends BaseDaoImpl<ConfigChargeSupplierDetail> implements ConfigChargeSupplierDetailDaoCustom {

}
