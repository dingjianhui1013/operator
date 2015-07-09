/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.key.entity.KeyManufacturer;
import com.itrus.ca.modules.profile.entity.ConfigApp;

/**
 * key厂家信息DAO接口
 * @author ZhangJingtao
 * @version 2014-06-08
 */
public interface KeyManufacturerDao extends KeyManufacturerDaoCustom, CrudRepository<KeyManufacturer, Long> {

/*	@Modifying
	@Query("delete KeyManufacturer where id = ?1")
	public int deleteById(Long id);
	*/
	@Modifying
	@Query("update KeyManufacturer set delFlag='" + KeyManufacturer.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface KeyManufacturerDaoCustom extends BaseDao<KeyManufacturer> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class KeyManufacturerDaoImpl extends BaseDaoImpl<KeyManufacturer> implements KeyManufacturerDaoCustom {

}
