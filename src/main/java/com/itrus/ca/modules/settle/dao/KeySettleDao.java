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
import com.itrus.ca.modules.settle.entity.KeySettle;

/**
 * key结算功能DAO接口
 * @author qt
 * @version 2015-11-12
 */
public interface KeySettleDao extends KeySettleDaoCustom, CrudRepository<KeySettle, Long> {

	@Modifying
	@Query("update KeySettle set delFlag='" + KeySettle.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author qt
 */
interface KeySettleDaoCustom extends BaseDao<KeySettle> {

}

/**
 * DAO自定义接口实现
 * @author qt
 */
@Component
class KeySettleDaoImpl extends BaseDaoImpl<KeySettle> implements KeySettleDaoCustom {

}
