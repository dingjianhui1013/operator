/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.work.entity.WorkCompany;

/**
 * 单位名称DAO接口
 * @author HUHAO
 * @version 2014-06-12
 */
public interface WorkCompanyDao extends WorkCompanyDaoCustom, CrudRepository<WorkCompany, Long> {

	@Modifying
	@Query("update WorkCompany set delFlag='" + WorkCompany.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Query("from WorkCompany where id=?1")
	public WorkCompany findById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface WorkCompanyDaoCustom extends BaseDao<WorkCompany> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class WorkCompanyDaoImpl extends BaseDaoImpl<WorkCompany> implements WorkCompanyDaoCustom {

}
