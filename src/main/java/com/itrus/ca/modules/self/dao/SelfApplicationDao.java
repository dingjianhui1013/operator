/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.self.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.modules.self.entity.SelfApplication;

/**
 * 申请表DAO接口
 * @author HuHao
 * @version 2016-03-10
 */
public interface SelfApplicationDao extends SelfApplicationDaoCustom, CrudRepository<SelfApplication, Long> {

	@Modifying
	@Query("update SelfApplication set status='" + BaseEntity.DEL_FLAG +"' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface SelfApplicationDaoCustom extends BaseDao<SelfApplication> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class SelfApplicationDaoImpl extends BaseDaoImpl<SelfApplication> implements SelfApplicationDaoCustom {

}
