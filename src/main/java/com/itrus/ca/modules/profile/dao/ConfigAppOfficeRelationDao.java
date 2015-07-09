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
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 供应商产品配置DAO接口
 * @author HUHAO
 * @version 2014-06-11
 */
public interface ConfigAppOfficeRelationDao extends ConfigAppOfficeRelationDaoCustom, CrudRepository<ConfigAppOfficeRelation, Long> {

	@Modifying
	@Query("update ConfigAppOfficeRelation set delFlag='" + ConfigAppOfficeRelation.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Modifying
	@Query("delete from ConfigAppOfficeRelation  where id=?1")
	public int deleteByofficeId(Long id);
	
	@Modifying
	@Query("delete from ConfigAppOfficeRelation where sysOffice in ?1")
	public int deleteByoffices(List<Office> offices);
	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface ConfigAppOfficeRelationDaoCustom extends BaseDao<ConfigAppOfficeRelation> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class ConfigAppOfficeRelationDaoImpl extends BaseDaoImpl<ConfigAppOfficeRelation> implements ConfigAppOfficeRelationDaoCustom {

}
