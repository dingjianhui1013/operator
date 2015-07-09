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
import com.itrus.ca.modules.settle.entity.SettleVTN;

/**
 * VTN管理DAO接口
 * @author HuHao
 * @version 2015-03-12
 */
public interface SettleVTNDao extends SettleVTNDaoCustom, CrudRepository<SettleVTN, Long> {

	@Modifying
	@Query("delete from  SettleVTN  where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface SettleVTNDaoCustom extends BaseDao<SettleVTN> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class SettleVTNDaoImpl extends BaseDaoImpl<SettleVTN> implements SettleVTNDaoCustom {

}
