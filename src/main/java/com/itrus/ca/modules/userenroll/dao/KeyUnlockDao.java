/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.userenroll.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.userenroll.entity.KeyUnlock;

/**
 * 解锁审批DAO接口
 * @author ZhangJingtao
 * @version 2014-06-22
 */
public interface KeyUnlockDao extends KeyUnlockDaoCustom, CrudRepository<KeyUnlock, Long> {

	@Modifying
	@Query("update KeyUnlock set delFlag='" + KeyUnlock.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface KeyUnlockDaoCustom extends BaseDao<KeyUnlock> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class KeyUnlockDaoImpl extends BaseDaoImpl<KeyUnlock> implements KeyUnlockDaoCustom {

}
