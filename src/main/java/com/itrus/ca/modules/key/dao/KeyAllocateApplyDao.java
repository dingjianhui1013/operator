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
import com.itrus.ca.modules.key.entity.KeyAllocateApply;

/**
 * 调拨管理DAO接口
 * @author HUHAO
 * @version 2014-06-28
 */
public interface KeyAllocateApplyDao extends KeyAllocateApplyDaoCustom, CrudRepository<KeyAllocateApply, Long> {

	@Modifying
	@Query("update KeyAllocateApply set delFlag='" + KeyAllocateApply.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface KeyAllocateApplyDaoCustom extends BaseDao<KeyAllocateApply> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class KeyAllocateApplyDaoImpl extends BaseDaoImpl<KeyAllocateApply> implements KeyAllocateApplyDaoCustom {

}
