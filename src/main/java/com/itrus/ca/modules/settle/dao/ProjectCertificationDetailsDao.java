/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.settle.entity.ProjectCertificationDetails;

/**
 * 项目发证明细DAO接口
 * @author qt
 * @version 2015-11-18
 */
public interface ProjectCertificationDetailsDao extends ProjectCertificationDetailsDaoCustom, CrudRepository<ProjectCertificationDetails, Long> {

	@Modifying
	@Query("update ProjectCertificationDetails set delFlag='" + ProjectCertificationDetails.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author qt
 */
interface ProjectCertificationDetailsDaoCustom extends BaseDao<ProjectCertificationDetails> {

}

/**
 * DAO自定义接口实现
 * @author qt
 */
@Component
class ProjectCertificationDetailsDaoImpl extends BaseDaoImpl<ProjectCertificationDetails> implements ProjectCertificationDetailsDaoCustom {

}
