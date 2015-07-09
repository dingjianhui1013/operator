/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.sys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.sys.entity.SysCrlContext;

/**
 * 信任源管理DAO接口
 * @author WangHongwei
 * @version 2014-06-03
 */
public interface SysCrlContextDao extends SysCrlContextDaoCustom, CrudRepository<SysCrlContext, Long> {

	@Modifying
	@Query("update SysCrlContext set delFlag='" + SysCrlContext.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author WangHongwei
 */
interface SysCrlContextDaoCustom extends BaseDao<SysCrlContext> {

}

/**
 * DAO自定义接口实现
 * @author WangHongwei
 */
@Component
class SysCrlContextDaoImpl extends BaseDaoImpl<SysCrlContext> implements SysCrlContextDaoCustom {

}
