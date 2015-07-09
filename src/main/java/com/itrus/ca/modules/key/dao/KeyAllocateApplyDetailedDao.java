package com.itrus.ca.modules.key.dao;



import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.key.entity.KeyAllocateApplyDetailed;

/**
 * 调拨管理DAO接口
 * @author CaoYichao
 * @version 2014-07-10
 */
public interface KeyAllocateApplyDetailedDao extends KeyAllocateApplyDetailedDaoCustom, CrudRepository<KeyAllocateApplyDetailed, Long> {

	/*@Modifying
	@Query("update KeyAllocateApply set delFlag='" + KeyAllocateApply.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);*/
	
}

/**
 * DAO自定义接口
 * @author CaoYichao
 */
interface KeyAllocateApplyDetailedDaoCustom extends BaseDao<KeyAllocateApplyDetailed> {

}

/**
 * DAO自定义接口实现
 * @author CaoYichao
 */
@Component
class KeyAllocateApplyDetailedDaoImpl extends BaseDaoImpl<KeyAllocateApplyDetailed> implements KeyAllocateApplyDetailedDaoCustom {

}
