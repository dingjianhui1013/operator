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
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyManufacturer;

/**
 * key类型信息DAO接口
 * @author ZhangJingtao
 * @version 2014-06-08
 */
public interface KeyGeneralInfoDao extends KeyGeneralInfoDaoCustom, CrudRepository<KeyGeneralInfo, Long> {

	@Modifying
	@Query("update KeyGeneralInfo  set delFlag='"+KeyGeneralInfo.DEL_FLAG_DELETE+"' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface KeyGeneralInfoDaoCustom extends BaseDao<KeyGeneralInfo> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class KeyGeneralInfoDaoImpl extends BaseDaoImpl<KeyGeneralInfo> implements KeyGeneralInfoDaoCustom {

}
