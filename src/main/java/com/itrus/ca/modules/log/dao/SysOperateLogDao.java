/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.log.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.log.entity.SysOperateLog;

/**
 * 后台日志DAO接口
 * @author ZhangJingtao
 * @version 2014-07-07
 */
public interface SysOperateLogDao extends SysOperateLogDaoCustom, CrudRepository<SysOperateLog, Long> {

	@Modifying
	@Query("update SysOperateLog set delFlag='" + SysOperateLog.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface SysOperateLogDaoCustom extends BaseDao<SysOperateLog> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class SysOperateLogDaoImpl extends BaseDaoImpl<SysOperateLog> implements SysOperateLogDaoCustom {

}
