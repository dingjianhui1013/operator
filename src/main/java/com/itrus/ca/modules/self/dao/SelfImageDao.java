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
import com.itrus.ca.modules.self.entity.SelfImage;

/**
 * 证件图片DAO接口
 * @author HuHao
 * @version 2016-03-10
 */
public interface SelfImageDao extends SelfImageDaoCustom, CrudRepository<SelfImage, Long> {

	@Modifying
	@Query("update SelfImage set status='" + BaseEntity.DEL_FLAG + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface SelfImageDaoCustom extends BaseDao<SelfImage> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class SelfImageDaoImpl extends BaseDaoImpl<SelfImage> implements SelfImageDaoCustom {

}
