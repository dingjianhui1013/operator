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
import com.itrus.ca.modules.key.entity.KeyCheckLog;

/**
 * 库存统计DAO接口
 * @author HUHAO
 * @version 2014-07-01
 */
public interface KeyCheckLogDao extends KeyCheckLogDaoCustom, CrudRepository<KeyCheckLog, Long> {

	@Modifying
	@Query("update KeyCheckLog set delFlag='" + KeyCheckLog.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface KeyCheckLogDaoCustom extends BaseDao<KeyCheckLog> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class KeyCheckLogDaoImpl extends BaseDaoImpl<KeyCheckLog> implements KeyCheckLogDaoCustom {

}
