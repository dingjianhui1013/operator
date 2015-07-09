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
import com.itrus.ca.modules.log.entity.TerminalLog;

/**
 * 前台日志DAO接口
 * @author ZhangJingtao
 * @version 2014-07-07
 */
public interface TerminalLogDao extends TerminalLogDaoCustom, CrudRepository<TerminalLog, Long> {

//	@Modifying
//	//@Query("update TerminalLog set delFlag='" + TerminalLog.DEL_FLAG_DELETE + "' where id = ?1")
//	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface TerminalLogDaoCustom extends BaseDao<TerminalLog> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class TerminalLogDaoImpl extends BaseDaoImpl<TerminalLog> implements TerminalLogDaoCustom {

}
